/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.subsystems;

import edu.wpi.first.wpilibj.Counter;

/**
 *
 * @author Ian
 */
public class ShooterSensor extends Counter {

    private int numAvgs = 10;
    private int readCounter = 0;
    private double[] reads = new double[numAvgs];
    private double ticks = 0;
    private double prevTicks =0;
    double period = 0;
    ShooterSensor(int digIn) {
        super(digIn);
    }
    
    public double getSpeed(){
       ticks = super.get();
      period = (0.020/(ticks-prevTicks));
      System.out.println(ticks);
//        reads[readCounter] = 
//        //System.out.println(super.getPeriod());
//        readCounter++;
//        if (readCounter >= numAvgs)
//        {
//            readCounter = 0;
//        }
//        double average = 0;
//        for (int i = 0; i < numAvgs; i++)
//        {
//            average += reads[i];
//        }
//        average /= numAvgs;
        prevTicks = ticks;
        return  period;
    }
}
