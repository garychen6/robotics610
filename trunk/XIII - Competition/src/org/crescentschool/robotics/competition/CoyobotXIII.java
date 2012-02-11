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
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.crescentschool.robotics.competition.commands.Autonomous;
import org.crescentschool.robotics.competition.commands.AutonomousShoot;
import org.crescentschool.robotics.competition.commands.BridgeMode;
import org.crescentschool.robotics.competition.commands.FlipperPresets;
import org.crescentschool.robotics.competition.commands.KajDrive;
import org.crescentschool.robotics.competition.commands.KinectAuton;
import org.crescentschool.robotics.competition.commands.ManualShooter;
import org.crescentschool.robotics.competition.constants.InputConstants;
import org.crescentschool.robotics.competition.subsystems.Camera;
import org.crescentschool.robotics.competition.subsystems.CoyoBotUltrasonic;
import org.crescentschool.robotics.competition.subsystems.DriveTrain;
import org.crescentschool.robotics.competition.subsystems.Feeder;
import org.crescentschool.robotics.competition.subsystems.Flipper;
import org.crescentschool.robotics.competition.subsystems.Intake;
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
    Shooter shooter;
    DriveTrain driveTrain;
    Flipper flipper;
    Intake intake;
    Feeder feeder;
    OI oi;
    int autonMode = 1;
    Turret turret;
    Camera camera;
    CoyoBotUltrasonic ultrasonic;
    boolean kajMode = false;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        // Initialize all subsystems
        oi = OI.getInstance();
        turret = Turret.getInstance();
        shooter = Shooter.getInstance();
        driveTrain = DriveTrain.getInstance();
        flipper = Flipper.getInstance();
        intake = Intake.getInstance();
        feeder = Feeder.getInstance();
        //leftArm = new KinectStick(1);
        //autonomous = new AutonomousShoot();
        camera = Camera.getInstance();
        ultrasonic = CoyoBotUltrasonic.getInstance();
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

    public void disabledPeriodic() {
        //System.out.println("Driver EncL: " + driveTrain.getLeftSpeed());
        //System.out.println("Driver EncR: " + driveTrain.getRightSpeed());
        printDiagnostics();
    }

    public void teleopInit() {
        // This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to 
        // continue until interrupted by another command, remove
        // this line or comment it out.
        //autonomous.cancel();
        //Scheduler.getInstance().add(new PIDTuning());
        Scheduler.getInstance().add(new ManualShooter());
        Scheduler.getInstance().add(new FlipperPresets());
        //Scheduler.getInstance().add(new TurretControl());
        //Scheduler.getInstance().add(new BridgeMode());
    }

    public void teleopPeriodic() {
        Scheduler.getInstance().run();
        //if(Buttons.isPressed(autonMode, null))
        if (!kajMode) {
            if (Math.abs(oi.getDriver().getRawAxis(InputConstants.kLeftYAxis)) > 0.2) {
                Scheduler.getInstance().add(new KajDrive());
                kajMode = true;
            } else if (Math.abs(oi.getDriver().getRawAxis(InputConstants.kRightXAxis)) > 0.2) {
                Scheduler.getInstance().add(new KajDrive());
                kajMode = true;
            }
        } else {
            if (Buttons.isPressed(InputConstants.kBButton, oi.getDriver())) {
                Scheduler.getInstance().add(new BridgeMode());
                kajMode = false;
            } else if (Buttons.isPressed(InputConstants.kAButton, oi.getDriver())) {
                Scheduler.getInstance().add(new BridgeMode());
                kajMode = false;
            }

        }
        Buttons.update();
        printDiagnostics();


    }

    public void teleopContinuous() {
        camera.processCamera();
    }

    private void printDiagnostics() {
        SmartDashboard.putDouble("Shooter Speed", -shooter.getShooterSpeed());
        SmartDashboard.putDouble("Left Drive Speed", -driveTrain.getLeftSpeed());
        SmartDashboard.putDouble("Right Drive Speed", driveTrain.getRightSpeed());
        SmartDashboard.putDouble("Horiz Gyro", driveTrain.getHorizAngle());
        SmartDashboard.putDouble("Vert Gyro", driveTrain.getVertAngle());
        SmartDashboard.putDouble("Camera Offset", camera.getX());
        SmartDashboard.putDouble("Flipper Pot", flipper.getPos());
        SmartDashboard.putDouble("Turret Pot", turret.getPos());
        SmartDashboard.putDouble("Ultrasonic", ultrasonic.getDistance());
    }
}
