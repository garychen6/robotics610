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
public class A_MiddleOneBall extends CommandGroup {

    Camera camera;

    public A_MiddleOneBall() {

        camera = Camera.getInstance();
        int distance = 50;
        int angle = 10;
        String side = "right";
        int goodReads = 0;

        camera.setRingLight(true);

        int offset = camera.getOffset(ImagingConstants.middleAreaThreshold);
        
        int count = 0;
        while (offset == 0 && count < 100 && goodReads < 5) {
            int newOffset = camera.getOffset(ImagingConstants.middleAreaThreshold);
            if (newOffset == -1) {
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
        if (offset < 0) {
            addSequential(new A_PositionMove(distance, -angle));
            addParallel(new A_Intake(false, false, 0, 1500));
            addSequential(new A_Wait(0.2));
            addSequential(new A_FireShooter());
            //TODO Sanity Check encoders before calling shoot
            //TODO Logic for checking if theres a ball (either here or in shooter)



        } else {

            addSequential(new A_PositionMove(distance, angle));
            addParallel(new A_Intake(false, false, 0, 1500));
            addSequential(new A_Wait(0.2));

            addSequential(new A_FireShooter());

        }


    }
}
