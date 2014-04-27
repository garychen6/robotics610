/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package org.crescentschool.robotics.competition;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.crescentschool.robotics.competition.commands.A_DriveForward;
import org.crescentschool.robotics.competition.commands.A_LeftStraightOneBall;
import org.crescentschool.robotics.competition.commands.A_MiddleOneBall;
import org.crescentschool.robotics.competition.commands.A_MiddleOneBallLeft;
import org.crescentschool.robotics.competition.commands.A_MiddleOneBallRight;
import org.crescentschool.robotics.competition.commands.A_MiddleTwoBall;
import org.crescentschool.robotics.competition.commands.A_MiddleTwoBallLeft;
import org.crescentschool.robotics.competition.commands.A_MiddleTwoBallRight;
import org.crescentschool.robotics.competition.commands.A_RightStraightOneBall;
import org.crescentschool.robotics.competition.commands.A_StraightTwoBall;
import org.crescentschool.robotics.competition.constants.InputConstants;
import org.crescentschool.robotics.competition.controls.DriverControls;
import org.crescentschool.robotics.competition.subsystems.BackgroundCompressor;
import org.crescentschool.robotics.competition.subsystems.Camera;
import org.crescentschool.robotics.competition.subsystems.Catapult;
import org.crescentschool.robotics.competition.subsystems.DriveTrain;
import org.crescentschool.robotics.competition.subsystems.Lights;

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
    Command rightAutonomousCommand;
    Command leftAutonomousCommand;
    Joystick driver;
    Joystick operator;
    int autoMode = -1;
    Timer timer;
    boolean autonomousStarted = false;
    int count = 0;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        backgroundCompressor = BackgroundCompressor.getInstance();
        oi = OI.getInstance();

        driver = oi.getDriver();

        operator = oi.getOperator();

        Lights.getInstance().setPattern(Lights.TELE);
        Scheduler.getInstance().removeAll();

        rightAutonomousCommand = new A_LeftStraightOneBall();
        leftAutonomousCommand = new A_LeftStraightOneBall();

    }

    /**
     * This function is run when autonomous mode starts.
     */
    public void autonomousInit() {
        autonomousStarted = false;
        System.out.println("Auto Init");
        timer = new Timer();
        timer.reset();
        timer.start();
        if (autoMode != 0 && autoMode != 1) {
            autonomousStarted = true;
            rightAutonomousCommand.start();
        }

    }

    /**
     * This function is called periodically during autonomous.
     */
    public void autonomousPeriodic() {

//        System.out.println("Auto");
        if ((autoMode == 0 || autoMode == 1) && !autonomousStarted) {
            if (timer.get() > 1) {
                int offset = 0;
                offset = Camera.getInstance().getOffset(10);

                if (offset == 0 && count < 100) {

                    count++;
                } else {
                    if (offset < 0) {
                        System.out.println("Left");
                        autonomousStarted = true;
                        leftAutonomousCommand.start();
                    } else {
                        System.out.println("Right");
                        autonomousStarted = true;
                        rightAutonomousCommand.start();
                    }
                }
            }
        } else {
            Scheduler.getInstance().run();

        }

    }

    /**
     * This function is run when driver control starts.
     */
    public void teleopInit() {
        Scheduler.getInstance().removeAll();
        Scheduler.getInstance().add(new DriverControls());
        Lights.getInstance().setPattern(Lights.TELE);

    }

    /**
     * This function is called periodically during operator control.
     */
    public void teleopPeriodic() {



        Scheduler.getInstance().run();
    }

//    public void disabledInit() {
////        autonomousCommand = new A_MiddleTwoBall();
//        SmartDashboard.putString("Auto", "None");
//    }
    public void disabledPeriodic() {
        SmartDashboard.putNumber("Gyro", DriveTrain.getInstance().getGyroDegrees());
        if (operator.getRawButton(InputConstants.squareButton)) {
            Lights.getInstance().setRedAlliance(false);
        } else if (operator.getRawButton(InputConstants.oButton)) {
            Lights.getInstance().setRedAlliance(true);

        }
        if (Lights.getInstance().isRedAlliance()) {
            SmartDashboard.putString("Alliance", "Red");
            Lights.getInstance().setPattern(Lights.RED_PRE);

        } else {
            Lights.getInstance().setPattern(Lights.BLUE_PRE);

            SmartDashboard.putString("Alliance", "Blue");

        }
        if (driver.getRawButton(InputConstants.triangleButton) && autoMode != 0) {
            autoMode = 0;
            Scheduler.getInstance().removeAll();
            rightAutonomousCommand = new A_MiddleTwoBallRight();
            leftAutonomousCommand = new A_MiddleTwoBallLeft();

        } else if (driver.getRawButton(InputConstants.xButton) && autoMode != 1) {

            autoMode = 1;
            Scheduler.getInstance().removeAll();

            rightAutonomousCommand = new A_MiddleOneBallRight();
            leftAutonomousCommand = new A_MiddleOneBallLeft();

        } else if (driver.getRawButton(InputConstants.squareButton) && autoMode != 2) {

            autoMode = 2;
            Scheduler.getInstance().removeAll();

            rightAutonomousCommand = new A_LeftStraightOneBall();

        } else if (driver.getRawButton(InputConstants.oButton) && autoMode != 3) {

            autoMode = 3;
            Scheduler.getInstance().removeAll();

            rightAutonomousCommand = new A_RightStraightOneBall();

        } else if (driver.getRawButton(InputConstants.r1Button) && autoMode != 4) {

            autoMode = 4;
            Scheduler.getInstance().removeAll();

            rightAutonomousCommand = new A_StraightTwoBall();

        } else if (driver.getRawButton(InputConstants.r2Button) && autoMode != 5) {

            autoMode = 5;
            Scheduler.getInstance().removeAll();

            rightAutonomousCommand = new A_DriveForward();

        }
        switch (autoMode) {
            case -1:
                SmartDashboard.putString("Auto", "None");
                break;
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
