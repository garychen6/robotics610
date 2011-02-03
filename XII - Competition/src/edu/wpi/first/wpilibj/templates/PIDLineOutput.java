/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.PIDOutput;

/**
 *
 * @author Mr. Lim
 */
public class PIDLineOutput implements PIDOutput{

    public double xValue = 0;

    PIDLineOutput()
    {

    }

    public void pidWrite (double x){
        xValue = x;
    }

}
