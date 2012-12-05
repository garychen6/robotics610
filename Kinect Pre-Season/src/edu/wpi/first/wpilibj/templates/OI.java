
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Joystick;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
  OI oi = null;
  Joystick joyDriver;
  
  public OI getInstance(){
      if(oi == null){
          oi = new OI();
      }
      return oi;
  }
  
  OI(){
      joyDriver = new Joystick(1);
  }
  
  public Joystick getDriver(){
      return joyDriver; 
  }
  
  
}

