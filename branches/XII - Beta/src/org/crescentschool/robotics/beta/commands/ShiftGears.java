/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.beta.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.crescentschool.robotics.beta.subsystems.DriveTrain;

/**
 *
 * @author Patrick
 */
public class ShiftGears extends Command {
    
    DriveTrain m_driveTrain;
    boolean target;
    
    /**
     * Creates a new ShiftGears command
     * @param high For choosing which to shift to. true will shift to high, false will shift to low.
     */
    public ShiftGears(boolean high) {
        this.target = high;
        m_driveTrain = DriveTrain.getInstance();
        requires(m_driveTrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        m_driveTrain.shift(target);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return m_driveTrain.isHighGear == target;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
