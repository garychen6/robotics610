/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.commands;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import org.crescentschool.robotics.competition.constants.ImagingConstants;
import org.crescentschool.robotics.competition.subsystems.Camera;

/**
 *
 * @author ianlo
 */
public class A_DriveForward extends CommandGroup {

    public A_DriveForward() {




        addSequential(new A_PositionMove(50, 0));




    }
}
