/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.controls;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import org.crescentschool.robotics.competition.OI;
import org.crescentschool.robotics.competition.commands.LockOn;
import org.crescentschool.robotics.competition.commands.Shoot;
import org.crescentschool.robotics.competition.constants.InputConstants;

/**
 *
 * @author robotics
 */
public class OperatorControls extends Command{
    
    protected void initialize() {
    }

    protected void execute() {
        if(getOperator().getRawButton(InputConstants.r2Button)){
            Scheduler.getInstance().add(new Shoot());
        }
        if(getOperator().getRawButton(InputConstants.l2Button)){
            Scheduler.getInstance().add(new LockOn());
        }
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    }

    protected void interrupted() {
    }
    
    public Joystick getOperator(){
        return OI.getInstance().getOperator();
    }
}
