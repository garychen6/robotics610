/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.subsystems;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.crescentschool.robotics.competition.constants.PIDConstants;

/**
 *
 * @author Warfa
 */
public class Ultrasonic extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
   static Ultrasonic instance = null;
   AnalogChannel ultrasonic;
   public static Ultrasonic getInstance(){
       if(instance == null){
           instance = new Ultrasonic();
       }
       return instance;
   }
   private Ultrasonic(){
       ultrasonic = new AnalogChannel(1);
       ultrasonic.setAverageBits(4);
   }
   public double getDistance(){
       return ultrasonic.getAverageVoltage()*PIDConstants.uConv;
   }
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}
