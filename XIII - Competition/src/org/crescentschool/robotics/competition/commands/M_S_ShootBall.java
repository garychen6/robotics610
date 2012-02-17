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
public class M_S_ShootBall extends Command {


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
    boolean shoot = false;
    int yOffset = 0;
    boolean dPadUp = false;
    boolean dPadDown = false;
    double equationSpeed = 0;

    public M_S_ShootBall() {
        System.err.println(this.toString());
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



        if (Buttons.isPressed(InputConstants.kStartButton, oi.getOperator())) {
            shooter.resetPID();
            turret.resetPID();
            camera.resetCamera();
        }
        if(Buttons.isPressed(InputConstants.kSelectButton, oi.getOperator())){
            yOffset = 0;
        }
        if (Buttons.isHeld(InputConstants.kL2Button, OI.getInstance().getOperator()) && !Buttons.isHeld(InputConstants.kR2Button, OI.getInstance().getOperator())) {
            equationSpeed = (80.167 * ultrasonic.getDistance() + 1212);
            shooter.setShooter(equationSpeed + yOffset);
        } else if (Math.abs(oi.getOperator().getRawAxis(InputConstants.kLeftYAxis)) > 0.1) {
            shooter.incRPM(-50 * MathUtils.pow(oi.getOperator().getRawAxis(InputConstants.kLeftYAxis), 3));
        }
        if (OI.getInstance().getOperator().getRawAxis(6) < -0.5) {
            if (!dPadUp) {
                yOffset += 20;
                //shooter.setShooter(equationSpeed + yOffset);
                dPadUp = true;
            }
        }
        else if (OI.getInstance().getOperator().getRawAxis(6) > 0.5) {
            if (!dPadDown) {
                yOffset -= 20;
                //shooter.setShooter(equationSpeed + yOffset);
                dPadDown = true;
            }
        }else{
               dPadDown = false;
               dPadUp = false;
        }
        
        SmartDashboard.putDouble("Average Speed", avgSpeed);
        SmartDashboard.putDouble("Wheel Difference", shooter.getRPM() + avgSpeed);
        SmartDashboard.putString("offsets", "y:" +yOffset);
        if (Buttons.isHeld(InputConstants.kR2Button, OI.getInstance().getOperator())) {
            intake.setIsShooting(true);
            intake.setIntakeReverse(-1);
            feeder.setFeeder(1);
        } else {
            feeder.setFeeder(0);
            intake.setIsShooting(false);
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
        cancel();
    }
}
