/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.commands;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.crescentschool.robotics.competition.OI;
import org.crescentschool.robotics.competition.constants.InputConstants;
import org.crescentschool.robotics.competition.subsystems.Logger;
import org.crescentschool.robotics.competition.subsystems.OurTimer;
import org.crescentschool.robotics.competition.subsystems.Pneumatics;
import org.crescentschool.robotics.competition.subsystems.Shooter;

/**
 *
 * @author robotics
 */
public class ShooterPIDCommand extends Command {

    //p=0.01, i=0.00001, d=0.0001
    private static double ff = 0.0022;
    private static double setpoint = 0;
    //5675 Long Range
    private static double current;
    private static CANJaguar controller;
    private static CANJaguar controller2;
    private static Counter opticalSensor;
    private static double shooterAngle = 1;
    private static double outputFinal = 0;
    //0 is bottom, 1 is top
    private static OI oi;
    private static Shooter shooter;
    /**
     * @param aShooterAngle the shooterAngle to set
     */
    public static void setShooterAngle(double aShooterAngle) {
        shooterAngle = aShooterAngle;
    }
    private boolean canError = false;
    private Preferences preferences;
    Joystick joystick;
    private int extendCount = 0;
    private int settleDownCount = 0;
    private int retractDelay = 10;
    private static double delay = 10;
    Pneumatics pneumatics;

    public ShooterPIDCommand(double ff, CANJaguar controller, CANJaguar controller2, Counter opticalSensor) {
        oi = OI.getInstance();
        pneumatics = Pneumatics.getInstance();
        shooter = Shooter.getInstance();
        preferences = Preferences.getInstance();
        requires(shooter);
        ShooterPIDCommand.ff = ff;
        ShooterPIDCommand.opticalSensor = opticalSensor;
        ShooterPIDCommand.opticalSensor.start();
        ShooterPIDCommand.controller = controller;
        ShooterPIDCommand.controller2 = controller2;
    }

    protected void initialize() {
        System.out.println("Running ShooterPIDCOmmand");
    }

    public void execute() {
        //System.out.println("ShooterPIDCommand");
        //Grab ff and setpoint from Smart Dashboard
            /*
         * 
         * 
         * WARFAAAA What was the FF Constant??
         * 
         * 
         * 
         * 
         * 
         */
        setFf(0.0019);
        //Temporary Code until we have the actual robot and a Pneumatics Class
        //NEEDS TO BE CHANGED BEFORE COMPETITION
        if (shooterAngle == 0) {
            delay = 0;
        } else {
            delay = 5;
        }

        //Logger to log the entire PID loop
       // logTime.stop();
       // logTime = OurTimer.getTimer("PID");

        //Read the speed from the optical
        if (opticalSensor != null) {
            //Simplified version of 60/((8/7)*period)
            //current = ((105.0) / (2.0 * opticalSensor.getPeriod()));
            current = (60 / (opticalSensor.getPeriod() * (8.0 / 7.0)));
            //Post the speed to the smartdashboard
            //System.out.println(current);
        } else {
            current = 300;
        }

        //This will be the VOLTAGE we send to the jag. Not percentVBus
        outputFinal = 0;

        //If we are up to speed and we want to fire
        //e.g. setpoint = 5000 we want to get up to 5100. 5000-5100 will give -100 and will be less than -100 if we are running faster than 5100
        if (setpoint - current < -100 && (oi.getOperator().getRawButton(InputConstants.r2Button))) {
            //This should always start off as 0 since it will reset when the shooter is recovering.
            if (extendCount == 1) {
                //Fire and log
                if(!shooter.isAuton()){
                pneumatics.setFeeder(true);
                }
                Logger.getLogger().debug("Speed: " + current);
            }
            //Keep counting up with the extended count until we reach retract delay.
            if (extendCount >= retractDelay) {
                //At that point retract.
                if(!shooter.isAuton()){
               
                pneumatics.setFeeder(false);
                }
                extendCount = 0;
            } else {
                extendCount++;
            }
            //While we are at the right speed, use ff while the disc is going through the shooter.
            outputFinal = ff * (setpoint + 200);


        } //If we are within 300 of our desired shooting speed and we are holding the trigger.
        //If we are within 300 of our desired shooting speed and we are holding the trigger.
        else if (shooterAngle == 1 && setpoint - current < 300 && (oi.getOperator().getRawButton(InputConstants.r2Button))) {
            //settleDownCount is the counter for when we want to stop at our coasting speed and then ramp up again.
            System.out.println("BAD");
            if (settleDownCount >= delay) {

                //Reset our extended count
                extendCount = 0;
                //Full blast since the delay is over. This will get us into the firing if statement.
                outputFinal = 12;

            } else {
                //If we have not yet finished the delay, then hold at the coasting speed until we are ready to fire.
                outputFinal = ff * setpoint;
                extendCount = 0;

                settleDownCount++;
            }

        } //If we are not up to speed and the trigger is held (Auto mode) 
        //The shooter is recovering from a shot.
        else if (setpoint - current > -100 && (oi.getOperator().getRawButton(InputConstants.r2Button))) {
            //Reset our settledown counter
            settleDownCount = 0;
            //Even though the wheel has slowed down, it doesn't mean our shark fin extend delay has not yet ended.
            //Check if it has ended and retract when it has.
            if (extendCount >= retractDelay) {
                  if(!shooter.isAuton()){
                pneumatics.setFeeder(false);
                  }
            } else {
                extendCount++;
            }
            //Run the motors at full power to ensure the fastest possible recovery time.
            outputFinal = 12;
        } //When the trigger isn't pressed or when we just want to coast.
        else {
            //Retract the feeder if the delay is over.
            if (extendCount >= retractDelay) {
                  if(!shooter.isAuton()){
                pneumatics.setFeeder(false);
                  }
            } else {
                extendCount++;
            }
            //Hold at 100 below our shooting speed
            outputFinal = ff * setpoint;
        }


        //Send the power to the motors.

        try {
            controller.setX(outputFinal);
            controller2.setX(outputFinal);
            SmartDashboard.putNumber("VoltageToMotor", outputFinal);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
            canError = true;
            handleCANError();
        }
        //Push our pid stats to the smart dashboard.
        pushPIDStats();


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
     * This program returns the setpoint value
     *
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
        ShooterPIDCommand.setpoint = setpoint - 100;
        //System.out.println("setPoint parameter "+setpoint+" actual setpoint: "+ShooterPIDCommand.setpoint);

    }

    /**
     * This method returns the feedforward value.
     *
     * @return ff the feedforward value
     */
    public static double getFf() {
        return ff;
    }

    /**
     * This method sets the feedforward value to the parameter ff.
     *
     * @param ff the new feedforward value
     */
    synchronized public static void setFf(double ff) {
        ShooterPIDCommand.ff = ff;
    }

    public static double getCurrent() {
        return current;
    }

    /**
     * This method prints out the PID statistics to the SmartDashboard for
     * debugging.
     *
     * It prints out error, p, i, d, current, the period of the optical sensor,
     * and the output.
     */
    public static void pushPIDStats() {
        SmartDashboard.putNumber("Speed", current);
//        SmartDashboard.putNumber("FF", ff);
//        SmartDashboard.putNumber("Setpoint", setpoint + 100);
//        SmartDashboard.putNumber("Voltage", outputFinal);
//        SmartDashboard.putNumber("Delay", delay);
    }

    public void handleCANError() {
        if (canError) {
            SmartDashboard.putString("Messages", "CAN Error!");
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }

            canError = false;
            try {
                controller.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
                controller.configNeutralMode(CANJaguar.NeutralMode.kCoast);
                controller.enableControl(0);
            } catch (CANTimeoutException ex) {
                ex.printStackTrace();
                canError = true;
                handleCANError();
            }
        }
    }
}
