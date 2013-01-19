
package org.crescentschool.robotics.competition.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.crescentschool.robotics.competition.OI;
import org.crescentschool.robotics.competition.constants.InputConstants;
import org.crescentschool.robotics.competition.subsystems.DriveTrain;

/**
 *
 * @author bradmiller
 */
public class KajDrive extends Command {
    DriveTrain driveTrain = DriveTrain.getInstance();
    OI joy = OI.getInstance();
    public KajDrive() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        double rightSpeed,leftSpeed;
        leftSpeed = joy.getDriver().getRawAxis(InputConstants.leftYAxis) + (joy.getDriver().getRawAxis(InputConstants.rightXAxis));
        rightSpeed = joy.getDriver().getRawAxis(InputConstants.leftYAxis) - (joy.getDriver().getRawAxis(InputConstants.rightXAxis));
        driveTrain.setLeftVBus(leftSpeed);
        driveTrain.setRightVBus(rightSpeed);  
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
