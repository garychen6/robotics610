/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package org.crescentschool.robotics.competition;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import org.crescentschool.robotics.competition.commands.A_MiddleTwoBall;
import org.crescentschool.robotics.competition.controls.DriverControls;
import org.crescentschool.robotics.competition.subsystems.BackgroundCompressor;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Coyobot extends IterativeRobot {

    BackgroundCompressor backgroundCompressor;
    OI oi;
    Command autonomousCommand;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        backgroundCompressor = BackgroundCompressor.getInstance();
        oi = OI.getInstance();
        autonomousCommand = new A_MiddleTwoBall();

    }

    /**
     * This function is run when autonomous mode starts.
     */
    public void autonomousInit() {
                autonomousCommand = new A_MiddleTwoBall();

        autonomousCommand.start();
        // schedule the autonomous command (example)
    }

    /**
     * This function is called periodically during autonomous.
     */
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }

    /**
     * This function is run when driver control starts.
     */
    public void teleopInit() {
Joystick driver;
Button button;
        driver = new Joystick(1);
driver.getButton(Joystick.ButtonType.kTop);
        Scheduler.getInstance().add(new DriverControls());


    }

    /**
     * This function is called periodically during operator control.
     */
    public void teleopPeriodic() {


        Scheduler.getInstance().run();
    }

    public void disabledPeriodic() {
        //ShooterPIDCommand.pushPIDStats();
    }

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        LiveWindow.run();

    }
}
