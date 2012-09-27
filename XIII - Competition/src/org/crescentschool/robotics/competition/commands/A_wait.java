/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.crescentschool.robotics.competition.subsystems.Feeder;
import org.crescentschool.robotics.competition.subsystems.Shooter;

/**
 *
 * @author Warfa
 */
public class A_wait extends Command {

    double timeout;
    Shooter shooter = Shooter.getInstance();
    Feeder feeder = Feeder.getInstance();
    boolean isFinished = false;

    public A_wait(double time) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        timeout = time;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        setTimeout(timeout);
        
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        if (shooter.getAutonOver()) {
            isFinished = true;
        } else if (isTimedOut()) {
            feeder.setFeeder(0);
            isFinished = true;
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
       
        return isFinished;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
