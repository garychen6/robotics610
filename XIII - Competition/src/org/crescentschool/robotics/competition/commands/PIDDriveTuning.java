package org.crescentschool.robotics.competition.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
import org.crescentschool.robotics.competition.Buttons;
import org.crescentschool.robotics.competition.OI;
import org.crescentschool.robotics.competition.constants.InputConstants;
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
    DriveTrain driveTrain = DriveTrain.getInstance();
    int count = 0;

    public PIDDriveTuning() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        if (count % 5 == 0) {
            //OI.printToDS(0, "Speed SetPoint: " + driveTrain.getLeftSpeedSetpoint());
            //OI.printToDS(1, "Speed: " + driveTrain.getLeftSpeed());
            OI.printToDS(2, "Pos SetPoint: " + driveTrain.getLeftPosSetpoint());
            OI.printToDS(3, "Pos: " + driveTrain.getLeftPos());
            OI.printToDS(1, "Gyro: "+driveTrain.getGyro().getAngle());
            count = 0;
        }
        count++;
        if (Buttons.isPressed(InputConstants.kR1button, oi.getDriver())) {
            driveTrain.incPSpeed();
        } else if (Buttons.isPressed(InputConstants.kR2button, oi.getDriver())) {
            driveTrain.decPSpeed();
        }
        if (Buttons.isPressed(InputConstants.kL1button, oi.getDriver())) {
            driveTrain.incISpeed();
        } else if (Buttons.isPressed(InputConstants.kL2button, oi.getDriver())) {
            driveTrain.decISpeed();
        }
        if (Buttons.isPressed(InputConstants.kStartbutton, oi.getDriver())) {
            driveTrain.reInit();
        }
        if (Buttons.isPressed(InputConstants.kR1button, oi.getOperator())) {
            driveTrain.incPPos();
        } else if (Buttons.isPressed(InputConstants.kR2button, oi.getOperator())) {
            driveTrain.decPPos();
        }
        if (Buttons.isPressed(InputConstants.kL1button, oi.getOperator())) {
            driveTrain.incPPos();
        } else if (Buttons.isPressed(InputConstants.kL2button, oi.getOperator())) {
            driveTrain.decPPos();
        }
        if (Buttons.isPressed(InputConstants.kStartbutton, oi.getOperator())) {
            driveTrain.reInit();
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
