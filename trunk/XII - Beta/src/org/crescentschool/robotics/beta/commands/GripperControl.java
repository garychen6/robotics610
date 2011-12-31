/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.beta.commands;

import org.crescentschool.robotics.beta.OI;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
import org.crescentschool.robotics.beta.subsystems.Gripper;

/**
 *
 * @author slim
 */
public class GripperControl extends Command {

    Gripper m_gripper;

    public GripperControl() {
        m_gripper = Gripper.getInstance();
        requires(m_gripper);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        
        Joystick joyOperator = OI.getInstance().getJoyOperator();
        
        //Suck/Spit gripper when trigger
        if (Math.abs(joyOperator.getRawAxis(3)) > .05) {
            m_gripper.runGripper(joyOperator.getRawAxis(3),joyOperator.getRawAxis(3));
        } else {
            //Rotate piece in gripper based on which side arm is on
            m_gripper.runGripper(0,0);
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
