/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.crescentschool.robotics.competition.constants.PIDConstants;
import org.crescentschool.robotics.competition.subsystems.Camera;
import org.crescentschool.robotics.competition.CoyobotXIII;
import org.crescentschool.robotics.competition.subsystems.Shooter;
import org.crescentschool.robotics.competition.subsystems.Turret;

/**
 *
 * @author Warfa
 */
public class A_T_lock extends Command {

    Turret turret = Turret.getInstance();
    Camera camera = Camera.getInstance();
    Shooter shooter = Shooter.getInstance();
    double offset = 0;
    boolean isFinished = false;
    boolean seeTargets = false;
    boolean startedLock = false;
    int count = 0;
    double turretError = 0;

    public A_T_lock() {
        System.out.println(this.toString());
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(turret);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        System.out.println("Phase 1");
        turret.resetPosition();
        System.out.println("Turret Pos Reset");
        turret.setPosition(8.93);
        System.out.println("Turret Pos Set");
        camera.setLight(true);
        shooter.setShooter(2200);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        if (Math.abs(turret.getPos() - 8.93) < 0.1) {
            seeTargets = true;
            System.out.println("Right Pos");
        }
        if (seeTargets) {
            Camera.getInstance().processCamera();
            offset = PIDConstants.cameraP * camera.getX();
//            offset =  camera.getX();
            turret.resetPosition();
            turret.incPosition(offset);
            System.out.println(offset);
            System.out.println("Locking On");
            if (!startedLock && camera.getX() > 0.065) {
                startedLock = true;
                System.out.println("Starting Lock");
            }
            if (Math.abs(camera.getX()) < 0.065) {
                count++;
                System.out.println("Frames within tolerance: " + count);
                if (startedLock && count >= 2) {
                    isFinished = true;
                }
            } else {
                count = 0;
            }
            turretError = Math.abs(turret.getPosSet()) - Math.abs(turret.getPos());
        }
        if (shooter.getAutonOver()) {
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
