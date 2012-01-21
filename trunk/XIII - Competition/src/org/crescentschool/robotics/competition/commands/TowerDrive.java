/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
import org.crescentschool.robotics.competition.OI;
import org.crescentschool.robotics.competition.constants.InputConstants;
import org.crescentschool.robotics.competition.subsystems.DriveTrain;

/**
 *
 * @author Robotics
 */
public class TowerDrive extends Command {

    OI oi;
    DriveTrain driveTrain;
    int set = 0;
    boolean dPadUp = false;
    boolean dPadDown = false;
    public TowerDrive() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        oi = OI.getInstance();
        driveTrain = DriveTrain.getInstance();
        requires(driveTrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        if(oi.getDriver().getRawAxis(6) == -1 && !dPadUp){
            set=10;
            driveTrain.posSetpoint(set);
            dPadUp = true;
        }else if(oi.getDriver().getRawAxis(6) == 1 && !dPadDown){
            set=0;
            driveTrain.posSetpoint(set);
            dPadDown = true;
        }
        if(oi.getDriver().getRawAxis(6) == 0){
            dPadDown = false;
            dPadUp = false;
        }
        //driveTrain.posControllerLeft.getSetpoint();
        //driveTrain.posControllerRight.getSetpoint();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
