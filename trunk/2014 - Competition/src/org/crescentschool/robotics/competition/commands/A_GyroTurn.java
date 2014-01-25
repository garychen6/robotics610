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
public class A_GyroTurn extends Command {

    double targetDegrees = 0;
    private DriveTrain driveTrain;
    private Preferences prefs;
    private int iCap = 10000;
    private int iCount = 0;
    private OI oi;
    private boolean finished = false;

    public A_GyroTurn(double targetDegrees) {
        this.targetDegrees = targetDegrees;
        //Get the robot preferences from the smartdashboard
        prefs = Preferences.getInstance();

        //Save the target number of inches.
        driveTrain = DriveTrain.getInstance();
        driveTrain.resetEncoders();
        oi = OI.getInstance();
        //Take control of the drivetrain
        requires(driveTrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        driveTrain.resetGyro();
        System.out.println("Gyro Turn " + targetDegrees);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        double p = prefs.getDouble("gyroP", 0);
        double i = prefs.getDouble("gyroI", 0);


        //Get the left and right values on the encoders
        double gyro = driveTrain.getGyroDegrees();
        double error = (targetDegrees - gyro);
        double leftSpeed = error * p;
        double rightSpeed = -error * p;

        SmartDashboard.putNumber("gyro", gyro);

        if (leftSpeed > 0.05) {
            if (iCount < iCap) {
                iCount++;
            }
        } else if (leftSpeed < -.05) {
            if (iCount > -iCap) {
                iCount--;
            }
        }
        if(Math.abs(error)<2){
            iCount=0;
            finished = true;
        }
        SmartDashboard.putNumber("Gyro", driveTrain.getGyroDegrees());

        SmartDashboard.putNumber("turnI", iCount * i);
        leftSpeed += i * iCount;
        rightSpeed -= i * iCount;
        driveTrain.setLeftVBus(leftSpeed);
        driveTrain.setRightVBus(rightSpeed);

    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return finished;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
