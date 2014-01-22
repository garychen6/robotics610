/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Victor;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotTemplate extends IterativeRobot {

    Victor catapult;
    Joystick joystick;
    DigitalInput optical;
    boolean rest;
    boolean fired;
    int stopping;
    int firing;

   
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        stopping = 0;
        catapult = new Victor(2);
        joystick = new Joystick(1);
        optical = new DigitalInput(1, 1);
        rest = true;
        firing = 0;
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        if (optical.get() && rest == false) {
            catapult.set(1);
            stopping = 10;
        } else if (joystick.getRawButton(InputConstants.kR2Button) && fired == false) {
            catapult.set(1);
            firing = 2;
            rest = false;
            fired = true;
        } else {
            if(firing > 0){
                catapult.set(1);
                firing--;
            } else if (stopping > 0) {
                rest = true;
                catapult.set(-0.2);
                stopping--;
            } else {
                catapult.set(0);
                fired = false;
            }
        }
    }

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    }
}
