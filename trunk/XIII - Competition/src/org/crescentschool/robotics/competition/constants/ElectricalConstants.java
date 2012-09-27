/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.constants;

/**
 * Constants related to the electrical system and electrical conversion constants
 * @author Warfa Jibril, Patrick White
 */
public class ElectricalConstants {

    /**
     * The CAN ID of the left drivetrain master Jaguar
     */
    public static final int DriveLeftMaster = 2;
    /**
     * The CAN ID of the left drivetrain slave Jaguar
     */
    public static final int DriveLeftSlave = 3;
    /**
     * The CAN ID of the right drivetrain master Jaguar
     */
    public static final int DriveRightMaster = 5;
    /**
     * The CAN ID of the right drivetrain slave Jaguar
     */
    public static final int DriveRightSlave = 4;
    /**
     * The number of lines on the encoder
     */
    public static final int DriveEncoderCounts = 256;
    /**
     * The CAN ID of the flipper Jaguar
     */
    public static final int JagFlipper = 6;
    /**
     * The offset for the gyro's voltage to prevent it from drifting
     */
    public static final int GyroAccumulatorCenter = 16;
    /**
     * The anslog port of the gyro
     */
    public static final int GyroPort = 1;
    /**
     * The anslog port of the vertical gyro
     */
    public static final int VertGyroPort = 2;
     /**
     * The anslog port of the ultrasonic
     */
    public static final int USonic = 3;
    /**
     * The CAN ID of the turret Jaguar
     */
    public static final int TurretJaguar = 9;
     /**
     * The CAN ID of the shooter Master Jaguar
     */
    public static final int shootJaguar = 8;
    /**
     * The CAN ID of the shooter Slave Jaguar
     */
    public static final int shootJaguarSlave = 7;
    /**
     * The number of volts the potentiometer changes by per degree of revolution
     */
    public static final double encToV = (7.2 / 360);
    /**
     * The number of volts per degree of revolution of the potentiometer
     */
    
    public static final double potDtoV = 0.00911111111;
    /**
     * The channel of the victor that controls the Intake
     */
    public static final int IntakeVictor = 3;
    /**
     * The channel of the first victor that controls the feeder
     */
    public static final int FeederVictor1 = 1;
    /**
     * The channel of the second victor that controls the feeder
     */
    public static final int FeederVictor2 = 2;
        /**
     * The Relay of the Camera Light
     */
    public static final int camLight = 1;
      /**
     * The Relay of the UltraSonic
     */
    public static final int uSonicDigital = 14;
}
