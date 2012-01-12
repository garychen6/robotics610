/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.subsystems;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SendablePIDController;
import org.crescentschool.robotics.competition.commands.TankDrive;

/**
 *
 * @author Warfa
 */
public class DriveTrain extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    private CANJaguar jagRightMaster;
    private CANJaguar jagLeftMaster;
    private CANJaguar jagRightSlave;
    private CANJaguar jagLeftSlave;
    private Gyro gyro;
    private SendablePIDController controllerRight;
    private SendablePIDController controllerLeft;
    private static DriveTrain instance = null;
    private double p;
    private double i;
    private double d;
    private PIDSource rightIn = new PIDSource() {

        public double pidGet() {
            try {
                return jagRightMaster.getPosition() - gyro.getAngle();
            } catch (CANTimeoutException ex) {
                ex.printStackTrace();
                return 0;
            }
        }
    };
    private PIDOutput rightOut = new PIDOutput() {

        public void pidWrite(double output) {
            try {
                jagRightMaster.setX(output);
                syncSlaves();
            } catch (CANTimeoutException ex) {
                ex.printStackTrace();
            }
        }
    };
    private PIDSource leftIn = new PIDSource() {

        public double pidGet() {
            try {
                return jagRightMaster.getPosition() + gyro.getAngle();
            } catch (CANTimeoutException ex) {
                ex.printStackTrace();
                return 0;
            }
        }
    };
    private PIDOutput leftOut = new PIDOutput() {

        public void pidWrite(double output) {
            try {
                jagLeftMaster.setX(output);
                syncSlaves();
            } catch (CANTimeoutException ex) {
                ex.printStackTrace();
            }
        }
    };

    public void initDefaultCommand() {
        setDefaultCommand(new TankDrive());
    }

    public static DriveTrain getInstance() {
        if (instance == null) {
            instance = new DriveTrain();
        }
        return instance;

    }
    private DriveTrain() {
        try {
            jagRightMaster = new CANJaguar(1);
            jagLeftMaster = new CANJaguar(2);
            jagRightSlave = new CANJaguar(3);
            jagLeftSlave = new CANJaguar(4);
            controllerRight = new SendablePIDController(p, i, d, rightIn, rightOut);
            controllerLeft = new SendablePIDController(p,i,d,leftIn,leftOut);
            
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }

    }

    private void syncSlaves() {
        try {
            jagLeftSlave.setX(jagLeftMaster.getX());
            jagRightSlave.setX(jagRightMaster.getX());
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }
    public void rightsetSetpoint(double setpoint) {
        controllerRight.setSetpoint(setpoint);
    }

    public void leftSetpoint(double setpoint) {
        controllerLeft.setSetpoint(setpoint);
    }

    public double getRightSetpoint() {
        return controllerRight.getSetpoint();
    }

    public double getLeftSetpoint() {
        return controllerLeft.getSetpoint();
    }
}
