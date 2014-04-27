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
public class A_MiddleOneBallRight extends CommandGroup {

    Camera camera;
    DriveTrain driveTrain;
    int distance = 40;
    int angle = 10;

    public A_MiddleOneBallRight() {
        driveTrain = DriveTrain.getInstance();
        driveTrain.resetEncoders();
        driveTrain.resetGyro();





        addParallel(new A_LoadShooter());

        addSequential(new A_PositionMove(distance, angle));
        addParallel(new A_Intake(false, false, 0, 1500));
        addSequential(new A_Wait(0.1));

        addSequential(new A_FireShooter());
        addSequential(new A_LoadShooter());


        Lights.getInstance().setPattern(Lights.TELE);




    }
}
