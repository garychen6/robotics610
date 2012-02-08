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
    double p, i, d;

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

    /**
     * Initializes Shooter and sets the Jaguars in vBus mode.
     */
    public Shooter() {
        p = PIDConstants.shooterP;
        i = PIDConstants.shooterI;
        d = 0;
        try {
            shootJaguar = new CANJaguar(ElectricalConstants.shootJaguar);
            shootJaguarSlave = new CANJaguar(ElectricalConstants.shootJaguarSlave);
            //shootJaguar.setSpeedReference(CANJaguar.SpeedReference.kQuadEncoder);
            //shootJaguar.configEncoderCodesPerRev(256);
            shootJaguar.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
            shootJaguarSlave.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
            shootJaguar.configNeutralMode(CANJaguar.NeutralMode.kCoast);
            shootJaguarSlave.configNeutralMode(CANJaguar.NeutralMode.kCoast);
            // shootJaguar.setPID(p, i, d);
            //shootJaguar.setPID(PIDConstants.shooterP, PIDConstants.shooterI, 0);
            shootJaguar.enableControl(0);
            shootJaguarSlave.enableControl();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets master Jaguar and syncs slave Jaguar.
     * @param rpm The speed to set the target speed for the shooter at in rpm.
     */
    public void setShooter(double rpm) {
        try {
            shootJaguar.setX(rpm);
            shootJaguarSlave.setX(shootJaguar.getOutputVoltage() / shootJaguar.getBusVoltage());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    /**
     * Gets the shooter's target speed.
     * @return  The target speed.
     */
    public double getShooterSetPoint() {
        try {
            return shootJaguar.getX();
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    /**
     * Return the shooter's speed.
     * @return Return shooter speed.
     */
    public double getShooterSpeed() {
        try {
            return shootJaguar.getSpeed();
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    /**
     * Adjust the P value.
     * @param x The value with which to increment "P".
     */
    public void incP(double x) {
        p += x;
        System.out.println("Shooter P: " + p + " I: " + i);
    }

    /**
     * Adjust the I value.
     * @param x The value with which to increment "I".
     */
    public void incI(double x) {
        i += x;
        System.out.println("Shooter P: " + p + " I: " + i);
    }

    /**
     * Resets Shooter P, I, and D, and syncs slave.
     */
    public void resetPID() {
        try {
            shootJaguar.setPID(p, i, d);
            shootJaguar.enableControl();
            shootJaguarSlave.setX(shootJaguar.getOutputVoltage() / shootJaguar.getBusVoltage());
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * The default command for the shooter.
     */
    protected void initDefaultCommand() {
        
    }
}
