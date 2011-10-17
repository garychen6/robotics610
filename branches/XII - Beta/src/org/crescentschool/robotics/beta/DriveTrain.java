/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.beta;

import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Solenoid;
import org.crescentschool.robotics.beta.constants.ElectricalBoard;

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
    
    public static DriveTrain getInstance() {
        if (instance == null) {
            instance = new DriveTrain();

            // Set default command here, like this:
            // instance.setDefaultCommand(new CommandIWantToRun());
        }
        return instance;
    }

    // Initialize your subsystem here
    private DriveTrain() {
        //Initialize Jaguars
        try {
            jaguarLeftMaster = new edu.wpi.first.wpilibj.CANJaguar(ElectricalBoard.kJaguarLeftMaster);
            jaguarLeftSlave = new edu.wpi.first.wpilibj.CANJaguar(ElectricalBoard.kJaguarLeftSlave);
            jaguarRightMaster = new edu.wpi.first.wpilibj.CANJaguar(ElectricalBoard.kJaguarRightMaster);
            jaguarRightSlave = new edu.wpi.first.wpilibj.CANJaguar(ElectricalBoard.kJaguarRightSlave);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
       //Initialize shifter solenoids
       shifterHigh = new Solenoid(ElectricalBoard.kSolenoidHighChannel);
       shifterLow = new Solenoid(ElectricalBoard.kSolenoidLowChannel);
    }
}
