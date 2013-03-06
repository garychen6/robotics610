/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.subsystems;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.crescentschool.robotics.competition.constants.ElectricalConstants;

/**
 *
 * @author jeffrey
 */
public class Intake extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    private static Intake instance = null;
    CANJaguar positionArm;
    Victor roller;
    public static Intake getInstance(){
        if(instance == null){
            instance = new Intake();
        }
        return instance;
    }
    Intake(){
        try{ 
            positionArm = new CANJaguar(ElectricalConstants.jagIntakePosition);
            roller = new Victor(ElectricalConstants.rollerVictor);
        } catch (CANTimeoutException ex) {
            System.out.println("CAN TIMEOUT!!! (Roller)");
        }
    }
    public void rollerForward(boolean forward){
        if(forward){
            roller.set(1);
        } else {
            roller.set(0);
        }
    }
    public void rollerReverse(boolean reverse){
        if(reverse){
            roller.set(-1);
        } else {
            roller.set(0);
        }
    }
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}
