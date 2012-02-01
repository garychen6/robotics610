package org.crescentschool.robotics.competition.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
import org.crescentschool.robotics.competition.OI;
import org.crescentschool.robotics.competition.subsystems.DriveTrain;
import org.crescentschool.robotics.competition.subsystems.Flipper;

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
    Flipper flip = Flipper.getInstance();
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

//          dsLCD.println(DriverStationLCD.Line.kMain6, 1, "RSetPoint: " +
//            driveTrain.getRightPosSetpoint());
//          dsLCD.println(DriverStationLCD.Line.kUser2, 1, "PosR: " +
//          driveTrain.getRPos());
//          dsLCD.println(DriverStationLCD.Line.kUser3, 1,
//          "LSetPoint: " + driveTrain.getLeftPosSetpoint());
//          dsLCD.println(DriverStationLCD.Line.kUser4, 1, "PosL "
//          + driveTrain.getLPos());
        //dsLCD.println(DriverStationLCD.Line.kUser5, 1, "PIDPosOutputLeft: " +
        //driveTrain.PIDPosLOutput());
        OI.printToDS(0, "Flipper Pot: " + flip.getPos());
        OI.printToDS(1, "P: " + flip.getPID()[0] + " D: "+ flip.getPID()[2]);
        OI.printToDS(2, "Speed SetPoint: " + driveTrain.getSpeedSetpoint());
        OI.printToDS(3, "Speed: " + driveTrain.getSpeed());
        
        if (driverJoy.getRawButton(6) && !btn1) {
            flip.setP(1);
            btn1 = true;
        } else if (!driverJoy.getRawButton(6)) {
            btn1 = false;
        }
        if (driverJoy.getRawButton(7) && !btn2) {
            flip.setD(-0.1);
            btn2 = true;
        } else if (!driverJoy.getRawButton(7)) {
            btn2 = false;
        }
        if (driverJoy.getRawButton(8) && !btn3) {
            flip.setP(-1);
            btn3 = true;
        } else if (!driverJoy.getRawButton(8)) {
            btn3 = false;
        }
        if (driverJoy.getRawButton(5)) {
            flip.setD(0.1);
            btn4 = true;
        } else if (!driverJoy.getRawButton(5)) {
            btn4 = false;
        }
        if (driverJoy.getRawButton(10)) {
            flip.resetPID();
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
