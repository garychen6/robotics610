/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.commands;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;
import org.crescentschool.robotics.competition.subsystems.DriveTrain;

/**
 *
 * @author Warfa
 */
public class AngleTurn extends Command {

    double angle;
    DriveTrain driveTrain;
    double error = 0;
    double errorI = 0;
    Preferences constantsTable = Preferences.getInstance();
    public AngleTurn(double angle) {
        this.angle = angle;
         driveTrain = DriveTrain.getInstance();
        requires(driveTrain);
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        driveTrain.getGyro().reset();
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        double ff = 0.001;
        double i = 0.005;
        double p = 0.1;
        error = angle - driveTrain.getGyro().getAngle();
        errorI += error;
        errorI = Math.min(1.0 / i, errorI);

        driveTrain.setRightVBus(error * -p - i * errorI-ff*angle);
        driveTrain.setLeftVBus(error * p + i * errorI+ff*angle);


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
