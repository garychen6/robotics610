/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.PID;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import org.crescentschool.robotics.competition.subsystems.Shooter;

/**
 *
 * @author Warfa
 */
public class PIDController implements Runnable {

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

    public PIDController(double p, double i) {
        this.p = p;
        this.i = p;
        shooter = Shooter.getInstance();
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
        error[0] = 0;
        error[1] = 0;
        error[2] = 0;
    }

    synchronized public void run() {
        System.out.println("Calculating");
        try {
            current = shooter.getSpeed();
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
        time = timer.get();
        error[2] = error[1];
        error[1] = error[0];
        error[0] = setpoint - current;

        outputChange = (error[0] - error[1]) * p + (error[0]) * i + ((error[0] - 2 * error[1] + error[2]) / (time - prevTime)) * d;
        output += outputChange;
        shooter.setShooter(output);
        prevTime = time;
        run();
    }

    /**
     * @param p the p to set
     */
    synchronized public void setP(double p) {
        this.p = p;
        System.out.println("P");
    }

    /**
     * @param i the i to set
     */
    synchronized public void setI(double i) {
        this.i = i;
        System.out.println("I");
    }

    /**
     * @param d the d to set
     */
    synchronized public void setD(double d) {
        this.d = d;
        System.out.println("D");
    }
}
