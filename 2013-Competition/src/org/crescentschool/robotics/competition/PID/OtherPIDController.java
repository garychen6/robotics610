/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.PID;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.crescentschool.robotics.competition.subsystems.OtherShooter;
import org.crescentschool.robotics.competition.subsystems.Shooter;

/**
 *
 * @author Warfa
 */
public class OtherPIDController extends Thread {

    private static double p = 0;
    private static double i = 0;
    private static double d = 0;

    private static double frontSetpoint = 0;
    private static double backSetpoint = 0;
    
    private static double frontOutput = 0;
    private static double frontOutputChange = 0;
    
    private static double backOutput = 0;
    private static double backOutputChange = 0;
    
    private static double[] frontError = new double[3];
     private static double[] backError = new double[3];
 
    private static Timer timer;
    private static double prevTime;
    private static double time;
    private static double frontCurrent;
    private static double backCurrent;
    
    private static OtherPIDController instance;
    public static OtherPIDController getInstance(){
        if(instance == null){
            instance = new OtherPIDController(0.0,0.0);
        }
        return instance;
    }

    public OtherPIDController(double p, double i) {
        this.p = p;
        this.i = p;
        timer = new Timer();
        timer.start();
        System.out.println("PID COntroller running");
    }

    /**
     * @return the p
     */
    public double getP() {
        return p;
    }

    /**
     * @return the i
     */
    public double getI() {
        return i;
    }

    

    /**
     * @param frontSetpoint the frontSetpoint to set
     */
    synchronized public void setFrontSetpoint(double frontSetpoint) {
        this.setFrontSetpoint(frontSetpoint);
        frontOutput = 0;
        frontError[0] = 0;
        frontError[1] = 0;
        frontError[2] = 0;
        
    }
    
        /**
     * @param backSetpoint the backSetpoint to set
     */
    synchronized public void setBackSetpoint(double frontSetpoint) {
        this.setBackSetpoint(backSetpoint);
        backOutput = 0;
        backError[0] = 0;
        backError[1] = 0;
        backError[2] = 0;
        
    }

    synchronized public void run() {
        System.out.println("Calculating");
        try {
            frontCurrent = OtherShooter.getInstance().getFrontSpeed();
            backCurrent = OtherShooter.getInstance().getBackSpeed();
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
        time = timer.get();
        OtherShooter.getInstance().setFrontShooter(calculateFront(frontCurrent, frontSetpoint, time));
        OtherShooter.getInstance().setBackShooter(calculateBack(backCurrent, backSetpoint, time));
        prevTime = time;
        
    }
    
    public static double calculateFront(double speed, double setpoint, double time){
        
        frontError[2] = frontError[1];
        frontError[1] = frontError[0];
        frontError[0] = getFrontSetpoint() - frontCurrent;

        frontOutputChange = (frontError[0] - frontError[1]) * p + (frontError[0]) * i + ((frontError[0] - 2 * frontError[1] + frontError[2]) / (time - prevTime)) * d;
        frontOutput += frontOutputChange ;
        SmartDashboard.putNumber("Output", frontOutput+(12.0/3000.0)*getFrontSetpoint());
        return(frontOutput+(12.0/3000.0)*getFrontSetpoint());
        
    }
    public static double calculateBack(double speed, double setpoint, double time){
        
        backError[2] = backError[1];
        backError[1] = backError[0];
        backError[0] = getBackSetpoint() - backCurrent;

        backOutputChange = (backError[0] - backError[1]) * p + (backError[0]) * i + ((backError[0] - 2 * backError[1] + backError[2]) / (time - prevTime)) * d;
        backOutput += backOutputChange ;
        SmartDashboard.putNumber("Output", backOutput+(12.0/3000.0)*getFrontSetpoint());
        
        return (backOutput+(12.0/3000.0)*getFrontSetpoint());
        
        
    }
    

    /**
     * @param p the p to set
     */
    synchronized public void setP(double p) {
        frontOutput = 0;
        this.p = p;
        System.out.println("P");
    }

    /**
     * @param i the i to set
     */
    synchronized public void setI(double i) {
       frontOutput = 0;
        this.i = i;
        System.out.println("I");
    }

    /**
     * @param d the d to set
     */
    synchronized public void setD(double d) {
        frontOutput = 0;
        this.d = d;
        System.out.println("D");
    }

    /**
     * @return the d
     */
    public double getD() {
        return d;
    }

    /**
     * @return the backSetpoint
     */
    public static double getBackSetpoint() {
        return backSetpoint;
    }



    /**
     * @return the frontSetpoint
     */
    public static double getFrontSetpoint() {
        return frontSetpoint;
    }

  
}

    