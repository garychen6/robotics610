/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.crescentschool.robotics.competition.subsystems.DriveTrain;

/**
 *
 * @author Warfa
 */
public class AutoBridge extends Command {

    DriveTrain driveTrain = DriveTrain.getInstance();
    // Zis is zee maximum angre that vee vill reach
    double maxAngle;

    public AutoBridge()
    {
        requires(driveTrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        driveTrain.reInit();
        maxAngle = driveTrain.getVertAngle();
        driveTrain.setSpeed(-120);
    }

    protected void execute() {
        if (maxAngle < driveTrain.getVertAngle()) {
            maxAngle = driveTrain.getVertAngle();
        }
        else if (driveTrain.getVertAngle() < (maxAngle - 5)) {
            driveTrain.setPos(-1.1);
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
