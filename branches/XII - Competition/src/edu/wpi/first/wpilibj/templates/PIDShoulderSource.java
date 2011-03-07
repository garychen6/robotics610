package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.PIDSource;

/**
 *
 * @author Mr. Lim
 */
public class PIDShoulderSource implements PIDSource {

    public double shoulderError = 0;

    PIDShoulderSource() {
    }

    public double pidGet() {
        return shoulderError;
    }
}
