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
    double set;

    public BridgeMode() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        oi = OI.getInstance();
        driveTrain = DriveTrain.getInstance();
        requires(driveTrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        set = 0;
        driveTrain.setPos(set);
        driveTrain.reInit();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        if (Buttons.isHeld(InputConstants.kBButton, oi.getDriver())) {
            set += 0.02;
            driveTrain.setPos(set);
        } else if (Buttons.isHeld(InputConstants.kAButton, oi.getDriver())) {
            set -= 0.02;
            driveTrain.setPos(set);
        }
    }

    protected void end() {
    }

    protected void interrupted() {
    }

    protected boolean isFinished() {
        return false;
    }
}
