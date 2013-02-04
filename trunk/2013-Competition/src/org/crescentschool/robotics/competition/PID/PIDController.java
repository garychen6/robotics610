/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.PID;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.GearTooth;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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

    
     
    public PIDController(double p, double i, double d, double ff, CANJaguar controller, GearTooth opticalSensor) {
        /*
        this.ff = ff;
        this.kP = p;
        this.kI = i;
        this.kD = d;
        timer = new Timer();
        timer.start();
        this.opticalSensor = opticalSensor;
        this.opticalSensor.start();
        this.controller = controller;
        */
    }

}
