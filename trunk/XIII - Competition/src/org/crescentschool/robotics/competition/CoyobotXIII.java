/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package org.crescentschool.robotics.competition;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import org.crescentschool.robotics.competition.commands.FlipperPresets;
import org.crescentschool.robotics.competition.commands.PIDDriveTuning;
import org.crescentschool.robotics.competition.subsystems.DriveTrain;
import org.crescentschool.robotics.competition.subsystems.Feeder;
import org.crescentschool.robotics.competition.subsystems.Flipper;
import org.crescentschool.robotics.competition.subsystems.Intake;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class CoyobotXIII extends IterativeRobot {

    //Compressor compressor;
    //Command autonomous = new Autonomous();
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        // instantiate the command used for the autonomous period
        //autonomousCommand = new TankDrive();

        // Initialize all subsystems
        //CommandBase.init
        OI.getInstance();
        DriveTrain.getInstance();
        Flipper.getInstance();
        Intake.getInstance();
        Feeder.getInstance();
        //Camera.getInstance();
    }

    public void autonomousInit() {
        // schedule the autonomous command (example)
       // autonomous.start();
        
    }

    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }

    public void teleopInit() {
        // This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to 
        // continue until interrupted by another command, remove
        // this line or comment it out.
        //autonomous.cancel();
        Scheduler.getInstance().add(new PIDDriveTuning());
        Scheduler.getInstance().add(new FlipperPresets());
    }

    public void teleopPeriodic() {
        Scheduler.getInstance().run();
        Buttons.update();
    }

    public void teleopContinuous() {
        //camera.processCamera();
    }
}
