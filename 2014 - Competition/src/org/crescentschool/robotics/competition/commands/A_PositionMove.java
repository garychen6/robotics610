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
        if (targetInches > 0) {
            if (targetAngle > 0) {
                if (Lights.getInstance().isRedAlliance()) {
                    Lights.getInstance().setPattern(Lights.HOT_LEFT_RED);
                } else {
                    Lights.getInstance().setPattern(Lights.HOT_LEFT_BLUE);

                }
            } else if (targetInches < 0) {
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
        driveTrain.resetEncoders();
        driveTrain.resetGyro();
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

//    protected void execute() {
//        double p = PIDConstants.positionP;
//        double i = PIDConstants.positionI;
//        double leftTargetInches = targetInches+targetAngle;
//        double rightTargetInches = targetInches-targetAngle;
//
//        //Get the left and right values on the encoders
//        double leftInches = driveTrain.getLeftEncoderInches();
//        double rightInches = driveTrain.getRightEncoderInches();
//        double leftSpeed = (leftTargetInches - leftInches) * p;
//        double rightSpeed = (rightTargetInches - rightInches) * p;
//        SmartDashboard.putNumber("leftEnc", leftInches);
//        SmartDashboard.putNumber("rightEnc", rightInches);
//
//        double encoderError = Math.abs(targetInches - (leftInches + rightInches) / 2.0);
//
//
//        if (Math.abs(leftTargetInches - leftInches) < 3 || Math.abs(rightTargetInches - rightInches) < 3) {
//            finishedCount++;
//            if (finishedCount > 10) {
//                finished = true;
//                iCount = 0;
//
//            }
//
//        } else {
//            finished = false;
//        }
////        //GYRO DRIVE STRAIGHT CODE
////        double gyroError = Math.abs(driveTrain.getGyroDegrees() - targetAngle);
////        if (driveTrain.getGyroDegrees() - targetAngle < -0.01) {
////
////            rightSpeed -= gyroError * 0.02;
////
////            leftSpeed += gyroError * 0.02;
////
////        } else if (driveTrain.getGyroDegrees() - targetAngle > 0.01) {
////            rightSpeed += gyroError * 0.02;
////            leftSpeed -= gyroError * 0.02;
////
////        }
//
//
//        SmartDashboard.putNumber("Gyro", driveTrain.getGyroDegrees());
//        SmartDashboard.putNumber("leftSpeed", leftSpeed);
//        SmartDashboard.putNumber("rightSpeed", rightSpeed);
//        driveTrain.setLeftVBus(leftSpeed);
//        driveTrain.setRightVBus(rightSpeed);
//
//    }
    protected void execute() {
//        if (targetInches > 0) {
//            if (targetAngle > 0) {
//                if (Lights.getInstance().isRedAlliance()) {
//                    Lights.getInstance().setPattern(Lights.HOT_LEFT_RED);
//                } else {
//                    Lights.getInstance().setPattern(Lights.HOT_LEFT_BLUE);
//
//                }
//            } else if (targetInches < 0) {
//                if (Lights.getInstance().isRedAlliance()) {
//                    Lights.getInstance().setPattern(Lights.HOT_RIGHT_RED);
//                } else {
//                    Lights.getInstance().setPattern(Lights.HOT_RIGHT_BLUE);
//
//                }
//            } else {
//                Lights.getInstance().setPattern(Lights.TELE);
//
//            }
//        } else {
//            Lights.getInstance().setPattern(Lights.TELE);
//
//        }

        double p = PIDConstants.positionP;
        double i = PIDConstants.positionI;


        //Get the left and right values on the encoders
        double leftInches = driveTrain.getLeftEncoderInches();
        double rightInches = driveTrain.getRightEncoderInches();
        double leftSpeed = (targetInches - leftInches) * p;
        double rightSpeed = (targetInches - rightInches) * p;
        SmartDashboard.putNumber("leftEnc", leftInches);
        SmartDashboard.putNumber("rightEnc", rightInches);




        if (Math.abs(targetInches - leftInches) < 3 || Math.abs(targetInches - rightInches) < 3) {
            finishedCount++;
            if (finishedCount > 10) {
                finished = true;
                iCount = 0;

            }

        } else {
            finished = false;
        }
//        //GYRO DRIVE STRAIGHT CODE
        double gyroError = Math.abs(driveTrain.getGyroDegrees() - targetAngle);
        if (driveTrain.getGyroDegrees() - targetAngle < -0.01) {

            rightSpeed -= gyroError * 0.02;

            leftSpeed += gyroError * 0.02;

        } else if (driveTrain.getGyroDegrees() - targetAngle > 0.01) {
            rightSpeed += gyroError * 0.02;
            leftSpeed -= gyroError * 0.02;

        }
//        double driveStraightP = Preferences.getInstance().getDouble("encoderP", 0);
//        SmartDashboard.putNumber("encoderP", driveStraightP);
//        double encoderError = driveTrain.getLeftEncoderInches() - driveTrain.getRightEncoderInches();
//        if (encoderError < -2) {
//                rightSpeed -= Math.abs(encoderError) * driveStraightP;
//                leftSpeed += Math.abs(encoderError) * driveStraightP;
//
//           
//        } else if (encoderError > 2) {
//                rightSpeed += Math.abs(encoderError) * driveStraightP;
//                leftSpeed -= Math.abs(encoderError) * driveStraightP;
//
//            
//        }

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
