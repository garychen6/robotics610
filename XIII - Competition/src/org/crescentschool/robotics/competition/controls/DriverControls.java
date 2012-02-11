/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.controls;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import org.crescentschool.robotics.competition.Buttons;
import org.crescentschool.robotics.competition.OI;
import org.crescentschool.robotics.competition.commands.AutoBridge;
import org.crescentschool.robotics.competition.commands.BridgeMode;
import org.crescentschool.robotics.competition.commands.FlipperPresets;
import org.crescentschool.robotics.competition.commands.KajDrive;
import org.crescentschool.robotics.competition.constants.InputConstants;
import org.crescentschool.robotics.competition.subsystems.Camera;
import org.crescentschool.robotics.competition.subsystems.DriveTrain;
import org.crescentschool.robotics.competition.subsystems.Feeder;
import org.crescentschool.robotics.competition.subsystems.Flipper;
import org.crescentschool.robotics.competition.subsystems.Intake;
import org.crescentschool.robotics.competition.subsystems.Shooter;
import org.crescentschool.robotics.competition.subsystems.Turret;

/**
 *
 * @author Warfa
 */
public class DriverControls extends Command {

    boolean kajMode = false;
    Shooter shooter;
    DriveTrain driveTrain;
    Flipper flipper;
    Intake intake;
    Feeder feeder;
    OI oi;
    Turret turret;
    Camera camera;

    public DriverControls() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        oi = OI.getInstance();
        turret = Turret.getInstance();
        shooter = Shooter.getInstance();
        driveTrain = DriveTrain.getInstance();
        flipper = Flipper.getInstance();
        intake = Intake.getInstance();
        feeder = Feeder.getInstance();
        camera = Camera.getInstance();
        Scheduler.getInstance().add(new FlipperPresets());
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        if (!kajMode) {
            if (Math.abs(oi.getDriver().getRawAxis(InputConstants.kLeftYAxis)) > 0.2) {
                Scheduler.getInstance().add(new KajDrive());
                kajMode = true;
            } else if (Math.abs(oi.getDriver().getRawAxis(InputConstants.kRightXAxis)) > 0.2) {
                Scheduler.getInstance().add(new KajDrive());
                kajMode = true;
            }
        } else {
            if (oi.getDriver().getRawAxis(6) == 1) {
                Scheduler.getInstance().add(new BridgeMode());
                kajMode = false;
            } else if (oi.getDriver().getRawAxis(5) == -1) {
                Scheduler.getInstance().add(new BridgeMode());
                kajMode = false;
            }
            if (oi.getDriver().getRawAxis(6) == -1) {
                Scheduler.getInstance().add(new BridgeMode());
                kajMode = false;
            } else if (oi.getDriver().getRawAxis(5) == 1) {
                Scheduler.getInstance().add(new BridgeMode());
                kajMode = false;
            }
        }
        if (Buttons.isPressed(InputConstants.kXButton, oi.getDriver())) {
            Scheduler.getInstance().add(new AutoBridge());
            kajMode = false;
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
