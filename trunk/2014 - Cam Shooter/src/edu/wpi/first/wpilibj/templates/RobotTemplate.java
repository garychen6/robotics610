/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.interfaces.Potentiometer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotTemplate extends IterativeRobot {

    int opticalPort = 1;
    
    Joystick joystick;
    DigitalInput optical;
     /*
             * 0 - Loading
             * 1 - Loaded
             * 2 - Firing
             * 3 - Fired
             * 
             */
    int stage = 0;
    double loadedPosition = 2.5;
    double firedPosition = 2.7;
    AnalogPotentiometer pot;
    Preferences prefs;
    boolean pastLimit = false;
    Victor catapult;
    Victor secondCatapult;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        catapult = new Victor(2);
        joystick = new Joystick(1);
        optical = new DigitalInput(1, opticalPort);
        pot = new AnalogPotentiometer(2);
        prefs = Preferences.getInstance();
        secondCatapult = new Victor(3);
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
    }

    public void teleopInit() {
        stage = 0;
        pastLimit = false;
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        double p = 1.6;
        double potValue = pot.get();
        SmartDashboard.putNumber("Stage", stage);
        double catapultPower = 0;
//        if(joystick.getRawButton(InputConstants.fireButton)){
//            catapult.set(1); 
//        } else {
//            catapult.set(0);
//        }
        if(joystick.getRawButton(3)){
            secondCatapult.set(0.8);
        }
        switch (stage) {
            case 0:
                double error = loadedPosition - potValue;
                if (pot.get() > 0.5 && !pastLimit) {
                    catapultPower = 1;
                } else {
                    pastLimit = true;
                }
                if (pastLimit) {
                    if (Math.abs(error) < 0.05) {
                        catapultPower = 0;
                        stage = 1;



                    } else {
                        catapultPower = error * p;
                    }
                }
                break;

            case 1:
                if (joystick.getRawButton(InputConstants.fireButton)) {
                    stage = 2;
                } else {
                    catapultPower = 0;
                }
                break;
            case 2:
                if (potValue < firedPosition) {
                    catapultPower = 1;
                } else {
                    stage = 3;
                }
                break;
            case 3:
                if (joystick.getRawButton(2)) {
                    stage = 0;
                    pastLimit = false;
                }
                catapultPower = 0;
                break;

        }
        SmartDashboard.putNumber("CatapultPower", catapultPower);

        SmartDashboard
                .putNumber("PotValue", pot.get());
        catapult.set(catapultPower);
//        if(joystick.getRawButton(InputConstants.l2button)){
//                        System.out.println("Button Pressed");
//
//            choochoo.set(0.75);
//        } else {
//            choochoo.set(0);
//        }
//        if (optical.get() && rest == false) {
//            catapult.set(1);
//
//            stopping = 10;
//        } else if (joystick.getRawButton(InputConstants.r2button)) {
//            catapult.set(1);
//            //System.out.println("Trigger pressed");
//            rest = false;
//        } else {
//            if (stopping > 0) {
//                rest = true;
//                catapult.set(-0.2);
//                stopping--;
//                //System.out.println("Stopping");
//
//            } else {
//
//                catapult.set(0);
//
//                //System.out.println("Stopped");
//
//            }
//
//
//        }

    }
}
/**
 * This function is called periodically during test mode
 */
