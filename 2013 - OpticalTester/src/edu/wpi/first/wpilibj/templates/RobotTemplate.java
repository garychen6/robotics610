/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotTemplate extends IterativeRobot {

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    CANJaguar shooter;
    Counter optical;
    Joystick operatorStick;
    long lastTime = System.currentTimeMillis();
    
    public void robotInit() {
        try {
            shooter = new CANJaguar(7);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
        optical = new Counter(1);
        optical.setSemiPeriodMode(true);
        optical.setMaxPeriod(1);
        optical.start();
        operatorStick = new Joystick(1);
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        
        try {
            shooter.setX(operatorStick.getRawAxis(4));
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
        SmartDashboard.putNumber("optPeriod", optical.getPeriod());
        SmartDashboard.putNumber("optCount", optical.get());
        SmartDashboard.putNumber("optRPM", (8.0 / 7.0) * 60 / (optical.getPeriod()));
        try {
            SmartDashboard.putNumber("jagCurrent", shooter.getOutputCurrent());
            SmartDashboard.putNumber("jagCurrent", shooter.getOutputVoltage());
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
        //SmartDashboard.putNumber("optCalcRPM", 60.0/(8.0*(System.currentTimeMillis() - lastTime) / optical.get()));
        optical.reset();
        lastTime = System.currentTimeMillis();
    }

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    }
}
