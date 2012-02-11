/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
import org.crescentschool.robotics.competition.Buttons;
import org.crescentschool.robotics.competition.OI;
import org.crescentschool.robotics.competition.constants.InputConstants;
import org.crescentschool.robotics.competition.subsystems.DriveTrain;

// Make this return true when this Command no longer needs to run execute()
// Called when another command which requires one or more of the same
// subsystems is scheduled to run
/**
 *
 * @author Robotics
 */
public class BridgeMode extends Command {

    OI oi;
    DriveTrain driveTrain;
    double x, y;

    public BridgeMode() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        oi = OI.getInstance();
        driveTrain = DriveTrain.getInstance();
        requires(driveTrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        x = 0;
        y = 0;
        driveTrain.reInit();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        OI.printToDS(0, "Pos SetPoint: " + driveTrain.getLeftPosSetpoint());
        OI.printToDS(1, "Pos: " + driveTrain.getLeftPos());
        OI.printToDS(2, "Gyro: " + driveTrain.getGyro().getAngle());
        if (Buttons.isPressed(InputConstants.kAButton, oi.getDriver())) {
            y += 0.25;
        } else if (Buttons.isPressed(InputConstants.kYButton, oi.getDriver())) {
            y -= 0.25;
        }
        if (Buttons.isHeld(InputConstants.kAButton, oi.getDriver())) {
            y += 0.02;
        } else if (Buttons.isHeld(InputConstants.kYButton, oi.getDriver())) {
            y -= 0.02;
        } else if (Buttons.isHeld(InputConstants.kXButton, oi.getDriver())) {
            x += 0.025;
        } else if (Buttons.isHeld(InputConstants.kBButton, oi.getDriver())) {
            x -= 0.025;
        }
        driveTrain.setRightPos(y - x);
        driveTrain.setLeftPos(y + x);
    }

    protected void end() {
    }

    protected void interrupted() {
    }

    protected boolean isFinished() {
        return false;
    }
}
