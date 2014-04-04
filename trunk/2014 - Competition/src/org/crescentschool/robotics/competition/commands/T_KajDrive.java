    /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
import org.crescentschool.robotics.competition.OI;
import org.crescentschool.robotics.competition.constants.InputConstants;
import org.crescentschool.robotics.competition.subsystems.DriveTrain;

/**
 *
 * @author ianlo
 */
public class T_KajDrive extends Command {

    private OI oi;
    private Joystick driver;
    private DriveTrain driveTrain;
    
    public T_KajDrive() {
        //Get the IO, joystick and drivetrain instances.
        oi = OI.getInstance();
        driver = oi.getDriver();
        driveTrain = DriveTrain.getInstance();
        System.out.println("Kaj Drive");

        //Take control of the drivetrain
        requires(driveTrain);

    }

    protected void initialize() {
    }

    protected void execute() {
        
        //Create variables for x, y, right speed and the left speed
        double rightSpeed, leftSpeed, x, y;
        //Set x and y to their axis values
        x = driver.getRawAxis(InputConstants.rightXAxis);
        y = driver.getRawAxis(InputConstants.leftYAxis);
        //Drive Smoothing

        //Set the left and rightspeed using x and y
        leftSpeed = y - x;
        rightSpeed = y + x;
        

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
