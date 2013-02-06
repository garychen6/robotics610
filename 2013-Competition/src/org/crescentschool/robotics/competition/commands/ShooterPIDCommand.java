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

    public ShooterPIDCommand(double p, double i, double d, double ff, CANJaguar controller, GearTooth opticalSensor) {
        shooter = Shooter.getInstance();
        requires(shooter);
        this.ff = ff;
        this.kP = p;
        this.kI = i;
        this.kD = d;
        timer = new Timer();
        timer.start();
        this.opticalSensor = opticalSensor;
        this.opticalSensor.start();
        this.controller = controller;
        pneumatics = Pneumatics.getInstance();
        oi = OI.getInstance();
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
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
            if(error[0]>0&&oi.getOperator().getRawButton(InputConstants.r2Button)){
                pneumatics.setFeeder(true);
                outputFinal = (output + ff * setpoint);
            } 
            else if(error[0]<0&&oi.getOperator().getRawButton(InputConstants.r2Button)){
                pneumatics.setFeeder(false);
                outputFinal = -12;
                System.out.println("Recovering...");
            }
            else{
                pneumatics.setFeeder(false);
                outputFinal = (output + ff * setpoint);
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
    protected  void interrupted() {
    }
        /**
     * @return the p
     */
    public static double getP() {
        return kP;
    }

    /**
     * @return the i
     */
    public static double getI() {
        return kI;
    }

    /**
     * @return the setpoint
     */
    public static double getSetpoint() {
        return setpoint;
    }

    /**
     * @param setpoint the setpoint to set
     */
    synchronized public static void setSetpoint(double setpoint) {
        ShooterPIDCommand.setpoint = setpoint;
        output = 0;
        error[0] = 0;
        error[1] = 0;
        error[2] = 0;
    }

    
    /**
     * @param p the p to set
     */
    synchronized public static void setP(double p) {
        output = 0;
        ShooterPIDCommand.kP = p;
    }

    /**
     * @param i the i to set
     */
    synchronized public static void setI(double i) {
        output = 0;
        ShooterPIDCommand.kI = i;
    }

    /**
     * @param d the d to set
     */
    synchronized public static void setD(double d) {
        output = 0;
        ShooterPIDCommand.kD = d;
    }

    /**
     * @return the ff
     */
    public static double getFf() {
        return ff;
    }

    /**
     * @param ff the ff to set
     */
    synchronized public static void setFf(double ff) {
        ShooterPIDCommand.ff = ff;
    }

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
