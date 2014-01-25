/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import org.crescentschool.robotics.competition.OI;
import org.crescentschool.robotics.competition.constants.InputConstants;
import org.crescentschool.robotics.competition.subsystems.Intake;

/**
 *
 * @author ianlo
 */
public class T_Intake extends Command {

    private OI oi;
    private Joystick driver;
    private Intake intake;
    private boolean intaking = false;
    private Timer intakeTimer;
    private boolean intakeRun = false;
    private int intakeReleasedMaxTime = 30;
    private int intakeReleasedCount = intakeReleasedMaxTime - 1;

    public T_Intake() {
        //Get the OI, joystick, and intake
        oi = OI.getInstance();
        driver = oi.getDriver();
        intake = Intake.getInstance();
        //Create a new timer that will count for 1 second after the intake is retracted
        intakeTimer = new Timer();
        intakeTimer.reset();
        intakeTimer.start();
        //Take control of the intake upon constructing
        requires(intake);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        System.out.println("Intake");
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        //If the button is pressed and it wasn't pressed last time, start intaking
        if (driver.getRawButton(InputConstants.l1Button)) {
            intaking = true;
            intakeReleasedCount = 0;

        } //If the button is not pressed, start a count and then set intaking to false if the count is up.
        else if (!driver.getRawButton(InputConstants.l1Button)) {
            if (intakeReleasedCount > intakeReleasedMaxTime) {
                intaking = false;

            } else {
                intakeReleasedCount++;

            }

        }
        //If intaking, bring the intake down and run the rollers

        if (intaking) {
            intake.setPositionDown(true);
            intake.setIntaking(0.5);
            //Restart the timer
            intakeTimer = new Timer();
            intakeTimer.reset();
            intakeTimer.start();
            intakeRun = true;

        } //Keep the intake running 1 second while you move the intake up. If the intake has not been run yet, DO NOT run the intake for 1 second. Wait until the button is pressed at least once first.
        else if (intakeTimer.get() < 1 && intakeRun) {
            //Bring the intake back up.
            intake.setPositionDown(false);

            intake.setIntaking(0.5);
        } //If the button is pressed, keep the intake up and outtake
        else if (driver.getRawButton(InputConstants.l2Button)) {
            intake.setPositionDown(true);
            intake.setIntaking(-0.5);
        } //If nothing is happening, stop the intake.
        else {
            intake.setIntaking(0);
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        //Intake will never finish on its own.
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
