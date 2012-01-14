
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Command;
import org.crescentschool.robotics.competition.OI;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Robotics
 */
public class PIDDriveTuning extends Command {
   
    OI oi = OI.getInstance();
    JoystickButton driverBtn1,driverBtn2,driverBtn3,driverBtn4;
    
    public PIDDriveTuning() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        driverBtn1 = new JoystickButton(oi.getDriver(), 1);
        driverBtn2 = new JoystickButton(oi.getDriver(), 2);
        driverBtn3 = new JoystickButton(oi.getDriver(), 3);
        driverBtn4 = new JoystickButton(oi.getDriver(), 4);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
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
