/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.crescentschool.robotics.beta;
import edu.wpi.first.wpilibj.Joystick;
import org.crescentschool.robotics.beta.constants.ElectricalConstants;

public class OI {
    
    private static OI instance = null;
    private Joystick joyDriver;
    private Joystick joyOperator;
    
    
    private OI() {
        joyDriver = new Joystick(ElectricalConstants.kJoyDriverPort);
        joyOperator = new Joystick(ElectricalConstants.kJoyOperatorPort);
    }
    
    public Joystick getJoyDriver(){
        return joyDriver;
    }
    
    public Joystick getJoyOperator(){
        return joyOperator;
    }
    
    public static OI getInstance() {
    if (instance == null) 
            instance = new OI();
        return instance;
    }
}

