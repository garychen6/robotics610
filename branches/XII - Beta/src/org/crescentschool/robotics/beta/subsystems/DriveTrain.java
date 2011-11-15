/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.beta.subsystems;

import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Solenoid;
import org.crescentschool.robotics.beta.OI;
import org.crescentschool.robotics.beta.commands.TankDrive;
import org.crescentschool.robotics.beta.constants.InputConstants;
import org.crescentschool.robotics.beta.constants.ElectricalConstants;
import org.crescentschool.robotics.beta.constants.PIDConstants;

/**
 *
 * @author Patrick
 */
public class DriveTrain extends Subsystem {

    private static DriveTrain instance = null;
    //Tower Mode
    private static boolean towerEnabled = false;
    //CAN Errors
    private static int canFaults = 0;
    //Jaguars
    private CANJaguar jaguarLeftMaster;
    private CANJaguar jaguarLeftSlave;
    private CANJaguar jaguarRightMaster;
    private CANJaguar jaguarRightSlave;
    //Shifter Solenoids
    private Solenoid shifterHigh;
    private Solenoid shifterLow;
    public boolean isHighGear;
    //CAN Error Flag
    boolean canInitialized = true;

    public static DriveTrain getInstance() {
        if (instance == null) {
            instance = new DriveTrain();

            // Set default command here, like this:
            instance.setDefaultCommand(new TankDrive());
        }
        return instance;
    }

    // Initialize your subsystem here
    private DriveTrain() {
        //Initialize Jaguars
        try {
            jaguarLeftMaster = new CANJaguar(ElectricalConstants.kJaguarLeftMaster);
            jaguarLeftSlave = new CANJaguar(ElectricalConstants.kJaguarLeftSlave);
            jaguarRightMaster = new CANJaguar(ElectricalConstants.kJaguarRightMaster);
            jaguarRightSlave = new CANJaguar(ElectricalConstants.kJaguarRightSlave);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
        //Initialize shifter solenoids
        shifterHigh = new Solenoid(ElectricalConstants.kSolenoidHighChannel);
        shifterLow = new Solenoid(ElectricalConstants.kSolenoidLowChannel);
        isHighGear = shifterHigh.get();
    }

    public void tankDrive() {
        try {
            jaguarLeftMaster.setX(OI.getInstance().getJoyDriver().getRawAxis(InputConstants.kDriverLeftXAxis));
            jaguarRightMaster.setX(OI.getInstance().getJoyDriver().getRawAxis(InputConstants.kDriverRightXAxis));
            syncSlaves();
            checkCAN();
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
            canInitialized = false;
        }
    }

    public void towerDrive(double xInput, double yInput) {
        try {
            jaguarLeftMaster.setX(-yInput - xInput);
            jaguarRightMaster.setX(-yInput + xInput);
            checkCAN();
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
            canInitialized = false;
        }
    }

    public void shift(boolean high) {
        shifterHigh.set(high);
        shifterLow.set(!high);
        isHighGear = high;
    }

    private void syncSlaves() {
        try {
            jaguarLeftSlave.setX(jaguarLeftMaster.getX());
            jaguarRightSlave.setX(jaguarRightMaster.getX());
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }

    public void towerDriveOn() {
        towerEnabled = true;
        try {
            jaguarLeftMaster.changeControlMode(CANJaguar.ControlMode.kPosition);
            jaguarRightMaster.changeControlMode(CANJaguar.ControlMode.kPosition);
            jaguarLeftMaster.setX(0);
            jaguarRightMaster.setX(0);
            jaguarLeftMaster.setPID(-PIDConstants.tdriveP, -PIDConstants.tdriveI, -PIDConstants.tdriveD);
            jaguarRightMaster.setPID(PIDConstants.tdriveP, PIDConstants.tdriveI, PIDConstants.tdriveD);
            jaguarLeftMaster.enableControl(0);
            jaguarRightMaster.enableControl(0);
        } catch (CANTimeoutException ex) {
            System.out.println(ex.toString());
            canInitialized = false;
        }
    }

    public void towerDriveOff() {
        towerEnabled = false;
        try {
            jaguarLeftMaster.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
            jaguarRightMaster.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
        } catch (CANTimeoutException ex) {
            System.out.println(ex.toString());
            canInitialized = false;
        }
    }
    
    //public void get

    public void checkCAN() {
        //Check to see if CANExceptions occur, if so, reinitialize CAN
        if (!canInitialized) {
            canFaults++;
            try {
                //Drivetrain
                jaguarLeftMaster.setSpeedReference(CANJaguar.SpeedReference.kEncoder);
                jaguarRightMaster.setSpeedReference(CANJaguar.SpeedReference.kEncoder);
                jaguarLeftMaster.setPositionReference(CANJaguar.PositionReference.kQuadEncoder);
                jaguarRightMaster.setPositionReference(CANJaguar.PositionReference.kQuadEncoder);
                jaguarLeftMaster.configEncoderCodesPerRev(256);
                jaguarRightMaster.configEncoderCodesPerRev(256);

                if (towerEnabled) {
                    towerDriveOn();
                } else {
                    towerDriveOff();
                }

                canInitialized = true;
            } catch (CANTimeoutException ex) {
                System.out.println(ex.toString());
                canInitialized = false;
            }
        }
    }
}
