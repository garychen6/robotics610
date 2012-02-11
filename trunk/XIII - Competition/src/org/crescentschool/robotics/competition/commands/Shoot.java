/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.crescentschool.robotics.competition.Buttons;
import org.crescentschool.robotics.competition.OI;
import org.crescentschool.robotics.competition.constants.InputConstants;
import org.crescentschool.robotics.competition.subsystems.Feeder;
import org.crescentschool.robotics.competition.subsystems.Intake;
import org.crescentschool.robotics.competition.subsystems.Shooter;

/**
 *
 * @author Warfa
 */
public class Shoot extends Command {
    Shooter shooter = Shooter.getInstance();
    Feeder feeder = Feeder.getInstance();
    Intake intake = Intake.getInstance();
    public Shoot() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(shooter);
        requires(feeder);
        requires(intake);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        intake.setInbotForward(1);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        if(Buttons.isHeld(InputConstants.kR2Button, OI.getInstance().getOperator())){
        if((shooter.getShooterSetPoint() - shooter.getShooterSpeed()) < 30){
            feeder.setFeeder(1);
        }
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
