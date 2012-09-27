/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.commands;

import com.sun.squawk.util.MathUtils;
import edu.wpi.first.wpilibj.command.Command;
import org.crescentschool.robotics.competition.subsystems.Camera;
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
    boolean isFinished = false;
    boolean seeTargets = false;
    boolean startedLock = false;
    int count = 0;
    double turretError = 0;
    double equationSpeed = 0;
    double height = 0;

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
        turret.setPosition(8.5);
        System.out.println("Turret Pos Set");
        camera.setLight(true);
        shooter.setShooter(2400);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        if (Math.abs(turret.getPos() - 8.5) < 0.2) {
            seeTargets = true;
            System.out.println("Right Pos");
            turret.resetPosition();
        }
        if (seeTargets) {
            //System.out.println(turret.getPos());
            Camera.getInstance().processCamera();
            double offset = 1.129363476 * MathUtils.atan(camera.getX() * 0.434812375);
            // System.out.println(offset);
//            offset =  camera.getX();
            //ystem.out.println("camera TurretPot:"+camera.getTurretPot());
            if (offset != 0.0) {
                turret.setPosition(camera.getTurretPot() - offset);
            }
            height = camera.getHeight();
            equationSpeed = 0.0406 * (height * height) - 2.063 * height + 2296.3;
            shooter.setShooter(equationSpeed);

            if (Math.abs(camera.getX()) < 0.035) {
                count++;
                System.out.println("Frames within tolerance: " + count);
                if (count >= 3) {
                    turret.resetPosition();
                    turret.incPosition(0);
                    isFinished = true;
                }
            } else {
                count = 0;
            }
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
