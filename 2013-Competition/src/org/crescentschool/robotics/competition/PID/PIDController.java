/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.PID;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
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
    private double setpoint = 0;
    private double output = 0;
    private double outputChange = 0;
    private double[] error = new double[3];
    private Shooter shooter;
    private Timer timer;
    private double prevTime;
    private double time;
    private double current;
    private static PIDController instance;
    public static PIDController getInstance(){
        if(instance == null){
            instance = new PIDController(0.0,0.0);
        }
        return instance;
    }

    public PIDController(double p, double i) {
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
        System.out.println("Calculating");
        try {
            current = Shooter.getInstance().getSpeed();
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
        time = timer.get();
        error[2] = error[1];
        error[1] = error[0];
        error[0] = setpoint - current;

        outputChange = (error[0] - error[1]) * p + (error[0]) * i + ((error[0] - 2 * error[1] + error[2]) / (time - prevTime)) * d;
        output += outputChange ;
        SmartDashboard.putNumber("Output", output+(12.0/3000.0)*setpoint);
        Shooter.getInstance().setShooter(output+(12.0/3000.0)*setpoint);
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
}
