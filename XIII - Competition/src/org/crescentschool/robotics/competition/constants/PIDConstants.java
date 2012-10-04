/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.constants;

/**
 * Constants related to PID tuning and control
 * @author Warfa Jibril, Patrick White
 */
public class PIDConstants {
/*
     * new
P: -0.25
I: -0.005
D: 0.0
*/
/*
   * old
P: 0.048
I: 0.0035
D: 0.0
*/
    
    public static final boolean pidTuneMode = true;
    /**
     * The proportional constant of the drive train position mode 
     */
    public static final double drivePositionP = 130;
    /**
     * The integral constant of the drive train position mode 
     */
    public static final double drivePositionI = 0;
    /**
     * The derivative constant of the drive train position mode 
     */
    public static final double drivePositionD = 0;
    /**
     * The proportional constant of the drive train speed mode 
     */
    public static final double driveSpeedP = 0.29;
    /**
     * The integral constant of the drive train speed mode 
     */
    public static final double driveSpeedI = 0;
    /**
     * The derivative constant of the drive train speed mode 
     */
    public static final double driveSpeedD = 0;
    /**
     * The proportional constant of the gyroscope position mode
     */
    public static final double gyroP = 0.025;
    /**
     * The proportional constant of the flipper position control
     */
    public static final double flipperP = -300;
    /**
     * The integral constant of the flipper position control
     */
    public static final double flipperI = 0;
    /**
     * The derivative constant of the flipper position control
     */
    public static final double flipperD = 0;
    public static final double middleGoalVbus = -0.6;
    public static final double shooterP = -0.285;
//    public static final double shooterI = 0.003;
//    public static final double shooterP = 0.041;
//    public static final double shooterP = 0.0;
    public static final double shooterI = -0.004;
    public static final double shooterD = 0.0;
    public static final double ultrasonicVtoF = 10.0 / (0.49 * 3.2808399);
    //TODO: Javadoc
    public static final double rPD = 0.0545415391;
    public static final double turretP = -325;
//    public static final double turretI = -0.1;
    public static final double turretI = -0.003;
    public static final double turretD = 0.0;
    public static final double turretVBusP = 0.4;
    public static final double tLockP = 0;
    //public static double cameraP = -0.12;
    //public static double cameraP = -0.12;
    public static double cameraP = 0;
    /**
     * The circumference of the wheel, in inches
     */
    public static final double wheelCircumference = 5.9 * Math.PI;
    /**
     * The maximum drive speed of the drivetrain, in feet per second
     */
    public static final double maxDriveSpeed = 10;
    
    /**
     * The diameter of the wheel in inches.
     */
    public static final double wheelDiameter = 5.9;
    
    public static final double potPerDegree = 0.019711111;
}
// flipper rev to degree = 0.00911111111
// flipper facing straight up = 5.62
// flipper straight down = 3.98
// flipper straight forward =4.84
// bridge dropping angle is 44.9 degrees
