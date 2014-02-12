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
import org.crescentschool.robotics.competition.constants.PIDConstants;
import org.crescentschool.robotics.competition.subsystems.DriveTrain;

/**
 *
 * @author ianlo
 */
public class A_GyroTurn extends Command {

    double targetDegrees = 0;
    private DriveTrain driveTrain;
    private Preferences prefs;
    private int iCap = 10000;
    private int iCount = 0;
    private OI oi;
    private boolean finished = false;
    private int finishedCount = 0;
    boolean badGyro = false;
    //POSTIVE ANGLE IS CLOCKWISE

    public A_GyroTurn(double targetDegrees) {
        setTimeout(PIDConstants.gyroTurnTimeout);
        this.targetDegrees = targetDegrees;
        //Get the robot preferences from the smartdashboard
        prefs = Preferences.getInstance();

        //Save the target number of inches.
        driveTrain = DriveTrain.getInstance();
        driveTrain.resetEncoders();
        driveTrain.resetGyro();
        oi = OI.getInstance();
        //Take control of the drivetrain
        requires(driveTrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        System.out.println("Gyro Turn " + targetDegrees);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        if (!badGyro) {
            double p = PIDConstants.gyroP;
            double i = PIDConstants.gyroI;


            //Get the left and right values on the encoders
            double gyro = driveTrain.getGyroDegrees();
            double error = (targetDegrees - gyro);
            double leftSpeed = error * p;
            double rightSpeed = -error * p;

            SmartDashboard.putNumber("Gyro", gyro);

            if (leftSpeed > 0.05) {
                if (iCount < iCap) {
                    iCount++;
                }
            } else if (leftSpeed < -.05) {
                if (iCount > -iCap) {
                    iCount--;
                }
            }
            if (Math.abs(error) < 1) {

                finishedCount++;
                iCount = 0;

                if (finishedCount > 20) {

                    finished = true;
                }

            } else {
                finishedCount = 0;
            }

            SmartDashboard.putNumber("turnI", iCount * i);
            leftSpeed += i * iCount;
            rightSpeed -= i * iCount;

            driveTrain.setLeftVBus(leftSpeed);
            driveTrain.setRightVBus(rightSpeed);
        } else {
            driveTrain.setLeftVBus(0);
            driveTrain.setRightVBus(0);
        }

    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {

        if (Math.abs(driveTrain.getLeftEncoderInches()) > PIDConstants.gyroPositionCheck||badGyro) {
            badGyro = true;
            return false;
        } else {
            return finished || isTimedOut();
        }
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
