/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.subsystems;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.crescentschool.robotics.competition.constants.ElectricalConstants;

public class Pneumatics extends Subsystem {

    private static Pneumatics instance = null;
    static Compressor compressor;
    Solenoid feeder;
    Solenoid powerTakeOff;
    DoubleSolenoid post;
    Solenoid tray;
    DoubleSolenoid shooterAngle;
    DoubleSolenoid trayFlip;
    //DoubleSolenoid hang;
    Relay hang;
    boolean feederHigh = false;

    public static Pneumatics getInstance() {
        if (instance == null) {
            instance = new Pneumatics();
        }
        return instance;
    }

    Pneumatics() {
        //Initialize the compressor and solenoids.
        compressor = new Compressor(ElectricalConstants.compressorSwitch, ElectricalConstants.compressorRelay);
        feeder = new Solenoid(ElectricalConstants.feeder);
        powerTakeOff = new Solenoid(ElectricalConstants.digitalModule, ElectricalConstants.powerTakeOff);
        shooterAngle = new DoubleSolenoid(ElectricalConstants.shooterAngleForward, ElectricalConstants.shooterAngleReverse);
        post = new DoubleSolenoid(ElectricalConstants.digitalModule, ElectricalConstants.postForward, ElectricalConstants.postReverse);
        trayFlip = new DoubleSolenoid(ElectricalConstants.digitalModule, ElectricalConstants.trayFlipForward, ElectricalConstants.trayFlipReverse);
        hang = new Relay(3);
        //Run the compressor
        compressor.start();

    }

    public static Compressor getCompressor() {
        return compressor;
    }

    public void initDefaultCommand() {
    }
    //Allows the Feeder solenoid to be controlled by other classes

    public void setFeeder(boolean fire) {
        feeder.set(fire);
    }

    public void setAngleUp(boolean fire) {
        if (!fire) {
            shooterAngle.set(DoubleSolenoid.Value.kForward);
        } else {
            shooterAngle.set(DoubleSolenoid.Value.kReverse);
        }
    }

    public void setPowerTakeOff(boolean fire) {
        powerTakeOff.set(fire);
    }

    public boolean getFeederState() {
        return feederHigh;
    }

    public void angleFeeder(boolean fire) {
        if (fire == true) {
            shooterAngle.set(DoubleSolenoid.Value.kForward);
        } else {
            shooterAngle.set(DoubleSolenoid.Value.kOff);
        }
        feederHigh = fire;
    }
    //Allows other classes to get the pressure switch value.

    public boolean getSwitchValue() {
        return compressor.getPressureSwitchValue();
    }

    public void postUp(boolean fire) {
        if(!fire){
            post.set(DoubleSolenoid.Value.kForward);
        } else{
            post.set(DoubleSolenoid.Value.kReverse);
        }
    }
    public void hangControl(boolean fire){
        if(fire){
            hang.set(Relay.Value.kForward);
        } else {
            hang.set(Relay.Value.kReverse);
        }
    }
    public void trayControl(boolean fire){
        if(fire){
            trayFlip.set(DoubleSolenoid.Value.kForward);
        } else {
            trayFlip.set(DoubleSolenoid.Value.kReverse);
        }
    }
}
