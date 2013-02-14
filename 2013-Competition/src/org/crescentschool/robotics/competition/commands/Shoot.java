/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.crescentschool.robotics.competition.constants.KinectConstants;
import org.crescentschool.robotics.competition.constants.PIDConstants;
import org.crescentschool.robotics.competition.subsystems.Shooter;
import org.crescentschool.robotics.competition.subsystems.*;
import org.crescentschool.robotics.competition.subsystems.Pneumatics;

/**
 *
 * @author Warfa
 */
public class Shoot extends Command {

    Shooter shooter;
    Pneumatics pneumatics;
    DriveTrain driveTrain;
    int nearSpeed = KinectConstants.baseNearShooterRPM;
    boolean fired = false;
    int shotFris = 0;
    public Shoot() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        driveTrain= DriveTrain.getInstance();
        requires(driveTrain);
        pneumatics = Pneumatics.getInstance();
        shooter = Shooter.getInstance();
    }

    // Called just before this Command runs the first time
    protected void initialize() {
         shooter.setPID(PIDConstants.shooterP, PIDConstants.shooterI, PIDConstants.shooterD, PIDConstants.shooterFF);
        pneumatics.setAngleUp(true);
        shooter.setSpeed(nearSpeed); 
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
            //ADD TRIM
            if(!fired&&ShooterPIDCommand.getCurrent() >= nearSpeed&&shotFris < 3){
                pneumatics.setFeeder(true);
                fired = true;
                shotFris++;
            }
            else if(ShooterPIDCommand.getCurrent() < nearSpeed){
                pneumatics.setFeeder(false);
                fired = false;
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
