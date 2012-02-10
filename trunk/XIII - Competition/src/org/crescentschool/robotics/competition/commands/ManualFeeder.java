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
import org.crescentschool.robotics.competition.subsystems.Feeder;

/**
 *
 * @author ian
 */
public class ManualFeeder extends Command {

    Feeder feeder = Feeder.getInstance();
    Joystick operator = OI.getInstance().getOperator();

    public ManualFeeder() {
        // Use requires() here to declare subsystem dependencies
        requires(feeder);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        if (Buttons.isHeld(InputConstants.kYButton, operator)) {
            feeder.setFeeder(1);
        } else if (Buttons.isHeld(InputConstants.kAButton, operator)) {
            feeder.setFeeder(-1);
        } else {
            feeder.setFeeder(0);
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
