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
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    static Feeder instance = null;
    Victor feed1, feed2;
    public static Feeder getInstance() {
        if (instance == null) {
            instance = new Feeder();
        }
        return instance;
    }

    private Feeder() {
        feed1 = new Victor(ElectricalConstants.FeederVictor1);
        feed2 = new Victor(ElectricalConstants.FeederVictor2);
    }
    
    public void setFeeder(double speed){
        //TODO: check direction of feeder
        feed1.set(speed);
        feed2.set(-speed);
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}
