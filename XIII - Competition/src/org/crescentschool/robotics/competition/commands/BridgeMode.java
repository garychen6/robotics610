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
public class BridgeMode extends Command {

    DriveTrain driveTrain = DriveTrain.getInstance();
    // Zis is zee maximum angre that vee vill reach
    double maxAngle;

    public BridgeMode() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(driveTrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        maxAngle = driveTrain.getVertAngle();
        driveTrain.setSpeed(50);
    }

    protected void execute() {
        if (maxAngle < driveTrain.getVertAngle()) {
            maxAngle = driveTrain.getVertAngle();
            driveTrain.setSpeed(50);
           
        }
        if(maxAngle - 10 > driveTrain.getVertAngle()){
            driveTrain.setSpeed(50);
        }
        /*else if (driveTrain.getVertAngle() < (maxAngle - 5)) {
            driveTrain.setPos(-1/6);
        }*/ else if (driveTrain.getVertAngle() < -5){
            driveTrain.setPos(-1);
            
        } else if (driveTrain.getVertAngle() > 5){
            driveTrain.setPos(1);
        }/* else if (driveTrain.getVertAngle() < 5 && driveTrain.getVertAngle() > -5){
            driveTrain.dance();
        }*/
        
        

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
