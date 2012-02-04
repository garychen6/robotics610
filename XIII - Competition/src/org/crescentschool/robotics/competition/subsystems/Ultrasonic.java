/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.subsystems;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.crescentschool.robotics.competition.constants.ElectricalConstants;
import org.crescentschool.robotics.competition.constants.PIDConstants;

/**
 *
 * @author Warfa
 */
public class Ultrasonic extends Subsystem {
    // Put methods for controlling this subsystem.
    // here. Call these from Commands.
   static Ultrasonic instance = null;
   AnalogChannel ultrasonic;
   /**
    * Ensure only one ultrasonic is instantiated.
    * @return The singleton ultrasonic instance.
    */
   public static Ultrasonic getInstance(){
       if(instance == null){
           instance = new Ultrasonic();
       }
       return instance;
   }
   private Ultrasonic(){
       ultrasonic = new AnalogChannel(ElectricalConstants.USonic);
       ultrasonic.setAverageBits(4);
   }
   /**
    * Gets the current distance in feet.
    * @return The current distance in feet.
    */
   public double getDistance(){
       return ultrasonic.getAverageVoltage()*PIDConstants.ultrasonicVtoF;
   }
   /**
    * The default command for the ultrasonic.
    */
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}
