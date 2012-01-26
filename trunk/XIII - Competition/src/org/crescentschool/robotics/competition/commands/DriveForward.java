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
public class DriveForward extends Command {
    DriveTrain driveTrain = DriveTrain.getInstance();
    int setPoint = 0;
    public DriveForward(int setPoint) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        this.setPoint = setPoint;
        requires(driveTrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        driveTrain.posSetpoint(setPoint);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        if(Math.abs(driveTrain.getLPos()+10)/Math.abs(setPoint+10) >0.9 || Math.abs(driveTrain.getLPos()+10)/Math.abs(setPoint+10) > 1.1){
            return true;
        }else return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
