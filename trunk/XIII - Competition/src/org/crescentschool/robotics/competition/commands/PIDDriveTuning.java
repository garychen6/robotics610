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

    public PIDDriveTuning() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        OI.printToDS(0, "Speed SetPoint: " + driveTrain.getSpeedSetpoint());
        OI.printToDS(1, "Speed: " + driveTrain.getSpeed());
        OI.printToDS(2, "Pos SetPoint: " + driveTrain.getPosSetpoint());
        OI.printToDS(3, "Pos: " + driveTrain.getPos());
        OI.printToDS(4, "Gyro: " + driveTrain.getGyro());

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
            driveTrain.initSpeedMode();
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
            driveTrain.initPosMode();
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
