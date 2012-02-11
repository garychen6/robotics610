/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.subsystems;

import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.crescentschool.robotics.competition.constants.ElectricalConstants;

/**
 *
 * @author Warfa
 */
public class Feeder extends Subsystem {

    static Feeder instance = null;
    Victor feed1, feed2;
    /**
     * Ensures that only one feeder is instantiated.
     * @return The singleton feeder instance.
     */
    public static Feeder getInstance() {
        if (instance == null) {
            instance = new Feeder();
        }
        return instance;
    }

    /**
     * Initializes both victors for the feeder.
     */
    private Feeder() {
        feed1 = new Victor(ElectricalConstants.FeederVictor1);
        feed2 = new Victor(ElectricalConstants.FeederVictor2);
    }

    /**
     * Sets the target speed for the both Victors for the Feeder.
     * @param speed 
     */
    public void setFeeder(double speed) {
        //TODO: check direction of feeder
        feed1.set(speed);
        feed2.set(speed);
    }

    /**
     * Sets the default command for the feeder.
     */
    public void initDefaultCommand() {
    }
}
