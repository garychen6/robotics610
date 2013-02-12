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
    Solenoid feeder;
    Solenoid powerTakeOff;
    Solenoid post;
    Solenoid tray;
    DoubleSolenoid shooterAngle;
    //Checks if the pneumatics object is already running.  If not, create one.

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
        post = new Solenoid(ElectricalConstants.digitalModule, ElectricalConstants.post);
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

    public void angleFeeder(boolean fire) {
        if (fire == true) {
            shooterAngle.set(DoubleSolenoid.Value.kForward);
        } else {
            shooterAngle.set(DoubleSolenoid.Value.kReverse);
        }
    }
    //Allows other classes to get the pressure switch value.

    public boolean getSwitchValue() {
        return compressor.getPressureSwitchValue();
    }

    public void postUp(boolean fire) {
        post.set(fire);
    }
}
