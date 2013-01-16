package org.crescentschool.robotics.competition.subsystems;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.crescentschool.robotics.competition.OI;
import org.crescentschool.robotics.competition.commands.KajDrive;
import org.crescentschool.robotics.competition.constants.ElectricalConstants;

/**
 *
 */
public class DriveTrain extends Subsystem {

    CANJaguar jagRightMaster;
    Victor vicRightSlave;
    Victor vicRightSlave2;
    CANJaguar jagLeftMaster;
    Victor vicLeftSlave;
    Victor vicLeftSlave2;
    private boolean canError = false;

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new KajDrive());

    }

    DriveTrain() {
        try {
            jagRightMaster = new CANJaguar(2);
            jagLeftMaster = new CANJaguar(3);
            vicRightSlave = new Victor(4);
            vicRightSlave2 = new Victor(5);
            vicLeftSlave = new Victor(6);
            vicLeftSlave2 = new Victor (7);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }
    
    public static DriveTrain getInstance() {
        if (instance == null) {
            instance = new DriveTrain();
        }
        return instance;
    }
    
    public void setLeftVBus(double setpoint) {
        if (controlMode != 1) {
            initVBusMode();
        }
        try {
            jagLeftMaster.setX(setpoint);

        } catch (CANTimeoutException ex) {
            canError = true;
            handleCANError();
            ex.printStackTrace();
        }
        syncSlaves();
    }

    /**
     * Sets the target normalized voltage for the left side of the drivetrain.
     * @param setpoint The target normalized voltage from -1 to 1.
     */
    public void setRightVBus(double setpoint) {
        if (controlMode != 1) {
            initVBusMode();
        }
        try {
            jagRightMaster.setX(setpoint);

        } catch (CANTimeoutException ex) {
            canError = true;
            handleCANError();
            ex.printStackTrace();
        }
        syncSlaves();
    }
    
    public void handleCANError() {
        if (canError) {
            System.out.println("CAN Error!");
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            canError = false;
        }
    }
    
    /**
     * Sets the slaves at the same voltage as the masters.
     */
    private void syncSlaves() {
        double voltage = OI.getInstance().getDS().getBatteryVoltage();
        try {
            vicLeftSlave.setX(jagLeftMaster.getOutputVoltage() / voltage);
            vicLeftSlave2.setX(jagLeftMaster.getOutputVoltage() / voltage);
            vicRightSlave.setX(jagRightMaster.getOutputVoltage() / voltage);
            vicRightSlave2.setX(jagRightMaster.getOutputVoltage() / voltage);
        } catch (CANTimeoutException ex) {
            canError = true;
            handleCANError();
            ex.printStackTrace();
        }
    }
    
}