/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.constants;

/**
 *
 * @author ianlo
 */
public class ImagingConstants {

    public static final int HThresholdMin = 50;
    /**
     * The maximum red value that an individual pixel in a camera image may have
     * to be included in the threshold
     */
    public static final int HThresholdMax = 110;
    /**
     * The minimum green value that an individual pixel in a camera image may
     * have to be included in the threshold
     */
    public static final int SThresholdMin = 70;
    /**
     * The minimum green value that an individual pixel in a camera image may
     * have to be included in the threshold
     */
    public static final int SThresholdMax = 255;
    /**
     * The minimum blue value that an individual pixel in a camera image may
     * have to be included in the threshold
     */
    public static final int VThresholdMin = 64;
    /**
     * The minimum blue value that an individual pixel in a camera image may
     * have to be included in the threshold
     */
    public static final int VThresholdMax = 255;
//    public static final int leftAreaThreshold = 130;
//    //RED
////    public static final int middleAreaThreshold = 100;
//    //BLUE
//        public static final int middleAreaThreshold = 130;
//    public static final int rightAreaThreshold = 80;
    public static final int middleWidthThreshold = 15;
}
