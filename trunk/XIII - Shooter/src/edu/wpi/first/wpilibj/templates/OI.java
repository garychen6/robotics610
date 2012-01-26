
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.DigitalIOButton;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {

    public static OI instance = null;
    public static Joystick joyDriver;
    public static Joystick joyOperator;
    

    public static OI getInstance() {


        if (instance == null) {
            instance = new OI();
        }
        return instance;
    }

    /**
     * Returns the Joystick object that corresponds to the driver's gamepad.
     * @return the driver Joystick object.
     */
    public Joystick getDriver() {
        return joyDriver;
    }
    
    /**
     * Returns the Joystick object that corresponds to the operator's gamepad.
     * @return the operator Joystick object.
     */
    public Joystick getOperator(){
        return joyOperator;
    }

    /**
     * 
     * @return the DriverStationLCD object
     */
   

    private OI() {
        joyDriver = new Joystick(1);
        joyOperator = new Joystick(2);
  
    }
}
