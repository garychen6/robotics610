/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.crescentschool.robotics.competition.OI;
import org.crescentschool.robotics.competition.constants.PIDConstants;
import org.crescentschool.robotics.competition.subsystems.Camera;
import org.crescentschool.robotics.competition.subsystems.DriveTrain;
import org.crescentschool.robotics.competition.subsystems.Ultrasonic;

/**
 *
 * @author Warfa
 */
public class CameraDrive extends Command {

    DriveTrain driveT = DriveTrain.getInstance();
    Ultrasonic uSonic = Ultrasonic.getInstance();
    OI oi = OI.getInstance();
    double xInput = 0;
    double yInput = 0;
    Camera cam = Camera.getInstance();
    double basketAng = 0;
    boolean dPadUp = false;
    boolean dPadDown = false;

    public CameraDrive() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(driveT);

    }

    // Called just before this Command runs the first time
    protected void initialize() {
        cam.processCamera();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        cam.processCamera();
        basketAng = Math.toDegrees(Math.tan(uSonic.getDistance() / (cam.getX()-160)));
        xInput = basketAng * PIDConstants.rPD;
        if (oi.getDriver().getRawAxis(6) == -1 && !dPadUp) {
            yInput += 1;
            dPadUp = true;
        } else if (oi.getDriver().getRawAxis(6) == 1 && !dPadDown) {
            yInput -= 1;
            dPadDown = true;
        }
        if (oi.getDriver().getRawAxis(6) == 0) {
            dPadDown = false;
            dPadUp = false;
        }
        driveT.leftPosSetpoint(-yInput - xInput);
        driveT.rightPosSetpoint(-yInput + xInput);
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
