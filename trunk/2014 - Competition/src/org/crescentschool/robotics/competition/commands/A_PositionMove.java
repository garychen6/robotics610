/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.crescentschool.robotics.competition.OI;
import org.crescentschool.robotics.competition.constants.InputConstants;
import org.crescentschool.robotics.competition.subsystems.DriveTrain;

/**
 *
 * @author ianlo
 */
public class A_PositionMove extends Command {

    private DriveTrain driveTrain;
    private double targetInches;
    private Preferences prefs;
    private int iCap = 10000;
    private int iCount = 0;
    private OI oi;
    private Joystick driver;
    private boolean finished = false;

    //NOT IMPLEMENTED/TESTED YET
    //Create a position move
    public A_PositionMove(double targetInches) {

        //Get the robot preferences from the smartdashboard
        prefs = Preferences.getInstance();

        //Save the target number of inches.
        this.targetInches = targetInches;
        driveTrain = DriveTrain.getInstance();
        
        oi = OI.getInstance();
        driver = oi.getDriver();
        //Take control of the drivetrain
        requires(driveTrain);
    }

    protected void initialize() {
        System.out.println("Position Move " + targetInches);
        driveTrain.resetEncoders();
        driveTrain.resetGyro();
    }

    protected void execute() {
        double p = prefs.getDouble("driveP", 0);
        double i = prefs.getDouble("driveI", 0);


        //Get the left and right values on the encoders
        double leftInches = driveTrain.getLeftEncoderInches();
        double rightInches = driveTrain.getRightEncoderInches();
        double leftSpeed = (targetInches - leftInches) * p;
        double rightSpeed = (targetInches - rightInches) * p;
        SmartDashboard.putNumber("leftEnc", leftInches);
        SmartDashboard.putNumber("rightEnc", rightInches);

        if (leftSpeed > 0.05) {
            if (iCount < iCap) {
                iCount++;
            }
        } else if (leftSpeed < -.05) {
            if (iCount > -iCap) {
                iCount--;
            }
        }
        double encoderError = Math.abs(targetInches-(leftInches+rightInches)/2.0);
        if(encoderError<0.1){
            finished = true;
            iCount=0;
        }
        double gyroError = Math.abs(driveTrain.getGyroDegrees());
        if (driveTrain.getGyroDegrees() < -0.5) {

            rightSpeed -= gyroError * 0.05;

            leftSpeed += gyroError * 0.05;

        } else if (driveTrain.getGyroDegrees() > 0.5) {
            rightSpeed += gyroError * 0.05;
            leftSpeed -= gyroError * 0.05;

        }
        
        SmartDashboard.putNumber("Gyro", driveTrain.getGyroDegrees());
        SmartDashboard.putNumber("driveI", iCount * i);
        leftSpeed += i * iCount;
        rightSpeed += i * iCount;
        driveTrain.setLeftVBus(leftSpeed);
        driveTrain.setRightVBus(rightSpeed);
        
    }

    protected boolean isFinished() {
        return finished;
    }

    protected void end() {
    }

    protected void interrupted() {
    }
}
