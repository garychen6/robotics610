package org.crescentschool.robotics.competition.commands;

import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
import org.crescentschool.robotics.competition.OI;
import org.crescentschool.robotics.competition.subsystems.DriveTrain;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
/**
 *
 * @author Robotics
 */
public class PIDDriveTuning extends Command {

    OI oi = OI.getInstance();
    Joystick driverJoy = oi.getDriver();
    Joystick OperJoy = oi.getOperator();
    DriveTrain driveTrain = DriveTrain.getInstance();
    DriverStationLCD dsLCD = oi.getDSLCD();
    boolean btn1 = false;
    boolean btn2 = false;
    boolean btn3 = false;
    boolean btn4 = false;
    boolean btn10 = false;
    boolean btn1a = false;
    boolean btn2a = false;
    boolean btn3a = false;
    boolean btn4a = false;
    boolean btn10a = false;

    public PIDDriveTuning() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        
          dsLCD.println(DriverStationLCD.Line.kMain6, 1, "RSetPoint: " +
            driveTrain.getRightPosSetpoint());
          dsLCD.println(DriverStationLCD.Line.kUser2, 1, "PosR: " +
          driveTrain.getRPos());
          dsLCD.println(DriverStationLCD.Line.kUser3, 1,
          "LSetPoint: " + driveTrain.getLeftPosSetpoint());
          dsLCD.println(DriverStationLCD.Line.kUser4, 1, "PosL "
          + driveTrain.getLPos());
          //dsLCD.println(DriverStationLCD.Line.kUser5, 1, "PIDPosOutputLeft: " +
          //driveTrain.PIDPosLOutput());
          //dsLCD.println(DriverStationLCD.Line.kUser6, 1, "Gyro: " +
          //driveTrain.getGyro().getAngle());
        dsLCD.updateLCD();
         
        if (driverJoy.getRawButton(1) && !btn1) {
            driveTrain.incIPos();
            btn1 = true;
        } else if (!driverJoy.getRawButton(1)) {
            btn1 = false;
        }
        if (driverJoy.getRawButton(2) && !btn2) {
            driveTrain.decIPos();
            btn2 = true;
        } else if (!driverJoy.getRawButton(2)) {
            btn2 = false;
        }
        if (driverJoy.getRawButton(3) && !btn3) {
            driveTrain.decPPos();
            btn3 = true;
        } else if (!driverJoy.getRawButton(3)) {
            btn3 = false;
        }
        if (driverJoy.getRawButton(4)) {
            driveTrain.incPPos();
            btn4 = true;
        } else if (!driverJoy.getRawButton(4)) {
            btn4 = false;
        }
        if (driverJoy.getRawButton(10)) {
            driveTrain.initPosMode();
            btn10 = true;
        } else if (!driverJoy.getRawButton(10)) {
            btn10 = false;
        }
        
        if (OperJoy.getRawButton(1) && !btn1a) {
            driveTrain.incISpeed();
            btn1a = true;
        } else if (!OperJoy.getRawButton(1)) {
            btn1a = false;
        }
        if (OperJoy.getRawButton(2) && !btn2a) {
            driveTrain.decISpeed();
            btn2a = true;;
        } else if (!OperJoy.getRawButton(2)) {
            btn2a = false;
        }
        if (OperJoy.getRawButton(3) && !btn3a) {
            driveTrain.decPSpeed();
            btn3a = true;;
        } else if (!OperJoy.getRawButton(3)) {
            btn3a = false;
        }
        if (OperJoy.getRawButton(4)) {
            driveTrain.incPSpeed();
            btn4a = true;
        } else if (!OperJoy.getRawButton(4)) {
            btn4a = false;
        }
        if (OperJoy.getRawButton(10)) {
            driveTrain.initSpeedMode();
            btn10a = true;
        } else if (!OperJoy.getRawButton(10)) {
            btn10a = false;
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
}
