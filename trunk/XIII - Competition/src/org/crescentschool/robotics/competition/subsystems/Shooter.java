/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.subsystems;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.crescentschool.robotics.competition.commands.Shoot;
import org.crescentschool.robotics.competition.constants.PIDConstants;

public class Shooter extends Subsystem {

    public CANJaguar topJaguar;
    public CANJaguar bottomJaguar;
    private static Shooter instance = null;

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


            topJaguar.setPID(PIDConstants.shooterTopP, PIDConstants.shooterTopI, 0);
            bottomJaguar.setPID(PIDConstants.shooterBottomP, PIDConstants.shooterBottomI, 0);

            topJaguar.enableControl(0);
            bottomJaguar.enableControl(0);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the target speed for the top shooter.
     * @param rpm 
     */
    public void setTopShooter(int rpm) {
        try {
            topJaguar.setX(rpm);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Sets the target speed for the bottom shooter.
     * @param rpm 
     */
    public void setBottomShooter(int rpm) {
        try {

            bottomJaguar.setX(rpm);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * The default command for the shooter.
     */
    protected void initDefaultCommand() {
        setDefaultCommand(new Shoot());
    }
}
