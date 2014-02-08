/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.crescentschool.robotics.competition.subsystems.Intake;
import org.crescentschool.robotics.competition.subsystems.Shooter;

/**
 *
 * @author ianlo
 */
public class A_FireShooter extends Command {

    Shooter shooter;
    int fireCount = 0;
    boolean finished = false;

    public A_FireShooter() {
        System.out.println("A_FireShooter");
        shooter = Shooter.getInstance();
        requires(shooter);

    }
    // Called just before this Command runs the first time

    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {

        if (fireCount < 10) {
            shooter.setMain(-1);
            fireCount++;
        } else {
            finished = true;
            shooter.setMain(0);
           
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        if (finished) {
            shooter.setMain(0);

        }
        return finished;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
