/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.PID;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.crescentschool.robotics.competition.subsystems.Shooter;

/**
 *
 * @author Warfa
 */
public class ClassicPID extends Thread {
    
    private double p = 0;
    private double i = 0;
    private double d = 0;
    private double ff = 0;
    private double setpoint = 0;
    private double currentSpeed = 0;
    double error = 0;
    double totalError = 0;
    private Timer timer;
    private double prevTime = 0;
    private double time = 0;
    private double output = 0;
    private static ClassicPID instance = null;
    private Subsystem subsystem = null;
    
    public static ClassicPID getInstance(Subsystem subsystem){
        if(instance == null){
            instance = new ClassicPID(subsystem);
        }
        return instance;
    }
    ClassicPID(Subsystem subsystem){
        timer = new Timer();
        timer.start();
        this.subsystem = subsystem;
        System.out.println("ClassicPID Controller running for: " + subsystem.getName());

    }

    /**
     * @return the p
     */
    public double getP() {
        return p;
    }

    /**
     * @param p the p to set
     */
    public void setP(double p) {
        this.p = p;
    }

    /**
     * @return the i
     */
    public double getI() {
        return i;
    }

    /**
     * @param i the i to set
     */
    public void setI(double i) {
        this.i = i;
    }

    /**
     * @return the d
     */
    public double getD() {
        return d;
    }

    /**
     * @param d the d to set
     */
    public void setD(double d) {
        this.d = d;
    }

    /**
     * @return the setpoint
     */
    public double getSetpoint() {
        return setpoint;
    }

    /**
     * @param setpoint the setpoint to set
     */
    public void setSetpoint(double setpoint) {
        this.setpoint = setpoint;
    }

    /**
     * @return the currentSpeed
     * 
     * 
     * 
     */
    public double getCurrentSpeed() {
        return currentSpeed;
    }

    /**
     * @param currentSpeed the currentSpeed to set
     */
    synchronized public void run() {
        time = timer.get();
        try {
            currentSpeed = Shooter.getInstance().getSpeed();
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
        error = setpoint - currentSpeed;
        totalError += error;
        output = (ff*setpoint)+(p*error)+Math.min(12,(i*totalError*(time-prevTime)))+(d*error)/(time-prevTime);
        if(subsystem.getName().equals("Shooter")){
            SmartDashboard.putNumber("Output", output);
            ((Shooter)subsystem).getInstance().setShooter(output);
        }
        prevTime = time;
    }
}
