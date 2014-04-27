/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.crescentschool.robotics.competition.commands.T_KajDrive;
import org.crescentschool.robotics.competition.constants.ElectricalConstants;

/**
 *
 * @author ianlo
 */
public class DriveTrain extends Subsystem {

    private static DriveTrain instance;
    private Talon leftFront, leftMiddle, leftBack;
    private Talon rightFront, rightMiddle, rightBack;
    private Encoder leftEncoder, rightEncoder;
    private Gyro gyro;

    public DriveTrain() {
        //Intialize the talons according to their channels.
        leftFront = new Talon(ElectricalConstants.talonDriveLeftFront);
        leftMiddle = new Talon(ElectricalConstants.talonDriveLeftMiddle);
        leftBack = new Talon(ElectricalConstants.talonDriveLeftBack);
        rightFront = new Talon(ElectricalConstants.talonDriveRightFront);
        rightMiddle = new Talon(ElectricalConstants.talonDriveRightMiddle);
        rightBack = new Talon(ElectricalConstants.talonDriveRightBack);
        
        //Initialize and start the encoders.
        leftEncoder = new Encoder(ElectricalConstants.leftEncoderASource, ElectricalConstants.leftEncoderBSource);
        rightEncoder = new Encoder(ElectricalConstants.rightEncoderASource, ElectricalConstants.rightEncoderBSource);
        rightEncoder.setReverseDirection(true);
        leftEncoder.start();
        rightEncoder.start();

        //Create the gyro, set it sensitivity, and start it at 0 degrees.
        gyro = new Gyro(ElectricalConstants.gyroInput);
        gyro.setSensitivity(ElectricalConstants.gyroSensitivity);
        gyro.reset();
     

    }

    //Get the singleton instance of drivetrain.
    static public DriveTrain getInstance() {
        if (instance == null) {
            instance = new DriveTrain();
        }
        return instance;
    }  
    //Start KajDrive when the drivetrain is first created.

    protected void initDefaultCommand() {
    }

    public void resetEncoders() {
        rightEncoder.reset();
        leftEncoder.reset();
    }

    //Return the gyro's reading rounded to the nearest int.
    public double getGyroDegrees() {

        return -gyro.getAngle();
    }

    public void resetGyro() {
        gyro.reset();
    }
    //Return the left encoder value in inches.

    public double getLeftEncoderInches() {
        return toInches(leftEncoder.get());

    }
    //Return the right encoder value in inches.

    public double getRightEncoderInches() {
        return toInches(rightEncoder.get());
    }
    //Set the left side of the drivetrain using vbus

    public void setLeftVBus(double value) {
        leftFront.set(value);
        leftMiddle.set(value);
        leftBack.set(value);
    }
    //Set the right side of the drivetrain using vbus.

    public void setRightVBus(double value) {
        rightFront.set(-value);
        rightMiddle.set(-value);
        rightBack.set(-value);
    }
    //Mr. Lim's equation to convert encoder counts to inches.

    private double toInches(int encCount) {
        return ((int) (encCount / 10.24 * Math.PI * 6 + 0.5)) / 100.0 * 3.8;
    }
}
