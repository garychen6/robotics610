/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.templates.commands.ShooterPIDCommand;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotTemplate extends IterativeRobot {
    /*
     Shooter shooter;
     DriveTrain driveTrain;
     Intake intake;
     */

    OI oi;
    //Command auton;
    Counter optical;
    CANJaguar shooter;
    CANJaguar shooter2;
    Compressor compressor;
    Relay tray;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        tray = new Relay(2);
        oi = OI.getInstance();
        compressor = new Compressor(1, 1);
        compressor.start();
        try {
            shooter = new CANJaguar(2);
            shooter.changeControlMode(CANJaguar.ControlMode.kSpeed);
            shooter.setSpeedReference(CANJaguar.SpeedReference.kQuadEncoder);
            shooter.configEncoderCodesPerRev(8);
            shooter.changeControlMode(CANJaguar.ControlMode.kVoltage);
            shooter.configNeutralMode(CANJaguar.NeutralMode.kCoast);
            shooter.enableControl(0);
            shooter2 = new CANJaguar(1);
            shooter2.changeControlMode(CANJaguar.ControlMode.kSpeed);
            shooter2.setSpeedReference(CANJaguar.SpeedReference.kQuadEncoder);
            shooter2.configEncoderCodesPerRev(8);
            shooter2.changeControlMode(CANJaguar.ControlMode.kVoltage);
            shooter2.configNeutralMode(CANJaguar.NeutralMode.kCoast);
            shooter2.enableControl(0);
            optical = new Counter(2);
            optical.setMaxPeriod(5);
            optical.setSemiPeriodMode(true);
            optical.start();
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
        ShooterPIDCommand shoot = new ShooterPIDCommand(0.00220, shooter, shooter2, optical);
        Thread thread = new Thread(shoot);
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();
        // auton = new Auton();

        // instantiate the command used for the autonomous period

    }

    public void autonomousInit() {
        // auton.start();
        // schedule the autonomous command (example)
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        //ShooterPIDCommand.pushPIDStats();

        Scheduler.getInstance().run();
    }

    public void teleopInit() {
        tray.set(Relay.Value.kForward);
        // This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to 
        // continue until interrupted by another command, remove
        // this line or comment it out.
        // System.out.println("Running");
        compressor.start();
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {


        ShooterPIDCommand.pushPIDStats();


        Scheduler.getInstance().run();
    }

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        LiveWindow.run();
    }
}
