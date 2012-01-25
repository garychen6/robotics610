/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.kitBot.subsystems;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.crescentschool.robotics.constants.ElectricalConstants;

/**
 *
 * @author Warfa
 */
public class Ultrasonic extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    AnalogChannel ultrasonic;
    static Ultrasonic instance = null;
    
    public static Ultrasonic getInstance(){
        if(instance == null){
            instance = new Ultrasonic();
        }
        return instance;
    }
    
    private Ultrasonic(){
        ultrasonic = new AnalogChannel(ElectricalConstants.kUltasonicChannel);
    }
    
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
    public double findRange(){
        return ultrasonic.getVoltage();
    }
}
