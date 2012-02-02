/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.subsystems;

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.crescentschool.robotics.competition.constants.ElectricalConstants;

/**
 *
 * @author Warfa
 */
public class Intake extends Subsystem {

    Victor intake;
    static Intake instance = null;

    /**
     * Ensures that only one intake is instantiated.
     * @return The singleton intake instance.
     */
    public static Intake getInstance() {
        if (instance == null) {
            instance = new Intake();
        }
        return instance;
    }

    private Intake() {
        intake = new Victor(ElectricalConstants.IntakeVictor);
    }

    /**
     * Sets the speed for the intake.
     * @param speed 
     */
    public void setInbotForward(double speed) {
        intake.set(speed);
    }

    /**
     * The default command for the intake.
     */
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}
