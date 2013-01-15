package org.crescentschool.robotics.competition.subsystems;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.crescentschool.robotics.competition.commands.KajDrive;
import org.crescentschool.robotics.competition.constants.ElectricalConstants;

/**
 *
 */
public class DriveTrain extends Subsystem {

    CANJaguar jagRightMaster;
    Victor vicRightSlave;
    Victor vicRightSlave2;
    CANJaguar jagLeftMaster;
    Victor vicLeftSlave;
    Victor vicLeftSlave2;

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new KajDrive());

    }

    DriveTrain() {
        try {
            jagRightMaster = new CANJaguar(2);
            jagLeftMaster = new CANJaguar(3);
            vicRightSlave = new Victor(4);
            vicRightSlave2 = new Victor(5);
            vicLeftSlave = new Victor(6);
            vicLeftSlave2 = new Victor (7);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }
}
