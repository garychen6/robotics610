/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 * @author ianlo
 */
public class A_DriveForward extends CommandGroup {

    public A_DriveForward() {

        addSequential(new A_PositionMove(50, -1));

    }
}
