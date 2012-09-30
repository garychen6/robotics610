/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;

import com.sun.squawk.util.MathUtils;
import edu.wpi.first.wpilibj.Joystick.ButtonType;
import edu.wpi.first.wpilibj.Main.OI;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.constants.InputConstants;
import edu.wpi.first.wpilibj.templates.subsystems.Arm;

/**
 *
 * @author ian
 */
public class ArmManual extends Command {
    
    // Initialize Subsystems and OI
    Arm arm = Arm.getArm();
    OI oi = OI.getInstance();
    double currentPos = arm.getPos();
    boolean isFinished = false;
    double y;
    // Called just before this Command runs the first time
    
    public ArmManual(){
        requires(arm);
    }
    protected void initialize() {
        arm.initPos();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        /*
        // take y input from the right y axis
        y =  MathUtils.pow(oi.getOperator().getRawAxis(InputConstants.kRightYAxis),3);
        
        arm.setVbus(y);
        */
        if(oi.getOperator().getRawButton(InputConstants.kL1Button)||oi.getOperator().getRawButton(InputConstants.kR1Button)){
            Scheduler.getInstance().add(new ArmPresets());
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
        
        double z = currentPos+oi.getOperator().getRawAxis(InputConstants.kRightYAxis)/1000;
        while(z>0.07&&z<0.93){
            arm.setPos(z);
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return isFinished;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        isFinished = true;
    }
}
