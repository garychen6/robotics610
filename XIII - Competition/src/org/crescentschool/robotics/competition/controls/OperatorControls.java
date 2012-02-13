/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.controls;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import org.crescentschool.robotics.competition.Buttons;
import org.crescentschool.robotics.competition.OI;
import org.crescentschool.robotics.competition.commands.mI_pickup;
import org.crescentschool.robotics.competition.commands.mS_shoot;
import org.crescentschool.robotics.competition.commands.AMT_T_turn;
import org.crescentschool.robotics.competition.constants.InputConstants;
import org.crescentschool.robotics.competition.subsystems.CoyoBotUltrasonic;
import org.crescentschool.robotics.competition.subsystems.Shooter;

/**
 *
 * @author Warfa
 */
public class OperatorControls extends Command {

    Shooter shooter = Shooter.getInstance();
    CoyoBotUltrasonic ultrasonic = CoyoBotUltrasonic.getInstance();
    boolean mI_pickup = true;

    public OperatorControls() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        Scheduler.getInstance().add(new AMT_T_turn());
        Scheduler.getInstance().add(new mS_shoot());
        Scheduler.getInstance().add(new mI_pickup());
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        if (Buttons.isPressed(InputConstants.kR2Button, OI.getInstance().getOperator())) {
            if (mI_pickup) {
                Scheduler.getInstance().add(new mS_shoot());
                mI_pickup = false;
            }
        }
        if (Buttons.isPressed(InputConstants.kR1Button, OI.getInstance().getOperator())) {
            if (!mI_pickup) {
                Scheduler.getInstance().add(new mI_pickup());
                mI_pickup = true;
            }
        }
        if (Buttons.isPressed(InputConstants.kL1Button, OI.getInstance().getOperator())) {
            if (!mI_pickup) {
                Scheduler.getInstance().add(new mI_pickup());
                mI_pickup = true;
            }
        }
        OI.printToDS(3, "Shooter SetPoint " + shooter.getRPM());
        OI.printToDS(4, "Shooter Speed " + shooter.getShooterSpeed());
        OI.printToDS(5, "Distance " + ultrasonic.getDistance());

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
