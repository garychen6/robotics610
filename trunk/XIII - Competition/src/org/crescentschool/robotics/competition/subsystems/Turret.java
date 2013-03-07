/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.subsystems;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.crescentschool.robotics.competition.commands.AMT_T_turn;
import org.crescentschool.robotics.competition.constants.ElectricalConstants;
import org.crescentschool.robotics.competition.constants.PIDConstants;
import org.crescentschool.robotics.competition.constants.PotConstants;

/**
 *
 * @author Warfa
 */
public class Turret extends Subsystem {

    CANJaguar turretJag;
    static Turret instance = null;
    double p, i, d;
    double position = PotConstants.turretCentre;
    // 1 = %VBus, 2 = Position
    private int controlMode = 2;
    boolean isLocked = false;

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
            initPosMode();
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
            handleCAN();
        }
    }

    public void initPosMode() {
        SmartDashboard.putString("Turret Mode", "Position");
        controlMode = 2;
        try {
            turretJag.configFaultTime(0.5);
            turretJag.configNeutralMode(CANJaguar.NeutralMode.kBrake);
            turretJag.setPositionReference(CANJaguar.PositionReference.kPotentiometer);
            turretJag.configPotentiometerTurns(10);
            turretJag.setPID(p, i, d);
            turretJag.changeControlMode(CANJaguar.ControlMode.kPosition);
            turretJag.configSoftPositionLimits(PotConstants.turretLoLimit, PotConstants.turretHiLimit);
            turretJag.enableControl();
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
            handleCAN();
        }
    }

    /**
     * Initializes Vbus mode for the Drivetrain.
     */
    private void initVBusMode() {
        SmartDashboard.putString("Turret Mode", "VBus");
        controlMode = 1;
        try {
            turretJag.configFaultTime(0.5);
            turretJag.configNeutralMode(CANJaguar.NeutralMode.kBrake);
            turretJag.setPositionReference(CANJaguar.PositionReference.kPotentiometer);
            turretJag.configPotentiometerTurns(10);
            turretJag.setPID(p, i, d);
            turretJag.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
            turretJag.configSoftPositionLimits(PotConstants.turretLoLimit, PotConstants.turretHiLimit);
            turretJag.enableControl();
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
            handleCAN();
        }
    }

    /**
     * Sets the target angle of the turret.
     * @param ang The target angle of the turret.
     */
    public void setPosition(double pos) {
        if (controlMode != 2) {
            initPosMode();
        }
        try {
            turretJag.setX(pos);
            position = pos;
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
            handleCAN();
        }

    }

    /**
     * Sets the target angle of the turret.
     * @param ang The target angle of the turret.
     */
    public void setVBus(double vBus) {
        if (controlMode != 1) {
            initVBusMode();
        }
        try {
            turretJag.setX(vBus);
            if (vBus < 0.05 && vBus > -0.05) {
                isLocked = true;
            } else {
                isLocked = false;
            }
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
            handleCAN();
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
            handleCAN();
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
            handleCAN();
            return 0;
        }

    }

    /**
     * The default command for the turret.
     */
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
//        setDefaultCommand(new AMT_T_turn());
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
    public void resetPID() {
        System.out.println("Turret PID Reset");
        if (controlMode == 1) {
            initVBusMode();
        } else {
            initPosMode();
        }
    }

    /**
     * Resets the turret's P and I values to the values in PID constants.
     */
    public void resetPosition() {
        try {
            position = turretJag.getPosition();
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
            handleCAN();
        }
    }

    /**
     * Gets if the turret is Locked On
     */
    public boolean isLocked() {
        return isLocked;
    }

    /**
     * Increments the turret's I value.
     * @param x The amount to increment the I value by.
     */
    public void incPosition(double inc) {
        if (controlMode != 2) {
            initPosMode();
        }
        //System.out.println("Position: " + position);
        position += inc;

        if (position > PotConstants.turretHiLimit) {
            position = PotConstants.turretHiLimit;
        }
        if (position < PotConstants.turretLoLimit) {
            position = PotConstants.turretLoLimit;
        }
        try {
            //SmartDashboard.putDouble("Turret Setpoint ", position);
            turretJag.setX(position);
            //System.out.println("Turret Target: "+ position);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
            handleCAN();
        }
    }

    public void handleCAN() {
        System.out.println("CAN Error!");
        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        resetPID();
    }
}
