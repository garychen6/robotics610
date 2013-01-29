/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.subsystems;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.crescentschool.robotics.competition.commands.DefaultPneumatics;
import org.crescentschool.robotics.competition.constants.PneumaticConstants;

/**
 *
 * @author jeffrey
 */
public class Pneumatics extends Subsystem {
    private static Pneumatics instance = null;
    Compressor compressor;
    //DoubleSolenoid doubleSolenoid;
    //sSolenoid singleSolenoid;
    
    public static Pneumatics getInstance(){
        if(instance == null){
            instance = new Pneumatics();
        }
        return instance;
    }
    Pneumatics(){
        compressor = new Compressor(PneumaticConstants.compressorSwitch, PneumaticConstants.compressorRelay);
        //doubleSolenoid = new DoubleSolenoid(PneumaticConstants.forwardChannel, PneumaticConstants.reverseChannel);
        //solenoid = new Solenoid(PneumaticConstants.forwardChannel);
        compressor.start();
    }
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new DefaultPneumatics());
    }
    public void forwardDoubleSolenoid(){
       // doubleSolenoid.set(DoubleSolenoid.Value.kForward);
    }
    public void reverseDoubleSolenoid(){
      //  doubleSolenoid.set(DoubleSolenoid.Value.kReverse);
    }
    public void solenoidControl(boolean controlSwitch){
        //solenoid.set(controlSwitch);
    }
    public boolean getSwitchValue(){
        return compressor.getPressureSwitchValue();
    }
}
