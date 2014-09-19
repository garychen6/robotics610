package edu.wpi.first.wpilibj.templates;

/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/


import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.templates.commands.DriverControls;
import edu.wpi.first.wpilibj.templates.subsystems.BackgroundCompressor;
import edu.wpi.first.wpilibj.templates.subsystems.Camera;
import edu.wpi.first.wpilibj.templates.subsystems.DriveTrain;
import edu.wpi.first.wpilibj.templates.subsystems.Lights;
import org.crescentschool.robotics.competition.constants.InputConstants;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotTemplate extends IterativeRobot {

    BackgroundCompressor backgroundCompressor;
    OI oi;
    Joystick driver;
    Joystick operator;
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
       

    }

    /**
     * This function is run when autonomous mode starts.
     */
    public void autonomousInit() {
       
        

    }

    /**
     * This function is called periodically during autonomous.
     */
    public void autonomousPeriodic() {
       

    }

    /**
     * This function is run when driver control starts.
     */
    public void teleopInit() {
        //Remove everything from the scheduler
        Scheduler.getInstance().removeAll();
        //Start driver controls
        Scheduler.getInstance().add(new DriverControls());
        Lights.getInstance().setPattern(Lights.TELE);

    }

    /**
     * This function is called periodically during operator control.
     */
    public void teleopPeriodic() {



        Scheduler.getInstance().run();
    }


    public void disabledPeriodic() {
        //Post the gyro value to the dashboard
        SmartDashboard.putNumber("Gyro", DriveTrain.getInstance().getGyroDegrees());
        //Change the  colour of the lights depending on the alliance
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
        
    }

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        LiveWindow.run();

    }
}
