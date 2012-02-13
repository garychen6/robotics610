/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.commands;

import edu.wpi.first.wpilibj.command.Command;
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
    public A_T_lock() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(turret);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
         turret.resetPosition();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
            offset = -0.4 * camera.getX();
            if (offset > 0.1)
                offset = 0.1;
            if (offset < -0.1)
                offset = -0.1;
            turret.incPosition(offset);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        if(-0.4*camera.getX() < 0.1)return true;
        else return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
