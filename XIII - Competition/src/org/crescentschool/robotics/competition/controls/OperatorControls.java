/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.controls;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import org.crescentschool.robotics.competition.Buttons;
import org.crescentschool.robotics.competition.OI;
import org.crescentschool.robotics.competition.commands.ManualBallIntake;
import org.crescentschool.robotics.competition.commands.ManualFeeder;
import org.crescentschool.robotics.competition.commands.ManualShooter;
import org.crescentschool.robotics.competition.commands.PIDTuning;
import org.crescentschool.robotics.competition.commands.Shoot;
import org.crescentschool.robotics.competition.commands.TurretControl;
import org.crescentschool.robotics.competition.constants.InputConstants;

/**
 *
 * @author Warfa
 */
public class OperatorControls extends Command {
    
    public OperatorControls() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        //Scheduler.getInstance().add(new ManualShooter());
        Scheduler.getInstance().add(new TurretControl());
        Scheduler.getInstance().add(new PIDTuning());
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        if(Buttons.isPressed(InputConstants.kR2Button, OI.getInstance().getOperator())){
            Scheduler.getInstance().add(new Shoot());
        }else{
           Scheduler.getInstance().add(new ManualBallIntake()); 
           Scheduler.getInstance().add(new ManualFeeder());
        }
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