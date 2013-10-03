package org.crescentschool.robotics.competition.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
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
    Joystick operator;
    Pneumatics pneumatics;
    boolean locked = false;
    boolean halfSpeed = false;

    public KajDrive() {

        shooter = Shooter.getInstance();
        driveTrain = DriveTrain.getInstance();
        oi = OI.getInstance();
        driver = oi.getDriver();
        operator = oi.getOperator();
        DriverControls.setDriveMode(0);
        shooter = Shooter.getInstance();
        pneumatics = Pneumatics.getInstance();
        System.out.println("KajDrive");
        requires(driveTrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        //  shooter.setLight(false);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        OurTimer time = OurTimer.getTimer("KajDrive");

        //shooter.setLight(false);
        double rightSpeed, leftSpeed, x, y;


        if (InputConstants.competitionCode == false) {
            //System.out.println("Locked: " + locked);
            if (operator.getRawButton(InputConstants.r1Button)) {
                halfSpeed = !halfSpeed;
            }
            if (!locked && operator.getRawAxis(InputConstants.leftXAxis) > 0.2
                    || operator.getRawAxis(InputConstants.leftXAxis) < -0.2
                    || operator.getRawAxis(InputConstants.leftYAxis) > 0.2
                    || operator.getRawAxis(InputConstants.leftYAxis) < -0.2
                    || operator.getRawAxis(InputConstants.rightXAxis) > 0.2
                    || operator.getRawAxis(InputConstants.rightXAxis) < -0.2
                    || operator.getRawAxis(InputConstants.rightYAxis) > 0.2
                    || operator.getRawAxis(InputConstants.rightYAxis) < -0.2) {
                locked = true;
            }
            if (locked) {
                x = operator.getRawAxis(InputConstants.rightXAxis);
                y = operator.getRawAxis(InputConstants.leftYAxis);
            } else {
                x = driver.getRawAxis(InputConstants.rightXAxis);
                y = driver.getRawAxis(InputConstants.leftYAxis);
            }
            if (operator.getRawButton(InputConstants.triangleButton)) {
                locked = false;
            }
            x = x * x * x;
            y = y * y * y;
            if (halfSpeed) {
                leftSpeed = (x - y) / 2;
                rightSpeed = (-x - y) / 2;
            } else {
                leftSpeed = x - y;
                rightSpeed = -x - y;
            }
        } else {
            x = driver.getRawAxis(InputConstants.rightXAxis);
            y = driver.getRawAxis(InputConstants.leftYAxis);
            x = x * x * x;
            y = y * y * y;
            leftSpeed = x - y;
            rightSpeed = -x - y;
        }
        if (driver.getRawButton(InputConstants.l1Button)) {
            driveTrain.setLeftVBus(leftSpeed / 2.0);
            driveTrain.setRightVBus(rightSpeed / 2.0);
        } else {
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
