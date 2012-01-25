/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.constants;

/**
 * 
 * @author Patrick
 */
public class ImagingConstants {
    /**
     * The minimum hue value that an individual pixel in a camera image may have to be included in the threshold
     */
    public static final int kHThresholdMin = 50;
    /**
     * The maximum hue value that an individual pixel in a camera image may have to be included in the threshold
     */
    public static final int kHThresholdMax = 100;
    /**
     * The minimum saturation value that an individual pixel in a camera image may have to be included in the threshold
     */
    public static final int kSThresholdMin = 0;
    /**
     * The minimum saturation value that an individual pixel in a camera image may have to be included in the threshold
     */
    public static final int kSThresholdMax = 255;
    /**
     * The minimum luminance value that an individual pixel in a camera image may have to be included in the threshold
     */
    public static final int kLThresholdMin = 179;
    /**
     * The minimum luminance value that an individual pixel in a camera image may have to be included in the threshold
     */
    public static final int kLThresholdMax = 255;
}
