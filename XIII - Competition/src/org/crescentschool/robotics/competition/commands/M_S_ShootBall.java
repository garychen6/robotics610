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
import org.crescentschool.robotics.competition.constants.PIDConstants;
import org.crescentschool.robotics.competition.constants.PotConstants;
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
    //Feeder feed = Feeder.getInstance();
    //CoyoBotUltrasonic uSonic = CoyoBotUltrasonic.getInstance();
    OI oi = OI.getInstance();
    Camera camera = Camera.getInstance();
    //Feeder feeder = Feeder.getInstance();
    //Intake intake = Intake.getInstance();
    //CoyoBotUltrasonic ultrasonic = CoyoBotUltrasonic.getInstance();
    int numAverages = 10;
    double[] speeds = new double[numAverages];
    double avgSpeed;
    int speedCounter = 0;
    boolean shoot = false;
    int yOffset = 0;
    double height;
    double distance = 0;
    boolean dPadUp = false;
    boolean dPadDown = false;
    boolean AAAAIIIIIIDAAAAAAAAANNNNNNNNGNNGNGNGOOOOOOOOOOOOOOOOOOOO = false;
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



        if (Buttons.isPressed(InputConstants.kStartButton, 2)) {
            shooter.resetPID();
            turret.resetPID();
            camera.resetCamera();
        }
        if (Buttons.isPressed(InputConstants.kSelectButton, 2)) {
            yOffset = 0;
        }
        if (Buttons.isHeld(InputConstants.kL2Button, OI.getInstance().getOperator()) && !Buttons.isHeld(InputConstants.kR2Button, OI.getInstance().getOperator())) {
            height = camera.getHeight();
            //equationSpeed = 0.1399*(height*height) - 7.1922*height  + 2294.2;
            //equationSpeed = 6.4*height  + 2330;
//            equationSpeed = -0.3911 * (height * height) + 40.317 * height + 1701;
            // equationSpeed = 0.4855 * (height * height) - 24.715 * height + 2640.3;
            //equationSpeed = 0.0471 * (height * height) + 2.6303 * height + 2246;
            //equationSpeed = 0.0222 * (height * height) + 0.4359 * height + 2172.6;
            equationSpeed = 0.0406 * (height * height) - 2.063 * height + 2246.3;
            SmartDashboard.putDouble("Camera Height", height);
            shooter.setShooter(equationSpeed + yOffset);
            //shooter.setShooter(2000 + yOffset);
        } else if (Math.abs(oi.getOperator().getRawAxis(InputConstants.kLeftYAxis)) > 0.1) {
            shooter.incRPM(-50 * MathUtils.pow(oi.getOperator().getRawAxis(InputConstants.kLeftYAxis), 3));
        }
        if (OI.getInstance().getOperator().getRawAxis(6) < -0.5) {
//        if (Buttons.isPressed(InputConstants.kYButton, 2)) {
//            if (!dPadUp) {
            yOffset += 4;
            System.out.println("yUP");
            //shooter.setShooter(equationSpeed + yOffset);
            dPadUp = true;
            SmartDashboard.putString("offsets", "y:" + yOffset);
//            }
        } else if (OI.getInstance().getOperator().getRawAxis(6) > 0.5) {
//        else if (Buttons.isPressed(InputConstants.kAButton, 2)) {
//            if (!dPadDown) {
            yOffset -= 4;
            //shooter.setShooter(equationSpeed + yOffset);
            dPadDown = true;
            SmartDashboard.putString("offsets", "y:" + yOffset);
//            }
//        }else{
//               dPadDown = false;
//               dPadUp = false;
        }

        SmartDashboard.putDouble("Average Speed", -avgSpeed);
        //SmartDashboard.putDouble("Wheel Difference", shooter.getRPM() + avgSpeed);
        if (Math.abs(shooter.getRPM() + avgSpeed) < 40) {
            AAAAIIIIIIDAAAAAAAAANNNNNNNNGNNGNGNGOOOOOOOOOOOOOOOOOOOO = true;
        } else {
            AAAAIIIIIIDAAAAAAAAANNNNNNNNGNNGNGNGOOOOOOOOOOOOOOOOOOOO = false;
        }
        SmartDashboard.putBoolean("AIDAN FIRE NOW!!!!", AAAAIIIIIIDAAAAAAAAANNNNNNNNGNNGNGNGOOOOOOOOOOOOOOOOOOOO);
//        if (Buttons.isHeld(InputConstants.kR2Button, OI.getInstance().getOperator())) {
//        if (OI.getInstance().getOperator().getRawButton(InputConstants.kR2Button)) {
//            intake.setIsShooting(true);
//            intake.setIntakeReverse(-1);
//            feeder.setFeeder(1);
//        } else {
//            feeder.setFeeder(0);
//            intake.setIsShooting(false);
//        }
//        if (Buttons.isPressed(InputConstants.kYButton, 2)) {
//            turret.resetPosition();
//            turret.incPosition(0);
//            turret.setPosition(PotConstants.turretCentre);
//        }
//        if (Buttons.isHeld(InputConstants.kYButton, OI.getInstance().getOperator())) {
//
//            shooter.setVbus(PIDConstants.middleGoalVbus);
//            feeder.setFeeder(1);
//            intake.setIntakeReverse(1);
//        }
  
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
        System.out.println(this + " canceled");
        System.out.println(this + " canceled");
        cancel();
    }
}
