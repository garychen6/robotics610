/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.crescentschool.robotics.competition.subsystems.Shooter;

/**
 *
 * @author Warfa
 */
public class ShootFris extends Command {
    
    Shooter shooter;
    public ShootFris() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        shooter = Shooter.getInstance();
        requires(shooter);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        shooter.setSpeed(1000);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
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
