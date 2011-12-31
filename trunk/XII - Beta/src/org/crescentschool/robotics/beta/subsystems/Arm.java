/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.beta.subsystems;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.crescentschool.robotics.beta.commands.*;
import org.crescentschool.robotics.beta.constants.*;

/**
 *
 * @author Patrick
 */
public class Arm extends Subsystem {

    private static Arm instance = null;
    //Jaguars
    private CANJaguar jaguarArmMaster;
    private CANJaguar jaguarArmSlave;
    
    public static Arm getInstance() {
        if (instance == null) {
            instance = new Arm();

            // Set default command here, like this:
             instance.setDefaultCommand(new ManualArmControl());
        }
        return instance;
    }

    // Initialize your subsystem here
    private Arm(){
        //initialize Jaguars
        try{
            jaguarArmMaster = new CANJaguar(ElectricalConstants.kJaguarArmMaster);
            jaguarArmMaster = new CANJaguar(ElectricalConstants.kJaguarArmMaster);
        } catch (CANTimeoutException ex){
            ex.printStackTrace();
        }
    }
    
    public void moveArm(double speed){
        try{
            jaguarArmMaster.setX(speed);
        } catch(CANTimeoutException ex){
            ex.printStackTrace();
        }
    }
    
    private void syncSlaves(){
        try {
            jaguarArmSlave.setX(jaguarArmMaster.getX());
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }
}
