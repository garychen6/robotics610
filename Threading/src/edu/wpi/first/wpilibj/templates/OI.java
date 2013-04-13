package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Preferences;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {

    DriverStation ds;
    //Shooter shooter;
    Joystick driver;
    static OI instance = null;

    /**
     * Get instance of OI.
     *
     * @return OI instance
     */
    public static OI getInstance() {
        if (instance == null) {
            instance = new OI();
        }
        return instance;
    }

    /**
     * Set driver and operator joysticks to the right joystick numbers.
     */
    OI() {
        driver = new Joystick(1);
    }

    public Joystick getDriver() {
        return driver;
    }
}
