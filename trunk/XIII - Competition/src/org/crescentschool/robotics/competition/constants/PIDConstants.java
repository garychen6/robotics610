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
    public static final double posP = 130;
    public static final double posI = 0;
    public static final double posD = 0;
    public static final double speedP = 0.29;
    public static final double speedI = 0;
    public static final double speedD = 0;
    public static final double gyroP = 0.04;
    public static final double flipP = 0.01;
    public static final double flipI = 0;
    public static final double flipD = 0;
    public static final double potT = 0.0028;
    public static final double shooterTopP = -0.17;
    public static final double shooterTopI = -0.003;
    public static final double shooterBottomP = -0.17;
    public static final double shooterBottomI = -0.004;
    public static final double uConv = 1 / (0.38582677165354330708661417322835 * 3.2808399);
    public static final double rPD = 0.0545415391;
    public static final double tP = 0;
    public static final double tI = 0;
    public static final double tD = 0;
    public static final double enc2T =  (7.2/360);
    // Dimensions in inches
    public static final double wheelCircumference = 4 * Math.PI;
    // Speeds in feet/second
    public static final double maxDriveSpeed = 10;
}
