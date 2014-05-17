/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.crescentschool.robotics.competition.constants.ElectricalConstants;
import org.crescentschool.robotics.competition.subsystems.Lights;

/**
 *
 * @author ianlo
 */
public class A_DriveForward extends CommandGroup {

    public A_DriveForward() {
        if (Lights.getInstance().isRedAlliance()) {
            Lights.getInstance().setPattern(Lights.HOT_LEFT_RED);
        } else {
            Lights.getInstance().setPattern(Lights.HOT_LEFT_BLUE);

        }
        
        System.out.println("Drive Forward");
        addSequential(new A_PositionMove(70, 0));


    }
}
