/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.crescentschool.robotics.competition.subsystems.Camera;
import org.crescentschool.robotics.competition.subsystems.DriveTrain;

/**
 *
 * @author Warfa
 */
public class T_D_Follow extends Command {

    DriveTrain driveTrain = DriveTrain.getInstance();
    Camera camera = Camera.getInstance();
    double targetSize;
    double targetX;

    public T_D_Follow() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(driveTrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        camera.processCamera();
        targetSize = camera.getSize();
        targetX = camera.getX();

        System.out.println("initTargetSize: " + targetSize);
        System.out.println("initTargetX: " + targetX);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        camera.processCamera();
        double error = targetSize - camera.getSize();
        double xError = targetX - camera.getX();
        driveTrain.setSpeed(1 / error);
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
