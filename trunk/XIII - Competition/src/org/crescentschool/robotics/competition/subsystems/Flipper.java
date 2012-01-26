/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.subsystems;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.crescentschool.robotics.competition.constants.ElectricalConstants;
import org.crescentschool.robotics.competition.constants.PIDConstants;

/**
 *
 * @author Warfa
 */
public class Flipper extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    CANJaguar jagFlip;
    Flipper instance = null;
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    public Flipper getInstance(){
        if(instance == null){
            instance = new Flipper();
        }
        return instance;
    }
    private Flipper(){
        try {
            jagFlip = new CANJaguar(ElectricalConstants.JagFlipper);
            jagFlip.configFaultTime(0.5);
            jagFlip.configNeutralMode(CANJaguar.NeutralMode.kCoast);
            jagFlip.setPositionReference(CANJaguar.PositionReference.kPotentiometer);
            jagFlip.changeControlMode(CANJaguar.ControlMode.kPosition);
            jagFlip.setPID(PIDConstants.flipP, PIDConstants.flipI, PIDConstants.flipD);
            jagFlip.enableControl(0);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    
    }
    public void setFlippers(double angle){
        try {
            double potT = 0.0028;
            jagFlip.setX(angle*potT);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }
}
