/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.commands;
import edu.wpi.first.wpilibj.command.Command;
import org.crescentschool.robotics.competition.subsystems.DriveTrain;
/**
 *
 * @author Robotics
 */
public class A_D_turn extends Command {
    DriveTrain driveTrain = DriveTrain.getInstance();
    private double setPoint;
    
    public A_D_turn(double setPoint) {
        this.setPoint = setPoint;
        requires(driveTrain);
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        driveTrain.setLeftPos(setPoint);
        driveTrain.setRightPos(-setPoint);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        if(Math.abs(Math.abs(driveTrain.getLeftPos()) - Math.abs(setPoint)) < 0.1) return true;
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
