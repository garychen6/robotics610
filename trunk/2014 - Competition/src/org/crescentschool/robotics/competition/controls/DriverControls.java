/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.controls;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.crescentschool.robotics.competition.OI;
import org.crescentschool.robotics.competition.commands.T_Intake;
import org.crescentschool.robotics.competition.commands.T_KajDrive;
import org.crescentschool.robotics.competition.commands.T_Catapult;
import org.crescentschool.robotics.competition.constants.ImagingConstants;
import org.crescentschool.robotics.competition.constants.InputConstants;
import org.crescentschool.robotics.competition.subsystems.Camera;
import org.crescentschool.robotics.competition.subsystems.DriveTrain;
import org.crescentschool.robotics.competition.subsystems.Intake;
import org.crescentschool.robotics.competition.subsystems.Catapult;

/**
 *
 * @author ianlo
 */
public class DriverControls extends Command {

    OI oi;
    Joystick driver;
    DriveTrain driveTrain;
    Intake intake;
    Catapult shooter;
    int driveMode = 0;
    Camera camera;
    boolean ringLightButtonPressed = false;
    boolean camLightOn = false;
    Joystick operator;
    int count = 0;
    int[] pastUltrasonicReads = new int[10];

    public DriverControls() {
        System.out.println("Driver Controls");
        oi = OI.getInstance();
        driver = oi.getDriver();
        shooter = Catapult.getInstance();
        driveTrain = DriveTrain.getInstance();
        intake = Intake.getInstance();
        driveTrain.resetEncoders();
        camera = Camera.getInstance();
        operator = oi.getOperator();
        Scheduler.getInstance().add(new T_KajDrive());
        Scheduler.getInstance().add(new T_Intake());
        Scheduler.getInstance().add(new T_Catapult());
        

        // camera = Camera.getInstance();
        System.out.println("Driver Controls");
        driveMode = 4;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        
//        SmartDashboard.putNumber("Ultrasonic", camera.getUltrasonicInches());
        SmartDashboard.putNumber("Gyro", driveTrain.getGyroDegrees());
                SmartDashboard.putNumber("leftEnc", driveTrain.getLeftEncoderInches());
                SmartDashboard.putNumber("rightEnc", driveTrain.getRightEncoderInches());

//        int offset = camera.getOffset(10);
//        if(offset!=0){
//            System.out.println(offset);
//        }
//        camera.getOffset(15);
        if (driver.getRawButton(InputConstants.startButton) && !ringLightButtonPressed) {
            ringLightButtonPressed = true;

            camLightOn = !camLightOn;

        } else if (!driver.getRawButton(InputConstants.startButton)) {
            ringLightButtonPressed = false;
        }
        camera.setRingLight(camLightOn);


        if ((Math.abs(driver.getRawAxis(InputConstants.leftYAxis)) > 0.2 || Math.abs(driver.getRawAxis(InputConstants.rightXAxis)) > 0.2) && driveMode != 4) {

            driveMode = 4;

            Scheduler.getInstance().add(new T_KajDrive());

        }
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
