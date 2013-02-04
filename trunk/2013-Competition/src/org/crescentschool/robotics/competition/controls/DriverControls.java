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
import org.crescentschool.robotics.competition.commands.*;
import org.crescentschool.robotics.competition.commands.KinectDriveTest;
import org.crescentschool.robotics.competition.constants.InputConstants;

/**
 *
 * @author robotics
 */
public class DriverControls extends Command {

    protected void initialize() {
        Scheduler.getInstance().add(new KajDrive());
    }

    protected void execute() {
        
       if (Math.abs(getDriver().getRawAxis(InputConstants.leftYAxis)) > 0.1 || Math.abs(getDriver().getRawAxis(InputConstants.rightXAxis)) > 0.1) {
            Scheduler.getInstance().add(new KajDrive());
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
}
