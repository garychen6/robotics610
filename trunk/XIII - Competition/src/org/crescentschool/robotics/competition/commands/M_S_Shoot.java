/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.commands;

import com.sun.squawk.util.MathUtils;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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
    int numAverages = 10;
    double[] speeds = new double[numAverages];
    double avgSpeed;
    int speedCounter = 0;
    
    public M_S_Shoot() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(shooter);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        
        // Update running average of shooter speeds
        speeds[speedCounter] = shooter.getShooterSpeed();
        speedCounter++;
        if (speedCounter > numAverages - 1)
            speedCounter = 0;
        for (int i=0; i < numAverages; i++)
            avgSpeed += speeds[i];
        avgSpeed /= numAverages;
        
        if (Math.abs(oi.getOperator().getRawAxis(InputConstants.kLeftYAxis)) > 0.1) {
            shooter.incRPM(-50 * MathUtils.pow(oi.getOperator().getRawAxis(InputConstants.kLeftYAxis), 3));
        }
        if (Buttons.isPressed(InputConstants.kStartButton, 2)) {
            shooter.resetPID();
            turret.resetPID();
            camera.resetCamera();
        }
        if(Buttons.isHeld(InputConstants.kL2Button, OI.getInstance().getOperator()) && !Buttons.isHeld(InputConstants.kR2Button, OI.getInstance().getOperator())){
            shooter.setShooter((80.167*ultrasonic.getDistance())+1212);
        }
         if(Buttons.isHeld(InputConstants.kR2Button, OI.getInstance().getOperator())){
            //SmartDashboard.putDouble("Wheel Difference", shooter.getRPM() + avgSpeed);
             if(Math.abs((shooter.getRPM() + avgSpeed)) < 100){
            intake.setIntakeReverse(-1);
            feeder.setFeeder(1);   
        }
        }else{
             feeder.setFeeder(0);
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
