/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import org.crescentschool.robotics.competition.OI;
import org.crescentschool.robotics.competition.constants.InputConstants;
import org.crescentschool.robotics.competition.subsystems.Catcher;
import org.crescentschool.robotics.competition.subsystems.Intake;

/**
 *
 * @author ianlo
 */
public class T_Catcher extends Command {

    private OI oi;
    private Joystick driver;
    private boolean catcherOpen = false;
    private Catcher catcher;
    private Intake intake;

    public T_Catcher() {
        //Get the OI, joystick, catcher AND the intake
        oi = OI.getInstance();
        driver = oi.getDriver();
        catcher = Catcher.getInstance();
        intake = Intake.getInstance();
        //Only take control of the catcher.
        requires(catcher);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        System.out.println("Catcher");
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        //Continually take control of the catcher until interrupted
        requires(catcher);
        // Catcher
        //If the button is pressed or the sensor is tripped, close the catcher
        if (driver.getRawButton(InputConstants.r2Button) || catcher.isCatcherSensorTripped()) {
            catcherOpen = false;
        } //Else if the other button is pressed, open the catcher
        else if (driver.getRawButton(InputConstants.r1Button)) {
            catcherOpen = true;
        }
        //If the catcher is open, open the catcher and intake.
        if (catcherOpen) {
            catcher.setCatcherOpen(true);
//            intake.setPositionDown(true);
            //If the catcher is open, take control of the intake as well. 
            requires(intake);
            //If any of the intake buttons are pressed, release control of the intake and close the catcher.
            if (driver.getRawButton(InputConstants.l1Button) || driver.getRawButton(InputConstants.l2Button)) {
                catcherOpen = false;
                Scheduler.getInstance().add(new T_Intake());
            }

        } //If not, close them.
        else {

            catcher.setCatcherOpen(false);
//            intake.setPositionDown(false);

        }
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        //Catcher will never finish on its own.
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
