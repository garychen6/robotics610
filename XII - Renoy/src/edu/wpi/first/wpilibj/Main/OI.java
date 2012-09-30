package edu.wpi.first.wpilibj.Main;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.Joystick;

/**
 * Warfa Jibril
 * Mr. Lim
 * ICS3U
 * March 6, 2012
 */
public class OI {
    //Create the Joystick and OI
    private Joystick operator;
    private Joystick driver;
    private DriverStation ds;
    private static OI instance = null;
    // Allows commands and Subsystems to use the current instance 
    public static OI getInstance(){
        if(instance == null){
            instance = new OI();
        }
        return instance;
    }
    // OI Constructor
    public OI() {
        // initialize the joystick
        operator = new Joystick(1);
        driver = new Joystick(2);
    }
    // return the Joystick for use in Commands and Subsystems
    public Joystick getOperator() {
        return operator;
    }
    
    public Joystick getDriver() {
        return driver;
    }
    // returns the DS for use in DriveTrain sync Slaves
    public DriverStation getDS() {
        return ds;
    }
}
