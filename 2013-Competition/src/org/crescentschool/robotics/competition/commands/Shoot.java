/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import org.crescentschool.robotics.competition.constants.ShootingConstants;
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
    int nearSpeed = ShootingConstants.baseNearShooterRPM;
    boolean fired = false;
    boolean finished = false;
    int shotFris = 0;
    int frisbees = 0;
    boolean delay = false;
    int speed = 0;
    public Shoot(int frisbees, boolean delay, int autonSpeed) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        speed = autonSpeed;
        driveTrain = DriveTrain.getInstance();
        requires(driveTrain);
        pneumatics = Pneumatics.getInstance();
        shooter = Shooter.getInstance();
        this.frisbees = frisbees;
        shotFris = 0;
        this.delay = delay;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        //shooter.setPID(PIDConstants.shooterP, PIDConstants.shooterI, PIDConstants.shooterD, PIDConstants.shooterFF);
        pneumatics.setAngleUp(true);
        shooter.setSpeed(speed+ 330);
        //OldShooterCode.setAuton(true);
        shotFris = 0;
        fired = false;
        if (delay) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        OurTimer time = OurTimer.getTimer("Shoot");

        //ADD TRIM
        if (!fired && ShooterPIDCommand.getCurrent() >= nearSpeed && shotFris < frisbees) {
            System.out.println(ShooterPIDCommand.getCurrent()-nearSpeed);
            pneumatics.setFeeder(true);
            fired = true;
            shotFris++;
            if (shotFris == frisbees) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
           // Logger.getLogger().debug(shotFris + "");
        } else if (ShooterPIDCommand.getCurrent() < nearSpeed) {
            pneumatics.setFeeder(false);
            fired = false;
        }
        if (shotFris >= frisbees) {
           // OldShooterCode.setAuton(false);
            pneumatics.setAngleUp(false);
            //Scheduler.getInstance().add(new PositionControl(true, -11.5, true, -11.5));
            finished = true;
        }
        time.stop();
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
