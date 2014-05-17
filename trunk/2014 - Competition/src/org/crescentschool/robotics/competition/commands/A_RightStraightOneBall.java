/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.crescentschool.robotics.competition.constants.ImagingConstants;
import org.crescentschool.robotics.competition.subsystems.Camera;

/**
 *
 * @author ianlo
 */
public class A_RightStraightOneBall extends CommandGroup {

    Camera camera;

    public A_RightStraightOneBall() {

        camera = Camera.getInstance();
        int distance = 50;
        String side = "right";
        int goodReads = 0;

        camera.setRingLight(true);

      
        System.out.println(side);
        addParallel(new A_LoadShooter());

        addSequential(new A_PositionMove(distance, 0));
        addParallel(new A_Intake(false, false, 0, 1500));
        addSequential(new A_Wait(0.2));
        addSequential(new A_FireShooter());


    }
}
