/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.subsystems;

import edu.wpi.first.wpilibj.GearTooth;

/**
 *
 * @author Ian
 */
public class ShooterSensor extends GearTooth {

    private int numAvgs = 4;
    private int readCounter = 0;
    private double[] reads = new double[numAvgs];

    ShooterSensor(int digIn) {
        super(digIn);
    }
    
    public double getSpeed(){
        reads[readCounter] = -30.0 / super.getPeriod();
        readCounter++;
        if (readCounter >= numAvgs)
        {
            readCounter = 0;
        }
        double average = 0;
        for (int i = 0; i < numAvgs; i++)
        {
            average += reads[i];
        }
        average /= numAvgs;
        return average;
    }
}
