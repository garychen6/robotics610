/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.PIDSource;

/**
 *
 * @author Mr. Lim
 */
public class PIDLineSource implements PIDSource {

    public double lineError = 0;

    PIDLineSource()
    {

    }

    public double pidGet()
    {
        return lineError;
    }
}
