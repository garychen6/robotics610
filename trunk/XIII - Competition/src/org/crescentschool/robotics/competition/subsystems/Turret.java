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

    CANJaguar turretJag;
    static Turret instance = null;
    double p, i, d;

    /**
     * Ensures that only one turret is instantiated.
     * @return The singleton turret instance.
     */
    public static Turret getInstance() {
        if (instance == null) {
            instance = new Turret();
        }
        return instance;
    }

    private Turret() {
        p = PIDConstants.turretP;
        i = PIDConstants.turretI;
        d = 0;
        try {
            turretJag = new CANJaguar(ElectricalConstants.TurretJaguar);
            turretJag.changeControlMode(CANJaguar.ControlMode.kPosition);
            turretJag.setPositionReference(CANJaguar.PositionReference.kPotentiometer);
            turretJag.setPID(p, i, d);
            //turretJag.changeControlMode(CANJaguar.ControlMode.kSpeed);
            //turretJag.enableControl(0);
            turretJag.changeControlMode(CANJaguar.ControlMode.kPosition);
            turretJag.enableControl(0);
            turretJag.configSoftPositionLimits(135, -135);


            // turretJag.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
            // turretJag.enableControl(0);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Sets the target angle of the turret.
     * @param ang The target angle of the turret.
     */
    public void setX(double ang) {
        try {
            turretJag.setX(ang);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }

    }

    /**
     * Gets the target position of the turret.
     * @return The target position of the turret as an angle.
     */
    public double getPosSet() {
        try {
            return turretJag.getX();
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
            return 0;
        }

    }

    /**
     * Gets the  position of the turret.
     * @return The position of the turret as an angle.
     */
    public double getPos() {
        try {
            return turretJag.getPosition();
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
            return 0;
        }

    }

    /**
     * The default command for the turret.
     */
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }

    /**
     * Increments the turret's P value.
     * @param x The amount to increment the P value by.
     */
    public void incTurretP(double x) {
        p += x;
        System.out.println("Turret P: " + p + " I: " + i);
    }

    /**
     * Decrements the turret's P value.
     * @param x The amount to decrement the P value by.
     */
    public void decTurretP(double x) {
        p -= x;
        System.out.println("Turret P: " + p + " I: " + i);
    }

    /**
     * Increments the turret's I value.
     * @param x The amount to increment the I value by.
     */
    public void incTurretI(double x) {
        i += x;
        System.out.println("Turret P: " + p + " I: " + i);
    }

    /**
     * Decrements the turret's I value.
     * @param x The amount to decrement the I value by.
     */
    public void decTurretI(double x) {
        i -= x;
        System.out.println("Turret P: " + p + " I: " + i);
    }

    /**
     * Resets the turret's P and I values to the values in PID constants.
     */
    public void resetTurretPID() {
        try {
            turretJag.setPID(p, i, d);
            turretJag.enableControl();

        } catch (CANTimeoutException ex) {
            ex.printStackTrace();

        }
    }
}
