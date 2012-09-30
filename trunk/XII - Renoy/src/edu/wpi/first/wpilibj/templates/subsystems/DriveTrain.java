/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Main.OI;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.constants.ElectricalConstants;
import edu.wpi.first.wpilibj.constants.PIDConstants;
import edu.wpi.first.wpilibj.templates.commands.KajDrive;

/**
 * Warfa Jibril
 * Mr. Lim
 * ICS3U
 * March 6, 2012
 */
public class DriveTrain extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    // Initialize all the robot systems pertaining to DriveTrain
    OI oi;
    CANJaguar leftMaster;
    CANJaguar rightMaster;
    CANJaguar leftSlave;
    CANJaguar rightSlave;
    public static DriveTrain instance = null;
    Compressor compressor;
    DoubleSolenoid highGear;
    boolean vBus = true;

    // Allows commands and Subsystems to use the current instance of Arm
    public static DriveTrain getDriveTrain() {
        if (instance == null) {
            instance = new DriveTrain();
        }
        return instance;
    }
    // DriveTrain Constructor

    public DriveTrain() {
        try {
            oi = OI.getInstance();
            leftMaster = new CANJaguar(ElectricalConstants.kJaguarLeftMaster);
            rightMaster = new CANJaguar(ElectricalConstants.kJaguarRightMaster);
            leftSlave = new CANJaguar(ElectricalConstants.kJaguarLeftSlave);
            rightSlave = new CANJaguar(ElectricalConstants.kJaguarRightSlave);
            compressor = new Compressor(ElectricalConstants.kCompressorPressureSwitchChannel, ElectricalConstants.kCompressorRelayChannel);
            compressor.start();
            highGear = new DoubleSolenoid(ElectricalConstants.kSolenoidModulePort, ElectricalConstants.kSolenoidHighChannel, ElectricalConstants.kSolenoidLowChannel);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
        initVBus();
    }
    //Initialize Percent V-bus Mode

    private void initVBus() {
        vBus = true;
        try {
            //no sensors and all motors set to coast
            leftMaster.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
            leftMaster.configNeutralMode(CANJaguar.NeutralMode.kCoast);
            leftMaster.configFaultTime(0.5);
            rightMaster.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
            rightMaster.configNeutralMode(CANJaguar.NeutralMode.kCoast);
            rightMaster.configFaultTime(0.5);
            leftSlave.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
            leftSlave.configNeutralMode(CANJaguar.NeutralMode.kCoast);
            leftSlave.configFaultTime(0.5);
            rightSlave.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
            rightSlave.configNeutralMode(CANJaguar.NeutralMode.kCoast);
            rightSlave.configFaultTime(0.5);
            leftMaster.enableControl();
            rightMaster.enableControl();
            leftSlave.enableControl();
            rightSlave.enableControl();
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
            try {
                Thread.sleep(500);
                initVBus();
            } catch (InterruptedException ex1) {
                ex1.printStackTrace();
            }
        }
    }
    //Sets the power to the left side of the driveTrain
    public void setVBusL(double power) {
        try {
            leftMaster.setX(-power);

        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
            initVBus();
        }
    }

    //Sets the power to the right side of the driveTrain
    public void setVBusR(double power) {
        try {
            rightMaster.setX(-power);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
            initVBus();
        }
    }
    //Synchronizes the secondary motors on each side of the driveTrain
    public void syncSlaves() {
        try {
            leftSlave.setX(leftMaster.getX());
            rightSlave.setX(rightMaster.getX());
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }
    //Sets the initial DriveTrain subsystem command to KajDrive
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new KajDrive());
    }
}
