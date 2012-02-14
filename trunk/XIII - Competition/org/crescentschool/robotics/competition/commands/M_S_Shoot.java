/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.commands;

import com.sun.squawk.util.MathUtils;
import edu.wpi.first.wpilibj.command.Command;
import org.crescentschool.robotics.competition.Buttons;
import org.crescentschool.robotics.competition.OI;
import org.crescentschool.robotics.competition.constants.InputConstants;
import org.crescentschool.robotics.competition.subsystems.Camera;
import org.crescentschool.robotics.competition.subsystems.Feeder;
import org.crescentschool.robotics.competition.subsystems.Shooter;
import org.crescentschool.robotics.competition.subsystems.Turret;
import org.crescentschool.robotics.competition.subsystems.CoyoBotUltrasonic;
import org.crescentschool.robotics.competition.subsystems.Intake;

/**
 *
 * @author Warfa
 */
public class M_S_Shoot extends Command {

    private double m_timeout;
    Shooter shooter = Shooter.getInstance();
    Turret turret = Turret.getInstance();
    Feeder feed = Feeder.getInstance();
    CoyoBotUltrasonic uSonic = CoyoBotUltrasonic.getInstance();
    OI oi = OI.getInstance();
    Camera camera = Camera.getInstance();
    Feeder feeder = Feeder.getInstance();
    Intake intake = Intake.getInstance();
    CoyoBotUltrasonic ultrasonic = CoyoBotUltrasonic.getInstance();
    int count = 1;
    double shooterSpeed = 0;
    double avgSpeed = 0;

    public M_S_Shoot() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(shooter);
        requires(intake);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        shooterSpeed += shooter.getShooterSpeed();
        count++;
        if (Math.abs(oi.getOperator().getRawAxis(InputConstants.kLeftYAxis)) > 0.1) {
            shooter.incRPM(-50 * MathUtils.pow(oi.getOperator().getRawAxis(InputConstants.kLeftYAxis), 3));
        }
        if (Buttons.isPressed(InputConstants.kStartButton, oi.getOperator())) {
            shooter.resetPID();
            turret.resetPID();
            camera.resetCamera();
        }
        if (Buttons.isHeld(InputConstants.kL2Button, OI.getInstance().getOperator()) && !Buttons.isPressed(InputConstants.kR2Button, OI.getInstance().getOperator())) {
            shooter.setShooter((72.529 * ultrasonic.getDistance()) + 1356.6);
        }
        if (Buttons.isHeld(InputConstants.kR2Button, OI.getInstance().getOperator())) {
            if (count == 10) {
                shooterSpeed /= 10;
                avgSpeed = shooterSpeed;
                if (Math.abs((shooter.getRPM() + shooterSpeed)) < 50) {
                    intake.setInbotForward(-1);
                    feeder.setFeeder(1);
                } else {
                    feeder.setFeeder(0);
                    intake.setInbotForward(0);
                }
                count = 0;
                shooterSpeed = 0;
            }
        } else {
            feeder.setFeeder(0);
            intake.setInbotForward(0);
        }


    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return isTimedOut();
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
