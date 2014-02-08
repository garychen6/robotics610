/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.crescentschool.robotics.competition.subsystems.Intake;

/**
 *
 * @author ianlo
 */
public class A_Intake extends Command {
    boolean positionDown,wristClosed;
    double intakeSpeed;
    Intake intake ;
    public A_Intake(boolean positionDown, boolean wristClosed, double intakeSpeed,double timeout) {
        intake = Intake.getInstance();
        this.positionDown = positionDown;
        this.wristClosed = wristClosed;
        this.intakeSpeed = intakeSpeed;
        setTimeout(timeout);
        requires(intake);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        intake.setPositionDown(positionDown);
        intake.setWrist(wristClosed);
        intake.setIntaking(intakeSpeed);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        if(isTimedOut()){
            intake.setIntaking(0);
        }
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
