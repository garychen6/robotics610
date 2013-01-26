/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.subsystems;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.crescentschool.robotics.competition.PID.PIDController;
import org.crescentschool.robotics.competition.constants.ElectricalConstants;

/**
 *
 * @author Warfa
 */
public class Shooter extends Subsystem {

    static Shooter instance = null;
    CANJaguar shooter;
    PIDController pidController;
    Thread PID;
    ShooterSensor gearTooth;

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
            shooter.configEncoderCodesPerRev(2);
            shooter.changeControlMode(CANJaguar.ControlMode.kVoltage);
            shooter.configNeutralMode(CANJaguar.NeutralMode.kCoast);
            shooter.enableControl(0);
            gearTooth = new ShooterSensor(1);
            gearTooth.setMaxPeriod(5);
            gearTooth.start();
            
            
            //pidController = PIDController.getInstance();
            pidController = new PIDController(0, 0, 0, SmartDashboard.getNumber("ff", 0), shooter, gearTooth);
            //pidController = ClassicPID.getInstance();
            pidController.start();
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }
    public double getVoltage() throws CANTimeoutException{
        return shooter.getOutputVoltage();
    }
    public void setPID(double p, double i, double d,double ff) {
        pidController.setP(p);
        pidController.setI(i);
        pidController.setD(d);
        pidController.setFf(ff);
    }

    public void setSpeed(double rpm) {
        pidController.setSetpoint(rpm);
        System.out.println("Setting Speed "+rpm);
    }

    synchronized public void setShooter(double val) {
        try {
            shooter.setX(val);
            System.out.println("PID OUTPUT"+val);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }

    public double getSpeed() throws CANTimeoutException {
        return (7.5/gearTooth.getPeriod());
    }

    protected void initDefaultCommand() {
    }
    public PIDController getPIDController() {
        return pidController;
    }
}
