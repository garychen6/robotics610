/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.PIDSource;

/**
 *
 * @author Mr. Lim
 */
public class PIDLineSource implements PIDSource {

    //Line Followers
    DigitalInput digLineLeft, digLineMiddle, digLineRight;
    public double lineError = 0;
    public double prevLineError = 0;

    PIDLineSource() {
        //Line Followers
        digLineLeft = new DigitalInput(ElectricalMap.kLightSensorLChannel);
        digLineMiddle = new DigitalInput(ElectricalMap.kLightSensorMChannel);
        digLineRight = new DigitalInput(ElectricalMap.kLightSensorRChannel);
    }

    public double pidGet() {
        //System.out.println("Left: " + digLineLeft.get() + "Right: " + digLineRight.get() + "Middle: " + digLineMiddle.get());
        //Read and update line follower error
        if (digLineLeft.get() && digLineMiddle.get() && digLineRight.get()) {
            lineError = prevLineError * 2;
        } else if (!digLineLeft.get() && !digLineMiddle.get() && !digLineRight.get()) {
            lineError = 0;
            prevLineError = 0;
        } else if (!digLineLeft.get() && digLineMiddle.get() && !digLineRight.get()) {
            lineError = 0;
            prevLineError = 0;
        } else if (digLineLeft.get() && !digLineMiddle.get() && digLineRight.get()) {
            lineError = 0;
            prevLineError = 0;
        } else if (!digLineLeft.get() && !digLineMiddle.get() && digLineRight.get()) {
            lineError = -1;
            prevLineError = -1;
        } else if (digLineLeft.get() && !digLineMiddle.get() && !digLineRight.get()) {
            lineError = 1;
            prevLineError = 1;
        } else if (!digLineLeft.get() && digLineMiddle.get() && digLineRight.get()) {
            lineError = -2;
            prevLineError = -2;
        } else if (digLineLeft.get() && digLineMiddle.get() && !digLineRight.get()) {
            lineError = 2;
            prevLineError = 2;
        }
        return lineError;
    }
}