/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
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
    boolean finished = false;
    int shotFris = 0;

    public Shoot() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        driveTrain = DriveTrain.getInstance();
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
        if (!fired && ShooterPIDCommand.getCurrent() >= nearSpeed && shotFris < 3) {
            pneumatics.setFeeder(true);
            fired = true;

            shotFris++;
            if (shotFris == 3) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
            System.out.println(shotFris);
        } else if (ShooterPIDCommand.getCurrent() < nearSpeed) {
            pneumatics.setFeeder(false);
            fired = false;
        }
        if (shotFris >= 3) {
            ShooterPIDCommand.setAuton(false);
            Scheduler.getInstance().add(new PositionControl(true, -15.5, true, -12.5));
            finished = true;
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
