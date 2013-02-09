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
    final public static int victorRightSlaveFront = 3;
    //PWM ID of the second right drivetrain Victor
    final public static int victorRightSlaveBack = 4;
    //PWM ID of the first left drivetrain Victor
    final public static int victorLeftSlaveFront = 2;
    //PWM ID of the second left drivetrain Victor
    final public static int victorLeftSlaveBack = 1;
    
    final public static int gyroAnalogInput = 3;
    //CAN ID of the shooter Jaguar
    final public static int jagShooter = 4; 
    //Driver Joystick
    final public static int driverJoystick = 1;
    //Operator Joystick
    final public static int operatorJoystick = 2;
    final public static int compressorSwitch = 1;
    final public static int compressorRelay = 1;
    final public static int digitalModule = 1;
    final public static int feeder = 1;
    final public static int powerTakeOff = 2;
    final public static int shooterAngleForward = 3;
    final public static int shooterAngleReverse = 4;
    final public static int LEDRelay = 2;
}
