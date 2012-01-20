
package org.crescentschool.robotics.kitBot.commands;

import org.crescentschool.robotics.kitBot.subsystems.DriveTrain;

/**
 *
 * @author george warfa
 */
public class TankDrive extends CommandBase {
    DriveTrain t_driveTrain;

    public TankDrive() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        t_driveTrain = DriveTrain.getInstance();
        requires(t_driveTrain);
       
        
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        t_driveTrain.TankDrive();
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
