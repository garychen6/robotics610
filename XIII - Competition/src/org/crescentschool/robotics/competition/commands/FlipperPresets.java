/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.command.Command;
import org.crescentschool.robotics.competition.Buttons;
import org.crescentschool.robotics.competition.OI;
import org.crescentschool.robotics.competition.constants.InputConstants;
import org.crescentschool.robotics.competition.subsystems.Flipper;

/**
 *
 * @author Warfa
 */
public class FlipperPresets extends Command {
    OI oi = OI.getInstance();
    Flipper flipper = Flipper.getInstance();
    
    
    public FlipperPresets() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(flipper);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        
    }
    

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
      
      if(Buttons.isPressed(InputConstants.kXbutton, oi.getDriver())){
          flipper.setFlippers(0);
         
      }
      
      
      if(Buttons.isPressed(InputConstants.kSquarebutton, oi.getDriver())){
          flipper.setFlippers(15);
         
      }
    
      
      if(Buttons.isPressed(InputConstants.kCirclebutton, oi.getDriver())){
          flipper.setFlippers(45);
          
      }
      
      
      if(Buttons.isPressed(InputConstants.kTributton, oi.getDriver())){
          flipper.setFlippers(90);
          
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
