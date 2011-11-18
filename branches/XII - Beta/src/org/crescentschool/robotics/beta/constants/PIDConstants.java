/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.beta.constants;

/**
 *
 * @author slim
 */
public class PIDConstants {
    //Drivetrain PID constants (Speed Control)
    public static final double driveP = 0.2;
    public static final double driveI = 0.004;
    public static final double driveD = 0.0;
    //Shoulder/Arm PID constants (Position Control)
    public static final double armP = -800;
    public static final double armI = 0;
    public static final double armD = 0;
    //Drivetrain PID constants (Position Control for Tower Drive)
    public static final double tdriveP = 300;
    public static final double tdriveI = 0;
    public static final double tdriveD = 0;
    //How many counts to increment each button press
    public static final double tdriveTicks = 0.02;
}
