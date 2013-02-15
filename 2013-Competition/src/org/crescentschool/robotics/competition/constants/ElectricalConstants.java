/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.constants;

/**
 *
 * @author Warfa
 */
public class ElectricalConstants {
    //CAN ID of the right drivetrain Jaguar
    final public static int jagRightMaster = 2;
    //CAN ID of the left drivetrain Jaguar
    final public static int jagLeftMaster = 3;
    //PWM ID of the first right drivetrain Victor
    final public static int victorRightSlaveFront = 1;
    //PWM ID of the second right drivetrain Victor
    final public static int victorRightSlaveBack = 2;
    //PWM ID of the first left drivetrain Victor
    final public static int victorLeftSlaveFront = 3;
    //PWM ID of the second left drivetrain Victor
    final public static int victorLeftSlaveBack = 4;
    
    final public static int gyroAnalogInput = 3;
    //CAN ID of the shooter Jaguar
    final public static int jagShooter = 4; 
    //Driver Joystick
    final public static int driverJoystick = 1;
    //Operator Joystick
    final public static int operatorJoystick = 2;
    //Compressor switch channel
    final public static int compressorSwitch = 1;
    //Compressor relay channel
    final public static int compressorRelay = 1;
    //Digital module
    final public static int digitalModule = 1;
    //Feeder solenoid channel
    final public static int feeder = 1;
    //Power take off solenoid channel
    final public static int powerTakeOff = 2;
    //Channel for double solenoid to extend the piston.
    final public static int shooterAngleForward = 3;
    //Channel for double solenoid to retract the piston.
    final public static int shooterAngleReverse = 4;
    final public static int postForward = 5;
    final public static int postReverse =6;
    final public static int trayFlipForward = 7;
    final public static int trayFlipReverse = 8;
    final public static int hangForward = 9;
    final public static int hangReverse = 10;
    //Channel for LED Relay
    final public static int LEDRelay = 2;
}
