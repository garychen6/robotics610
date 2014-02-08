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
public class A_MiddleOneBall extends CommandGroup {

    Preferences prefs;

    public A_MiddleOneBall() {

        prefs = Preferences.getInstance();
        int distance = prefs.getInt("distance", 0);
        int angle = prefs.getInt("angle", 0);
        String side = prefs.getString("hot", "left");
        addParallel(new A_LoadShooter());

        addSequential(new A_PositionMove(distance));
        addParallel(new A_Intake(false, false, 0, 1500));

        if (side.equals("left")) {

            addSequential(new A_GyroTurn(-angle));

            addSequential(new A_FireShooter());



        } else {

            addSequential(new A_GyroTurn(angle));

            addSequential(new A_FireShooter());

        }


    }
}
