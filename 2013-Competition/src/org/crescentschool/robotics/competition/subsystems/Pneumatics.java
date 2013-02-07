/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.subsystems;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.crescentschool.robotics.competition.constants.ElectricalConstants;

public class Pneumatics extends Subsystem {
    private static Pneumatics instance = null;
    static Compressor compressor;
    DoubleSolenoid feeder;
    Solenoid powerTakeOff;
    DoubleSolenoid shooterAngle;
    
    
    public static Pneumatics getInstance(){
        if(instance == null){
            instance = new Pneumatics();
        }
        return instance;
    }
    Pneumatics(){
        compressor = new Compressor(ElectricalConstants.compressorSwitch, ElectricalConstants.compressorRelay);
        feeder = new DoubleSolenoid(ElectricalConstants.digitalModule,ElectricalConstants.feeder);
        powerTakeOff = new Solenoid(ElectricalConstants.digitalModule,ElectricalConstants.powerTakeOff);
        shooterAngle = new DoubleSolenoid(ElectricalConstants.shooterAngleForward,ElectricalConstants.shooterAngleReverse);
        compressor.start();
        
    }
    public static Compressor getCompressor(){
        return compressor;
    }
    public void initDefaultCommand() {
    }
    public void setFeeder(boolean fire){
        if(fire){
        feeder.set(DoubleSolenoid.Value.kForward);
        }
        else{
        feeder.set(DoubleSolenoid.Value.kReverse);
        }
    }
    public void setAngleUp(boolean fire){
        if(!fire){
            shooterAngle.set(DoubleSolenoid.Value.kForward);
        } else{
            shooterAngle.set(DoubleSolenoid.Value.kReverse);
        }
    }
    public void setPowerTakeOff(boolean fire){
        powerTakeOff.set(fire);
    }
    public void angleFeeder(boolean fire){
        if(fire == true){
            shooterAngle.set(DoubleSolenoid.Value.kForward);
        } else {
            shooterAngle.set(DoubleSolenoid.Value.kReverse);
        }
    }
    public boolean getSwitchValue(){
        return compressor.getPressureSwitchValue();
    }
}
