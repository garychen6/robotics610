    /*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;

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
    Joystick driver;
    Talon frontLeft, midLeft, rearLeft;
    Talon frontRight, midRight, rearRight;
    Victor intakeLeft, intakeRight;
    Encoder leftEnc, rightEnc;
    Gyro driveGyro;
    DigitalInput proximity;
    boolean intaking = false;
    boolean outtaking = false;
    boolean intakeButtonPressed = false;
    int printCount = 0;
    Timer timer = new Timer();

    DoubleSolenoid catchSol;
    DoubleSolenoid rightIntakeArm;
    DoubleSolenoid leftIntakeArm;

    boolean closed = false;

    public void robotInit() {

        rightIntakeArm = new DoubleSolenoid(1, 1, 2);
        catchSol = new DoubleSolenoid(1, 3, 4);
        leftIntakeArm = new DoubleSolenoid(1, 5, 6);
        driver = new Joystick(1);

        frontLeft = new Talon(6);
        midLeft = new Talon(5);
        rearLeft = new Talon(4);
        frontRight = new Talon(3);
        midRight = new Talon(2);
        rearRight = new Talon(1);

        intakeLeft = new Victor(7);
        intakeRight = new Victor(8);

        leftEnc = new Encoder(2, 1);
        leftEnc.start();
        rightEnc = new Encoder(4, 3);
        rightEnc.setReverseDirection(true);
        rightEnc.start();
        proximity = new DigitalInput(1, 5);
        driveGyro = new Gyro(1);

        driveGyro.setSensitivity(0.0016);
        driveGyro.reset();
        timer = new Timer();
        timer.reset();
        timer.start();
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
//        if(driver.getRawButton(InputConstants.xButton)){
//            catchSol.set(DoubleSolenoid.Value.kForward);
//        } else {
//                        catchSol.set(DoubleSolenoid.Value.kReverse);
//
//        }
//        if(driver.getRawButton(InputConstants.oButton)){
//            leftIntakeArm.set(DoubleSolenoid.Value.kForward);
//        } else {
//                        leftIntakeArm.set(DoubleSolenoid.Value.kReverse);
//
//        }
//        if(driver.getRawButton(InputConstants.squareButton)){
//            rightIntakeArm.set(DoubleSolenoid.Value.kForward);
//        } else {
//                        rightIntakeArm.set(DoubleSolenoid.Value.kReverse);
//
//        }

        //Intake
        if (driver.getRawButton(InputConstants.r1Button)) {
            outtaking = true;

        } else if (!driver.getRawButton(InputConstants.r1Button)) {
            outtaking = false;
        }
        if (driver.getRawButton(InputConstants.l1Button) && !intakeButtonPressed) {
            intakeButtonPressed = true;
            intaking = true;
            outtaking = false;

        } else if (!driver.getRawButton(InputConstants.l1Button)) {
            intakeButtonPressed = false;
            intaking = false;
        }
        //Extend and run
        if (intaking) {
            leftIntakeArm.set(DoubleSolenoid.Value.kReverse);
            rightIntakeArm.set(DoubleSolenoid.Value.kReverse);

            intakeLeft.set(1);
            intakeRight.set(-1);
            outtaking = false;

            timer = new Timer();
            timer.reset();
            timer.start();
        } //Retract and run
        else if (timer.get() < 1) {
            leftIntakeArm.set(DoubleSolenoid.Value.kForward);
            rightIntakeArm.set(DoubleSolenoid.Value.kForward);
            intakeLeft.set(1);
            intakeRight.set(-1);
            outtaking = false;

        } //Spit
        else if (outtaking) {
            leftIntakeArm.set(DoubleSolenoid.Value.kForward);
            rightIntakeArm.set(DoubleSolenoid.Value.kForward);
            intakeLeft.set(-1);
            intakeRight.set(1);
        } //Retract and stop
        else {
            leftIntakeArm.set(DoubleSolenoid.Value.kForward);
            rightIntakeArm.set(DoubleSolenoid.Value.kForward);
            intakeLeft.set(0);
            intakeRight.set(0);
        }

        // Catcher
        if (driver.getRawButton(InputConstants.l2Button) || proximity.get() || true) {
            closed = true;
        } else if (driver.getRawButton(InputConstants.r2Button)) {
            closed = false;
        }
        if (closed) {
            catchSol.set(DoubleSolenoid.Value.kReverse);

        } else {
            catchSol.set(DoubleSolenoid.Value.kForward);

        }

        //Drivetrain
        double y = driver.getRawAxis(InputConstants.leftYAxis);
        double x = driver.getRawAxis(InputConstants.rightXAxis);

        double leftSpeed = y - x;
        double rightSpeed = y + x;

        frontLeft.set(leftSpeed);
        midLeft.set(leftSpeed);
        rearLeft.set(leftSpeed);

        frontRight.set(rightSpeed);
        midRight.set(rightSpeed);
        rearRight.set(rightSpeed);

        //printSensors();
    }

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {

    }

    double toInches(int encCount) {
        return ((int) (encCount / 10.24 * Math.PI * 6 + 0.5)) / 100.0;
    }

    void printSensors() {
        if (printCount > 20) {
            System.out.println("Left: " + toInches(leftEnc.getRaw())
                    + " Right: " + toInches(rightEnc.getRaw())
                    + " Gyro: " + (int) driveGyro.getAngle());
            printCount = 0;
        }
        printCount++;
    }
}
