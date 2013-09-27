/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.controls;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import java.io.IOException;
import org.crescentschool.robotics.competition.OI;
import org.crescentschool.robotics.competition.commands.*;
import org.crescentschool.robotics.competition.constants.InputConstants;
import org.crescentschool.robotics.competition.constants.KinectConstants;
import org.crescentschool.robotics.competition.constants.ShootingConstants;
import org.crescentschool.robotics.competition.subsystems.DriveTrain;
import org.crescentschool.robotics.competition.subsystems.Pneumatics;
import org.crescentschool.robotics.competition.subsystems.Shooter;

/**
 *
 * @author robotics
 */
public class DriverControls extends Command {

    Pneumatics pneumatics = Pneumatics.getInstance();
    OI oi = OI.getInstance();
    DriveTrain driveTrain = DriveTrain.getInstance();
    Preferences constantsTable = Preferences.getInstance();
    Shooter shooter = Shooter.getInstance();
    Joystick driver = oi.getDriver();
    public static int driveMode = 0;
    boolean pressed = false;
    boolean trayOut = false;
    boolean pressedTray = false;
    // 0 = Kaj, 1 = Hang, 2 = Position, 3 = auto turn

    protected void initialize() {
        Scheduler.getInstance().add(new KajDrive());
    }

    protected void execute() {

        if (getDriveMode() != 0 && driver.getRawButton(InputConstants.r1Button)) {
            Scheduler.getInstance().add(new KajDrive());
            //pneumatics.setPowerTakeOff(false);
            OperatorControls.setTrimming(false);
            setDriveMode(0);
        }




//        if (getDriveMode() != 1 && driver.getRawButton(InputConstants.l2Button) && driver.getRawButton(InputConstants.r2Button)) {
//            pneumatics.setPowerTakeOff(true);
//            setDriveMode(1);
//        }

        if (getDriveMode() != 2 && driver.getRawAxis(InputConstants.dPadY) < -0.2) {
            Scheduler.getInstance().add(new PositionControl(true, 5.0, true, 5.0));
            setDriveMode(2);
        }
        if (getDriveMode() != 2 && driver.getRawAxis(InputConstants.dPadY) > 0.2) {
            Scheduler.getInstance().add(new PositionControl(true, -5.0, true, -5.0));
            shooter.setSpeed(KinectConstants.baseNearShooterRPM - KinectConstants.moveBack);
            setDriveMode(2);
        }

        if (driver.getRawAxis(InputConstants.dPadX) > 0.2) {
//            Scheduler.getInstance().add(new PositionControl(true, constantsTable.getDouble("TurnShoot", 0), false, -0.5));
//            driveTrain.setRightVBus(-1.0);
            //driveTrain.setAngle(constantsTable.getDouble("TurnShoot", 0), true, 3);
            setDriveMode(2);
            Scheduler.getInstance().add(new AngleTurn(constantsTable.getDouble("AngleTurn", 0)));
        }
        if (driver.getRawAxis(InputConstants.dPadX) < -0.2) {
            setDriveMode(2);
            Scheduler.getInstance().add(new AngleTurn(-constantsTable.getDouble("AngleTurn", 0)));
        }
        pneumatics.postUp(driver.getRawButton(InputConstants.l1Button));
        if (driver.getRawButton(InputConstants.squareButton)) {
            pneumatics.hangControl(true);
        }
        if (driver.getRawButton(InputConstants.xButton)) {
            pneumatics.hangControl(false);
            shooter.setSpeed(ShootingConstants.baseNearShooterRPM-200);
        }
         if (driver.getRawButton(InputConstants.l2Button)) {
            pneumatics.hangControl(true);
        }
        if (driver.getRawButton(InputConstants.r2Button)) {
            pneumatics.hangControl(false);
            shooter.setSpeed(ShootingConstants.baseNearShooterRPM-200);
        }
        if (!pressed && driver.getRawButton(InputConstants.triangleButton)) {
            // trayOut = !trayOut;
            //pneumatics.trayControl(trayOut);
            pressed = true;
        }
        if (!driver.getRawButton(InputConstants.triangleButton)) {
            pressed = false;
        }

        if (!pressedTray && driver.getRawButton(InputConstants.selectButton)) {
            pneumatics.setTray(trayOut);
            trayOut = !trayOut;
            pressedTray = true;
        }
        if(!driver.getRawButton(InputConstants.selectButton)){
            pressedTray = false;
        }
        
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    }

    protected void interrupted() {
    }

    public Joystick getDriver() {
        return OI.getInstance().getDriver();
    }

    /**
     * @return the driveMode
     */
    public static int getDriveMode() {
        return driveMode;
    }

    /**
     * @param driveMode the driveMode to set
     */
    public static void setDriveMode(int driveMode2) {
        driveMode = driveMode2;
    }
}
