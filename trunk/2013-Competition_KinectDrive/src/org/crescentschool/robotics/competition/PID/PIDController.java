/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.PID;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.crescentschool.robotics.competition.subsystems.ShooterSensor;

/**
 *
 * @author Warfa
 */
public class PIDController extends Thread {

    private double kP = 0;
    private double kI = 0;
    private double kD = 0;
    private double p = 0;
    private double i = 0;
    private double d = 0;
    private double ff = 1.5 / 580.0;
    private double setpoint = 0;
    private double output = 0;
    private double outputChange = 0;
    private double[] error = new double[3];
    private Timer timer;
    private double prevTime;
    private double time;
    private double current;
    private CANJaguar controller;
    private Counter opticalSensor;
    
    
   public PIDController(double p, double i, double d, double ff, CANJaguar controller, ShooterSensor gearTooth) {
        this.ff = ff;
        this.kP = p;
        this.kI = i;
        this.kD = d;
        timer = new Timer();
        timer.start();
        if (gearTooth != null) {
            this.opticalSensor = gearTooth;
            //Measure the period of the white part of the disc
            this.opticalSensor.setSemiPeriodMode(false);
            this.opticalSensor.setMaxPeriod(1);
            this.opticalSensor.start();
        }
        this.controller = controller;
    }

    /**
     * @return the p
     */
    public double getP() {
        return kP;
    }

    /**
     * @return the i
     */
    public double getI() {
        return kI;
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
            if (opticalSensor != null) {
                current = -(60/(opticalSensor.getPeriod()*(8.0/7.0)));
                //System.out.println(current);
            } else {
                current = controller.getSpeed();
            }
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
        time = timer.get();
        error[2] = error[1];
        error[1] = error[0];
        error[0] = setpoint - current;
        p= (error[0] - error[1]) * kP; 
        i=  (error[0]) * kI;
        d = ((error[0] - 2 * error[1] + error[2]) / (time - prevTime)) * kD;
        outputChange = p+i+d;
        output += outputChange;
        SmartDashboard.putNumber("Error", error[0]);
        SmartDashboard.putNumber("P",p);
        SmartDashboard.putNumber("I",i);
        SmartDashboard.putNumber("D",d);
        SmartDashboard.putNumber("SpeedNum", current);
        SmartDashboard.putNumber("SpeedNumGraph", current);
        SmartDashboard.putNumber("OpticalPeriod", opticalSensor.getPeriod());
        try {
            SmartDashboard.putNumber("JagNum", controller.getSpeed());
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
        
        SmartDashboard.putNumber("Output", output + ff * setpoint);
        try {
           // System.out.println(time +","+current);
            controller.setX(output + ff * setpoint);
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
        this.kP = p;
        System.out.println("P");
    }

    /**
     * @param i the i to set
     */
    synchronized public void setI(double i) {
        output = 0;
        this.kI = i;
        System.out.println("I");
    }

    /**
     * @param d the d to set
     */
    synchronized public void setD(double d) {
        output = 0;
        this.kD = d;
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
