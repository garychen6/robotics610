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
import org.crescentschool.robotics.competition.constants.PotConstants;

/**
 *
 * @author Warfa
 */
public class Flipper extends Subsystem {

    CANJaguar jagFlip;
    static Flipper instance = null;
    double p, i, d;
    // -1 = balance assist, 0 = ball collection, 1 = barrier, 2 = bridge, 3 = retract
    int flipperPosition = 3;

    /**
     * Sets the default command for the flipper.
     */
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }

    /**
     * Ensures only one flipper is instantiated.
     * @return The singleton flipper instance.
     */
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
            jagFlip.configNeutralMode(CANJaguar.NeutralMode.kBrake);
            jagFlip.configPotentiometerTurns(10);
            jagFlip.setPositionReference(CANJaguar.PositionReference.kPotentiometer);
            jagFlip.setPID(p, i, d);
            jagFlip.enableControl(0);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
        gotoPosition();
    }
    public void reInit(){
        try {
            jagFlip = new CANJaguar(ElectricalConstants.JagFlipper);
            jagFlip.changeControlMode(CANJaguar.ControlMode.kPosition);
            jagFlip.configFaultTime(0.5);
            jagFlip.configNeutralMode(CANJaguar.NeutralMode.kBrake);
            jagFlip.configPotentiometerTurns(10);
            jagFlip.setPositionReference(CANJaguar.PositionReference.kPotentiometer);
            jagFlip.setPID(p, i, d);
            jagFlip.enableControl(0);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }
    /**
     * Sets the P value of the victors on the flipper.
     * @param inc The increment for the P Value.
     */
    public void setP(double inc) {
        p += inc;
    }

    /**
     * Sets the I value of the victors on the flipper.
     * @param inc The increment for the I Value.
     */
    public void setI(double inc) {
        i += inc;
    }

    /**
     * Sets the D value of the victors on the flipper.
     * @param inc The increment for the D Value.
     */
    public void setD(double inc) {
        d += inc;
    }

    /**
     * Gets the array of PID values for the flipper.
     * @return The array of PID values for the flipper. pid[0] = P, pid[1] = I, pid[2] = d.
     */
    public double[] getPID() {
        double[] pid = new double[3];
        pid[0] = p;
        pid[1] = i;
        pid[2] = d;
        return pid;
    }

    /**
     * Resets the PID values to the values previously set.
     */
    public void resetPID() {
        try {
            jagFlip.setPID(p, i, d);
            jagFlip.enableControl(0);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Sets the target angle of the flipper in degrees. 0 is parallel to the ground.
     * @param angle The target angle of the flipper.
     */
    public void setFlippers(double angle) {
        try {
            jagFlip.setX(5.395 + ElectricalConstants.potDtoV * angle);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * -1 = balance assist, 0 = ball collection, 1 = barrier, 2 = bridge, 3 = retract
     * @param angle Position Mode 
     */
    public void setPos(int position) {
        flipperPosition = position;
         gotoPosition();
    }

    /**
     * Gets the current angle of the flipper.
     * @return The current angle of the flipper.
     */
    public double getPos() {
        try {
            return jagFlip.getPosition();
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    /**
     * Moves flipper to next higher position
     */
    public void incFlipper() {
        flipperPosition++;
        if (flipperPosition > 3) {
            flipperPosition = 3;
        }
      
        gotoPosition();
    }

    /**
     * Moves flipper to next lower position
     */
    public void decFlipper() {
        flipperPosition--;
        if (flipperPosition < -1) {
            flipperPosition = -1;
        }
        gotoPosition();
    }

    public void gotoPosition() {
        switch (flipperPosition) {
            case -1:
                setFlippers(PotConstants.flipperBridgeAssist);
                break;
            case 0:
                setFlippers(PotConstants.flipperBallPickup);
                break;
            case 1:
                setFlippers(PotConstants.flipperBarrier);
                break;
            case 2:
                setFlippers(PotConstants.flipperBridge);
                break;
            case 3:
                setFlippers(PotConstants.flipperRetract);
                break;

        }
    }
}
