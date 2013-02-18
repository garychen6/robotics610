package org.crescentschool.robotics.competition.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.crescentschool.robotics.competition.OI;
import org.crescentschool.robotics.competition.constants.InputConstants;
import org.crescentschool.robotics.competition.subsystems.*;
import org.crescentschool.robotics.competition.controls.*;


/**
 *
 * @author bradmiller
 */
public class KajDrive extends Command {
    Shooter shooter;
    DriveTrain driveTrain;
    OI oi;
    Joystick driver;
    Pneumatics pneumatics;
  

    public KajDrive() {
        
        shooter = Shooter.getInstance();
        driveTrain = DriveTrain.getInstance();
        oi = OI.getInstance();
        driver = oi.getDriver();
        DriverControls.setDriveMode(0);
        shooter = Shooter.getInstance();
        pneumatics = Pneumatics.getInstance();
        requires(driveTrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        shooter.setLight(false);
        
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
                OurTimer time = OurTimer.getTimer("KajDrive");

        shooter.setLight(false);
        double rightSpeed, leftSpeed, x, y;
        x = driver.getRawAxis(InputConstants.rightXAxis);
        y = driver.getRawAxis(InputConstants.leftYAxis);
        x = x * x * x;
        y = y * y * y;
        leftSpeed = x - y;
        rightSpeed = -x - y;
        if (driver.getRawButton(InputConstants.l1Button)) {
            driveTrain.setLeftVBus(leftSpeed/2.0);
            driveTrain.setRightVBus(rightSpeed/2.0);
        } else{
            driveTrain.setLeftVBus(leftSpeed);
            driveTrain.setRightVBus(rightSpeed);
        }
        time.stop();
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
