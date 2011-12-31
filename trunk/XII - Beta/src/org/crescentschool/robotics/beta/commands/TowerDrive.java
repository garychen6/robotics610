/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.beta.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.crescentschool.robotics.beta.subsystems.DriveTrain;

/**
 *
 * @author slim
 */
public class TowerDrive extends Command {

    DriveTrain m_driveTrain;
    double xInput = 0;
    double yInput = 0;

    public TowerDrive() {
        m_driveTrain = DriveTrain.getInstance();
        requires(m_driveTrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        double xInput = 0;
        double yInput = 0;
        m_driveTrain.towerDriveOn();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        m_driveTrain.towerDrive(xInput, yInput);
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
