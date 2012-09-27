/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.crescentschool.robotics.competition.constants.PIDConstants;
import org.crescentschool.robotics.competition.constants.PotConstants;
import org.crescentschool.robotics.competition.subsystems.DriveTrain;
import org.crescentschool.robotics.competition.subsystems.Flipper;
import org.crescentschool.robotics.competition.subsystems.Shooter;
import org.crescentschool.robotics.competition.subsystems.Turret;

/**
 *
 * @author Warfa
 */
public class A_D_distance extends Command {

    DriveTrain driveTrain = DriveTrain.getInstance();
    Shooter shooter = Shooter.getInstance();
    Flipper flipper = Flipper.getInstance();
    double setPoint = 0;
    boolean isFinished = false;

    public A_D_distance(double inches) {
        System.out.println(this.toString());
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        
        this.setPoint = setPoint/PIDConstants.wheelCircumference;
        requires(driveTrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        driveTrain.reInit();
        Turret.getInstance().resetPosition();
        Turret.getInstance().setPosition(5);
        
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        driveTrain.setPos(setPoint);
        System.out.println("Position DriveTrain: "+driveTrain.getLeftPos());
        if (Math.abs(driveTrain.getLeftPos()) > 2 && Math.abs(driveTrain.getLeftPos()) < 3) {
            flipper.setFlippers(PotConstants.flipperBridge);
        }
        if (Math.abs(driveTrain.getLeftPos()) > 18) {
            flipper.setFlippers(PotConstants.flipperBallPickup);
        }
        if (shooter.getAutonOver()) {
            flipper.setFlippers(PotConstants.flipperBridge);
            isFinished = true;
        } else if (Math.abs(Math.abs(driveTrain.getLeftPos()) - Math.abs(setPoint)) < 0.1) {
            flipper.setFlippers(PotConstants.flipperBridge);
            isFinished = true;
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return isFinished;
    }

    // Called once after isFinished returns true
    protected void end() {
        System.out.println(this + " finished");
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        System.out.println(this + " canceled");
        isFinished = true;
    }
}
