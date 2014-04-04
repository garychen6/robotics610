/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.CommandGroup;
import org.crescentschool.robotics.competition.constants.ElectricalConstants;
import org.crescentschool.robotics.competition.subsystems.Camera;
import org.crescentschool.robotics.competition.subsystems.DriveTrain;
import org.crescentschool.robotics.competition.subsystems.Lights;

/**
 *
 * @author ianlo
 */
public class A_MiddleTwoBall extends CommandGroup {

    Camera camera;
    DriveTrain driveTrain;

    public A_MiddleTwoBall() {
        driveTrain = DriveTrain.getInstance();
        driveTrain.resetEncoders();
        driveTrain.resetGyro();

        camera = Camera.getInstance();

        int distance = 40;
        int angle = 10;
        String side = "right";

        camera.setRingLight(true);
        addSequential(new A_Wait(1));
        Timer timer = new Timer();
        timer.reset();
        timer.start();

        while (timer.get() < 0.5) {
        }
        System.out.println("Middle Two Ball");

        int offset = 0;
        int count = 0;
        while (offset == 0 && count <100) {
            
            offset = camera.getOffset(10);
            count++;
        }
        if(count >=100){
            Lights.getInstance().setPattern(Lights.TELE);
        }
        System.out.println("Middle Two Ball");



        //TODO use camera.getoffset()
        if (offset == -1) {
//            if (Lights.getInstance().isRedAlliance()) {
//                Lights.getInstance().setPattern(Lights.HOT_LEFT_RED);
//            } else {
//                Lights.getInstance().setPattern(Lights.HOT_LEFT_BLUE);
//
//            }
            System.out.println("Going Left");
            addParallel(new A_LoadShooter());

            addSequential(new A_PositionMove(distance, -angle));
            addParallel(new A_Intake(false, false, 0, 1500));
            addSequential(new A_Wait(0.1));

            addSequential(new A_FireShooter());
            addParallel(new A_LoadShooter());
            addSequential(new A_Wait(1));


            addParallel(new A_Intake(true, true, ElectricalConstants.intakeSpeed, 1500));

            addParallel(new A_PositionMove((int) -(distance / 2.0), 0));
            addSequential(new A_LoadShooter());

            addSequential(new A_Wait(0.75));

            addParallel(new A_Intake(false, true, ElectricalConstants.intakeSpeed, 1500));

            addSequential(new A_Wait(0.2));

            addParallel(new A_Intake(false, false, 0, 1500));

            addSequential(new A_PositionMove(distance + 10, angle * 2));

            addParallel(new A_Intake(false, false, 0, 1500));
            addSequential(new A_Wait(0.5));

            addSequential(new A_FireShooter());
            addSequential(new A_LoadShooter());


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

            addParallel(new A_PositionMove((int) -(distance / 2.0), 0));
            addSequential(new A_LoadShooter());
            addSequential(new A_Wait(0.75));


            addParallel(new A_Intake(false, true, ElectricalConstants.intakeSpeed, 1500));
            addSequential(new A_Wait(0.2));


            addParallel(new A_Intake(false, true, 0, 1500));

            addSequential(new A_PositionMove(distance + 10, -angle * 2));
            addParallel(new A_Intake(false, false, 0, 1500));
            addSequential(new A_Wait(0.5));

            addSequential(new A_FireShooter());
            addSequential(new A_LoadShooter());


        }

        Lights.getInstance().setPattern(Lights.TELE);




    }
}
