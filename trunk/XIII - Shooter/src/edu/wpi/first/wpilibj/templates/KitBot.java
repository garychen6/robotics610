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
    AnalogChannel ultraSonic ;
    double realRange;
    int topPower;
    int bottomPower;
    boolean buttonPressed = false;
    double mToF;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        // instantiate the command used for the autonomous period
        ultraSonic = new AnalogChannel(1);
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
        
        vToM = 0.38582677165354330708661417322835;
        mToF = 3.2808399;
        ultraSonic.setAverageBits(4);
        topPower = 700;
        bottomPower = -1220;
        

    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        //shoot.setBottomShooter((int) (-300 * ultraSonic.getAverageVoltage() / vToM));
        //shoot.setTopShooter((int) (130 * ultraSonic.getAverageVoltage() / vToM));
        

        try {
            //shoot.bottomJaguar.setX(OI.joyDriver.getRawAxis(1));
            if(OI.getInstance().getDriver().getRawButton(6) && !buttonPressed){
                topPower += 5;
                buttonPressed = true;
            }else if(OI.getInstance().getDriver().getRawButton(6) == false){
                buttonPressed = false;
            }
            if(OI.getInstance().getDriver().getRawButton(5) && !buttonPressed){
                topPower -= 5;
                buttonPressed = true;
            }else if(OI.getInstance().getDriver().getRawButton(5) == false){
                buttonPressed = false;
            }
            if(OI.getInstance().getDriver().getRawButton(8) && !buttonPressed){
                bottomPower -=5;
                buttonPressed = true;
            }else if(OI.getInstance().getDriver().getRawButton(8) == false){
                buttonPressed = false;
            }
            if(OI.getInstance().getDriver().getRawButton(7) && !buttonPressed){
                bottomPower +=5;
                buttonPressed = true;
            }else if(OI.getInstance().getDriver().getRawButton(7)){
                buttonPressed = false;
            }
            
            
            if (OI.getInstance().getDriver().getRawButton(4) == true) {
                
                //shoot.topJaguar.setX(23.85 * (realRange));
                //shoot.bottomJaguar.setX(-12.02 * (realRange) - 1089.9);
                
                System.out.println("Button Pressed " + (int)(realRange));
                
            } else {
                realRange = ultraSonic.getAverageVoltage() / vToM*mToF;
                //shoot.topJaguar.setX(23.85 * (realRange));
                //shoot.bottomJaguar.setX(-12.02 * (realRange) - 1089.9);
                shoot.topJaguar.setX(topPower);
                shoot.bottomJaguar.setX(bottomPower);
                System.out.print("Distance = " + (realRange));
                System.out.print(" bottom = "+ (int)shoot.bottomJaguar.getSpeed());
                System.out.println(" top = " + (int)shoot.topJaguar.getSpeed());
               
                
              //  
                
              //  System.out.println(
              //      "top = " + (int) (shoot.topJaguar.getSpeed()) + " goal = " + (int) (180 * ultraSonic.getAverageVoltage()
              //      / vToM));
            // System.out.println(
              //      "bottom = " + (int) (shoot.bottomJaguar.getSpeed()) + " goal = " + (int) (-350 * ultraSonic.getAverageVoltage()
              //      / vToM));
            }

            
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }



    }
}
