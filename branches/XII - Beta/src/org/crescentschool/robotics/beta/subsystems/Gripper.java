/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.beta.subsystems;

import edu.wpi.first.wpilibj.Victor;
import org.crescentschool.robotics.beta.constants.ElectricalConstants;

/**
 *
 * @author slim
 */
public class Gripper {
    private Victor vicGripperTop, vicGripperBottom;
    
    private Gripper(){
        vicGripperTop = new Victor(ElectricalConstants.kVictorGripperTopChannel);
        vicGripperBottom = new Victor(ElectricalConstants.kVictorGripperBottomChannel);
    }
    
    public void runGripper(double topSpeed, double bottomSpeed)
    {
        vicGripperTop.set(topSpeed);
        vicGripperBottom.set(bottomSpeed);
    }
}
