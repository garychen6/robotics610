/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;

import edu.wpi.first.wpilibj.Main.OI;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.constants.InputConstants;
import edu.wpi.first.wpilibj.templates.subsystems.Claw;

/**
 * Warfa Jibril
 * Mr. Lim
 * ICS3U
 * March 6, 2012
 */
public class ClawControl extends Command {
    // Initialize the Subsystems
    Claw claw = Claw.getClaw();
    OI oi = OI.getInstance();

    public ClawControl() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        System.out.println("Claw Initializing");
        requires(claw);

    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        // if R2 is pressed
        if (oi.getOperator().getRawButton(InputConstants.kR2Button)) {
            // run the tread forward
            claw.setTread(0.8);
        }
        // if L2 is pressed
        else if (oi.getOperator().getRawButton(InputConstants.kL2Button)) {
            // run the tread backwards
            claw.setTread(-0.8);
        }else{
            // if neither: stop the claw
            claw.setTread(0);
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
