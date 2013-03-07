/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package org.crescentschool.robotics.competition;

import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.KinectStick;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.image.ParticleAnalysisReport;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.crescentschool.robotics.competition.commands.A_D_distance;
//TODO: What happened to A_shootOnly? 
import org.crescentschool.robotics.competition.commands.A_shootAfter;
import org.crescentschool.robotics.competition.commands.A_shootFirst;
import org.crescentschool.robotics.competition.commands.A_shootOnly;
import org.crescentschool.robotics.competition.commands.G;
import org.crescentschool.robotics.competition.constants.InputConstants;
import org.crescentschool.robotics.competition.controls.DriverControls;
import org.crescentschool.robotics.competition.controls.OperatorControls;
import org.crescentschool.robotics.competition.subsystems.Camera;
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

    Gyro gyro;
    Command autonomous;
    KinectStick leftArm;
    Shooter shooter;
    DriveTrain driveTrain;
    Flipper flipper;
    Intake intake;
    Feeder feeder;
    OI oi;
    int autonMode = 1;
    String autonName = "";
    Turret turret;
    Camera camera;
    // CoyoBotUltrasonic ultrasonic;
    boolean kajMode = false;
    boolean autonChanged = false;
    CommandGroup auton;
    double autonWaitTime = 0;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        gyro = new Gyro(2);
        gyro.reset();
        // Initialize all subsystems
        oi = OI.getInstance();
        turret = Turret.getInstance();
        shooter = Shooter.getInstance();
        driveTrain = DriveTrain.getInstance();
        flipper = Flipper.getInstance();
        intake = Intake.getInstance();
        feeder = Feeder.getInstance();
        //leftArm = new KinectStick(1);
        // autonomous = new A_ST_shoot();
        camera = Camera.getInstance();
        //ultrasonic = CoyoBotUltrasonic.getInstance();
        auton = new G();
        //auton = new A_shootFirst();
    }

    public void autonomousInit() {
        // schedule the autonomous command (example)
        //auton.start(;
       auton.start();
        //driveTrain.setNewPos(2);
       /*
         turret.initPosMode();
         shooter.resetPID();
         auton.start();
         */
    }

    public void autonomousPeriodic() {

        Scheduler.getInstance().run();
        /* printDiagnostics();
         OI.printToDS(1, "Auton Mode: " + autonName);
         */
    }

    public void disabledPeriodicd() {
        //System.out.println("Driver EncL: " + driveTrain.getLeftSpeed());
        //System.out.println("Driver EncR: " + driveTrain.getRightSpeed());
        printDiagnostics();
        SmartDashboard.putString("Auton Running", autonName);
        SmartDashboard.putDouble("Shooter waitTime", autonWaitTime);
        switch (autonMode) {
            case 1:
                autonName = "ShootOnly";
                if (autonChanged) {
                    auton = new A_shootOnly(autonWaitTime);
                    autonChanged = false;
                }
                break;
            case 2:
                autonName = "ShootFirst";
                if (autonChanged) {
                    auton = new A_shootFirst();
                    autonChanged = false;
                }
                break;
            case 3:
                autonName = "ShootAfter";
                if (autonChanged) {
                    auton = new A_shootAfter();
                    autonChanged = false;
                }
                break;
        }
        if (Buttons.isPressed(InputConstants.kStartButton, 1)) {
            autonChanged = true;
            autonWaitTime += 0.5;
        }
        if (Buttons.isPressed(InputConstants.kSelectButton, 1)) {
            autonChanged = true;
            autonWaitTime -= 0.5;
        }
        if (Buttons.isPressed(InputConstants.kXButton, 1)) {
            autonChanged = true;
            autonMode = 1;
        }
        if (Buttons.isPressed(InputConstants.kAButton, 1)) {
            autonChanged = true;
            autonMode = 2;
        }
        if (Buttons.isPressed(InputConstants.kBButton, 1)) {
            autonChanged = true;
            autonMode = 3;
        }

//        System.out.println("Dpad X: "+ OI.getInstance().getOperator().getRawAxis(5));
//        System.out.println("Dpad Y: "+ OI.getInstance().getOperator().getRawAxis(6));
    }

    public void teleopInit() {
        // This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to 
        // continue until interrupted by another command, remove
        // this line or comment it out.
        //autonomous.System.out.println(this + " canceled");cancel();
        shooter.setAutonOver(true);
        Scheduler.getInstance().add(new DriverControls());
        Scheduler.getInstance().add(new OperatorControls());
//        Scheduler.getInstance().add(new M_P_Tuning());
    }

    public void teleopPeriodic() {
        Scheduler.getInstance().run();
        //camera.processCamera();
        Buttons.update();
        printDiagnostics();
        System.out.println(gyro.getAngle());


    }

    public void teleopContinuous() {
    }

    private void printDiagnostics() {
        //SmartDashboard.putDouble("Shooter Speed", (shooter.getShooterSpeed() - 1212) / -80.167);
        //SmartDashboard.putDouble("Left Drive Speed", -driveTrain.getLeftSpeed());
        //SmartDashboard.putDouble("Right Drive Speed", driveTrain.getRightSpeed());
        //SmartDashboard.putDouble("Horiz Gyro", driveTrain.getHorizAngle());
        //SmartDashboard.putDouble("Vert Gyro", driveTrain.getVertAngle());
        //SmartDashboard.putDouble("Camera Offset", camera.getX());
        //SmartDashboard.putDouble("Flipper Pot", flipper.getPos());
        SmartDashboard.putDouble("Turret Potentiometer", turret.getPos());
        SmartDashboard.putDouble("Turret Potentiometer SetPoint", turret.getPosSet());
        //SmartDashboard.putDouble("Turret Set", turret.getPosSet());
        //SmartDashboard.putDouble("Ultrasonic", ultrasonic.getDistance());
        ParticleAnalysisReport topTarget = Camera.getInstance().getTopTarget();
        if (Buttons.isHeld(InputConstants.kL2Button, oi.getOperator())) {
            if (topTarget != null) {
                SmartDashboard.putString("bounddata", topTarget.boundingRectLeft + ";" + topTarget.boundingRectTop + ";" + topTarget.boundingRectWidth + ";" + topTarget.boundingRectHeight + ";1");
            }
        } else {
            if (topTarget != null) {
                SmartDashboard.putString("bounddata", topTarget.boundingRectLeft + ";" + topTarget.boundingRectTop + ";" + topTarget.boundingRectWidth + ";" + topTarget.boundingRectHeight + ";0");
            }
        }
    }
}
