/*
 * Copyright Crescent School 2010.
 */

package org.crescentschool.robotics.coyobotxi.util;

/**
 * This class represents the electrical map of the robot and provides
 * useful constants which correspond to inputs and outputs on the robot.
 * It is not to be instantiated.
 * @author patrickwhite
 */
public class ElectricalMap {
    
    /* UI CONSTANTS */
    public static final int kLeftJoystickPort = 1; //USB PORT
    public static final int kRightJoystickPort = 2; //USB PORT
    public static final int kGamePadPort = 3;

    /* DRIVE SYSTEM CONSTANTS */
    public static final String kInvertedSide = "RIGHT";
    public static final int kFrontLeftJaguar = 3; //PWM ON SLOT 4
    public static final int kFrontRightJaguar = 2; //PWM ON SLOT 4
    public static final int kRearLeftJaguar = 4; //PWM ON SLOT 4
    public static final int kRearRightJaguar = 1; //PWM ON SLOT 4
    public static final int kRollerVictor = 5; //PWM ON SLOT 4

    /* ANALOG CONSTANTS */
    public static final int kGyroChannel = 1;
//    public static final int kLightSensor = 2;

    /* DIGITAL I/O CONSTANTS */
    public static final int kPressureSwitch = 1;
    public static final int kLeftAEncoder = 2;
    public static final int kLeftBEncoder = 3;
    public static final int kRightAEncoder = 4;
    public static final int kRightBEncoder = 5;
    public static final int kLeftLightSensor = 6;
    public static final int kRightLightSensor = 7;

    /* RELAY CONSTANTS */
    public static final int kCompressorChannel = 1;

    /* SOLENOID CONSTANTS */
    public static final int kLockSolenoid =       1;
    public static final int kLoadSolenoid =       2;
    public static final int kFootAdjustSolenoid = 3;
    public static final int kPushDownSolenoid =   4;
    public static final int kPushUpSolenoid =     5;

    

}
