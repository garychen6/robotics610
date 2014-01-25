package org.crescentschool.robotics.competition;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Scheduler;
import org.crescentschool.robotics.competition.controls.DriverControls;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {

    private Joystick driver;
    private Joystick operator;
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
        operator = new Joystick(2);

    }

    /**
     * @return the driver
     */
    public Joystick getDriver() {
        return driver;
    }

    /**
     * @return the operator
     */
    public Joystick getOperator() {
        return operator;
    }
}
