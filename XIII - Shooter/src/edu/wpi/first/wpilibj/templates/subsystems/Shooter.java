/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Shooter extends Subsystem {

    public CANJaguar topJaguar;
    public CANJaguar bottomJaguar;
    private double p, i;
    private static Shooter instance = null;

    public static Shooter getInstance() {
        if (instance == null) {
            instance = new Shooter();

            // Set default command here, like this:
            // instance.setDefaultCommand(new CommandIWantToRun());
        }
        return instance;
    }

    // Initialize your subsystem here
    public Shooter() {
        p = -0.17;
        i = -0.003;
        try {
            topJaguar = new CANJaguar(6);
            bottomJaguar = new CANJaguar(7);
            topJaguar.changeControlMode(CANJaguar.ControlMode.kSpeed);
            bottomJaguar.changeControlMode(CANJaguar.ControlMode.kSpeed);
            topJaguar.configEncoderCodesPerRev(256);
            bottomJaguar.configEncoderCodesPerRev(256);
            topJaguar.setSpeedReference(CANJaguar.SpeedReference.kQuadEncoder);
            bottomJaguar.setSpeedReference(CANJaguar.SpeedReference.kQuadEncoder);
            topJaguar.setPositionReference(CANJaguar.PositionReference.kQuadEncoder);
            bottomJaguar.setPositionReference(CANJaguar.PositionReference.kQuadEncoder);
            
            topJaguar.configNeutralMode(CANJaguar.NeutralMode.kCoast);
            bottomJaguar.configNeutralMode(CANJaguar.NeutralMode.kCoast);
            

            topJaguar.setPID(p, i, 0);
            bottomJaguar.setPID(p, -0.004, 0);

            topJaguar.enableControl(0);
            bottomJaguar.enableControl(0);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setTopShooter(int rpm) {
        try {
            topJaguar.setX(rpm);

        } catch (Exception e) {
            e.printStackTrace();
        }
    
    }

    

    public void setBottomShooter(int rpm) {
        try {

            bottomJaguar.setX(rpm);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void initDefaultCommand() {
    }
}
