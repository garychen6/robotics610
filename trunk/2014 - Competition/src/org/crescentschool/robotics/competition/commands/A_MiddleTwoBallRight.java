/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.crescentschool.robotics.competition.constants.ElectricalConstants;
import org.crescentschool.robotics.competition.subsystems.Camera;
import org.crescentschool.robotics.competition.subsystems.DriveTrain;
import org.crescentschool.robotics.competition.subsystems.Lights;

/**
 *
 * @author ianlo
 */
public class A_MiddleTwoBallRight extends CommandGroup {

    Camera camera;
    DriveTrain driveTrain;
    int distance = 40;
    int angle = 10;

    public A_MiddleTwoBallRight() {
        driveTrain = DriveTrain.getInstance();
        driveTrain.resetEncoders();
        driveTrain.resetGyro();





        addParallel(new A_LoadShooter());

        addSequential(new A_PositionMove(distance, angle));
        addParallel(new A_Intake(false, false, 0, 1500));
        addSequential(new A_Wait(0.1));

        addSequential(new A_FireShooter());
        addParallel(new A_LoadShooter());
        addSequential(new A_Wait(1));


        addParallel(new A_Intake(true, true, ElectricalConstants.intakeSpeed, 1500));

        addParallel(new A_PositionMove((int) -distance-20, 0));
        addSequential(new A_LoadShooter());
        addSequential(new A_Wait(0.75));


        addParallel(new A_Intake(false, true, ElectricalConstants.intakeSpeed, 1500));
        addSequential(new A_Wait(0.2));


        addParallel(new A_Intake(false, true, 0, 1500));

        addSequential(new A_PositionMove(distance + 20, -angle * 2));
        addParallel(new A_Intake(false, false, 0, 1500));
        addSequential(new A_Wait(0.5));

        addSequential(new A_FireShooter());
        addSequential(new A_LoadShooter());




    }
}
