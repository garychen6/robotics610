/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.commands;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;
import org.crescentschool.robotics.competition.OI;
import org.crescentschool.robotics.competition.constants.InputConstants;
import org.crescentschool.robotics.competition.constants.ShootingConstants;
import org.crescentschool.robotics.competition.subsystems.DriveTrain;
import org.crescentschool.robotics.competition.subsystems.OurTimer;
import org.crescentschool.robotics.competition.subsystems.Pneumatics;

/**
 *
 * @author Warfa
 */
public class TrimDrive extends Command {

    DriveTrain driveTrain;
    OI oi;
    boolean trimStick = false;
    boolean trimming = false;
    double trimTime = 0;
    double trimPower = 0;
    Preferences constantsTable;
    Pneumatics pneumatics;

    public TrimDrive() {
        oi = OI.getInstance();
        pneumatics = Pneumatics.getInstance();
        driveTrain = DriveTrain.getInstance();
        requires(driveTrain);
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        if (pneumatics.isAngleHigh()) {
            trimPower = ShootingConstants.trimPower;
        } else {
            trimPower = 1.0;
        }
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        OurTimer logTime = OurTimer.getTimer("TrimDrive");

        double x = oi.getOperator().getRawAxis(InputConstants.rightXAxis);
        if (x > 0.2 && !trimStick && trimTime <= 0) {
            driveTrain.setLeftVBus(trimPower);
            driveTrain.setRightVBus(-trimPower);
            trimStick = true;
            trimTime = ShootingConstants.trimTime;
        } else if (x < -0.2 && !trimStick && trimTime <= 0) {
            driveTrain.setLeftVBus(-trimPower);
            driveTrain.setRightVBus(trimPower);
            trimStick = true;
            trimTime = ShootingConstants.trimTime;
        } else if (Math.abs(x) < 0.2) {
            trimStick = false;
        }
        if (trimTime > 0) {
            trimTime--;
        }
        if (trimTime <= 0) {
            trimming = false;
            driveTrain.setRightVBus(0);
            driveTrain.setLeftVBus(0);
        }
        logTime.stop();
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
