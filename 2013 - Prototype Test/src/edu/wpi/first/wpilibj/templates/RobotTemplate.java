/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;

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
    Joystick driverStick = new Joystick(1);
    Talon frontLeft, midLeft, rearLeft;
    Talon frontRight, midRight, rearRight;
    Encoder leftEnc, rightEnc;
    Gyro driveGyro;
    int printCount = 0;
/// i dont like it
    public void robotInit() {
        frontLeft = new Talon(6);
        midLeft = new Talon(5);
        rearLeft = new Talon(4);
        frontRight = new Talon(3);
        midRight = new Talon(2);
        rearRight = new Talon(1);
        leftEnc = new Encoder(2, 1);
        leftEnc.start();
        rightEnc = new Encoder(4, 3);
        rightEnc.setReverseDirection(true);
        rightEnc.start();
        driveGyro = new Gyro(1);

        driveGyro.setSensitivity(0.0016);
        driveGyro.reset();
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
        double y = driverStick.getRawAxis(2);
        double rightY= driverStick.getRawAxis(4);
        double x = driverStick.getRawAxis(3);

        double leftSpeed = y - x;
        double rightSpeed = y + x;
        //double leftSpeed = y;
        //double rightSpeed = rightY;

        frontLeft.set(leftSpeed);
        midLeft.set(leftSpeed);
        rearLeft.set(leftSpeed);

        frontRight.set(rightSpeed);
        midRight.set(rightSpeed);
        rearRight.set(rightSpeed);
        
        printSensors();
    }

    public void disabledPeriodic() {
        printSensors();
    }

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    }
    
    double toInches(int encCount)
    {
        return ((int)(encCount / 10.24 * Math.PI * 6 + 0.5)) / 100.0;
    }
    
    void printSensors()
    {
        if (printCount > 20) {
            System.out.println("Left: " + toInches(leftEnc.getRaw())
                    + " Right: " + toInches(rightEnc.getRaw())
                    + " Gyro: " + (int) driveGyro.getAngle());
            printCount = 0;
        }
        printCount++;
    }
}
