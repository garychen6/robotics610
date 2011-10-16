/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

/**
 *
 * This class contains a centralized file for various constants.
 * These constants are primarily pot positions, and autonomous parameters.
 */
public class Constants {

    //Analog Module
    /**
     * The factor by which to scale down the gyro's influence during turns
     */
    public static final double kGyroFactor = 60;
    /**
     * The potentiometer value representing the position of the arm at the front raised position
     */
    public static final double pArmFrontRaised = 0.414;
    /**
     * The potentiometer value representing the position of the arm at the back pickup position
     */
    public static final double pArmBackPickUp = 0.875;
    /**
     * The number of encoder turns from the position at the start of the autonomous to the scoring rack.
     */
    public static final double dStartToRack = 10.0;
    /**
     * The number of encoder turns to back up from the rack to the turning position
     */
    public static final double dRackToTurn = 3.69;
    /**
     * The number of encoder turns to back up from the turning position to the tube position
     */
    public static final double dTurnToTube = 3.31;
    /**
     * The number of encoder turns to drive from the (second) tube to the rack
     */
    public static final double dTubeToRack = 6.61;
    /**
     * The number of encoder turns to decelerate over
     */
    public static final double dDecel = 2.7;
    /**
     * The time to raise the arm before extending it
     */
    public static final double tExtendWait = 0.5;
    /**
     * The time to roll the tube on the peg before releasing
     */
    public static final double tRollTime = 1.35;
    /**
     * The time to allow for release before beginning to back up
     */
    public static final double tBackUpStart = 2;
    /**
     * The time for which to drive backwards during 1-tube dead reckoning
     */
    public static final double tBackUpDuration = 5;
    /**
     * The time to wait before bringing the arm around in 1-tube dead reckoning
     */
    public static final double tBackUpArmWait = 3;
    /**
     * The speed at which to drive while following the line
     */
    public static final double sLineFollow = 0.8;
    /**
     * The speed at which to drive when under gyro control
     */
    public static final double sGyro = 0.8;
    /**
     * The speed at which to spin the outward belt when turning the tube
     */
    public static final double sGripperOffset = 0.7;
    /**
     * The speed at which to back up during the final phase of autonomous
     */
    public static final double sFinalBackUp = 0.8;
    /**
     * The angle of the turn
     */
    public static final double aTurn = 29.46;
}
