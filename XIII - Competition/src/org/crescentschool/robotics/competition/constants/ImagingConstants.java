/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.constants;

/**
 * Constants related to capturing and processing images
 * @author Patrick
 */
public class ImagingConstants {
    /**
     * The minimum hue value that an individual pixel in a camera image may have to be included in the threshold
     */
    public static final int kHThresholdMin = 45;
    /**
     * The maximum hue value that an individual pixel in a camera image may have to be included in the threshold
     */
    public static final int kHThresholdMax = 133;
    /**
     * The minimum saturation value that an individual pixel in a camera image may have to be included in the threshold
     */
    public static final int kSThresholdMin = 10;
    /**
     * The minimum saturation value that an individual pixel in a camera image may have to be included in the threshold
     */
    public static final int kSThresholdMax = 255;
    /**
     * The minimum luminance value that an individual ixel in a camera image may have to be included in the threshold
     */
    public static final int kLThresholdMin = 50;
    /**
     * The minimum luminance value that an individual pixel in a camera image may have to be included in the threshold
     */
    public static final int kLThresholdMax = 255;
}
