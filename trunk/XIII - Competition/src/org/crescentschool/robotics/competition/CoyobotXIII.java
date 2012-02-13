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
import edu.wpi.first.wpilibj.image.ParticleAnalysisReport;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.crescentschool.robotics.competition.commands.A_1;
import org.crescentschool.robotics.competition.commands.A_ST_shoot;
import org.crescentschool.robotics.competition.commands.A_K_human;
import org.crescentschool.robotics.competition.constants.InputConstants;
import org.crescentschool.robotics.competition.controls.DriverControls;
import org.crescentschool.robotics.competition.controls.OperatorControls;
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
        //autonomous = new A_ST_shoot();
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
                    autonomous = new A_ST_shoot();
                    break;
                case 2:
                    autonomous = new A_1();
                    break;
                case 3:
                    autonomous = new A_K_human();
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
        Scheduler.getInstance().add(new DriverControls());
        Scheduler.getInstance().add(new OperatorControls());
    }

    public void teleopPeriodic() {
        camera.processCamera();
        Scheduler.getInstance().run();
        Buttons.update();
        printDiagnostics();
        
    }

    public void teleopContinuous() {
    }

    private void printDiagnostics() {
        SmartDashboard.putDouble("Shooter Speed", -shooter.getShooterSpeed());
        //SmartDashboard.putDouble("Left Drive Speed", -driveTrain.getLeftSpeed());
        //SmartDashboard.putDouble("Right Drive Speed", driveTrain.getRightSpeed());
        //SmartDashboard.putDouble("Horiz Gyro", driveTrain.getHorizAngle());
        //SmartDashboard.putDouble("Vert Gyro", driveTrain.getVertAngle());
        SmartDashboard.putDouble("Camera Offset", camera.getX());
        //SmartDashboard.putDouble("Flipper Pot", flipper.getPos());
        SmartDashboard.putDouble("Turret Pot", turret.getPos());
        SmartDashboard.putDouble("Turret Set", turret.getPosSet());
        SmartDashboard.putDouble("Ultrasonic", ultrasonic.getDistance());
        ParticleAnalysisReport topTarget = Camera.getInstance().getTopTarget();
        if(Buttons.isHeld(InputConstants.kL2Button, oi.getOperator())){
            if(topTarget != null)SmartDashboard.putString("bounddata", topTarget.boundingRectLeft + ";" + topTarget.boundingRectTop + ";" + topTarget.boundingRectWidth + ";" + topTarget.boundingRectHeight + ";1");
        } else {
            if(topTarget != null)SmartDashboard.putString("bounddata", topTarget.boundingRectLeft + ";" + topTarget.boundingRectTop + ";" + topTarget.boundingRectWidth + ";" + topTarget.boundingRectHeight + ";0");
        }
    }
}
