
package org.crescentschool.robotics.competition.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.crescentschool.robotics.competition.OI;
import org.crescentschool.robotics.competition.constants.InputConstants;
import org.crescentschool.robotics.competition.subsystems.*;

/**
 *
 * @author bradmiller
 */
public class KajDrive extends Command {
    DriveTrain driveTrain;
    OI oi;
    Pneumatics pneumatics;
    public KajDrive() {
        driveTrain = DriveTrain.getInstance();
        oi = OI.getInstance();
        requires(driveTrain);
        pneumatics = Pneumatics.getInstance();
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        
        double rightSpeed,leftSpeed,x,y;
        x = oi.getDriver().getRawAxis(InputConstants.rightXAxis);
        y = oi.getDriver().getRawAxis(InputConstants.leftYAxis);
        x = x * x * x;
        y = y * y * y;
        leftSpeed = y + x;
        rightSpeed = y - x;
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
