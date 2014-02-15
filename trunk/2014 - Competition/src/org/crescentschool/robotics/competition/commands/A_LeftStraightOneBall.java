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
public class A_LeftStraightOneBall extends CommandGroup {

    Preferences prefs;
    Camera camera;

    public A_LeftStraightOneBall() {

        prefs = Preferences.getInstance();
        camera = Camera.getInstance();
        int distance = prefs.getInt("distance", 0);;
        int angle = prefs.getInt("angle", 0);
        String side = "right";
        int goodReads = 0;

        camera.setRingLight(true);

        int offset = camera.getOffset(ImagingConstants.leftAreaThreshold);
        int count = 0;
        while (offset == 0 && count < 5000 && goodReads < 5) {
            int newOffset = camera.getOffset(ImagingConstants.leftAreaThreshold);
            if (newOffset == 1) {
                offset = newOffset;
                goodReads = 5;
            } else if (newOffset != 0) {

                goodReads++;
                if (goodReads == 5) {
                    offset = newOffset;
                }

            }
            count++;
        }

        if (offset < 0) {
            side = "Going Left";
        } else {
            side = "Going Right";
        }
        System.out.println(side);
        addParallel(new A_LoadShooter());



        //TODO use camera.getoffset()
        if (offset > 0) {
            addSequential(new A_Wait(5));


        }
        addSequential(new A_PositionMove(distance, 0));
        addParallel(new A_Intake(false, false, 0, 1500));
        addSequential(new A_Wait(0.2));
        addSequential(new A_FireShooter());


    }
}
