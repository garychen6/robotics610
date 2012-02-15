/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.subsystems;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.crescentschool.robotics.competition.constants.ElectricalConstants;
import org.crescentschool.robotics.competition.constants.PIDConstants;

/**
 *
 * @author Warfa
 */
public class CoyoBotUltrasonic extends Subsystem {
    // Put methods for controlling this subsystem.
    // here. Call these from Commands.
   static CoyoBotUltrasonic instance = null;
   AnalogChannel ultrasonic;
   DigitalOutput uSonic;
   /**
    * Ensure only one ultrasonic is instantiated.
    * @return The singleton ultrasonic instance.
    */
   public static CoyoBotUltrasonic getInstance(){
       if(instance == null){
           instance = new CoyoBotUltrasonic();
       }
       return instance;
   }
   private CoyoBotUltrasonic(){
       ultrasonic = new AnalogChannel(ElectricalConstants.USonic);
       ultrasonic.setAverageBits(8);
       uSonic = new DigitalOutput(ElectricalConstants.uSonicDigital);
   }
   /**
    * Gets the current distance in feet.
    * @return The current distance in feet.
    */
   public double getDistance(){
       return ultrasonic.getAverageVoltage()*PIDConstants.ultrasonicVtoF;
   }
      /**
    * Turns On and Off The Ultrasonic
    * @return Set On and Off, True and False
    */
   public void setUSonic(boolean on){
       uSonic.set(on);
   }
   /**
    * The default command for the ultrasonic.
    */
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}
