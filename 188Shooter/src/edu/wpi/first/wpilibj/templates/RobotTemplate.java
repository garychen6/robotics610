/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.can.CANTimeoutException;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotTemplate extends IterativeRobot {

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     *
     */
    CANJaguar roller;
    CANJaguar rollerSlave;
    Joystick operator;
    double p, i, d;
    double rpm;
    int count = 0;
    boolean top = false;
    boolean bottom = false;
    boolean top2 = false;
    boolean bottom2 = false;

    public void robotInit() {
        p = 0.22;
        i = 0.003;
        d = 0;
        rpm = 0;
        operator = new Joystick(1);
        try {
            roller = new CANJaguar(7);
            rollerSlave = new CANJaguar(6);
            roller.setSpeedReference(CANJaguar.SpeedReference.kQuadEncoder);
            roller.configEncoderCodesPerRev(256);
            roller.configNeutralMode(CANJaguar.NeutralMode.kCoast);
            roller.changeControlMode(CANJaguar.ControlMode.kSpeed);
            rollerSlave.configNeutralMode(CANJaguar.NeutralMode.kCoast);
            rollerSlave.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
            roller.setPID(p, i, d);
            roller.enableControl();
            rollerSlave.enableControl();
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
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
        try {
            count++;
            if (operator.getRawButton(4) && !top) {
                p += 0.01;
                roller.setPID(p, i, d);
                roller.enableControl();
                System.out.println("P: " + p + " I: " + i + " D: " + d);
                top = true;
            } else if (!operator.getRawButton(4) && top) {
                top = false;
            }
            if (operator.getRawButton(2) && !bottom) {
                p -= 0.01;
                bottom = true;
                roller.setPID(p, i, d);
                roller.enableControl();
                System.out.println("P: " + p + " I: " + i + " D: " + d);
            } else if (!operator.getRawButton(2) && bottom) {
                bottom = false;
            }
            if (operator.getRawButton(3) && !top2) {
                i += 0.001;
                top2 = true;
                roller.setPID(p, i, d);
                roller.enableControl();
                System.out.println("P: " + p + " I: " + i + " D: " + d);
            } else if (!operator.getRawButton(3) && top2) {
                top2 = false;
            }
            if (operator.getRawButton(1) && !bottom2) {
                i -= 0.001;
                bottom2 = true;
                roller.setPID(p, i, d);
                roller.enableControl();
                System.out.println("P: " + p + " I: " + i + " D: " + d);
            } else if (!operator.getRawButton(1) && bottom2) {
                bottom2 = false;
            }
            if (operator.getRawAxis(6) == -1) {
                rpm += 10;
                roller.setX(rpm);
                System.out.println("rpm: " + rpm);
            } else if (operator.getRawAxis(6) == 1) {
                rpm -= 10;
                roller.setX(rpm);
                System.out.println("rpm: " + rpm);
            }
            roller.setX(rpm);
            if(count % 5 == 0){
            System.out.println("Speed: "+roller.getSpeed());
            }
            rollerSlave.setX(roller.getOutputVoltage() / roller.getBusVoltage());

        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }

    }
}
