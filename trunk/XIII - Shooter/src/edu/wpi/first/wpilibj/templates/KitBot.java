package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.templates.commands.CommandBase;
import edu.wpi.first.wpilibj.templates.commands.ExampleCommand;
import edu.wpi.first.wpilibj.templates.subsystems.Shooter;

public class KitBot extends IterativeRobot {

    Shooter shoot = new Shooter();
    Command autonomousCommand;
    double vToM;
    AnalogChannel ultraSonic;
    double realRange;
    int topPower;
    int bottomPower;
    boolean buttonPressed = false;
    double mToF;
    int buttonCase = 0;
    boolean warfa = false;
    DriverStationLCD drStationLCD;

    public void robotInit() {

        ultraSonic = new AnalogChannel(1);
        autonomousCommand = new ExampleCommand();


        CommandBase.init();
    }

    public void autonomousInit() {

        autonomousCommand.start();
    }

    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }

    public void teleopInit() {

        vToM = 0.38582677165354330708661417322835;
        mToF = 3.2808399;
        ultraSonic.setAverageBits(4);
        topPower = 880;
        bottomPower = -1280;
        realRange = ultraSonic.getAverageVoltage() / vToM * mToF;
        drStationLCD = DriverStationLCD.getInstance();

    }

    public void teleopPeriodic() {

        try {
            /*
             * if (!OI.getInstance().getDriver().getRawButton(2) && buttonCase
             * == 0 && warfa == false) { buttonCase++; warfa = true; } if
             * (!OI.getInstance().getDriver().getRawButton(2) && buttonCase == 1
             * && warfa == false) { buttonCase--; warfa = true; } if
             * (OI.getInstance().getDriver().getRawButton(2) && warfa == true) {
             * warfa = false; }
             */
            switch (buttonCase) {
                case 0: {
                    if (OI.getInstance().getDriver().getRawButton(6) && !buttonPressed) {
                        topPower += 5;
                        buttonPressed = true;
                    } else if (!OI.getInstance().getDriver().getRawButton(6)) {
                        buttonPressed = false;
                    }
                    if (OI.getInstance().getDriver().getRawButton(5) && !buttonPressed) {
                        topPower -= 5;
                        buttonPressed = true;
                    } else if (!OI.getInstance().getDriver().getRawButton(5)) {
                        buttonPressed = false;
                    }
                    if (OI.getInstance().getDriver().getRawButton(8) && !buttonPressed) {
                        bottomPower -= 5;
                        buttonPressed = true;
                    } else if (!OI.getInstance().getDriver().getRawButton(8)) {
                        buttonPressed = false;
                    }
                    if (OI.getInstance().getDriver().getRawButton(7) && !buttonPressed) {
                        bottomPower += 5;
                        buttonPressed = true;
                    } else if (OI.getInstance().getDriver().getRawButton(7)) {
                        buttonPressed = false;
                    } else {
                        shoot.topJaguar.setX(topPower);
                        shoot.bottomJaguar.setX(bottomPower);
                        drStationLCD.println(DriverStationLCD.Line.kMain6, 1, "Triggers Mode");
                        drStationLCD.println(DriverStationLCD.Line.kUser2, 1, ("Distance = " + (realRange) + " bottom = " + (int) shoot.bottomJaguar.getSpeed() + " top = " + (int) shoot.topJaguar.getSpeed()));
                        System.out.println("Triggers Mode");
                        System.out.println("Distance = " + (realRange) + " bottom = " + (int) shoot.bottomJaguar.getSpeed() + " top = " + (int) shoot.topJaguar.getSpeed());
                    }

                }
                /*
                 * case 1: { if (OI.getInstance().getDriver().getRawButton(4) ==
                 * true) {
                 *
                 * shoot.topJaguar.setX(23.38 * (realRange)+442.952);
                 * shoot.bottomJaguar.setX(-13.519 * (realRange) - 1071.91);
                 *
                 * System.out.println("Button Pressed " + (int) (realRange));
                 *
                 * } else { realRange = ultraSonic.getAverageVoltage() / vToM *
                 * mToF; shoot.topJaguar.setX(0); shoot.bottomJaguar.setX(0);
                 * shoot.topJaguar.setX(23.38 * (realRange) + 442.952);
                 * shoot.bottomJaguar.setX(-13.519 * (realRange) - 1071.91);
                 * drStationLCD.println(DriverStationLCD.Line.kMain6, 1,
                 * "Ultrasonic Mode");
                 * drStationLCD.println(DriverStationLCD.Line.kUser2, 1,
                 * ("Distance = " + (realRange) + " bottom = " + (int)
                 * shoot.bottomJaguar.getSpeed() + " top = " + (int)
                 * shoot.topJaguar.getSpeed())); System.out.println("Ultrasonic
                 * Mode"); System.out.println("Distance = " + (realRange) + "
                 * bottom = " + (int) shoot.bottomJaguar.getSpeed() + " top = "
                 * + (int) shoot.topJaguar.getSpeed()); }
                }
                 */


            }

        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }

    }
}
