/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.PIDOutput;

/**
 *
 * @author Warfa Jibril
 */

public class PIDShoulderOutput implements PIDOutput{

    public double zValue = 0;

    PIDShoulderOutput()
    {

    }

    public void pidWrite (double z){
        zValue = z;
    }

}

