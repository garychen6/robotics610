/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.commands;

import com.sun.squawk.util.MathUtils;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.crescentschool.robotics.competition.OI;
import org.crescentschool.robotics.competition.subsystems.Camera;
import org.crescentschool.robotics.competition.subsystems.Feeder;
import org.crescentschool.robotics.competition.subsystems.Intake;
import org.crescentschool.robotics.competition.subsystems.Shooter;
import org.crescentschool.robotics.competition.subsystems.Turret;

/**
 *
 * @author Warfa
 */
public class A_S_shoot extends Command {

    Shooter shooter = Shooter.getInstance();
    Turret turret = Turret.getInstance();
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
    boolean setSpeed = false;
    int ballsFiring = 0;

    public A_S_shoot(int balls) {
        ballsFiring = balls;
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(shooter);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
         turret.incPosition(0);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        
        Camera.getInstance().processCamera();
            double offset = 1.129363476 * MathUtils.atan(camera.getX() * 0.434812375);
            if (offset != 0.0) {
                turret.setPosition(camera.getTurretPot() - offset);
            }
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
        //equationSpeed = 0.1399 * (height * height) - 7.1922 * height + 2294.2;
        //equationSpeed = 6.4*height  + 2330;
            //equationSpeed = 0.4855 * (height * height) - 24.715 * height + 2640.3;
            //equationSpeed = 0.0439 * (height * height) + 1.5826 * height + 2321.6;
            //equationSpeed = 0.0471 * (height * height) + 2.6303 * height + 2296;
            equationSpeed = 0.0406 * (height * height) - 2.063 * height + 2316.3;
            shooter.setShooter(equationSpeed);
  
        OI.printToDS(5, "Height: " + height);
        SmartDashboard.putDouble("Camera Height", height);
        //turret.incPosition(0);
        SmartDashboard.putDouble("Wheel Difference", Math.abs(shooter.getRPM() + avgSpeed));
        if (Math.abs(shooter.getRPM() + avgSpeed) < 20 && !counted) {
            if (count >= 2) {
                intake.setIntakeReverse(-1);
            }
            feeder.setFeeder(0.7);

            count++;
            counted = true;
        } else if (Math.abs(shooter.getRPM() + avgSpeed) > 20) {
            feeder.setFeeder(0);
            intake.setIntakeReverse(0);
            counted = false;
        }
        if (count >= (ballsFiring + 1)) {
            isFinished = true;
        }
        SmartDashboard.putInt("Balls Fired", count);
        if (shooter.getAutonOver()) {
            isFinished = true;
        }
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
        isFinished = true;
    }
}
