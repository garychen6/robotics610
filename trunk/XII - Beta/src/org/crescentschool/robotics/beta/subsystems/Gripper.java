/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.beta.subsystems;

import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.crescentschool.robotics.beta.commands.GripperControl;
import org.crescentschool.robotics.beta.constants.ElectricalConstants;

/**
 *
 * @author slim
 */
public class Gripper extends Subsystem{

    private static Gripper instance = null;
    private Victor vicGripperTop, vicGripperBottom;

    public static Gripper getInstance() {
        if (instance == null) {
            instance = new Gripper();

            // Set default command here, like this:
            instance.setDefaultCommand(new GripperControl());
        }
        return instance;
    }

    private Gripper() {
        vicGripperTop = new Victor(ElectricalConstants.kVictorGripperTopChannel);
        vicGripperBottom = new Victor(ElectricalConstants.kVictorGripperBottomChannel);
    }

    public void runGripper(double topSpeed, double bottomSpeed) {
        vicGripperTop.set(topSpeed);
        vicGripperBottom.set(bottomSpeed);
    }
}
