/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.controls;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import org.crescentschool.robotics.competition.OI;
import org.crescentschool.robotics.competition.commands.*;
import org.crescentschool.robotics.competition.constants.InputConstants;
import org.crescentschool.robotics.competition.subsystems.Pneumatics;

/**
 *
 * @author robotics
 */
public class DriverControls extends Command {

    Pneumatics pneumatics = Pneumatics.getInstance();
    OI oi = OI.getInstance();
    Joystick driver = oi.getDriver();

    int driveMode = 0;
    // 0 = Kaj, 1 = Hang, 2 = Position

    protected void initialize() {
        Scheduler.getInstance().add(new KajDrive());
    }

    protected void execute() {

        if (driveMode != 0 && driver.getRawButton(InputConstants.r1Button)){
            Scheduler.getInstance().add(new KajDrive());
            pneumatics.setPowerTakeOff(false);
            driveMode = 0;
        }
  
        if (driveMode != 1 && driver.getRawButton(InputConstants.l2Button) && driver.getRawButton(InputConstants.r2Button)) {
            pneumatics.setPowerTakeOff(true);
            driveMode = 1;
        }
        
        if (driver.getRawAxis(InputConstants.dPadY) > 0.2) {
            Scheduler.getInstance().add(new PositionControl(4,4));
            driveMode = 2;
        }
        if (driveMode != 2 && driver.getRawAxis(InputConstants.dPadY) < -0.2) {
            Scheduler.getInstance().add(new PositionControl(-4,-4));
            driveMode = 2;
        }
        if(driver.getRawButton(InputConstants.l1Button)){
            pneumatics.postUp(true);
        } else {
            pneumatics.postUp(false);
        }
        
        //System.out.println("DCon");
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
}
