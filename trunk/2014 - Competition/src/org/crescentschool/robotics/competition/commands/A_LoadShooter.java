/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.crescentschool.robotics.competition.constants.InputConstants;
import org.crescentschool.robotics.competition.subsystems.Catapult;

/**
 *
 * @author ianlo
 */
public class A_LoadShooter extends Command {

    Catapult shooter;
    boolean finished = false;
    int finishedCount = 0;

    public A_LoadShooter() {
        System.out.println("A_LoadShooter");
        shooter = Catapult.getInstance();
        requires(shooter);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        if (shooter.isLoading()) {
            shooter.setMain(-1);
            finishedCount = 0;
        } else {
            shooter.setMain(0);
            if(finishedCount<20){
                finishedCount++;
            } else {
                finished=  true;
            }
        }
        
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
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
