
package org.crescentschool.robotics.kitBot;

import edu.wpi.first.wpilibj.Joystick;

public class OI {
    // Process operator interface input here.
    public static OI instance = null;
    public static Joystick joyDriver;
    public static OI getInstance(){
        if(instance == null){
            instance = new OI();
        }
        return instance;
    }
    public Joystick getDriver(){
        return joyDriver;
    }
    private OI(){        
         joyDriver = new Joystick(1);
    }
    
}

