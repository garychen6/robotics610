/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.commands;

import com.sun.squawk.util.MathUtils;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.crescentschool.robotics.competition.subsystems.*;
import org.crescentschool.robotics.competition.*;
import org.crescentschool.robotics.competition.constants.InputConstants;

/**
 *
 * @author robotics
 */
public class TurnTest extends Command {

    DriveTrain driveTrain;
    OI oi;
    double lastAngle = 0;
    public TurnTest() {
        driveTrain = DriveTrain.getInstance();
        requires(driveTrain);

        oi = OI.getInstance();
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        Preferences constantsTable = Preferences.getInstance();
        
        double angle = Math.toDegrees(MathUtils.atan(constantsTable.getDouble("OFFSET",0)*Math.tan(Math.toRadians(28.5))));
        //driveTrain.getGyro().reset();
        SmartDashboard.putNumber("AngleTurn", angle);

        
        driveTrain.setAngle(constantsTable.getDouble("Angle",0));
        if(lastAngle != constantsTable.getDouble("Angle",0)){
            driveTrain.setErrorI(0);
        }
        lastAngle = constantsTable.getDouble("Angle",0);
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
