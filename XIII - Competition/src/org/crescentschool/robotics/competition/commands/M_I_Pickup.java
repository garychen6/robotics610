/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.crescentschool.robotics.competition.*;
import org.crescentschool.robotics.competition.subsystems.Intake;
import org.crescentschool.robotics.competition.OI;
import org.crescentschool.robotics.competition.constants.InputConstants;
import org.crescentschool.robotics.competition.subsystems.Feeder;

/**
 *
 * @author ian
 */
public class M_I_Pickup extends Command {

    Intake intake = Intake.getInstance();
    Feeder feeder = Feeder.getInstance();
    double speed = 1;
    OI oi = OI.getInstance();
    public M_I_Pickup() {
        System.out.println(this.toString());
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis
        requires(intake);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        if(!intake.isShooting()){
            if (Buttons.isHeld(InputConstants.kL1Button, oi.getOperator())
                    || Buttons.isHeld(InputConstants.kR1Button, oi.getDriver())) {
                intake.setIntakeReverse(speed);
                feeder.setFeeder(-speed);
            } else if (Buttons.isHeld(InputConstants.kR1Button, oi.getOperator())
                    || Buttons.isHeld(InputConstants.kL1Button, oi.getDriver())) {
                intake.setIntakeReverse(-speed);
            } else {
                intake.setIntakeReverse(0);
                feeder.setFeeder(0);
            }
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
        System.out.println(this + " canceled");
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        System.out.println(this + " canceled");cancel();
    }
}
