/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
import org.crescentschool.robotics.competition.OI;
import org.crescentschool.robotics.competition.subsystems.DriveTrain;
import org.crescentschool.robotics.competition.subsystems.Shooter;

/**
 *
 * @author jamiekilburn
 */
public class T_Catapult extends Command {

    Shooter shooter;
    Joystick driver;
    private OI oi;
    public T_Catapult() {

        shooter = Shooter.getInstance();
          oi = OI.getInstance();
        driver = oi.getDriver();
        

        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute(){
      
   
        if(driver.getRawButton(2)){
            shooter.setMain(1);
        }else{
            shooter.setMain(0);
        }
        System.out.println(shooter.isLoading());
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