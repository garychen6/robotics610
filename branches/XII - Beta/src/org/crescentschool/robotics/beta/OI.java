/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.crescentschool.robotics.beta;
import edu.wpi.first.wpilibj.Joystick;
import org.crescentschool.robotics.beta.constants.PhysicalConstants;

public class OI {
    
    private static OI instance = null;
    private Joystick joyDriver;
    
    
    private OI() {
        joyDriver = new Joystick(PhysicalConstants.kJoyDriverPort);
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

