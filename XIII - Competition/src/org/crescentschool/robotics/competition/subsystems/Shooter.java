/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.subsystems;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.crescentschool.robotics.competition.commands.Shoot;
import org.crescentschool.robotics.competition.constants.ElectricalConstants;
import org.crescentschool.robotics.competition.constants.PIDConstants;

/**
 *
 */
public class Shooter extends Subsystem {
    
    public CANJaguar shootJaguar;
    public CANJaguar shootJaguarSlave;
    private static Shooter instance = null;
    double p,i,d;
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

    public Shooter() {
        p = PIDConstants.shooterP;
        i = PIDConstants.shooterI;
        d = 0;
        try {
            shootJaguar = new CANJaguar(ElectricalConstants.shootJaguar);
            shootJaguarSlave = new CANJaguar(ElectricalConstants.shootJaguarSlave);
            shootJaguar.changeControlMode(CANJaguar.ControlMode.kSpeed);
            shootJaguar.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
            shootJaguar.configEncoderCodesPerRev(256);
            shootJaguar.setSpeedReference(CANJaguar.SpeedReference.kQuadEncoder);
            shootJaguar.setPositionReference(CANJaguar.PositionReference.kQuadEncoder);
            shootJaguar.configNeutralMode(CANJaguar.NeutralMode.kCoast);
            shootJaguarSlave.configNeutralMode(CANJaguar.NeutralMode.kCoast);
            shootJaguar.setPID(p, i, d);
            //shootJaguar.setPID(PIDConstants.shooterP, PIDConstants.shooterI, 0);
            shootJaguar.enableControl(0);
            shootJaguarSlave.enableControl();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void setShooter(int rpm) {
        try {
            shootJaguar.setX(rpm);
            shootJaguarSlave.setX(shootJaguar.getOutputVoltage()/shootJaguar.getBusVoltage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    public double getShooterSetPoint(){
        try {
            return shootJaguar.getX();
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
            return 0;
        }
    }
    public double getShooterSpeed(){
        try {
            return shootJaguar.getSpeed();
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
            return 0;
        }
    }
    public void incP(double x){
        p+= x;
    }
    public void incI(double x){
        i+= x;
    }
    public void resetPID(){
        try {
            shootJaguar.setPID(p, i, d);
            shootJaguar.enableControl();
            shootJaguarSlave.setX(shootJaguar.getOutputVoltage()/shootJaguar.getBusVoltage());
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }
    /**
     * The default command for the shooter.
     */
    protected void initDefaultCommand() {
        setDefaultCommand(new Shoot());
    }
}
