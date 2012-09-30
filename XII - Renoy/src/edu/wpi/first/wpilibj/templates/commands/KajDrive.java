
package edu.wpi.first.wpilibj.templates.commands;

import com.sun.squawk.util.MathUtils;
import edu.wpi.first.wpilibj.Main.OI;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.constants.InputConstants;
import edu.wpi.first.wpilibj.templates.subsystems.DriveTrain;

/**
 * Warfa Jibril
 * Mr. Lim
 * ICS3U
 * March 6, 2012
 */
public class KajDrive extends Command {
    // Initialize Subsystems
    DriveTrain driveTrain = DriveTrain.getDriveTrain();
    OI oi = OI.getInstance();
    double x;
    double y;
    boolean isFinished = false;
    public KajDrive() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        System.out.println("Kaj Initializing");
        requires(driveTrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        // take x input from the right x axis
        x = MathUtils.pow(oi.getDriver().getRawAxis(InputConstants.kRightXAxis),3);
        // take y input from the left y axis
        y =  MathUtils.pow(oi.getDriver().getRawAxis(InputConstants.kLeftYAxis),3);
        // set the left side and right side of the drive train  to take into account both the x and y movement
        driveTrain.setVBusL(y-x);
        driveTrain.setVBusR(y+x);
        // make sure the secondary drivetrain motors are synced up
        driveTrain.syncSlaves();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return isFinished;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        // if interrupted end command
        isFinished = true;
    }
}
