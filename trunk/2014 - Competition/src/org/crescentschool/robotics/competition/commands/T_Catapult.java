/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.crescentschool.robotics.competition.OI;
import org.crescentschool.robotics.competition.constants.InputConstants;
import org.crescentschool.robotics.competition.subsystems.Intake;
import org.crescentschool.robotics.competition.subsystems.Catapult;
import org.crescentschool.robotics.competition.subsystems.Lights;

public class T_Catapult extends Command {

    Catapult shooter;
    Joystick driver;
    private OI oi;
    int fireCount = 0;
    boolean firing = false;
    int loadCount = 0;
    Intake intake;
    boolean truss = false;
    Joystick operator;
    boolean loaded = false;
    int loadedTime = 0;

    public T_Catapult() {
        System.out.println("Catapult");
        shooter = Catapult.getInstance();
        oi = OI.getInstance();
        driver = oi.getDriver();
        intake = Intake.getInstance();
        operator = oi.getOperator();
        requires(shooter);

        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        requires(shooter);
        //Start the sensor
        shooter.turnOnSensor();
        //Post the sensor value to the dashboard
        if (shooter.isLoading()) {
            SmartDashboard.putNumber("Catapult Sensor", 1);

        } else {
            SmartDashboard.putNumber("Catapult Sensor", -1);

        }
        //If firing hasn't started yet,
        if (!firing) {
            if (shooter.isLoading() && !loaded) {
                //Load the shooter
                shooter.setMain(-1);
                loadedTime = 0;
            } else {
                
                if (loadedTime < 1) {
                    loadedTime++;
                } else {
                    //Stop the shooter if its loaded
                    loaded = true;
                    shooter.setMain(0);
                    //Wait for a button press
                    if (operator.getRawButton(InputConstants.r2Button)||driver.getRawButton(InputConstants.l2Button)) {
                        truss = false;

                        firing = true;
                        fireCount = 0;
                        //If the wrist is closed, add a delay
                        if (!intake.getWristClosed()) {
                            fireCount = 10;
                        }
                        //Take control of the intake
                        requires(intake);
                    } else if (operator.getRawButton(InputConstants.r1Button)) {
                        
                        truss = true;
                        firing = true;
                        fireCount = 0;
                                                //If the wrist is closed, add a delay

                        if (!intake.getWristClosed()) {
                            fireCount = 10;
                        }
                        //Take control of the intake
                        requires(intake);
                    }

                }
            }
        } else {
            //Keep the intake
            requires(intake);
            //Open the wrist
            intake.setWrist(false);
            //Set the hardstop
            shooter.setHardStop(truss);
            //Count the delay
            if (fireCount < 15) {
                shooter.setMain(0);
                fireCount++;
            } else if (!shooter.isLoading()) {
                //After the delay is over, begin firing
                fireCount++;
                loadCount = 0;
                shooter.setMain(-1);
            } else {
                //Stop the shooter
                shooter.setMain(0);
                if (loadCount > 20) {
                    firing = false;
                    loaded = false;
                } else {
                    loadCount++;
                }
                Lights.getInstance().setPattern(Lights.TELE);

            }
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
    }
}