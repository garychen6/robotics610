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
import org.crescentschool.robotics.competition.subsystems.Lights;

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
    private int finishedCount = 0;
    private boolean finished = false;
    private int targetAngle;

    //NOT IMPLEMENTED/TESTED YET
    //Create a position move
    public A_PositionMove(int targetInches, int targetAngle) {
        setTimeout(PIDConstants.positionMoveTimeout);
        //Get the robot preferences from the smartdashboard
        prefs = Preferences.getInstance();

        //Save the target number of inches.
        this.targetInches = targetInches;
        this.targetAngle = targetAngle;

        driveTrain = DriveTrain.getInstance();
        driveTrain.resetEncoders();
        driveTrain.resetGyro();
        finishedCount = 0;
        oi = OI.getInstance();
        driver = oi.getDriver();
        if (targetInches > 0) {
            if (targetAngle < 0) {
                if (Lights.getInstance().isRedAlliance()) {
                    Lights.getInstance().setPattern(Lights.HOT_LEFT_RED);
                } else {
                    Lights.getInstance().setPattern(Lights.HOT_LEFT_BLUE);

                }
            } else if (targetInches > 0) {
                if (Lights.getInstance().isRedAlliance()) {
                    Lights.getInstance().setPattern(Lights.HOT_RIGHT_RED);
                } else {
                    Lights.getInstance().setPattern(Lights.HOT_RIGHT_BLUE);

                }
            } else {
                Lights.getInstance().setPattern(Lights.TELE);

            }
        } else {
            Lights.getInstance().setPattern(Lights.TELE);

        }
        //Take control of the drivetrain
        requires(driveTrain);
    }

    protected void initialize() {
        System.out.println("Position Move " + targetInches + " angle " + targetAngle);

    }

    protected void execute() {
        double p = PIDConstants.positionP;
        double i = PIDConstants.positionI;


        //Get the left and right values on the encoders
        double leftInches = driveTrain.getLeftEncoderInches();
        double rightInches = driveTrain.getRightEncoderInches();
        double leftSpeed = (targetInches - leftInches) * p;
        double rightSpeed = (targetInches - rightInches) * p;
        SmartDashboard.putNumber("leftEnc", leftInches);
        SmartDashboard.putNumber("rightEnc", rightInches);

        if (iCount < iCap) {
            iCount++;
        }
        if (iCount
                > -iCap) {
            iCount--;
        }

        double encoderError = Math.abs(targetInches - (leftInches + rightInches) / 2.0);


        if (Math.abs(targetInches - leftInches) < 3 || Math.abs(targetInches - rightInches) < 3) {
            finishedCount++;
            if (finishedCount > 10) {
                finished = true;
                iCount = 0;

            }

        } else {
            finished = false;
        }
        double gyroError = Math.abs(driveTrain.getGyroDegrees() - targetAngle);
        if (driveTrain.getGyroDegrees() - targetAngle < -0.01) {

            rightSpeed -= gyroError * 0.02;

            leftSpeed += gyroError * 0.02;

        } else if (driveTrain.getGyroDegrees() - targetAngle > 0.01) {
            rightSpeed += gyroError * 0.02;
            leftSpeed -= gyroError * 0.02;

        }

        SmartDashboard.putNumber("Gyro", driveTrain.getGyroDegrees());


        leftSpeed += i * iCount;
        rightSpeed += i * iCount;
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
