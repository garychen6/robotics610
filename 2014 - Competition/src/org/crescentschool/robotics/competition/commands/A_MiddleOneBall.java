/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.CommandGroup;
import org.crescentschool.robotics.competition.subsystems.Camera;
import org.crescentschool.robotics.competition.subsystems.Lights;

/**
 *
 * @author ianlo
 */
public class A_MiddleOneBall extends CommandGroup {

    Camera camera;

    public A_MiddleOneBall() {

        camera = Camera.getInstance();
        camera.setRingLight(true);
        int distance = 50;
        int angle = 10;
        String side = "right";
        int goodReads = 0;
        System.out.println("A_Middle One Ball");

        addSequential(new A_Wait(1));
        Timer timer = new Timer();
        timer.reset();
        timer.start();

        while (timer.get() < 0.5) {
        }
        int offset = 0;
        int count = 0;
        while (offset == 0 && count < 100) {
            offset = camera.getOffset(10);
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

//        if (true) {
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

        Lights.getInstance().setPattern(Lights.TELE);

    }
}
