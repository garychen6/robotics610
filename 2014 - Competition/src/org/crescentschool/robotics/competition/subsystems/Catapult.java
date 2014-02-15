/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.crescentschool.robotics.competition.OI;
import org.crescentschool.robotics.competition.constants.ElectricalConstants;

/**
 *
 * @author jamiekilburn
 */
public class Catapult extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    OI oi;
    Talon catapult;
    DigitalInput optical;
    private static Catapult instance;
    private DoubleSolenoid hardStopSol;

    private Catapult() {
        optical = new DigitalInput(1, ElectricalConstants.opticalPort);
        oi = OI.getInstance();
        catapult = new Talon(ElectricalConstants.catapultTalon);
        hardStopSol = new DoubleSolenoid(ElectricalConstants.shooterHardStopSol,5);
    }

    public static Catapult getInstance() {
        if (instance == null) {
            instance = new Catapult();
        }
        return instance;
    }

    public void setHardStop(boolean truss) {
        if (truss) {
            hardStopSol.set(DoubleSolenoid.Value.kForward);
        } else {
            hardStopSol.set(DoubleSolenoid.Value.kReverse);
        }
    }

    public boolean isLoading() {
        return !optical.get();
    }

    public void setMain(double v) {
        
        catapult.set(v);
    }

    public void initDefaultCommand() {
    }
}