/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package org.crescentschool.robotics.competition;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import org.crescentschool.robotics.competition.commands.Autonomous;
import org.crescentschool.robotics.competition.commands.PIDDriveTuning;
import org.crescentschool.robotics.competition.constants.ElectricalConstants;
import org.crescentschool.robotics.competition.subsystems.DriveTrain;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class CoyobotXIII extends IterativeRobot {

    Compressor compressor;
    Command autonomous = new Autonomous();
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
       

        //Camera.getInstance();
        compressor = new Compressor(ElectricalConstants.kCompressorPressureSwitchChannel, ElectricalConstants.kCompressorRelayChannel);
        compressor.start();
    }

    public void autonomousInit() {
        // schedule the autonomous command (example)
        autonomous.start();
    }

    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }

    public void teleopInit() {
        // This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to 
        // continue until interrupted by another command, remove
        // this line or comment it out.
        autonomous.cancel();
        Scheduler.getInstance().add(new PIDDriveTuning());
    }

    public void teleopPeriodic() {
        Scheduler.getInstance().run();
    }

    public void teleopContinuous() {
        //camera.processCamera();
    }
}