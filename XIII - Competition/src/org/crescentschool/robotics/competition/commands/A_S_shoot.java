/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.crescentschool.robotics.competition.subsystems.Camera;
import org.crescentschool.robotics.competition.subsystems.Feeder;
import org.crescentschool.robotics.competition.subsystems.Intake;
import org.crescentschool.robotics.competition.subsystems.Shooter;

/**
 *
 * @author Warfa
 */
public class A_S_shoot extends Command {

    Shooter shooter = Shooter.getInstance();
    Camera camera = Camera.getInstance();
    Feeder feeder = Feeder.getInstance();
    Intake intake = Intake.getInstance();
    double height = 0;
    double equationSpeed = 0;
    double yOffset = 0;
    int numAverages = 10;
    double[] speeds = new double[numAverages];
    double avgSpeed;
    int speedCounter = 0;
    int count = 1;
    boolean counted = true;
    boolean isFinished = false;

    public A_S_shoot() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(shooter);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        Camera.getInstance().processCamera();
        if (shooter.getShooterSpeed() < 0 || shooter.getShooterSpeed() > 3000) {
            speeds[speedCounter] = shooter.getShooterSpeed();
            speedCounter++;
        }
        if (speedCounter > numAverages - 1) {
            speedCounter = 0;
        }
        avgSpeed = 0;
        for (int i = 0; i < numAverages; i++) {
            avgSpeed += speeds[i];
        }
        avgSpeed /= numAverages;
        height = camera.getHeight();
        equationSpeed = 0.1399 * (height * height) - 7.1922 * height + 2294.2;
        SmartDashboard.putDouble("Camera Height", height);
        shooter.setShooter(equationSpeed + yOffset);
        SmartDashboard.putDouble("Wheel Difference", Math.abs(shooter.getRPM() + avgSpeed));
        if (Math.abs(shooter.getRPM() + avgSpeed) < 55 && !counted) {
            intake.setIntakeReverse(-1);
            feeder.setFeeder(1);
            count++;
            counted = true;
        } else if(Math.abs(shooter.getRPM() + avgSpeed) > 55){
            feeder.setFeeder(0);
            intake.setIntakeReverse(0);
            counted = false;
        }
        if (count >= 3) {
            isFinished = true;
        }
        SmartDashboard.putInt("Balls Fired", count);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return isFinished;
    }

    // Called once after isFinished returns true
    protected void end() {
        System.out.println(this + " finished");
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
