package org.crescentschool.robotics.competition.commands;

import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Command;
import org.crescentschool.robotics.competition.OI;
import org.crescentschool.robotics.competition.constants.InputConstants;
import org.crescentschool.robotics.competition.subsystems.DriveTrain;

/**
 *
 * @author bradmiller
 */
public class TankDrive extends Command {

    DriveTrain driveTrain = DriveTrain.getInstance();
    OI oi = OI.getInstance();

    public TankDrive() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(driveTrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        //driveTrain.rightVBusSetpoint(oi.getDriver().getRawAxis(InputConstants.kDriverRightYAxis));
        //driveTrain.leftVBusSetpoint(oi.getDriver().getRawAxis(InputConstants.kDriverLeftYAxis));
        driveTrain.setRightSpeed(1000* oi.getDriver().getRawAxis(InputConstants.kDriverRightYAxis));
        driveTrain.setLeftSpeed(1000* oi.getDriver().getRawAxis(InputConstants.kDriverLeftYAxis));
       
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
