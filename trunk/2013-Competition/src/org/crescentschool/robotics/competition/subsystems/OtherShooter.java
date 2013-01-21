/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.subsystems;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.command.Subsystem;
//import org.crescentschool.robotics.competition.PID.OtherPIDController;
import org.crescentschool.robotics.competition.PID.PIDController;

/**
 *
 * @author Warfa
 */
public class OtherShooter extends Subsystem {

    static OtherShooter instance = null;
    CANJaguar frontWheel;
    CANJaguar backWheel;
    PIDController frontPidController;
    PIDController backPidController;
    Thread PID;

    public static OtherShooter getInstance() {
        if (instance == null) {
            instance = new OtherShooter();
        }
        return instance;
    }

    OtherShooter() {
        try {
            frontWheel = new CANJaguar(2);
            frontWheel.changeControlMode(CANJaguar.ControlMode.kSpeed);
            frontWheel.setSpeedReference(CANJaguar.SpeedReference.kQuadEncoder);
            frontWheel.configEncoderCodesPerRev(256);
            frontWheel.changeControlMode(CANJaguar.ControlMode.kVoltage);
            frontWheel.configNeutralMode(CANJaguar.NeutralMode.kCoast);
            frontWheel.enableControl(0);
            frontPidController = new PIDController(0, 0, 0, frontWheel, null);
            //pidController = ClassicPID.getInstance();
            frontPidController.start();

            backWheel = new CANJaguar(3);
            backWheel.changeControlMode(CANJaguar.ControlMode.kSpeed);
            backWheel.setSpeedReference(CANJaguar.SpeedReference.kQuadEncoder);
            backWheel.configEncoderCodesPerRev(256);
            backWheel.changeControlMode(CANJaguar.ControlMode.kVoltage);
            backWheel.configNeutralMode(CANJaguar.NeutralMode.kCoast);
            backWheel.enableControl(0);
            backPidController = new PIDController(0, 0, 0, backWheel, null);
            //pidController = ClassicPID.getInstance();
            backPidController.start();


            
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }

    public double getFrontVoltage() throws CANTimeoutException {
        return frontWheel.getOutputVoltage();
    }

    public double getBackVoltage() throws CANTimeoutException {
        return backWheel.getOutputVoltage();
    }

    public void setPID(double p, double i, double d) {
        frontPidController.setP(p);
        frontPidController.setI(i);
        frontPidController.setD(d);
        backPidController.setP(p);
        backPidController.setI(i);
        backPidController.setD(d);
    }

    public void setFrontSpeed(double rpm) {
        frontPidController.setSetpoint(rpm);
    }

    public void setBackSpeed(double rpm) {
        frontPidController.setSetpoint(rpm);
    }

    synchronized public void setFrontShooter(double val) {
        try {
            frontWheel.setX(val);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }

    synchronized public void setBackShooter(double val) {
        try {
            backWheel.setX(val);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }

    public double getFrontSpeed() throws CANTimeoutException {
        return frontWheel.getSpeed();
    }

    public double getBackSpeed() throws CANTimeoutException {
        return backWheel.getSpeed();
    }

    protected void initDefaultCommand() {
    }

    public PIDController getFrontPIDController() throws CANTimeoutException {
        return frontPidController;
    }
    public PIDController getBackPIDController() throws CANTimeoutException { 
        return backPidController;
    }
}
