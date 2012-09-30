/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.constants.ElectricalConstants;
import edu.wpi.first.wpilibj.templates.commands.ClawControl;

/**
 * Warfa Jibril
 * Mr. Lim
 * ICS3U
 * March 6, 2012
 */
public class Claw extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    // Initialize all the robot systems pertaining to the claw
    Victor topTread;
    Victor bottomTread;
    public static Claw instance = null;

    // Allows commands and Subsystems to use the current instance of Claw
    public static Claw getClaw() {
        if (instance == null) {
            instance = new Claw();
        }
        return instance;
    }
    // Claw Constructor

    public Claw() {
        topTread = new Victor(ElectricalConstants.kVictorGripperTopChannel);
        bottomTread = new Victor(ElectricalConstants.kVictorGripperBottomChannel);
    }
    //Sets the tread victors (-1 - 1)

    public void setTread(double x) {
        topTread.set(x);
        bottomTread.set(x);
    }
    // Set the default command for a subsystem here.
    public void initDefaultCommand() {

        //setDefaultCommand(new MySpecialCommand());
        setDefaultCommand(new ClawControl());
    }
}
