/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.crescentschool.robotics.competition.subsystems.Intake;
/**
 *
 * @author Warfa
 */
public class aI_timed extends Command {
    public double m_timeout;
    Intake intake = Intake.getInstance();
    double speed = 1;
    
    public aI_timed(double timeout) {
        m_timeout = timeout;
        requires(intake);
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        setTimeout(m_timeout);
    }
      
    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        intake.setInbotForward(speed);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return isTimedOut();
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
