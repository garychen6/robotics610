/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.commands;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.CommandGroup;
import org.crescentschool.robotics.competition.constants.ElectricalConstants;
import org.crescentschool.robotics.competition.constants.ImagingConstants;
import org.crescentschool.robotics.competition.subsystems.Camera;
import org.crescentschool.robotics.competition.subsystems.DriveTrain;

/**
 *
 * @author ianlo
 */
public class A_MiddleTwoBall extends CommandGroup {

    Camera camera;
    DriveTrain driveTrain;
    Preferences prefs;

    public A_MiddleTwoBall() {
        prefs = Preferences.getInstance();
        driveTrain = DriveTrain.getInstance();
        driveTrain.resetEncoders();
        driveTrain.resetGyro();
        camera = Camera.getInstance();
        int distance = prefs.getInt("distance", 0);
        int angle = prefs.getInt("angle", 0);
        String side = "right";
        int goodReads = 0;

        camera.setRingLight(true);

        int offset = camera.getOffset(ImagingConstants.middleAreaThreshold);
        int count = 0;
//        while (offset == 0 && count < 100 && goodReads < 10) {
//            int newOffset = camera.getOffset(ImagingConstants.middleAreaThreshold);
//
//            if (newOffset == -1) {
//                offset = -1;
//                goodReads = 10;
//            } else if (newOffset == 1) {
//
//                goodReads++;
//                if (goodReads == 10) {
////                    System.out.println("Going Right");
//
//                    offset = 1;
//                }
//
//            }
//            count++;
//        }
        addSequential(new A_Wait(0.5));



        //TODO use camera.getoffset()
        if (false) {
            System.out.println("Going Left");
            addParallel(new A_LoadShooter());

            addSequential(new A_PositionMove(distance, -angle));
            addParallel(new A_Intake(false, false, 0, 1500));
            addSequential(new A_Wait(0.1));

            addSequential(new A_FireShooter());
            addParallel(new A_LoadShooter());
            addSequential(new A_Wait(1));


            addParallel(new A_Intake(true, true, ElectricalConstants.intakeSpeed, 1500));

            addParallel(new A_PositionMove(-distance, 0));
            addSequential(new A_LoadShooter());

            addSequential(new A_Wait(0.5));

            addParallel(new A_Intake(false, true, ElectricalConstants.intakeSpeed, 1500));

            addSequential(new A_Wait(0.2));

            addParallel(new A_Intake(false, false, 0, 1500));

            addSequential(new A_PositionMove(distance, angle * 2));

            addParallel(new A_Intake(false, false, 0, 1500));
            addSequential(new A_Wait(0.1));

            addSequential(new A_FireShooter());
            addSequential(new A_PositionMove((int) (-distance), angle));


            //TODO Sanity Check encoders before calling shoot
            //TODO Logic for checking if theres a ball (either here or in shooter)



        } else {
            System.out.println("Going Right");

            addParallel(new A_LoadShooter());

            addSequential(new A_PositionMove(distance, angle));
            addParallel(new A_Intake(false, false, 0, 1500));
            addSequential(new A_Wait(0.1));

            addSequential(new A_FireShooter());
            addParallel(new A_LoadShooter());
            addSequential(new A_Wait(1));


            addParallel(new A_Intake(true, true, ElectricalConstants.intakeSpeed, 1500));

            addParallel(new A_PositionMove(-distance, 0));
            addSequential(new A_LoadShooter());
            addSequential(new A_Wait(0.5));


            addParallel(new A_Intake(false, true, ElectricalConstants.intakeSpeed, 1500));
            addSequential(new A_Wait(0.2));


            addParallel(new A_Intake(false, true, 0, 1500));

            addSequential(new A_PositionMove(distance, -angle * 2));

            addParallel(new A_Intake(false, false, 0, 1500));
            addSequential(new A_Wait(0.1));

            addSequential(new A_FireShooter());
            addSequential(new A_PositionMove((int) (-distance), -angle));


        }





    }
}
