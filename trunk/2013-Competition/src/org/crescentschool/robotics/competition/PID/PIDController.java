/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.PID;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.crescentschool.robotics.competition.subsystems.Shooter;

/**
 *
 * @author Warfa
 */
public class PIDController extends Thread {

    private double p = 0;
    private double i = 0;
    private double d = 0;
    private double ff = 0;
    private double setpoint = 0;
    private double output = 0;
    private double outputChange = 0;
    private double[] error = new double[3];
    private Timer timer;
    private double prevTime;
    private double time;
    private double current;
    private static IanPID instance;
    private CANJaguar controller;



    public PIDController(double p, double i,double d, CANJaguar controller) {
        this.p = p;
        this.i = i;
        this.d = d;
        timer = new Timer();
        timer.start();
        this.controller = controller;
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
     * @return the setpoint
     */
    public double getSetpoint() {
        return setpoint;
    }

    /**
     * @param setpoint the setpoint to set
     */
    synchronized public void setSetpoint(double setpoint) {
        this.setpoint = setpoint;
        output = 0;
        error[0] = 0;
        error[1] = 0;
        error[2] = 0;

    }

    synchronized public void run() {
        try {
            current = controller.getSpeed();
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
        time = timer.get();
        error[2] = error[1];
        error[1] = error[0];
        error[0] = setpoint - current;

        outputChange = (error[0] - error[1]) * p + (error[0]) * i + ((error[0] - 2 * error[1] + error[2]) / (time - prevTime)) * d;
        output += outputChange;
     
            SmartDashboard.putNumber("Output", output + ff * setpoint);
        try {
            controller.setX(output + getFf() * setpoint);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
  
        
        prevTime = time;
    }

    /**
     * @param p the p to set
     */
    synchronized public void setP(double p) {
        output = 0;
        this.p = p;
        System.out.println("P");
    }

    /**
     * @param i the i to set
     */
    synchronized public void setI(double i) {
        output = 0;
        this.i = i;
        System.out.println("I");
    }

    /**
     * @param d the d to set
     */
    synchronized public void setD(double d) {
        output = 0;
        this.d = d;
        System.out.println("D");
    }

    /**
     * @return the ff
     */
    public double getFf() {
        return ff;
    }

    /**
     * @param ff the ff to set
     */
    public void setFf(double ff) {
        this.ff = ff;
    }
}
