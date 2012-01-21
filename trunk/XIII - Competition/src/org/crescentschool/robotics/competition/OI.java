package org.crescentschool.robotics.competition;

import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.Joystick;

public class OI {

    public static OI instance = null;
    public static Joystick joyDriver;
    public static Joystick joyOperator;
    public static DriverStationLCD drStationLCD;

    public static OI getInstance() {


        if (instance == null) {
            instance = new OI();
        }
        return instance;
    }

    public Joystick getDriver() {
        return joyDriver;
    }
    public Joystick getOperator(){
        return joyOperator;
    }

    public DriverStationLCD getDSLCD() {
        return drStationLCD;
    }

    private OI() {
        joyDriver = new Joystick(1);
        joyOperator = new Joystick(2);
        drStationLCD = DriverStationLCD.getInstance();
    }
}
