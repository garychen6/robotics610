/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import org.crescentschool.robotics.competition.OI;
import org.crescentschool.robotics.competition.constants.InputConstants;
import org.crescentschool.robotics.competition.subsystems.Intake;
import org.crescentschool.robotics.competition.subsystems.Shooter;

/**
 *
 * @author jamiekilburn
 */
public class T_Catapult extends Command {

    Shooter shooter;
    Joystick driver;
    private OI oi;
    int fireCount = 0;
    boolean firing = false;
    int loadCount = 0;
    Preferences prefs;
    Intake intake;
    boolean truss = false;
    Joystick operator;

    public T_Catapult() {
        System.out.println("Catapult");
        shooter = Shooter.getInstance();
        oi = OI.getInstance();
        driver = oi.getDriver();
        prefs = Preferences.getInstance();
        intake = Intake.getInstance();
        requires(shooter);
        operator = oi.getOperator();
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        requires(shooter);
        shooter.setHardStop(driver.getRawButton(InputConstants.oButton));

        if (!firing) {

            if (shooter.isLoading()) {
                shooter.setMain(-1);
            } else {
                shooter.setMain(0);
                if (operator.getRawButton(InputConstants.r2Button)) {
                    truss = false;

                    firing = true;
                    fireCount = 0;
                    if(!intake.getWristClosed()){
                        fireCount = 10;
                    }
                    requires(intake);
                } else if (operator.getRawButton(InputConstants.r1Button)) {
                    truss = true;
                    firing = true;
                    fireCount = 0;
                    if(!intake.getWristClosed()){
                        fireCount = 10;
                    }
                    requires(intake);
                }
            }
        } else {
            requires(intake);
            intake.setWrist(false);
            shooter.setHardStop(truss);
            if (fireCount < 10) {
                shooter.setMain(0);
                fireCount++;
            } else if (fireCount < 20) {

                fireCount++;
                loadCount = 0;
                shooter.setMain(-1);
            } else {

                shooter.setMain(0);
                if (loadCount > 20) {
                    firing = false;

                } else {
                    loadCount++;
                }
            }
        }
//        System.out.println(shooter.isLoading());
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