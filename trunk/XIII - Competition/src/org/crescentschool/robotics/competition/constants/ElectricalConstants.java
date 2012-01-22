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
    /**
     * The CAN ID of the left drivetrain master Jaguar
     */
    public static final int DriveLeftMaster = 2;
    /**
     * The CAN ID of the left drivetrain slave Jaguar
     */
    public static final int DriveLeftSlave = 1;
    /**
     * The CAN ID of the right drivetrain master Jaguar
     */
    public static final int DriveRightMaster = 4;
    /**
     * The CAN ID of the right drivetrain slave Jaguar
     */
    public static final int DriveRightSlave = 3;
    /**
     * The number of lines on the encoder
     */
    public static final int DriveEncoderCounts = 256;
    /**
     * The digital channel that the relay for the compressor is plugged in to
     */
    public static final int kCompressorRelayChannel = 1;
    /**
     * The digital I/O channel that the compressor's pressure switch is plugged in to
     */
    public static final int kCompressorPressureSwitchChannel = 1;
}
