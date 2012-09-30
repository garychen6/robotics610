/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;

import edu.wpi.first.wpilibj.Main.OI;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.constants.InputConstants;
import edu.wpi.first.wpilibj.templates.subsystems.Arm;

/**
 * Warfa Jibril
 * Mr. Lim
 * ICS3U
 * March 6, 2012
 */
public class ArmPresets extends Command {
    
    // Initialize Subsystems and OI
    Arm arm = Arm.getArm();
    OI oi = OI.getInstance();

    public ArmPresets() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        System.out.println("Arm Initializing");
        requires(arm);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        arm.initPos();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        // Net Height = 10ft 5inches
        // top Pos = 0.4016
        // if L1 is pressed
        if (oi.getOperator().getRawButton(InputConstants.kL1Button)) {
            // bring the arm to the ground
            arm.setPos(0.078);
            // extend the arm to the middle position
            arm.setArmExtend(2);
        }
        // if R1 is pressed
        if (oi.getOperator().getRawButton(InputConstants.kR1Button)) {
            // bring the arm the rim position
            arm.setPos(0.485);
            // extend the arm fully
            arm.setArmExtend(3);
        }
        if(Math.abs(oi.getOperator().getRawAxis(InputConstants.kRightYAxis))>0.5){
            Scheduler.getInstance().add(new ArmManual());
        }
        if(oi.getOperator().getRawButton(InputConstants.kXButton)){
            arm.setArmExtend(1);
        }
        if(oi.getOperator().getRawButton(InputConstants.kAButton)){
            arm.setArmExtend(2);
        }
        if(oi.getOperator().getRawButton(InputConstants.kBButton)){
            arm.setArmExtend(3);
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
