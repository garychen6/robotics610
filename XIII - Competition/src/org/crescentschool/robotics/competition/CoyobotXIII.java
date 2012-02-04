/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package org.crescentschool.robotics.competition;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.KinectStick;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import org.crescentschool.robotics.competition.commands.Autonomous;
import org.crescentschool.robotics.competition.commands.AutonomousShoot;
import org.crescentschool.robotics.competition.commands.KajDrive;
import org.crescentschool.robotics.competition.commands.KinectAuton;
import org.crescentschool.robotics.competition.commands.PIDTuning;
import org.crescentschool.robotics.competition.subsystems.Shooter;
import org.crescentschool.robotics.competition.subsystems.Turret;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class CoyobotXIII extends IterativeRobot {

    Command autonomous;
    KinectStick leftArm;
    int autonMode = 1;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        // Initialize all subsystems
        OI.getInstance();
        Turret.getInstance();
        Shooter.getInstance();
       // DriveTrain.getInstance();
       // Flipper.getInstance();
       // Intake.getInstance();
       // Feeder.getInstance();
        //leftArm = new KinectStick(1);
        //autonomous = new AutonomousShoot();
        //Camera.getInstance();
    }

    public void autonomousInit() {
        // schedule the autonomous command (example)
        autonomous.start();
    }

    public void autonomousPeriodic() {
        if (leftArm.getY(Hand.kLeft) > 0.7) {
            if (autonMode == 3) {
                autonMode = 1;
            } else {
                autonomous.cancel();
                autonMode++;
            }
            switch (autonMode) {
                default:
                case 1:
                    autonomous = new AutonomousShoot();
                    break;
                case 2:
                    autonomous = new Autonomous();
                    break;
                case 3:
                    autonomous = new KinectAuton();
                    break;
            }
            autonomous.start();
        }
        Scheduler.getInstance().run();
    }

    public void teleopInit() {
        // This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to 
        // continue until interrupted by another command, remove
        // this line or comment it out.
        //autonomous.cancel();
        Scheduler.getInstance().add(new PIDTuning());
        //Scheduler.getInstance().add(new FlipperPresets());
        //Scheduler.getInstance().add(new TurretControl());
        Scheduler.getInstance().add(new KajDrive());
    }

    public void teleopPeriodic() {
        Scheduler.getInstance().run();
        Buttons.update();
    }

    public void teleopContinuous() {
        //camera.processCamera();
    }
}
