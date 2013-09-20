/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.subsystems;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.crescentschool.robotics.competition.commands.OldShooterCode;
import org.crescentschool.robotics.competition.commands.ShooterPIDCommand;
import org.crescentschool.robotics.competition.constants.ElectricalConstants;

/**
 *
 * @author Warfa
 */
public class Shooter extends Subsystem {

    static Shooter instance = null;
    CANJaguar shooter;
    CANJaguar shooter2;
    Counter optical;
    ShooterPIDCommand shoot;
    Relay light;
    /**
     * Ensures that only one shooter is instantiated.
     * @return The singleton shooter instance.
     */
    public static Shooter getInstance() {
        if (instance == null) {
            instance = new Shooter();
        }
        return instance;
    }

    Shooter() {
        try {
            shooter = new CANJaguar(ElectricalConstants.jagShooter);
            shooter.changeControlMode(CANJaguar.ControlMode.kSpeed);
            shooter.setSpeedReference(CANJaguar.SpeedReference.kQuadEncoder);
            shooter.configEncoderCodesPerRev(8);
            shooter.changeControlMode(CANJaguar.ControlMode.kVoltage);
            shooter.configNeutralMode(CANJaguar.NeutralMode.kCoast);
            shooter.enableControl(0);
            shooter2 = new CANJaguar(ElectricalConstants.jagShooter);
            shooter2.changeControlMode(CANJaguar.ControlMode.kSpeed);
            shooter2.setSpeedReference(CANJaguar.SpeedReference.kQuadEncoder);
            shooter2.configEncoderCodesPerRev(8);
            shooter2.changeControlMode(CANJaguar.ControlMode.kVoltage);
            shooter2.configNeutralMode(CANJaguar.NeutralMode.kCoast);
            shooter2.enableControl(0);
            optical = new Counter(2);
            optical.setMaxPeriod(5);
            optical.setSemiPeriodMode(true);
            optical.start();
            light = new Relay(ElectricalConstants.LEDRelay);
            shoot = new ShooterPIDCommand(0.00220, shooter, shooter2, optical);
            Thread thread = new Thread(shoot);
            thread.setPriority(Thread.MIN_PRIORITY);
            thread.start();
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * This method toggles the state of the light.
     * @param on whether the light is on or off.
     */
    public void setLight(boolean on){
        if(on){
            light.set(Relay.Value.kForward);
        }
        else light.set(Relay.Value.kOff);
    }
    
    /**
     * This method gets the output voltage of the shooter.
     * @return output voltage of the shooter
     * @throws CANTimeoutException 
     */
    public double getVoltage() throws CANTimeoutException {
        return shooter.getOutputVoltage();
    }

    /**
     * This method sets the PID values for the shooter.
     * @param p set the proportional value
     * @param i set the integral value
     * @param d set the derivative value
     * @param ff set the feedforward value
     */
    public void setPID(double p, double i, double d, double ff) {
        
    }
    
    /**
     * This method gets the speed of the shooter.
     * @return -7.5/optical sensor period
     * @throws CANTimeoutException
     */
    public double getSpeed() throws CANTimeoutException {
        return (-7.5 / optical.getPeriod());
    }
    
    /**
     * Sets the speed of setpoint as negative rpm.
     * @param rpm the parameter to set the setpoint speed to.
     */
    public void setSpeed(double rpm) {
        // Made negative so that we don't shoot backwards! -Mr. Lim
        shoot.setSetpoint(rpm);
    }
    
//    public ShooterPIDCommand getPIDController(){
//        return pidController;
//    }
    
    /**
     * Initialize PID control as the default command.
     */
    protected void initDefaultCommand() {
    }
}
