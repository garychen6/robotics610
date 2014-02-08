/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.controls;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.crescentschool.robotics.competition.OI;
import org.crescentschool.robotics.competition.commands.T_Catcher;
import org.crescentschool.robotics.competition.commands.A_GyroTurn;
import org.crescentschool.robotics.competition.commands.T_Intake;
import org.crescentschool.robotics.competition.commands.T_KajDrive;
import org.crescentschool.robotics.competition.commands.A_PositionMove;
import org.crescentschool.robotics.competition.commands.T_Catapult;
import org.crescentschool.robotics.competition.constants.InputConstants;
import org.crescentschool.robotics.competition.subsystems.Camera;
import org.crescentschool.robotics.competition.subsystems.Catcher;
import org.crescentschool.robotics.competition.subsystems.DriveTrain;
import org.crescentschool.robotics.competition.subsystems.Intake;
import org.crescentschool.robotics.competition.subsystems.Shooter;

/**
 *
 * @author ianlo
 */
public class DriverControls extends Command {

    Preferences prefs;
    OI oi;
    Joystick driver;
    DriveTrain driveTrain;
    Catcher catcher;
    Intake intake;
    Shooter shooter;
    int driveMode = 0;
    Camera camera;

    public DriverControls() {
        prefs = Preferences.getInstance();
        oi = OI.getInstance();
        driver = oi.getDriver();
        shooter = Shooter.getInstance();
        driveTrain = DriveTrain.getInstance();
//        catcher = Catcher.getInstance();
        intake = Intake.getInstance();
        driveTrain.resetEncoders();
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
        //SmartDashboard.putNumber("Offset", camera.getOffset());
        int distance = prefs.getInt("distance", 0);
        int angle = prefs.getInt("angle", 0);


        if (driver.getRawButton(InputConstants.triangleButton) && driveMode != 0) {
            driveMode = 0;
            Scheduler.getInstance().add(new A_PositionMove(distance));
        } else if (driver.getRawButton(InputConstants.xButton) && driveMode != 1) {
            driveMode = 1;

            Scheduler.getInstance().add(new A_PositionMove(-distance));

        } else if (driver.getRawButton(InputConstants.squareButton) && driveMode != 2) {
            driveMode = 2;

            Scheduler.getInstance().add(new A_GyroTurn(-angle));

        } else if (driver.getRawButton(InputConstants.oButton) && driveMode != 3) {
            driveMode = 3;

            Scheduler.getInstance().add(new A_GyroTurn(angle));

        } else if ((Math.abs(driver.getRawAxis(InputConstants.leftYAxis)) > 0.2 || Math.abs(driver.getRawAxis(InputConstants.rightXAxis)) > 0.2) && driveMode != 4) {
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
