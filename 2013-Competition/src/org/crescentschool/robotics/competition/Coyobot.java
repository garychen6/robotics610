/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package org.crescentschool.robotics.competition;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.crescentschool.robotics.competition.commands.KajDrive;
import org.crescentschool.robotics.competition.subsystems.DriveTrain;
import org.crescentschool.robotics.competition.subsystems.Shooter;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Coyobot extends IterativeRobot {
    
    Command autonomousCommand;
    Preferences pid;
    Shooter shooter;
    DriveTrain driveTrain;
    
    //Encoder encoder;
    //GearTooth counter;
    
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
       // autonomousCommand = new KajDrive();
       // shooter = Shooter.getInstance();
        //pid = Preferences.getInstance();
        driveTrain = DriveTrain.getInstance();
        //encoder = new Encoder(1, 2, false, CounterBase.EncodingType.k1X);
        //counter = new GearTooth(1);
        //counter.setMaxPeriod(5);
        //counter.start();
        
        
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
        autonomousCommand.cancel();
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        //shooter.getInstance().getPIDController().run();
        //ShooterPID.getInstance().run();
        //System.out.println(30.0/counter.getPeriod());
        
        //shooter.getInstance().getPIDController().run();
      //  try {
            // try {
                //SmartDashboard.putNumber("SpeedNum",30.0/counter.getPeriod());
                //SmartDashboard.putNumber("Speed", shooter.getSpeed());
                 //SmartDashboard.putNumber("SpeedNum", shooter.getSpeed());
                //SmartDashboard.putNumber("Voltage", shooter.getVoltage());
               
      //  } catch (CANTimeoutException ex) {
        //    ex.printStackTrace();
        //}
           Scheduler.getInstance().run();
//        } catch (CANTimeoutException ex) {
//            ex.printStackTrace();
//        }
//        if (OI.getInstance().getDriver().getRawButton(5)) {
//            //System.out.println("P: "+pid.getDouble("p", 0)+" I: "+pid.getDouble("i", 0)+" D: "+pid.getDouble("d", 0));
//            shooter.setPID(pid.getDouble("p", 0), pid.getDouble("i", 0), pid.getDouble("d", 0));
//            shooter.setSpeed(pid.getDouble("setpoint", 0));
//            
//        }
    }

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        LiveWindow.run();
    }
}
