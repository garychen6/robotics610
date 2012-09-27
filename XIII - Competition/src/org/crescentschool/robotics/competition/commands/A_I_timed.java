/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.crescentschool.robotics.competition.subsystems.Feeder;
import org.crescentschool.robotics.competition.subsystems.Intake;
import org.crescentschool.robotics.competition.subsystems.Shooter;

/**
 *
 * @author Warfa
 */
public class A_I_timed extends Command {

    public double m_timeout;
    Intake intake = Intake.getInstance();
    Shooter shooter = Shooter.getInstance();
    Feeder feeder = Feeder.getInstance();
    double speed = 1;
    boolean isFinished = false;
    public A_I_timed(double timeout) {
        System.out.println(this.toString());
        m_timeout = timeout;
        requires(intake);
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        setTimeout(m_timeout);
        intake.setIntakeReverse(speed);
        feeder.setFeeder(-0.5);
        
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        if (shooter.getAutonOver()) {
            isFinished = true;
        } else if (isTimedOut()) {
            isFinished = true;
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return isTimedOut();
    }

    // Called once after isFinished returns true
    protected void end() {
        System.out.println(this + " finished");
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        System.out.println(this + " canceled");
        cancel();
    }
}
