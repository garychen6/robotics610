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
    Shooter shooter = Shooter.getInstance();
    Feeder feed = Feeder.getInstance();
    Ultrasonic uSonic = Ultrasonic.getInstance();
    OI oi = OI.getInstance();
    public Shoot() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(shooter);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
         shooter.setTopShooter(700);
       shooter.setBottomShooter(-1220);

    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
         shooter.setTopShooter((int)(23.88*uSonic.getDistance() + 442.952));
         shooter.setBottomShooter((int)(-13.519*uSonic.getDistance() - 1071.91));
         feed.setFeeder(-oi.getOperator().getRawAxis(6));
         
         
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
