/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.templates.commands.ShooterPIDCommand;

/**
 *
 * @author Warfa
 */
public class Shooter extends Subsystem {

    static Shooter instance = null;
    CANJaguar shooter;
    //Counter optical;

    /**
     * Ensures that only one shooter is instantiated.
     *
     * @return The singleton shooter instance.
     */
    public static Shooter getInstance() {
        if (instance == null) {
            instance = new Shooter();
        }
        return instance;
    }

    Shooter() {
        System.out.println("Shooter");
        
    }

    /**
     * This method toggles the state of the light.
     *
     * @param on whether the light is on or off.
     */
    public void setLight(boolean on) {
    }

    /**
     * This method gets the output voltage of the shooter.
     *
     * @return output voltage of the shooter
     * @throws CANTimeoutException
     */
    public double getVoltage() throws CANTimeoutException {
        return shooter.getOutputVoltage();
    }

    /**
     * This method sets the PID values for the shooter.
     *
     * @param p set the proportional value
     * @param i set the integral value
     * @param d set the derivative value
     * @param ff set the feedforward value
     */
    public void setPID(double ff) {
        
        ShooterPIDCommand.setFf(ff);
    }

    /**
     * This method gets the speed of the shooter.
     *
     * @return -7.5/optical sensor period
     * @throws CANTimeoutException
     */
    public double getSpeed() throws CANTimeoutException {
        return 0;
        //return (-7.5 / optical.getPeriod());
    }

    /**
     * Sets the speed of setpoint as negative rpm.
     *
     * @param rpm the parameter to set the setpoint speed to.
     */
    public void setSpeed(double rpm) {
        // Made negative so that we don't shoot backwards! -Mr. Lim
        ShooterPIDCommand.setSetpoint(-rpm);
    }

//    public ShooterPIDCommand getPIDController(){
//        return pidController;
//    }
    /**
     * Initialize PID control as the default command.
     */
    protected void initDefaultCommand() {
        //setDefaultCommand(new ShooterPIDCommand(preferences.getDouble("p",0), preferences.getDouble("i",0), preferences.getDouble("d",0), preferences.getDouble("ff",0), shooter, optical));
    }
}
