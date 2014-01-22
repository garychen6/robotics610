/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

/**
 *
 * @author matthewtory
 */
public class Constants {
   
    
    public static final double GYRO_SENSITIVITY = 0.0016;
    
    public static final double BRAKE_TIME_MSEC = 800;
    
    public static final double LOW_SPEED = 0.2;
    public static final double MID_SPEED = 0.6;
    public static final double HIGH_SPEED = 1.0;
    
    public static final double WHEEL_DIAMETER_IN = 6.0;
    public static final double WHEEL_CIRC_IN = WHEEL_DIAMETER_IN*Math.PI;
    
    public static final int MAX_ENC_TICKS = 360;
    
    public static final double INCH_PER_TICK = WHEEL_CIRC_IN/MAX_ENC_TICKS;
    public static final double TICK_PER_INCH = MAX_ENC_TICKS/WHEEL_CIRC_IN;
    
    
    
}
