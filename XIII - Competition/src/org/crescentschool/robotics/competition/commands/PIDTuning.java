package org.crescentschool.robotics.competition.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.crescentschool.robotics.competition.Buttons;
import org.crescentschool.robotics.competition.OI;
import org.crescentschool.robotics.competition.constants.InputConstants;
import org.crescentschool.robotics.competition.subsystems.DriveTrain;
import org.crescentschool.robotics.competition.subsystems.Shooter;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
/**
 *
 * @author Robotics
 */
public class PIDTuning extends Command {

    OI oi = OI.getInstance();
    DriveTrain driveTrain = DriveTrain.getInstance();
    Shooter shooter = Shooter.getInstance();
    int count = 0;
    int rpm = 0;

    public PIDTuning() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        if (count % 5 == 0) {
            //OI.printToDS(0, "Speed SetPoint: " + driveTrain.getLeftSpeedSetpoint());
            //OI.printToDS(1, "Speed: " + driveTrain.getLeftSpeed());
            //OI.printToDS(2, "Pos SetPoint: " + driveTrain.getLeftPosSetpoint());
            //OI.printToDS(3, "Pos: " + driveTrain.getLeftPos());
            //OI.printToDS(4, "Accel: "+driveTrain.getAccel());
            OI.printToDS(0, "ShooterSet: " + shooter.getShooterSetPoint());
            System.out.println("Shooter Speed " + shooter.getShooterSpeed());
            OI.printToDS(2, "Vertical Gyro: " + driveTrain.getVertAngle());
            //  OI.printToDS(3, "Horizontal Gyro: " + driveTrain.getGyro().getAngle());
            count = 0;
        }
        count++;
        shooter.setShooter(oi.getOperator().getRawAxis(2));
        if (Buttons.isPressed(InputConstants.kR1button, oi.getDriver())) {
            shooter.incP(0.01);
        } else if (Buttons.isPressed(InputConstants.kR2button, oi.getDriver())) {
            shooter.incP(-0.01);
        }
        if (Buttons.isPressed(InputConstants.kL1button, oi.getDriver())) {
            shooter.incI(0.001);
        } else if (Buttons.isPressed(InputConstants.kL2button, oi.getDriver())) {
            shooter.incI(-0.001);
        }
        if (Buttons.isPressed(InputConstants.kStartbutton, oi.getDriver())) {
            shooter.resetPID();
        }
        if (Buttons.isPressed(InputConstants.kR1button, oi.getOperator())) {
            rpm += 10;
            shooter.setShooter(rpm);
        } else if (Buttons.isPressed(InputConstants.kR2button, oi.getOperator())) {
            rpm -= 10;
            shooter.setShooter(rpm);
        }

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
