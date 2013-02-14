/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.commands;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.GearTooth;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.crescentschool.robotics.competition.OI;
import org.crescentschool.robotics.competition.constants.InputConstants;
import org.crescentschool.robotics.competition.subsystems.Pneumatics;
import org.crescentschool.robotics.competition.subsystems.Shooter;

/**
 *
 * @author robotics
 */
public class ShooterPIDCommand extends Command {
    
    private static double kP = 0;
    private static double kI = 0;
    private static double kD = 0;
    private static double p = 0;
    private static double i = 0;
    private static double d = 0;
    private static double prevSpeed = 0;
    private static double ff = 1.5 / 580.0;
    private static double setpoint = 0;
    private static double output = 0;
    private static double outputChange = 0;
    private static double[] error = new double[3];
    private static Timer timer;
    private static double prevTime;
    private static double time;
    private static double current;
    private static CANJaguar controller;
    private static Counter opticalSensor;
    private static Shooter shooter;
    private static Pneumatics pneumatics;
    private static OI oi;
    private static int feedDelay = 0;
    private static boolean auton = false;

    /**
     * @return the auton
     */
    public static boolean isAuton() {
        return auton;
    }

    /**
     * @param aAuton the auton to set
     */
    public static void setAuton(boolean aAuton) {
        auton = aAuton;
    }

    /**
     * This program gets an instance of the shooter, pneumatics, and oi, and
     * also changes the ff, kP, kI, and kD values to the ones in this method.
     *
     * It also starts a timer for calculating the time.
     *
     * @param p the shooter's Position value for its PID control.
     * @param i the shooter's Integral value for its PID control.
     * @param d the shooter's Derivative value for its PID control.
     * @param ff the shooter's FeedForward value for its FeedForward control.
     * @param controller the motor controller to be passed in
     * @param opticalSensor the optical sensor (counter) to be passed in
     */
    public ShooterPIDCommand(double p, double i, double d, double ff, CANJaguar controller, Counter opticalSensor) {
        shooter = Shooter.getInstance();
        requires(shooter);
        ShooterPIDCommand.ff = ff;
        ShooterPIDCommand.kP = p;
        ShooterPIDCommand.kI = i;
        ShooterPIDCommand.kD = d;
        timer = new Timer();
        timer.start();
        ShooterPIDCommand.opticalSensor = opticalSensor;
        ShooterPIDCommand.opticalSensor.start();
        ShooterPIDCommand.controller = controller;
        pneumatics = Pneumatics.getInstance();
        oi = OI.getInstance();
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    /**
     * This program runs speed control code.
     */
    
    protected void execute() {

        try {
            if (opticalSensor != null) {
                current = -(60 / (opticalSensor.getPeriod() * (8.0 / 7.0)));
            } else {
                current = controller.getSpeed();
            }
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
        time = timer.get();
        error[2] = error[1];
        error[1] = error[0];
        error[0] = setpoint - current;
        p = (error[0] - error[1]) * kP;
        i = (error[0]) * kI;
        d = ((error[0] - 2 * error[1] + error[2]) / (time - prevTime)) * kD;
        outputChange = p + i + d;
        output += outputChange;
        double outputFinal = 0;
        prevTime = time;
        pushPIDStats();
        if (error[0] > 200 && (oi.getOperator().getRawButton(InputConstants.r2Button)||auton)) {
            pneumatics.setFeeder(true);
            if (feedDelay == 0) {
                feedDelay = 10;
            }
            outputFinal = -12;
        } else if (error[0] < 200 && (oi.getOperator().getRawButton(InputConstants.r2Button)||auton)) {
            if (feedDelay == 0) {
                pneumatics.setFeeder(false);
            }
            outputFinal = -12;
        } else{
            if (feedDelay == 0) {
                pneumatics.setFeeder(false);
            }
            outputFinal = (output + ff * setpoint);
        }

        if (feedDelay > 0) {
            feedDelay--;
        }

        try {
            controller.setX(outputFinal);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }

    /**
     * This method returns the proportional value.
     * @return the proportional value
     */
    public static double getP() {
        return kP;
    }

    /**
     * This method returns the integral value.
     * @return the integral value
     */
    public static double getI() {
        return kI;
    }

    /**
     * This method returns the derivative value.
     * @return the derivative value
     */
    public static double getD() {
        return kD;
    }
    
    /**
     * This program returns the setpoint value
     * @return the setpoint value
     */
    public static double getSetpoint() {
        return setpoint;
    }

    /**
     * This method sets the setpoint value to the parameter setpoint PLUS 200.
     * 
     * It also resets the output and error[0]-[2] values to 0.
     * 
     * @param setpoint 200 less than the new setpoint to be set
     */
    synchronized public static void setSetpoint(double setpoint) {
        ShooterPIDCommand.setpoint = setpoint + 200;
        output = 0;
        error[0] = 0;
        error[1] = 0;
        error[2] = 0;
    }

    /**
     * This method sets the proportional value to the parameter p
     * @param p the new proportional value
     */
    synchronized public static void setP(double p) {
        output = 0;
        ShooterPIDCommand.kP = p;
    }

    /**
     * This method sets the integral value to the parameter i
     * @param i the new integral value
     */
    synchronized public static void setI(double i) {
        output = 0;
        ShooterPIDCommand.kI = i;
    }

    /**
     * This method sets the derivative value to the parameter d.
     * @param d the new derivative value
     */
    synchronized public static void setD(double d) {
        output = 0;
        ShooterPIDCommand.kD = d;
    }

    /**
     * This method returns the feedforward value.
     * @return ff the feedforward value
     */
    public static double getFf() {
        return ff;
    }

    /**
     * This method sets the feedforward value to the parameter ff.
     * @param ff the new feedforward value
     */
    synchronized public static void setFf(double ff) {
        ShooterPIDCommand.ff = ff;
    }
    public static double getCurrent(){
        return -current;
    }
    /**
     * This method prints out the PID statistics to the SmartDashboard for
     * debugging.
     *
     * It prints out error, p, i, d, current, the period of the optical sensor,
     * and the output.
     */
    public static void pushPIDStats() {
        SmartDashboard.putNumber("Error", error[0]);
        SmartDashboard.putNumber("P", p);
        SmartDashboard.putNumber("I", i);
        SmartDashboard.putNumber("D", d);
        SmartDashboard.putNumber("SpeedNum", -current);
        SmartDashboard.putNumber("SpeedNumGraph", -current);
        SmartDashboard.putNumber("OpticalPeriod", opticalSensor.getPeriod());
        SmartDashboard.putNumber("Output", output + ff * setpoint);
    }
}
