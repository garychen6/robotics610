/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.beta.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.crescentschool.robotics.beta.OI;
import org.crescentschool.robotics.beta.constants.InputConstants;
import org.crescentschool.robotics.beta.subsystems.*;

/**
 *
 * @author Patrick
 */
public class ManualArmControl extends Command {
    
    Arm m_arm;
    
    public ManualArmControl() {
        m_arm = Arm.getInstance();
        requires(m_arm);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        m_arm.moveArm(OI.getInstance().getJoyOperator().getRawAxis(InputConstants.kOperatorLeftYAxis));
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
