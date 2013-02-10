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
import org.crescentschool.robotics.competition.subsystems.DriveTrain;

/**
 *
 * @author robotics
 */
public class OperatorControls extends Command {

    OI oi = OI.getInstance();
    Joystick operator = oi.getOperator();
    Shooter shooter = Shooter.getInstance();
    Pneumatics pneumatics = Pneumatics.getInstance();
    int nearSpeed = KinectConstants.baseNearShooterRPM;
    int farSpeed = KinectConstants.baseFarShooterRPM;
    boolean upPosition = true;
    boolean lightOn = false;
    protected void initialize() {
    }

    protected void execute() {

        if (operator.getRawButton(InputConstants.l2Button)) {
            shooter.setSpeed(farSpeed);
            shooter.setPID(PIDConstants.p, PIDConstants.i, PIDConstants.d, PIDConstants.ff);
            pneumatics.setAngleUp(false);
            upPosition = false;
        } else if (operator.getRawButton(InputConstants.l1Button)) {
            shooter.setSpeed(nearSpeed);
            shooter.setPID(PIDConstants.p, PIDConstants.i, PIDConstants.d, PIDConstants.ff);
            pneumatics.setAngleUp(true);
            upPosition = true;
        }
        
        
        //btn1 reset
        if (operator.getRawButton(InputConstants.xButton)) {
            nearSpeed = KinectConstants.baseNearShooterRPM;
            farSpeed = KinectConstants.baseFarShooterRPM;
            if(upPosition){
                shooter.setSpeed(nearSpeed);
                shooter.setPID(PIDConstants.p, PIDConstants.i, PIDConstants.d, PIDConstants.ff);
            } else{
                shooter.setSpeed(farSpeed);
                shooter.setPID(PIDConstants.p, PIDConstants.i, PIDConstants.d, PIDConstants.ff);
            }
        }
        // ir leds
        if(operator.getRawButton(InputConstants.oButton)){
            shooter.setLight(true);
        }
        if(operator.getRawButton(InputConstants.xButton)){
            shooter.setLight(false);
        }
        
        // rightY trim
        if (Math.abs(operator.getRawAxis(InputConstants.rightYAxis)) > 0.1) {
            if (upPosition) {
                nearSpeed += operator.getRawAxis(InputConstants.rightYAxis) * -10;
                shooter.setSpeed(nearSpeed);
                shooter.setPID(PIDConstants.p, PIDConstants.i, PIDConstants.d, PIDConstants.ff);
            } else {
                farSpeed += operator.getRawAxis(InputConstants.rightYAxis) * -10;
                shooter.setSpeed(farSpeed);
                shooter.setPID(PIDConstants.p, PIDConstants.i, PIDConstants.d, PIDConstants.ff);
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
