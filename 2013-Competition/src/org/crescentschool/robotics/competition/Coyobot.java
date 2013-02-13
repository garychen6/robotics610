/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package org.crescentschool.robotics.competition;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import java.io.IOException;
import org.crescentschool.robotics.competition.commands.*;
import org.crescentschool.robotics.competition.constants.*;
import org.crescentschool.robotics.competition.controls.*;
import org.crescentschool.robotics.competition.subsystems.*;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Coyobot extends IterativeRobot {

    Preferences constantsTable;
    Shooter shooter;
    DriveTrain driveTrain;
    Pneumatics pneumatics;
    Command autonomousCommand;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        shooter = Shooter.getInstance();
        driveTrain = DriveTrain.getInstance();
        constantsTable = Preferences.getInstance();

    }

    public void autonomousInit() {
        // schedule the autonomous command (example)
        driveTrain.getGyro().reset();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }

    public void teleopInit() {
        Scheduler.getInstance().add(new DriverControls());
        Scheduler.getInstance().add(new OperatorControls());
    }

    /**
     * This function is called periodically during operator control
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

        // In Test mode, press Operator R1 to reload all shooter PID values + setpoints from prefs
        if (OI.getInstance().getOperator().getRawButton(InputConstants.r1Button)) {
            shooter.setPID(constantsTable.getDouble("p", 0), constantsTable.getDouble("i", 0), constantsTable.getDouble("d", 0), constantsTable.getDouble("ff", 0));
            driveTrain.setPID(constantsTable.getDouble("DriveP", 0), constantsTable.getDouble("DriveI", 0), constantsTable.getDouble("DriveD", 0));
            shooter.setSpeed(constantsTable.getDouble("setpoint", 0));
        }
    }
}
