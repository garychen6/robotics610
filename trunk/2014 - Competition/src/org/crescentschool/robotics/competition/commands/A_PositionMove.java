/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.crescentschool.robotics.competition.OI;
import org.crescentschool.robotics.competition.constants.PIDConstants;
import org.crescentschool.robotics.competition.subsystems.DriveTrain;
import org.crescentschool.robotics.competition.subsystems.Lights;

/**
 *
 * @author ianlo
 */
public class A_PositionMove extends Command {

    private DriveTrain driveTrain;
    private double targetInches;
    private int iCap = 10000;
    private int iCount = 0;
    private OI oi;
    private Joystick driver;
    private int finishedCount = 0;
    private boolean finished = false;
    private int targetAngle;

    //NOT IMPLEMENTED/TESTED YET
    //Create a position move
    public A_PositionMove(int targetInches, int targetAngle) {
        setTimeout(PIDConstants.positionMoveTimeout);

        //Save the target number of inches.
        this.targetInches = targetInches;
        this.targetAngle = targetAngle;

        driveTrain = DriveTrain.getInstance();
        driveTrain.resetEncoders();
        driveTrain.resetGyro();
        finishedCount = 0;
        oi = OI.getInstance();
        driver = oi.getDriver();
        
        //Take control of the drivetrain
        requires(driveTrain);
    }

    protected void initialize() {
        System.out.println("Position Move " + targetInches + " angle " + targetAngle);
        //Reset the gyro and encoders
        driveTrain.resetEncoders();
        driveTrain.resetGyro();
        //If we're going forwards, set the light pattern
        if (targetAngle < 0) {
            if (Lights.getInstance().isRedAlliance()) {
                Lights.getInstance().setPattern(Lights.HOT_LEFT_RED);
            } else {
                Lights.getInstance().setPattern(Lights.HOT_LEFT_BLUE);

            }
        } else if (targetAngle > 0) {
            if (Lights.getInstance().isRedAlliance()) {
                Lights.getInstance().setPattern(Lights.HOT_RIGHT_RED);
            } else {
                Lights.getInstance().setPattern(Lights.HOT_RIGHT_BLUE);

            }
        }

    }

    protected void execute() {


        double p = PIDConstants.positionP;


        //Get the left and right values on the encoders
        double leftInches = driveTrain.getLeftEncoderInches();
        double rightInches = driveTrain.getRightEncoderInches();
        double leftSpeed = (targetInches - leftInches) * p;
        double rightSpeed = (targetInches - rightInches) * p;
        SmartDashboard.putNumber("leftEnc", leftInches);
        SmartDashboard.putNumber("rightEnc", rightInches);



        //If its within 3 inches of the target, add to a counter
        if (Math.abs(targetInches - leftInches) < 3 || Math.abs(targetInches - rightInches) < 3) {
            finishedCount++;
            //If its within the 3 inches for 10 counts, finish the command
            if (finishedCount > 10) {
                finished = true;
                iCount = 0;

            }

        } else {
            finished = false;
        }
//        GYRO DRIVE STRAIGHT CODE
        double gyroError = Math.abs(driveTrain.getGyroDegrees() - targetAngle);
        //Gyro correction code, just uses a p of 0.02
        if (driveTrain.getGyroDegrees() - targetAngle < -0.01) {

            rightSpeed -= gyroError * 0.02;

            leftSpeed += gyroError * 0.02;

        } else if (driveTrain.getGyroDegrees() - targetAngle > 0.01) {
            rightSpeed += gyroError * 0.02;
            leftSpeed -= gyroError * 0.02;

        }

        //Post data to the dashboard
        SmartDashboard.putNumber("Gyro", driveTrain.getGyroDegrees());


        SmartDashboard.putNumber("leftSpeed", leftSpeed);
        SmartDashboard.putNumber("rightSpeed", rightSpeed);
        driveTrain.setLeftVBus(leftSpeed);
        driveTrain.setRightVBus(rightSpeed);

    }

    protected boolean isFinished() {
        if (finished || isTimedOut()) {
            System.out.println("Position Move Finished");
            driveTrain.setLeftVBus(0);
            driveTrain.setRightVBus(0);
        }
        return finished || isTimedOut();
    }

    protected void end() {
    }

    protected void interrupted() {
    }
}
