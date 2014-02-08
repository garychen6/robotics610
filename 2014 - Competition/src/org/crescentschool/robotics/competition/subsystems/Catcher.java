/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.crescentschool.robotics.competition.commands.T_Catcher;
import org.crescentschool.robotics.competition.constants.ElectricalConstants;

/**
 *
 * @author ianlo
 */
public class Catcher extends Subsystem {

    private static Catcher instance;
    private DoubleSolenoid catcherSol;
    private DigitalInput catcherSensor;

    private Catcher() {
        //Initialize the solenoid and sensor that will be used for the catcher
        catcherSol = new DoubleSolenoid(ElectricalConstants.catcherSolenoidForward, ElectricalConstants.catcherSolenoidReverse);
        catcherSensor = new DigitalInput(ElectricalConstants.catcherSensor);

    }
    //Returns if there is an object close to the sensor.

    public boolean isCatcherSensorTripped() {

        return catcherSensor.get();

    }
    //Opens the catcher by setting the solenoids.

    public void setCatcherOpen(boolean open) {
        if (open) {
            catcherSol.set(DoubleSolenoid.Value.kReverse);
        } else {
            catcherSol.set(DoubleSolenoid.Value.kForward);
        }
    }
//Get the singleton instance of the catcher.

    static public Catcher getInstance() {
        if (instance == null) {
            instance = new Catcher();
        }
        return instance;
    }
    //Run the teleop catcher command when first created.

    protected void initDefaultCommand() {
    }
}
