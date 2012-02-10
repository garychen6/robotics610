package org.crescentschool.robotics.competition;

import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.Joystick;

public class OI {

    private static OI instance = null;
    private static Joystick joyDriver;
    private static Joystick joyOperator;
    private static DriverStationLCD drStationLCD;

    /**
     * Returns the operator interface.
     * @return the OI
     */
    public static OI getInstance() {
        if (instance == null) {
            instance = new OI();
        }
        return instance;
    }

    /**
     * Returns the Joystick object that corresponds to the driver's gamepad.
     * @return the driver Joystick object.
     */
    public Joystick getDriver() {
        return joyDriver;
    }

    /**
     * Returns the Joystick object that corresponds to the operator's gamepad.
     * @return the operator Joystick object.
     */
    public Joystick getOperator() {
        return joyOperator;
    }

    /**
     * Prints a message to the driver station console.
     * @param line The zero-indexed line to print to. Valid numbers are 0 to 5.
     * @param message The message to print.
     */
    public static void printToDS(int line, String message) {
        DriverStationLCD.Line index = null;
        switch (line) {
            case 0:
                index = DriverStationLCD.Line.kMain6;
                break;
            case 1:
                index = DriverStationLCD.Line.kUser2;
                break;
            case 2:
                index = DriverStationLCD.Line.kUser3;
                break;
            case 3:
                index = DriverStationLCD.Line.kUser4;
                break;
            case 4:
                index = DriverStationLCD.Line.kUser5;
                break;
            case 5:
                index = DriverStationLCD.Line.kUser6;
                break;
        }
        drStationLCD.println(index, 1, message);
        drStationLCD.updateLCD();
    }

    private OI() {
        joyDriver = new Joystick(1);
        joyOperator = new Joystick(2);
        drStationLCD = DriverStationLCD.getInstance();
        for (int i = 1; i <= 10; i++) {
            Buttons.register(i, joyDriver);
        }
        for (int i = 1; i <= 10; i++) {
            Buttons.register(i, joyOperator);
        }

    }
}
