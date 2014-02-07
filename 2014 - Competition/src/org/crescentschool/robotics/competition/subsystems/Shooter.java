/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.subsystems;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.crescentschool.robotics.competition.OI;
import org.crescentschool.robotics.competition.commands.T_Catapult;

/**
 *
 * @author jamiekilburn
 */
public class Shooter extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    int stage = 0;
    OI oi;
    double loadedPosition = 2.5;
    double firedPosition = 2.7;
    AnalogPotentiometer pot;
    Preferences prefs;
    boolean pastLimit = false;
    Talon catapult;
    DigitalInput optical;
  
    int opticalPort = 1;
    private static Shooter instance;

    private Shooter() {
        optical = new DigitalInput(1, opticalPort);
        oi = OI.getInstance();
        prefs = Preferences.getInstance();
        catapult = new Talon(7);
    }

    public static Shooter getInstance() {
        if (instance == null) {
            instance = new Shooter();
        }
        return instance;
    }

    public boolean isLoading() {

        return optical.get();
    }

    public void setMain(double v) {
        catapult.set(v);
    }

    public void initDefaultCommand() {

        
        
        
        
        
        
        
        
        
        

        
        


        setDefaultCommand(new T_Catapult());

    }
}