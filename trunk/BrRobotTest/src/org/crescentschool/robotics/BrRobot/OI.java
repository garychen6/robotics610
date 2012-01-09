
package org.crescentschool.robotics.BrRobot;

import edu.wpi.first.wpilibj.Joystick;

public class OI {
    
    private static OI instance = null;
    private Joystick joyDriver;
    
    
    private OI() {
        joyDriver = new Joystick(1);
      
    }
    
    public Joystick getJoyDriver(){
        return joyDriver;
    }
    
    public static OI getInstance() {
    if (instance == null) 
            instance = new OI();
        return instance;
    }
}