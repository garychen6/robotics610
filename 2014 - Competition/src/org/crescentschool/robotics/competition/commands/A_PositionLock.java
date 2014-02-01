/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.crescentschool.robotics.competition.OI;
import org.crescentschool.robotics.competition.subsystems.DriveTrain;

/**
 *
 * @author jamie
 */
public class A_PositionLock extends Command {
     private DriveTrain driveTrain;
    private double targetInches;
    private Preferences preferences;
    private OI oi;
    private Joystick driver;
   double leftStop;
     double rightStop;
    
    public A_PositionLock() {
        driveTrain = DriveTrain.getInstance();
        preferences = Preferences.getInstance();
        this.targetInches = targetInches;
        
        
        
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
         leftStop = driveTrain.getLeftEncoderInches();
        rightStop = driveTrain.getRightEncoderInches();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        double p = preferences.getDouble("P", 0);
        
       
         
        double leftDifference = Math.abs(driveTrain.getLeftEncoderInches()-leftStop);
        double rightDifference = Math.abs(driveTrain.getRightEncoderInches()-rightStop);
         System.out.println("Left: " +leftDifference);
         System.out.println("Right: " + rightDifference);
        if(leftDifference > 0.1){
            driveTrain.setLeftVBus(p*(driveTrain.getLeftEncoderInches()-leftStop)); 
             SmartDashboard.putNumber("Leftdiff", leftStop - driveTrain.getRightEncoderInches());
        }
        if(rightDifference > 0.1){
            driveTrain.setRightVBus(p*(driveTrain.getRightEncoderInches()-rightStop));  
            SmartDashboard.putNumber("RightDiff", rightStop - driveTrain.getLeftEncoderInches());
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