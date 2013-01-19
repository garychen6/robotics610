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
public class IanPID extends Thread {

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
    private static IanPID instance;
    private Subsystem subsystem;

    public static IanPID getInstance(Subsystem subsystem) {
        if (instance == null) {
            instance = new IanPID(0.0, 0.0, subsystem);
        }
        return instance;
    }

    public IanPID(double p, double i, Subsystem subsystem) {
        this.p = p;
        this.i = p;
        timer = new Timer();
        timer.start();
        this.subsystem = subsystem;
        System.out.println("IanPID Controller running for: " + subsystem.getName());
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
        output += outputChange;
        
        if (subsystem.getName().equals("Shooter")) {
            SmartDashboard.putNumber("Output", output + (12.0 / 3000.0) * setpoint);
            ((Shooter)subsystem).getInstance().setShooter(output + (12.0 / 3000.0) * setpoint);
        } else if(subsystem.getName().equals("DriveTrain")){
            //SmartDashboard.putNumber("Output", output + (12.0/3000.0)*setpoint);
            //((DriveTrain)subsystem).getInstance().setSpeed(output + (12.0/3000.0)*setpoint);
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
}
