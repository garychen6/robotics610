/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.constants;

/**
 *
 * @author Warfa
 */
public class PIDConstants {

    public static final boolean pidTuneMode = true;
    /**
     * The P constant of the drive train position mode 
     */
    public static final double drivePositionP = 130;
    /**
     * The I constant of the drive train position mode 
     */
    public static final double drivePositionI = 0;
    /**
     * The D constant of the drive train position mode 
     */
    public static final double drivePositionD = 0;
    /**
     * The P constant of the drive train speed mode 
     */
    public static final double driveSpeedP = 0.29;
    /**
     * The I constant of the drive train speed mode 
     */
    public static final double driveSpeedI = 0;
    /**
     * The D constant of the drive train speed mode 
     */
    public static final double driveSpeedD = 0;
    public static final double gyroP = 0.04;
    public static final double flipperP = -1000;
    public static final double flipperI = 0;
    public static final double flipperD = 0;
    public static final double potT = 0.00911111111;
    public static final double shooterTopP = -0.17;
    public static final double shooterTopI = -0.003;
    public static final double shooterBottomP = -0.17;
    public static final double shooterBottomI = -0.004;
    public static final double ultrasonicVtoF = 1 / (0.38582677165354330708661417322835 * 3.2808399);
    //TODO: Javadoc
    public static final double rPD = 0.0545415391;
    public static final double turretP = 0;
    public static final double turretI = 0;
    public static final double turretD = 0;
    
    public static final double tLockP = 0;
    /**
     * The circumference of the wheel, in inches
     */
    public static final double wheelCircumference = 4 * Math.PI;
    /**
     * The maximum drive speed of the drivetrain, in feet per second
     */
    public static final double maxDriveSpeed = 10;
}
// flipper rev to degree = 0.00911111111
// flipper facing straight up = 5.62
// flipper straight down = 3.98
// flipper straight forward =4.84
// bridge dropping angle is 44.9 degrees