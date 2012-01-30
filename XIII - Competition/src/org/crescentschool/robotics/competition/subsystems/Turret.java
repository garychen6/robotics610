/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.subsystems;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.crescentschool.robotics.competition.constants.ElectricalConstants;
import org.crescentschool.robotics.competition.constants.PIDConstants;

/**
 *
 * @author Warfa
 */
public class Turret extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    CANJaguar turretJag;
    static Turret instance = null;

    public static Turret getInstance() {
        if (instance == null) {
            instance = new Turret();
        }
        return instance;
    }

    private Turret() {
        try {
            turretJag = new CANJaguar(ElectricalConstants.TurretJaguar);
            turretJag.changeControlMode(CANJaguar.ControlMode.kPosition);
            turretJag.setPositionReference(CANJaguar.PositionReference.kPotentiometer);
            turretJag.configEncoderCodesPerRev(256);
            turretJag.setPID(PIDConstants.turretP, PIDConstants.turretI, PIDConstants.turretD);
            turretJag.changeControlMode(CANJaguar.ControlMode.kSpeed);
            turretJag.enableControl(0);
            turretJag.changeControlMode(CANJaguar.ControlMode.kPosition);
            turretJag.enableControl(0);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }

    public void setX(double ang) {
        try {
            turretJag.setX(ang);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    
    }
    public double getPos() {
        try {
           return turretJag.getX();
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
            return 0;
        }
    
    }
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}
