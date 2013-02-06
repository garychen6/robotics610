/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.subsystems;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.GearTooth;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.crescentschool.robotics.competition.commands.ShooterPIDCommand;
import org.crescentschool.robotics.competition.constants.ElectricalConstants;

/**
 *
 * @author Warfa
 */
public class Shooter extends Subsystem {

    static Shooter instance = null;
    CANJaguar shooter;
    ShooterPIDCommand pidController;
    GearTooth gearTooth;

    public static Shooter getInstance() {
        if (instance == null) {
            instance = new Shooter();
        }
        return instance;
    }

    Shooter() {
        try {
            gearTooth = new GearTooth(2);
            gearTooth.setMaxPeriod(5);
            gearTooth.setSemiPeriodMode(true);
            gearTooth.start();
            
            shooter = new CANJaguar(ElectricalConstants.jagShooter);
            shooter.changeControlMode(CANJaguar.ControlMode.kSpeed);
            shooter.setSpeedReference(CANJaguar.SpeedReference.kQuadEncoder);
            shooter.configEncoderCodesPerRev(8);
            shooter.changeControlMode(CANJaguar.ControlMode.kVoltage);
            shooter.configNeutralMode(CANJaguar.NeutralMode.kCoast);
            shooter.enableControl(0);
            
            
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }

    public double getVoltage() throws CANTimeoutException {
        return shooter.getOutputVoltage();
    }

    public void setPID(double p, double i, double d, double ff) {
        pidController.setP(p);
        pidController.setI(i);
        pidController.setD(d);
        pidController.setFf(ff);
    }

    public double getSpeed() throws CANTimeoutException {
        return (-7.5 / gearTooth.getPeriod());
    }

    public void setSpeed(double rpm) {
        // Made negative so that we don't shoot backwards! -Mr. Lim
        pidController.setSetpoint(-rpm);
    }
    public ShooterPIDCommand getPIDController(){
        return pidController;
    }
    protected void initDefaultCommand() {
        setDefaultCommand(new ShooterPIDCommand(0, 0, 0, 0, shooter, gearTooth));
    }
}
