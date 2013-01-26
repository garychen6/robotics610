/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.commands;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Command;
import org.crescentschool.robotics.competition.OI;
import org.crescentschool.robotics.competition.constants.InputConstants;
import org.crescentschool.robotics.competition.subsystems.Pneumatics;

/**
 *
 * @author jeffrey
 */
public class DefaultPneumatics extends Command {
    OI oi;
    Pneumatics pneumatics;
    public DefaultPneumatics() {
        pneumatics = Pneumatics.getInstance();
        oi = OI.getInstance();
        // Use requires() here to declare subsystem dependencies
        requires(pneumatics);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        if(oi.getDriver().getRawButton(InputConstants.squareButton)){
            pneumatics.forwardDoubleSolenoid();
        } else {
            pneumatics.reverseDoubleSolenoid();
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
