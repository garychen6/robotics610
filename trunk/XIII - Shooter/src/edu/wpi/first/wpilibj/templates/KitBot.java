/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.templates.commands.CommandBase;
import edu.wpi.first.wpilibj.templates.commands.ExampleCommand;
import edu.wpi.first.wpilibj.templates.subsystems.Shooter;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class KitBot extends IterativeRobot {

    Shooter shoot = new Shooter();
    Command autonomousCommand;
    double vToM;
    AnalogChannel ultraSonic;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        // instantiate the command used for the autonomous period
        autonomousCommand = new ExampleCommand();

        // Initialize all subsystems
        CommandBase.init();
    }

    public void autonomousInit() {
        // schedule the autonomous command (example)
        autonomousCommand.start();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }

    public void teleopInit() {
        // This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to 
        // continue until interrupted by another command, remove
        // this line or comment it out.
        ultraSonic = new AnalogChannel(1);
        vToM = 0.38582677165354330708661417322835;
        ultraSonic.setAverageBits(4);


    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        //shoot.setBottomShooter((int) (-300 * ultraSonic.getAverageVoltage() / vToM));
        //shoot.setTopShooter((int) (130 * ultraSonic.getAverageVoltage() / vToM));
        

        try {
            System.out.println("top = " + (int) (shoot.topJaguar.getSpeed()) + " goal = " + (int) (130 * ultraSonic.getAverageVoltage() / vToM));
            System.out.println("bottom = " + (int) (shoot.bottomJaguar.getSpeed()) + " goal = " + (int) (-300 * ultraSonic.getAverageVoltage() / vToM));
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }



    }
}
