/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.subsystems;

import edu.wpi.first.wpilibj.CANJaguar;
import org.crescentschool.robotics.competition.PID.PIDController;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 * @author Warfa
 */
public class Shooter extends Subsystem {
    
    static Shooter instance = null;
    CANJaguar shooter;
    PIDController pidController;
   
    public static Shooter getInstance(){
        if(instance == null){
            instance = new Shooter();
        }
        return instance;
    }
   
    Shooter(){
        try {
            shooter = new CANJaguar(1);
            shooter.changeControlMode(CANJaguar.ControlMode.kVoltage);
            shooter.setSpeedReference(CANJaguar.SpeedReference.kQuadEncoder);
            shooter.configEncoderCodesPerRev(256);
            shooter.enableControl(0);
            pidController = new PIDController(0.1,0.01);
            new Thread(pidController).start();
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }
    public void setPID(double p, double i, double d){
        pidController.setP(p);
        pidController.setI(i);
        pidController.setD(d);
    }
    public void setSpeed(double rpm){
        pidController.setSetpoint(rpm); 
        
    }
    
    synchronized public void setShooter(double val){
        try {
            shooter.setX(val);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }
    public double getSpeed() throws CANTimeoutException{
        return shooter.getSpeed();
        //return 0;
    }
    protected void initDefaultCommand() {
    }
}
