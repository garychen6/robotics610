/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package org.crescentschool.robotics.competition;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.crescentschool.robotics.competition.commands.A_DriveForward;
import org.crescentschool.robotics.competition.commands.A_LeftStraightOneBall;
import org.crescentschool.robotics.competition.commands.A_MiddleOneBall;
import org.crescentschool.robotics.competition.commands.A_MiddleTwoBall;
import org.crescentschool.robotics.competition.commands.A_RightStraightOneBall;
import org.crescentschool.robotics.competition.commands.A_StraightTwoBall;
import org.crescentschool.robotics.competition.constants.InputConstants;
import org.crescentschool.robotics.competition.controls.DriverControls;
import org.crescentschool.robotics.competition.subsystems.BackgroundCompressor;
import org.crescentschool.robotics.competition.subsystems.Catapult;

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
    Catapult shooter;
    Command autonomousCommand;
    Joystick driver;
    Joystick operator;
    int autoMode = 0;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        backgroundCompressor = BackgroundCompressor.getInstance();
        oi = OI.getInstance();
        autonomousCommand = new A_MiddleTwoBall();
        SmartDashboard.putString("Autonomous Mode:", "Two Ball");
        System.out.println("Two Ball");
        driver = oi.getDriver();
        operator = oi.getOperator();
    }

    /**
     * This function is run when autonomous mode starts.
     */
    public void autonomousInit() {
        switch (autoMode) {
            case 0:
                autonomousCommand = new A_MiddleTwoBall();

                break;
            case 1:
                autonomousCommand = new A_MiddleOneBall();

                break;
            case 2:
                autonomousCommand = new A_LeftStraightOneBall();

                break;
            case 3:
                autonomousCommand = new A_RightStraightOneBall();

                break;
            case 4:
                autonomousCommand = new A_StraightTwoBall();

                break;
            case 5:
                autonomousCommand = new A_DriveForward();

                break;
            default:
                autonomousCommand = new A_MiddleOneBall();
                break;
        }
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
        Scheduler.getInstance().removeAll();
        Scheduler.getInstance().add(new DriverControls());

    }

    /**
     * This function is called periodically during operator control.
     */
    public void teleopPeriodic() {



        Scheduler.getInstance().run();
    }

    public void disabledPeriodic() {
        if (driver.getRawButton(InputConstants.triangleButton)) {

            autoMode = 0;

        } else if (driver.getRawButton(InputConstants.xButton)) {

            autoMode = 1;
        } else if (driver.getRawButton(InputConstants.squareButton)) {

            autoMode = 2;

        } else if (driver.getRawButton(InputConstants.oButton)) {

            autoMode = 3;
        } else if (driver.getRawButton(InputConstants.r1Button)) {

            autoMode = 4;
        } else if (driver.getRawButton(InputConstants.r2Button)) {

            autoMode = 5;
        }
        switch (autoMode) {
            case 0:
                SmartDashboard.putString("Auto", "Middle Two Ball");
                break;
            case 1:
                SmartDashboard.putString("Auto", "Middle One Ball");

                break;
            case 2:
                SmartDashboard.putString("Auto", "Left Straight One Ball");

                break;
            case 3:
                SmartDashboard.putString("Auto", "Right Straight One Ball");

                break;
            case 4:
                SmartDashboard.putString("Auto", "Straight Two Ball");

                break;
                 case 5:
                SmartDashboard.putString("Auto", "Drive Forward");

                break;
            default:
                SmartDashboard.putString("Auto", "Middle One Ball");
                break;
        }
    }

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        LiveWindow.run();

    }
}
