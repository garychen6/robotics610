/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.crescentschool.robotics.competition.constants.InputConstants;
import org.crescentschool.robotics.competition.subsystems.Shooter;

/**
 *
 * @author ianlo
 */
public class A_LoadShooter extends Command {

    Shooter shooter;

    public A_LoadShooter() {
        System.out.println("A_LoadShooter");
        shooter = Shooter.getInstance();
        requires(shooter);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        if (shooter.isLoading()) {
            shooter.setMain(-1);
        } else {
            shooter.setMain(0);

        }
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return !shooter.isLoading();
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
