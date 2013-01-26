/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.subsystems;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.crescentschool.robotics.competition.constants.ElectricalConstants;

/**
 *
 * @author jeffrey
 */
public class Pneumatics extends Subsystem {
    private static Pneumatics instance = null;
    Compressor compressor;
    
    public static Pneumatics getInstance(){
        if(instance == null){
            instance = new Pneumatics();
        }
        return instance;
    }
    Pneumatics(){
        compressor = new Compressor(ElectricalConstants.compressorSwitch, ElectricalConstants.compressorRelay);
        compressor.start();
    }
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}
