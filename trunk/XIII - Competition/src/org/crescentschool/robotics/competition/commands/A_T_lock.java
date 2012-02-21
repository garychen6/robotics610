/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.crescentschool.robotics.competition.constants.PIDConstants;
import org.crescentschool.robotics.competition.subsystems.Camera;
import org.crescentschool.robotics.competition.subsystems.Turret;

/**
 *
 * @author Warfa
 */
public class A_T_lock extends Command {

    Turret turret = Turret.getInstance();
    Camera camera = Camera.getInstance();
    double offset = 0;
    boolean isFinished = false;
    boolean seeTargets = false;

    public A_T_lock() {
        System.out.println(this.toString());
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(turret);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        System.out.println("Phase 1");
        turret.setPosition(8.5);
        camera.setLight(true);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        if (Math.abs(turret.getPos() - 8.5) < 0.1) {
            seeTargets = true;
        }
        if (seeTargets) {
            double offset = PIDConstants.cameraP * camera.getX();
            turret.incPosition(offset);
            if(Math.abs(offset) < 0.01){
                isFinished = true;
            }
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
        cancel();
    }
}
