/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.commands;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 * @author ianlo
 */
public class A_MiddleTwoBall extends CommandGroup {

    Preferences prefs;

    public A_MiddleTwoBall() {

        prefs = Preferences.getInstance();
        int distance = prefs.getInt("distance", 0);
        int angle = prefs.getInt("angle", 0);
        String side = prefs.getString("hot", "left");
        addSequential(new A_PositionMove(distance));
        if (side.equals("left")) {
            addSequential(new A_GyroTurn(angle));
            addSequential(new A_Wait(1));

            addSequential(new A_GyroTurn(-angle));
            addSequential(new A_PositionMove(-distance));
            addSequential(new A_PositionMove(distance));

            addSequential(new A_GyroTurn(-angle));

        } else {
            addSequential(new A_GyroTurn(-angle));
            addSequential(new A_Wait(1));

            addSequential(new A_GyroTurn(angle));

            addSequential(new A_PositionMove(-distance));
            addSequential(new A_PositionMove(distance));

            addSequential(new A_GyroTurn(angle));

        }





    }
}
