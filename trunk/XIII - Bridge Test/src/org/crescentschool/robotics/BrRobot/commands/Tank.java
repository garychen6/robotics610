/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.BrRobot.commands;

import org.crescentschool.robotics.BrRobot.subsystems.DriveTrain;

/**
 *
 * @author Warfa
 */
public class Tank extends CommandBase {
     DriveTrain m_driveTrain;
    public Tank() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        m_driveTrain = CommandBase.m_drive;
        requires(m_driveTrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        m_driveTrain.tank();
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
