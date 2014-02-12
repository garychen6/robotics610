/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Preferences;
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
    boolean wristButtonFired = false;
    boolean armButtonFired = false;
    boolean arm = false;
    boolean wrist = false;
    private int intakeReleasedMaxTime = 5;
    private int intakeReleasedCount = 0;
    private Preferences prefs;
    Joystick operator;

    public T_Intake() {
        //Get the OI, joystick, and intake
        oi = OI.getInstance();
        driver = oi.getDriver();
        intake = Intake.getInstance();
        //Create a new timer that will count for 1 second after the intake is retracted
        intakeTimer = new Timer();
        intakeTimer.reset();
        intakeTimer.start();
        operator = oi.getOperator();

        prefs = Preferences.getInstance();
        //Take control of the intake upon constructing
        requires(intake);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        System.out.println("Intake");
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {

//        if (driver.getRawButton(InputConstants.l1Button)) {
//            intake.setIntaking(1);
//        } else if (driver.getRawButton(InputConstants.r1Button)) {
//            intake.setIntaking(-1);
//        } else {
//            intake.setIntaking(0);
//        }
//
//        if (driver.getRawButton(InputConstants.l2Button) && !wristButtonFired) {
//            wrist = !wrist;
//
//            intake.setWrist(wrist);
//
//            wristButtonFired = true;
//        } else if (!driver.getRawButton(InputConstants.l2Button)) {
//            wristButtonFired = false;
//        }
//        if (driver.getRawButton(InputConstants.r2Button) && !armButtonFired) {
//            arm = !arm;
//
//            intake.setPositionDown(arm);
//            if (!arm) {
//                wrist = true;
//                intake.setWrist(wrist);
//
//            }
//            armButtonFired = true;
//
//        } else if (!driver.getRawButton(InputConstants.r2Button)) {
//            armButtonFired = false;
//        }
        //If the button is pressed and it wasn't pressed last time, start intaking
        if (driver.getRawButton(InputConstants.l2Button)) {
            intaking = true;
            intakeReleasedCount = 0;

        } //If the button is not pressed, start a count and then set intaking to false if the count is up.
        else if (!driver.getRawButton(InputConstants.l2Button)) {
            if (intakeReleasedCount > intakeReleasedMaxTime) {
                intaking = false;

            } else {
                intakeReleasedCount++;

            }

        }
        //If intaking, bring the intake down and run the rollers

        if (intaking) {
            intake.setPositionDown(true);
            intake.setWrist(true);
            intake.setIntaking(prefs.getDouble("intakeSpeed", 0));
            //Restart the timer
            intakeTimer = new Timer();
            intakeTimer.reset();
            intakeTimer.start();
            intakeRun = true;

        } //Keep the intake running 1 second while you move the intake up. If the intake has not been run yet, DO NOT run the intake for 1 second. Wait until the button is pressed at least once first.
        else if (intakeTimer.get() < 1 && intakeRun) {
            //Bring the intake back up.
            intake.setPositionDown(false);
            intake.setWrist(false);
            intake.setIntaking(prefs.getDouble("intakeSpeed", 0));
        } //If the button is pressed, keep the intake up and outtake
        else if (driver.getRawButton(InputConstants.r2Button)) {
            intake.setPositionDown(false);
            intake.setWrist(true);

            intake.setIntaking(-prefs.getDouble("intakeSpeed", 0));
        } else if (operator.getRawButton(InputConstants.l1Button)||driver.getRawButton(InputConstants.l1Button)) {
            intake.setWrist(false);

        } else {
            intake.setIntaking(0);
            intake.setWrist(true);
            intake.setPositionDown(false);
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
