/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.crescentschool.robotics.competition.subsystems.CoyoBotUltrasonic;
import org.crescentschool.robotics.competition.subsystems.Feeder;
import org.crescentschool.robotics.competition.subsystems.Intake;
import org.crescentschool.robotics.competition.subsystems.Shooter;

/**
 *
 * @author Warfa
 */
public class aS_shoot extends Command {
    Shooter shooter = Shooter.getInstance();
    CoyoBotUltrasonic ultrasonic = CoyoBotUltrasonic.getInstance();
    Intake intake = Intake.getInstance();
    Feeder feeder = Feeder.getInstance();
    public aS_shoot() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(shooter);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
         shooter.setShooter((80.167*ultrasonic.getDistance())+1212);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        if(Math.abs((shooter.getRPM() + shooter.getShooterSpeed())) < 100){
            intake.setInbotForward(-1);
            feeder.setFeeder(1);
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
       if(Math.abs((shooter.getRPM() + shooter.getShooterSpeed())) < 100){
            return true;
        }
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
