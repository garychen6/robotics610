/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.commands;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.CommandGroup;
import org.crescentschool.robotics.competition.constants.ElectricalConstants;
import org.crescentschool.robotics.competition.subsystems.Camera;
import org.crescentschool.robotics.competition.subsystems.DriveTrain;

/**
 *
 * @author ianlo
 */
public class A_StraightTwoBall extends CommandGroup {

    Preferences prefs;
    Camera camera;
    DriveTrain driveTrain;

    public A_StraightTwoBall() {
        driveTrain = DriveTrain.getInstance();
        driveTrain.resetEncoders();
        driveTrain.resetGyro();
        prefs = Preferences.getInstance();
        camera = Camera.getInstance();
        int distance = prefs.getInt("distance", 0);;
        addParallel(new A_LoadShooter());




        addSequential(new A_PositionMove(distance, 0));
        addParallel(new A_Intake(false, false, 0, 1500));
        addSequential(new A_Wait(0.1));

        addSequential(new A_FireShooter());

        addParallel(new A_LoadShooter());
        addSequential(new A_Wait(2));

        addParallel(new A_Intake(true, true, ElectricalConstants.intakeSpeed, 1500));

        addSequential(new A_PositionMove(-distance, 0));
        addSequential(new A_Wait(0.2));

        addParallel(new A_Intake(false, true, 0, 1500));


        addSequential(new A_Wait(0.2));

        addSequential(new A_PositionMove(distance, 0));
        addParallel(new A_Intake(false, false, 0, 1500));
        addSequential(new A_Wait(0.1));

        addSequential(new A_FireShooter());


    }
}
