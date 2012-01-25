/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.kitBot.subsystems;

import org.crescentschool.robotics.constants.ElectricalConstants;
import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 * @author Warfa
 */
public class Shooter extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    static Shooter instance = null;
    CANJaguar jagBottom;
    CANJaguar jagTop;
    
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
    public static Shooter getInstance(){
        if(instance == null){
            instance = new Shooter();
        }
        return instance;
    }
    
    private Shooter(){
        try {
            jagBottom = new CANJaguar(ElectricalConstants.kJagShooterBottomID);
            jagTop = new CANJaguar(ElectricalConstants.kJagShooterTopID);
            jagBottom.changeControlMode(CANJaguar.ControlMode.kSpeed);
            jagTop.changeControlMode(CANJaguar.ControlMode.kSpeed);
            jagBottom.setSpeedReference(CANJaguar.SpeedReference.kQuadEncoder);
            jagTop.setSpeedReference(CANJaguar.SpeedReference.kQuadEncoder);
            jagBottom.setPID(-0.17, 0.003, 0);
            jagTop.setPID(-0.17, 0.003, 0);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }
}
