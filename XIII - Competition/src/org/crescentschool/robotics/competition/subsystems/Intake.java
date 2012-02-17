/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.subsystems;

import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.crescentschool.robotics.competition.commands.M_I_Pickup;
import org.crescentschool.robotics.competition.constants.ElectricalConstants;

/**
 *
 * @author Warfa
 */
public class Intake extends Subsystem {

    Victor intake;
    static Intake instance = null;
    boolean isShooting = false;
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
    public void setIntakeReverse(double speed) {
        intake.set(speed);
    }
     /**
     * Gets if Shooting
     * @param 
     */
    public boolean isShooting() {
        return isShooting;
    }
     /**
     * Sets if Shooting
     * @param true or false
     */
    public void setIsShooting(boolean isShooting) {
        this.isShooting = isShooting;
    }
    /**
     * The default command for the intake.
     */
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
//        setDefaultCommand(new M_I_Pickup());
    }
}
