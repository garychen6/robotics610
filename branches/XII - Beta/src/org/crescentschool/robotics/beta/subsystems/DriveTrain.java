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
import org.crescentschool.robotics.beta.constants.PhysicalConstants;

/**
 *
 * @author Patrick
 */
public class DriveTrain extends Subsystem {

    private static DriveTrain instance = null;
    //Jaguars
    private CANJaguar jaguarLeftMaster;
    private CANJaguar jaguarLeftSlave;
    private CANJaguar jaguarRightMaster;
    private CANJaguar jaguarRightSlave;
    //Shifter Solenoids
    private Solenoid shifterHigh;
    private Solenoid shifterLow;
    public boolean isHighGear;
    
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
            jaguarLeftMaster = new CANJaguar(PhysicalConstants.kJaguarLeftMaster);
            jaguarLeftSlave = new CANJaguar(PhysicalConstants.kJaguarLeftSlave);
            jaguarRightMaster = new CANJaguar(PhysicalConstants.kJaguarRightMaster);
            jaguarRightSlave = new CANJaguar(PhysicalConstants.kJaguarRightSlave);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
       //Initialize shifter solenoids
       shifterHigh = new Solenoid(PhysicalConstants.kSolenoidHighChannel);
       shifterLow = new Solenoid(PhysicalConstants.kSolenoidLowChannel);
       isHighGear = shifterHigh.get();
    }
    public void tankDrive(){
        try{
            jaguarLeftMaster.setX(OI.getInstance().getJoyDriver().getRawAxis(PhysicalConstants.kDriverLeftXAxis));
            jaguarRightMaster.setX(OI.getInstance().getJoyDriver().getRawAxis(PhysicalConstants.kDriverRightXAxis));
        } catch(CANTimeoutException ex){
            ex.printStackTrace();
        }
    }
    public void shift(boolean high){
        shifterHigh.set(high);
        shifterLow.set(!high);
        isHighGear = high;
    }
    private void syncSlaves(){
        try {
            jaguarLeftSlave.setX(jaguarLeftMaster.getX());
            jaguarRightSlave.setX(jaguarRightMaster.getX());
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }
}
