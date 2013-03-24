/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.subsystems;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.crescentschool.robotics.competition.commands.*;
import org.crescentschool.robotics.competition.constants.*;

/**
 *
 * @author Ian
 */
public class Intake extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    CANJaguar arm;
    Victor rollers;
    static Intake intake = null;
    private int armPos = 0;
    DoubleSolenoid leftGate;
    Solenoid rightGate;
    Preferences preferences;
    double target;

    public static Intake getInstance() {
        if (intake == null) {
            intake = new Intake();
        }
        return intake;
    }

    public void leftOpen(boolean open) {
        if (!open) {
            leftGate.set(DoubleSolenoid.Value.kReverse);
        } else {
            leftGate.set(DoubleSolenoid.Value.kForward);
        }
        
    }

    public void rightOpen(boolean open) {
        rightGate.set(open);

    }

    public Intake() {
        preferences = Preferences.getInstance();

        leftGate = new DoubleSolenoid(ElectricalConstants.rightGateForward, ElectricalConstants.rightGateReverse);

        rightGate = new Solenoid(ElectricalConstants.leftGate);
        try {
            arm = new CANJaguar(ElectricalConstants.armJag);
            arm.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
            arm.configNeutralMode(CANJaguar.NeutralMode.kBrake);

            arm.configFaultTime(0.5);
            arm.configPotentiometerTurns(1);

            arm.setPositionReference(CANJaguar.PositionReference.kPotentiometer);
            arm.setPID(0, 0, 0);

            arm.enableControl(0);



        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
        rollers = new Victor(ElectricalConstants.rollerVic);

    }

    public double getPot() {
        try {

            return arm.getPosition();
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    public void setIntake(double power) {
        try {
            arm.setX(power);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }

    public void initDefaultCommand() {
        //setDefaultCommand(new PickUp(false, 0, false));
    }

    public void setRollers(double i) {
        rollers.set(i);

    }

    /**
     * @return the armPos
     */
    public int getArmPos() {
        return armPos;
    }

    /**
     * @param armPos the armPos to set
     */
    public void setArmPos(int armPos) {


        this.armPos = armPos;
        try {
            double p = 0;

            switch (armPos) {
                case 0:
                    //target = PIDConstants.armIntake;
                    target = preferences.getDouble("intake", 0);
                    p = 2;
                    break;
                case 1:
                    //target = PIDConstants.armFeed;
                    target = preferences.getDouble("feed", 0);

                    p = 2.8;
                    break;
                case 2:
                    //target = PIDConstants.armStow;
                    target = preferences.getDouble("stow", 0);
                    SmartDashboard.putNumber("armCurrent", arm.getOutputCurrent());
                    p = 4;
                    break;
            }
            double change = arm.getPosition() - target;
            SmartDashboard.putNumber("armPosition", arm.getPosition());
            //System.out.println(arm.getPosition());
            //arm.setX(change * p*0.9);

        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }

    public double getChange() {
        try {
            return arm.getPosition() - target;
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
            return 0;
        }
    }
}
