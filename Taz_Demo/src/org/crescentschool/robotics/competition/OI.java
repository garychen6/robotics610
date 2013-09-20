package org.crescentschool.robotics.competition;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Preferences;
import org.crescentschool.robotics.competition.constants.ElectricalConstants;
import org.crescentschool.robotics.competition.subsystems.Shooter;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {

    DriverStation ds;
    Preferences pid;
    Joystick driver;
    Joystick operator;
    Shooter shooter;
    static OI instance = null;

    /**
     * Get instance of OI.
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
        driver = new Joystick(ElectricalConstants.driverJoystick);
        operator = new Joystick(ElectricalConstants.operatorJoystick);
    }

    /**
     * Get the driver joystick.
     * @return driver joystick
     */
    public Joystick getDriver() {
        return driver;
    }
    
    /**
     * Get the operator joystick.
     * @return operator joystick
     */
    public Joystick getOperator() {
        return operator;
    }
}
