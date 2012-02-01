/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.subsystems;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.crescentschool.robotics.competition.constants.ElectricalConstants;
import org.crescentschool.robotics.competition.constants.PIDConstants;

/**
 *
 * @author Warfa
 */
public class Flipper extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    CANJaguar jagFlip;
    static Flipper instance = null;
    double p, i, d;

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }

    public static Flipper getInstance() {
        if (instance == null) {
            instance = new Flipper();
        }
        return instance;
    }

    private Flipper() {
        p = PIDConstants.flipperP;
        i = PIDConstants.flipperI;
        d = PIDConstants.flipperD;
        try {
            jagFlip = new CANJaguar(ElectricalConstants.JagFlipper);
            jagFlip.changeControlMode(CANJaguar.ControlMode.kPosition);
            jagFlip.configFaultTime(0.5);
            jagFlip.configNeutralMode(CANJaguar.NeutralMode.kCoast);
            jagFlip.configPotentiometerTurns(10);
            jagFlip.setPositionReference(CANJaguar.PositionReference.kPotentiometer);
            jagFlip.setPID(p, i, d);
            jagFlip.enableControl(0);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }

    }

    public void setP(double inc) {
        p += inc;
    }

    public void setI(double inc) {
        i += inc;
    }

    public void setD(double inc) {
        d += inc;
    }
    public double[] getPID(){
        double[] pid = new double[3];
        pid[0] = p;
        pid[1] = i;
        pid[2] = d;
        return pid;
    }
    public void resetPID() {
        try {
            jagFlip.setPID(p, i, d);
            jagFlip.enableControl(0);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }

    public void setFlippers(double angle) {
        try {
            jagFlip.setX(4.84 + ElectricalConstants.potDtoV * angle);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }

    public double getPos() {
        try {
            return jagFlip.getPosition();
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
            return 0;
        }
    }
}
