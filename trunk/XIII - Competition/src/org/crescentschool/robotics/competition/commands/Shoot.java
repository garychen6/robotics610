/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.crescentschool.robotics.competition.OI;
import org.crescentschool.robotics.competition.subsystems.Feeder;
import org.crescentschool.robotics.competition.subsystems.Shooter;
import org.crescentschool.robotics.competition.subsystems.Ultrasonic;

/**
 *
 * @author Warfa
 */
public class Shoot extends Command {
    private double m_timeout;
    Shooter shooter = Shooter.getInstance();
    Feeder feed = Feeder.getInstance();
    Ultrasonic uSonic = Ultrasonic.getInstance();
    OI oi = OI.getInstance();
    public Shoot() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
       // requires(shooter);
        requires(feed);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
         

    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        
         feed.setFeeder(oi.getOperator().getRawAxis(2));
         
         
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return isTimedOut();
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
