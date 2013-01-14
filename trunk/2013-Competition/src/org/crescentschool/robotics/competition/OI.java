package org.crescentschool.robotics.competition;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Preferences;
import org.crescentschool.robotics.competition.subsystems.Shooter;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {

    DriverStation ds;
    Preferences pid;
    Joystick driver;
    Shooter shooter;
    static OI instance = null;

    public static OI getInstance() {
        if (instance == null) {
            instance = new OI();
        }
        return instance;
    }

    OI() {
        driver = new Joystick(1);
        shooter = Shooter.getInstance();
    }

    public Joystick getDriver() {
        return driver;
    }
}
