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
import org.crescentschool.robotics.competition.constants.KinectConstants;
import org.crescentschool.robotics.competition.constants.PIDConstants;
import org.crescentschool.robotics.competition.subsystems.Pneumatics;
import org.crescentschool.robotics.competition.subsystems.Shooter;
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
    int nearSpeed = KinectConstants.baseNearShooterRPM;
    int farSpeed = KinectConstants.baseFarShooterRPM;
    boolean upPosition = true;
    boolean lightOn = false;
    boolean locking = false;
    
    protected void initialize() {
    }
    
    protected void execute() {
        
        if (operator.getRawButton(InputConstants.l2Button)) {
            shooter.setSpeed(farSpeed);
            shooter.setPID(PIDConstants.shooterP, PIDConstants.shooterI, PIDConstants.shooterD, PIDConstants.shooterFF);
            pneumatics.setAngleUp(false);
            upPosition = false;
        } else if (operator.getRawButton(InputConstants.l1Button)) {
            shooter.setSpeed(nearSpeed);
            shooter.setPID(PIDConstants.shooterP, PIDConstants.shooterI, PIDConstants.shooterD, PIDConstants.shooterFF);
            pneumatics.setAngleUp(true);
            upPosition = true;
        }
        if(driver.getRawAxis(InputConstants.dPadY) < -0.2){
            shooter.setSpeed(nearSpeed - KinectConstants.moveBack);
        }
        
        //btn1 reset
        if (operator.getRawButton(InputConstants.xButton)) {
            nearSpeed = KinectConstants.baseNearShooterRPM;
            farSpeed = KinectConstants.baseFarShooterRPM;
            if (upPosition) {
                shooter.setSpeed(nearSpeed);
                shooter.setPID(PIDConstants.shooterP, PIDConstants.shooterI, PIDConstants.shooterD, PIDConstants.shooterFF);
            } else {
                shooter.setSpeed(farSpeed);
                shooter.setPID(PIDConstants.shooterP, PIDConstants.shooterI, PIDConstants.shooterD, PIDConstants.shooterFF);
            }
        }
        if (!locking && operator.getRawButton(InputConstants.r1Button)){
            shooter.setLight(true);
            try{
                Scheduler.getInstance().add(new LockOn());
                locking = true;
            }
            catch (IOException ex){
                ex.printStackTrace();
            }
        }
        if (locking && !operator.getRawButton(InputConstants.r1Button)) {
            shooter.setLight(false);
            Scheduler.getInstance().add(new PositionControl(true, 0, true, 0));
            locking = false;
        }
        
        // rightY trim
        if (Math.abs(operator.getRawAxis(InputConstants.leftYAxis)) > 0.1) {
            if (upPosition) {
                nearSpeed += operator.getRawAxis(InputConstants.leftYAxis) * -10;
                shooter.setSpeed(nearSpeed);
                shooter.setPID(PIDConstants.shooterP, PIDConstants.shooterI, PIDConstants.shooterD, PIDConstants.shooterFF);
            } else {
                farSpeed += operator.getRawAxis(InputConstants.leftYAxis) * -10;
                shooter.setSpeed(farSpeed);
                shooter.setPID(PIDConstants.shooterP, PIDConstants.shooterI, PIDConstants.shooterD, PIDConstants.shooterFF);
            }
        }
     
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
}
