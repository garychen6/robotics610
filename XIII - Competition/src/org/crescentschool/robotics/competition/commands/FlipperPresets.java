/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.command.Command;
import org.crescentschool.robotics.competition.OI;
import org.crescentschool.robotics.competition.subsystems.Flipper;

/**
 *
 * @author Warfa
 */
public class FlipperPresets extends Command {
    OI oi = OI.getInstance();
    Flipper flipper = Flipper.getInstance();
    Joystick driver = oi.getDriver();
    boolean button1 = false;
    boolean button2 = false;
    boolean button3 = false;
    boolean button4 = false;
    
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
      
      if(oi.getDriver().getRawButton(1) && !button1){
          flipper.setFlippers(0);
          button1 = true;
      }
      if(!oi.getDriver().getRawButton(1)){
          button1 = false;
      }
      
      if(oi.getDriver().getRawButton(2)){
          flipper.setFlippers(15);
          button1 = true;
      }
      if(!oi.getDriver().getRawButton(2)){
          button2 = false;
      }
      
      if(oi.getDriver().getRawButton(3)){
          flipper.setFlippers(45);
          button1 = true;
      }
      if(!oi.getDriver().getRawButton(3)){
          button3 = false;
      }
      
      if(oi.getDriver().getRawButton(4)){
          flipper.setFlippers(90);
          button1 = true;
      }
      if(!oi.getDriver().getRawButton(4)){
          button4 = false;
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
