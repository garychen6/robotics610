    /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.templates.OI;
import edu.wpi.first.wpilibj.templates.subsystems.DriveTrain;
import org.crescentschool.robotics.competition.constants.InputConstants;

/**
 *
 * @author ianlo
 */
public class T_GuestDrive extends Command {

    private OI oi;
    private Joystick operator, driver;
    private DriveTrain driveTrain;
    private boolean guestAccess = false;

    public T_GuestDrive() {
        //Get the IO, joystick and drivetrain instances.
        oi = OI.getInstance();
        driver = oi.getDriver();
        operator = oi.getOperator();
        driveTrain = DriveTrain.getInstance();
        System.out.println("Guest Drive");

        //Take control of the drivetrain
        requires(driveTrain);

    }

    protected void initialize() {
    }

    protected void execute() {
        //Create variables for x, y, right speed and the left speed
        double rightSpeed, leftSpeed, x, y;
        if (Math.abs(driver.getRawAxis(InputConstants.rightXAxis)) > 0.1 || Math.abs(driver.getRawAxis(InputConstants.rightYAxis)) > 0.1 || Math.abs(driver.getRawAxis(InputConstants.leftXAxis)) > 0.1 || Math.abs(driver.getRawAxis(InputConstants.leftYAxis)) > 0.1) {
            guestAccess = false;
        } else if (driver.getRawButton(InputConstants.squareButton) && guestAccess == false) {
            guestAccess = true;
            System.out.println("Guest Access Enabled");
        }

        if (guestAccess) {
            //Set x and y to their axis values
            x = operator.getRawAxis(InputConstants.rightXAxis);
            y = operator.getRawAxis(InputConstants.leftYAxis);

            //Set the left and rightspeed using x and y
            leftSpeed = y - x;
            rightSpeed = y + x;
            leftSpeed /= 2.0;
            rightSpeed /= 2.0;

        } else {

            //Set x and y to their axis values
            x = driver.getRawAxis(InputConstants.rightXAxis);
            y = driver.getRawAxis(InputConstants.leftYAxis);

            //Set the left and rightspeed using x and y
            leftSpeed = y - x;
            rightSpeed = y + x;

        }

        //Set the left and right side of the drive
        driveTrain.setLeftVBus(-leftSpeed);
        driveTrain.setRightVBus(-rightSpeed);

    }

    protected boolean isFinished() {
        //KajDrive is never finished.
        return false;
    }

    protected void end() {
    }

    protected void interrupted() {
    }
}
