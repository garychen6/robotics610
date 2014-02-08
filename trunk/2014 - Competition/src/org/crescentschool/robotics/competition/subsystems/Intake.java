/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.crescentschool.robotics.competition.commands.T_Intake;
import org.crescentschool.robotics.competition.constants.ElectricalConstants;

/**
 *
 * @author ianlo
 */
public class Intake extends Subsystem {

    private static Intake instance;
    private Talon leftRoller, rightRoller;
    private DoubleSolenoid intakeSol;
        private DoubleSolenoid wristSol;
        Preferences prefs;

    private Intake() {
        //Initialize the talons for the intake rollers
        leftRoller = new Talon(ElectricalConstants.leftIntakeRoller);
        rightRoller = new Talon(ElectricalConstants.rightIntakeRoller);
        //Initialize the solenoids that will control the intake position
        intakeSol = new DoubleSolenoid(ElectricalConstants.intakeSolenoidForward, ElectricalConstants.intakeSolenoidReverse);
        wristSol=new DoubleSolenoid(ElectricalConstants.leftIntakeWristSolenoidForward,ElectricalConstants.leftIntakeWristSolenoidReverse);
        prefs = Preferences.getInstance();
    }
    //Set the position of the intake
    public void setPositionDown(boolean down) {
        //Set the solenoids accordingly.
        if (down) {

            intakeSol.set(DoubleSolenoid.Value.kReverse);
        } else {
            intakeSol.set(DoubleSolenoid.Value.kForward);
        }
    }
    //Set the rollers at a current vbus.
    //A value of 1 should be intaking
    public void setIntaking(double intaking) {
        leftRoller.set(-intaking);
        rightRoller.set(intaking);
    }
    public void setWrist(boolean closed){
        if (!closed) {

            wristSol.set(DoubleSolenoid.Value.kReverse);
        } else {
            wristSol.set(DoubleSolenoid.Value.kForward);
        }
    }
    //Get the singleton instance of the Intake
    static public Intake getInstance() {
        if (instance == null) {
            instance = new Intake();
        }
        return instance;
    }
    //Run Teleop Intake when the intake is first created.
    protected void initDefaultCommand() {
    }
}