/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.commands;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import org.crescentschool.robotics.competition.subsystems.Camera;

/**
 *
 * @author ianlo
 */
public class A_MiddleOneBall extends CommandGroup {

    Preferences prefs;
    Camera camera;

    public A_MiddleOneBall() {

        prefs = Preferences.getInstance();
        camera = Camera.getInstance();
        int distance = 40;
        int angle = 30;
        String side = "right";


        camera.setRingLight(true);

        int offset = camera.getOffset();
        int count = 0;
        while (offset == 0 && count < 5000) {
            offset = camera.getOffset();
            count++;
        }

        if (offset < 0) {
            side = "Going Left";
        } else {
            side = "Going Right";
        }
        System.out.println(side);
        addParallel(new A_LoadShooter());

        addSequential(new A_PositionMove(distance));
        addParallel(new A_Intake(false, false, 0, 1500));

        //TODO use camera.getoffset()
        if (offset<0) {

            addSequential(new A_GyroTurn(-angle));
            //TODO Sanity Check encoders before calling shoot
            //TODO Logic for checking if theres a ball (either here or in shooter)
            addSequential(new A_FireShooter());



        } else {

            addSequential(new A_GyroTurn(angle));

            addSequential(new A_FireShooter());

        }


    }
}
