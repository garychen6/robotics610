/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.controls;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import java.io.IOException;
import org.crescentschool.robotics.competition.OI;
import org.crescentschool.robotics.competition.constants.InputConstants;
import org.crescentschool.robotics.competition.constants.ShootingConstants;
import org.crescentschool.robotics.competition.constants.PIDConstants;
import org.crescentschool.robotics.competition.subsystems.Pneumatics;
import org.crescentschool.robotics.competition.subsystems.*;
import org.crescentschool.robotics.competition.commands.*;

/**
 *
 * @author robotics
 */
public class OperatorControls extends Command {

    OI oi = OI.getInstance();
    Joystick operator = oi.getOperator();
    Joystick driver = oi.getDriver();
    Shooter shooter = Shooter.getInstance();
    Pneumatics pneumatics = Pneumatics.getInstance();
    DriveTrain driveTrain = DriveTrain.getInstance();
    int nearSpeed = ShootingConstants.baseNearShooterRPM;
    int farSpeed = ShootingConstants.baseFarShooterRPM;
    int leftSpeed = ShootingConstants.baseLeftMiddleShooter;
    int rightSpeed = ShootingConstants.baseRightMiddleShooter;
    boolean lightOn = false;
    boolean locking = false;
    OurTimer time;
    boolean trimStick = false;
    int trimTime = 0;
    double trimPower = ShootingConstants.trimPower;
    private static boolean trimming = false;
    private static int shootingPosition = 0;
    //0 = Pyramid Radius, 1 = Feeder, 2 = Left, 3 = Right

    protected void initialize() {
    }

    protected void execute() {


        if (operator.getRawButton(InputConstants.l2Button)) {
            ShooterPIDCommand.setShooterAngle(1);
            setShootingPosition(1);
            shooter.setSpeed(farSpeed);
            shooter.setPID(PIDConstants.shooterP, PIDConstants.shooterI, PIDConstants.shooterD, PIDConstants.shooterFF);
            pneumatics.setAngleUp(false);
        } else if (operator.getRawButton(InputConstants.l1Button)) {
            setShootingPosition(0);
            shooter.setSpeed(nearSpeed);
            shooter.setPID(PIDConstants.shooterP, PIDConstants.shooterI, PIDConstants.shooterD, PIDConstants.shooterFF);
            pneumatics.setAngleUp(true);
        } else if (operator.getRawButton(InputConstants.squareButton)) {
            setShootingPosition(2);
            shooter.setSpeed(leftSpeed);
            shooter.setPID(PIDConstants.shooterP, PIDConstants.shooterI, PIDConstants.shooterD, PIDConstants.shooterFF);
            pneumatics.setAngleUp(false);
        } else if (operator.getRawButton(InputConstants.oButton)) {
            setShootingPosition(3);
            shooter.setSpeed(rightSpeed);
            shooter.setPID(PIDConstants.shooterP, PIDConstants.shooterI, PIDConstants.shooterD, PIDConstants.shooterFF);
            pneumatics.setAngleUp(false);
        }
        
        //shooter.setLight(operator.getRawButton(InputConstants.startButton));
        if (driver.getRawAxis(InputConstants.dPadY) < -0.2) {
            shooter.setSpeed(nearSpeed - ShootingConstants.moveBack);
        }

        //btn1 reset
        if (operator.getRawButton(InputConstants.l1Button)) {
            ShooterPIDCommand.setShooterAngle(0);
            nearSpeed = ShootingConstants.baseNearShooterRPM;
            farSpeed = ShootingConstants.baseFarShooterRPM;
            leftSpeed = ShootingConstants.baseLeftMiddleShooter;
            rightSpeed = ShootingConstants.baseRightMiddleShooter;

            if (getShootingPosition() == 0) {
                shooter.setSpeed(nearSpeed);
                shooter.setPID(PIDConstants.shooterP, PIDConstants.shooterI, PIDConstants.shooterD, PIDConstants.shooterFF);
            } else if (getShootingPosition() == 1) {
                shooter.setSpeed(farSpeed);
                shooter.setPID(PIDConstants.shooterP, PIDConstants.shooterI, PIDConstants.shooterD, PIDConstants.shooterFF);
            } else if (getShootingPosition() == 2) {
                shooter.setSpeed(leftSpeed);
                shooter.setPID(PIDConstants.shooterP, PIDConstants.shooterI, PIDConstants.shooterD, PIDConstants.shooterFF);
            } else if (getShootingPosition() == 3) {
                shooter.setSpeed(rightSpeed);
                shooter.setPID(PIDConstants.shooterP, PIDConstants.shooterI, PIDConstants.shooterD, PIDConstants.shooterFF);
            }
        }

//        if (!locking && operator.getRawButton(InputConstants.r1Button)) {
//            time = OurTimer.getTimer("tracktime");
//            shooter.setLight(true);
//            setTrimming(false);
//            try {
//                Scheduler.getInstance().add(new LockOn());
//              
//                locking = true;
//                
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
//        }
//        if (locking && !operator.getRawButton(InputConstants.r1Button)) {
//            time.stop();
//            shooter.setLight(false);
//            Scheduler.getInstance().add(new PositionControl(true, 0, true, 0));
//            locking = false;
//        }

        //rightX trim
//        double x = operator.getRawAxis(InputConstants.rightXAxis);
//        if(Math.abs(x) > 0.2 && !isTrimming()){
//            Scheduler.getInstance().add(new TrimDrive());
//            setTrimming(true);
//            DriverControls.setDriveMode(4);
//        }
        
        //shooter.setLight(operator.getRawButton(InputConstants.startButton));
       

        // leftY trim
//        if (Math.abs(operator.getRawAxis(InputConstants.leftYAxis)) > 0.1) {
//            if (getShootingPosition() == 0) {
//                nearSpeed += operator.getRawAxis(InputConstants.leftYAxis) * -10;
//                shooter.setSpeed(nearSpeed);
//                shooter.setPID(PIDConstants.shooterP, PIDConstants.shooterI, PIDConstants.shooterD, PIDConstants.shooterFF);
//            } else if (getShootingPosition() == 1) {
//                farSpeed += operator.getRawAxis(InputConstants.leftYAxis) * -10;
//                shooter.setSpeed(farSpeed);
//                shooter.setPID(PIDConstants.shooterP, PIDConstants.shooterI, PIDConstants.shooterD, PIDConstants.shooterFF);
//            } else if (getShootingPosition() == 2) {
//                leftSpeed += operator.getRawAxis(InputConstants.leftYAxis) * -10;
//                shooter.setSpeed(leftSpeed);
//                shooter.setPID(PIDConstants.shooterP, PIDConstants.shooterI, PIDConstants.shooterD, PIDConstants.shooterFF);
//            } else if (getShootingPosition() == 3) {
//                rightSpeed += operator.getRawAxis(InputConstants.leftYAxis) * -10;
//                shooter.setSpeed(rightSpeed);
//                shooter.setPID(PIDConstants.shooterP, PIDConstants.shooterI, PIDConstants.shooterD, PIDConstants.shooterFF);
//            }
//        }

    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    }

    protected void interrupted() {
    }

    public Joystick getOperator() {
        return OI.getInstance().getOperator();
    }

    /**
     * @return the trimming
     */
    public static boolean isTrimming() {
        return trimming;
    }

    /**
     * @param trimming the trimming to set
     */
    public static void setTrimming(boolean trimming1) {
        trimming = trimming1;
    }

    /**
     * @return the shootingPosition
     */
    public static int getShootingPosition() {
        return shootingPosition;
    }

    /**
     * @param shootingPosition the shootingPosition to set
     */
    public void setShootingPosition(int shootingPosition) {
        this.shootingPosition = shootingPosition;
    }
}
