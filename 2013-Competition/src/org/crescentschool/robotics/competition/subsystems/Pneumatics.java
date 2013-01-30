/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.subsystems;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.crescentschool.robotics.competition.constants.PneumaticConstants;

/**
 *
 * @author jeffrey
 */
public class Pneumatics extends Subsystem {
    private static Pneumatics instance = null;
    Compressor compressor;
    Solenoid feeder;
    Solenoid powerTakeOff;
    
    public static Pneumatics getInstance(){
        if(instance == null){
            instance = new Pneumatics();
        }
        return instance;
    }
    Pneumatics(){
        compressor = new Compressor(PneumaticConstants.compressorSwitch, PneumaticConstants.compressorRelay);
        feeder = new Solenoid(PneumaticConstants.digitalModule,PneumaticConstants.feeder);
        powerTakeOff = new Solenoid(PneumaticConstants.digitalModule,PneumaticConstants.powerTakeOff);
        compressor.start();
    }
    public void initDefaultCommand() {
    }
    public void setFeeder(boolean fire){
        feeder.set(fire);
    }
    public void setPowerTakeOff(boolean fire){
        powerTakeOff.set(fire);
    }
    public boolean getSwitchValue(){
        return compressor.getPressureSwitchValue();
    }
}
