package org.crescentschool.robotics.competition.subsystems;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.crescentschool.robotics.competition.commands.PIDTuning;
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
//            shootJaguar.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
//            shootJaguarSlave.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
//            shootJaguar.enableControl();
//            shootJaguarSlave.enableControl();
            resetPID();
            
        } catch (Exception e) {
            e.printStackTrace();
            handleCANError();  
        }
    }

    /**
     * Sets master Jaguar and syncs slave Jaguar.
     * @param rpm The speed to set the target speed for the shooter at in rpm.
     */
    public void setShooter(double rpm) {
        try {
            shootJaguar.setX(-rpm);
            System.out.println("ShooterSet: " + rpm);
        } catch (Exception e) {
            e.printStackTrace();
            handleCANError();
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
            handleCANError();
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
            handleCANError();
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
    public void syncSlaves(){
        try {
            shootJaguarSlave.setX(shootJaguar.getOutputVoltage() / 12);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
            handleCANError();
        }
    }
    /**
     * Resets Shooter P, I, and D, and syncs slave.
     */
    public void resetPID() {
        try {
            System.out.println("Shooter PID Reset"+p+i+d);
            shootJaguar.setSpeedReference(CANJaguar.SpeedReference.kQuadEncoder);
            shootJaguar.configEncoderCodesPerRev(256);
            //shootJaguar.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
            shootJaguar.changeControlMode(CANJaguar.ControlMode.kSpeed);
            shootJaguarSlave.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
            shootJaguar.configNeutralMode(CANJaguar.NeutralMode.kCoast);
            shootJaguarSlave.configNeutralMode(CANJaguar.NeutralMode.kCoast);
            shootJaguar.setPID(-p, -i, -d);
            shootJaguar.enableControl();
            syncSlaves();
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * The default command for the shooter.
     */
    protected void initDefaultCommand() {
        //setDefaultCommand(new PIDTuning());
    }
    
    private void handleCANError()
    {
        System.out.println("Shooter CAN Error!");
        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        resetPID();
    }
}
