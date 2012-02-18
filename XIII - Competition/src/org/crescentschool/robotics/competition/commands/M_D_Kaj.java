package org.crescentschool.robotics.competition.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.crescentschool.robotics.competition.Buttons;
import org.crescentschool.robotics.competition.OI;
import org.crescentschool.robotics.competition.constants.InputConstants;
import org.crescentschool.robotics.competition.subsystems.DriveTrain;

/**
 *
 * @author bradmiller
 */
public class M_D_Kaj extends Command {

    DriveTrain driveTrain = DriveTrain.getInstance();
    OI oi = OI.getInstance();

    public M_D_Kaj() {
        System.out.println(this.toString());
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(driveTrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        double x, y;
        double left, right;
        x = oi.getDriver().getRawAxis(InputConstants.kRightXAxis);
        y = oi.getDriver().getRawAxis(InputConstants.kLeftYAxis);
        left = y - x;
        right = y + x;
        driveTrain.setRightVBus(right);
        driveTrain.setLeftVBus(left);
        if (Buttons.isPressed(InputConstants.kStartButton, 1)) {
            driveTrain.reInit();
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
        System.out.println(this + " canceled");
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        System.out.println(this + " canceled");cancel();
    }
}
