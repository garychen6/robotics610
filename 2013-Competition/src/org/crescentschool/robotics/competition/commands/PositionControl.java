/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.crescentschool.robotics.competition.OI;
import org.crescentschool.robotics.competition.constants.InputConstants;
import org.crescentschool.robotics.competition.subsystems.DriveTrain;
import org.crescentschool.robotics.competition.controls.*;

/**
 *
 * @author robotics
 */
public class PositionControl extends Command {

    DriveTrain driveTrain;
    OI oi;
    double setPointRight = 0;
    double setPointLeft = 0;
    double trim = 0;
    double axis = 0;
    //negative is left, positive is right
    boolean leftPositionMode = false;
    boolean rightPositionMode = false;

    public PositionControl(boolean leftPositionMode, double left, boolean rightPositionMode, double right) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        driveTrain = DriveTrain.getInstance();
        oi = OI.getInstance();
        this.leftPositionMode = leftPositionMode;
        this.rightPositionMode = rightPositionMode;
        setPointRight = right;
        setPointLeft = left;
        requires(driveTrain);
    }
    // Called just before this Command runs the first time

    protected void initialize() {
        if (leftPositionMode) {
            driveTrain.initPositionLeft();
            driveTrain.setPositionLeft(setPointLeft);
        }
        if (rightPositionMode) {
            driveTrain.initPositionRight();
            driveTrain.setPositionRight(setPointRight);
        }
        DriverControls.setDriveMode(2);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        /*
        axis = oi.getOperator().getRawAxis(InputConstants.rightXAxis);
        if (Math.abs(oi.getOperator().getRawAxis(InputConstants.rightXAxis)) > 0.1) {
            trim += axis * 0.05;
            driveTrain.setPositionLeft(trim);
            driveTrain.setPositionRight(-trim);
        }

        driveTrain.syncSlaves(false,0);
        */
        SmartDashboard.putNumber("Position Left", driveTrain.getPositionLeft());
        SmartDashboard.putNumber("Position Right", driveTrain.getPositionRight());
        
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
