/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import org.crescentschool.robotics.competition.subsystems.*;
import org.crescentschool.robotics.competition.*;
import org.crescentschool.robotics.competition.constants.*;

/**
 *
 * @author Ian
 */
public class PickUp extends Command {

    Intake intake = null;
    int pos = 2;
    //0 = pickup, 1 = feed, 2 = stow
    OI oi;
    Joystick joystick;
    boolean auton = false;
    boolean square = false;
    boolean triangle = false;
    int autoPot;
    boolean finished = false;
    boolean gates = false;
    Timer timer = new Timer();
    Timer teleTime = new Timer();
    int count = 0;
    boolean feedButton = false;
    boolean feeding = false;
    boolean inPosition = false;
    Preferences preferences;

    public PickUp(boolean auton, int pos, boolean gates) {
        oi = OI.getInstance();
        intake = Intake.getInstance();
        joystick = oi.getDriver();
        requires(intake);
        autoPot = pos;
        System.out.println("PickUp");
        this.auton = auton;
        timer = new Timer();
        timer.reset();
        timer.start();
        this.gates = gates;
        finished = false;
        count = 0;
        preferences = Preferences.getInstance();

    }

    // Called just before this Command runs the first time
    protected void initialize() {
        timer.reset();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        if (!auton) {
            if (joystick.getRawButton(InputConstants.r2Button)) {
                intake.setRollers(-1);
            } else if (joystick.getRawButton(InputConstants.l2Button) && !feedButton) {
                intake.leftOpen(false);
                intake.rightOpen(false);
                pos = 0;
                intake.setRollers(1);
                feedButton = true;
                feeding = false;
            } else if (!joystick.getRawButton(InputConstants.l2Button) && feedButton) {
                intake.leftOpen(false);
                intake.rightOpen(false);

                teleTime = new Timer();
                teleTime.reset();
                teleTime.start();
                pos = 1;
                intake.setRollers(1);
                feedButton = false;
                feeding = true;
            } else if (!joystick.getRawButton(InputConstants.l2Button)) {
                if (feeding) {
                    //PIDConstants.feedTime
                    if (teleTime.get() > 0.4 * 5 && inPosition) {
                        intake.setRollers(0);
                        pos = 2;
                        feeding = false;
                        inPosition = false; 
                        intake.leftOpen(false);
                        intake.rightOpen(false);

                    } else if (teleTime.get() > 0.8 * 1.5 && inPosition) {
                        intake.leftOpen(false);
                        intake.rightOpen(false);
                    }  else if (teleTime.get() > 0.8 && inPosition) {
                        intake.rightOpen(true);

                    } else if (Math.abs(intake.getChange()) < 0.08 && pos == 1) {
                        intake.leftOpen(true);

                        if (!inPosition) {
                            teleTime.reset();
                            inPosition = true;
                        }
                    }
                } else {
                    intake.setRollers(0);
                }

            }

            /*

             if (joystick.getRawButton(InputConstants.squareButton) && !square) {
             pos--;
             square = true;
             if (pos < 0) {
             pos = 0;
             }
             System.out.println("Intake Position: " + pos);
             } else if (!joystick.getRawButton(InputConstants.squareButton)) {
             square = false;
             }
             if (joystick.getRawButton(InputConstants.triangleButton) && !triangle) {
             pos++;
             triangle = true;
             if (pos > 2) {
             pos = 2;
             }
             System.out.println("Intake Position: " + pos);

             } else if (!joystick.getRawButton(InputConstants.triangleButton)) {
             triangle = false;
             }
             */
            /*
             if (joystick.getRawButton(InputConstants.xButton)) {
             intake.setRollers(1);
             } else if (joystick.getRawButton(InputConstants.oButton)) {
             intake.setRollers(-1);
             } else {
             intake.setRollers(0);
             }
             */
            /*
             if (joystick.getRawButton(InputConstants.r1Button)) {
             intake.setIntake(0.4);


             } else if (joystick.getRawButton(InputConstants.l1Button)) {
             intake.setIntake(-0.75);
             } else {
             intake.setIntake(0);
             }
             */

            //System.out.println(intake.getPot());


            intake.setArmPos(pos);

        } else {
            if (gates) {
                intake.setRollers(1);
                intake.leftOpen(true);
                //PIDConstants.feedTime
                if (timer.get() > 0.8) {
                    intake.rightOpen(true);
                }
                if (timer.get() > 0.8 * 1.5) {
                    //intake.rightOpen(false);
                    //intake.leftOpen(false);
                }
                if (timer.get() > 0.4 * 5) {
                    System.out.println("gates");
                    finished = true;
                }
            } else {
                intake.setArmPos(autoPot);
                intake.setRollers(1);
                if (Math.abs(intake.getChange()) < 0.08) {
                    count++;
                    if (count > 5) {
                        finished = true;
                    }

                } else {
                    count = 0;
                }

            }


            // Make this return true when this Command no longer needs to run execute()

        }
    }

    protected boolean isFinished() {

        if (auton) {
            if (finished) {
                intake.rightOpen(false);
                intake.leftOpen(false);
                System.out.println("PickUp Finished");
            }
            return finished;

        } else {
            return false;
        }
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
