/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.crescentschool.robotics.competition.constants.PIDConstants;
import org.crescentschool.robotics.competition.subsystems.Flipper;

/**
 *
 * @author Warfa
 */
public class setFlipper extends Command {
    
    double angle;
    Flipper flip = Flipper.getInstance();
    public setFlipper(double angle) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(flip);
        this.angle = angle;
        
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        flip.setFlippers(angle);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
         if(Math.abs(Math.abs(flip.getPos())-Math.abs(angle* PIDConstants.potT)) < 0.1){
             return true;
         }
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
