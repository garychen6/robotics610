package org.crescentschool.robotics.competition.subsystems;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.crescentschool.robotics.competition.OI;
import org.crescentschool.robotics.competition.commands.KajDrive;
import org.crescentschool.robotics.competition.constants.ElectricalConstants;

/**
 *
 */
public class DriveTrain extends Subsystem {

    private static DriveTrain instance = null;
    CANJaguar jagRightMaster;
    Victor vr1;
    Victor vr2;
    CANJaguar jagLeftMaster;
    Victor vl1;
    Victor vl2;
    private boolean canError = false;

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new KajDrive());

    }

    DriveTrain() {
        try {
            jagRightMaster = new CANJaguar(ElectricalConstants.DriveRightMaster);
            jagLeftMaster = new CANJaguar(ElectricalConstants.DriveLeftMaster);
            vr1 = new Victor(ElectricalConstants.DriveRightSlave1);
            vr2 = new Victor(ElectricalConstants.DriveRightSlave2);
            vl1 = new Victor(ElectricalConstants.DriveLeftSlave1);
            vl2 = new Victor(ElectricalConstants.DriveLeftSlave2);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }
    void initVBus(){
        try {
            jagRightMaster.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
            jagRightMaster.configNeutralMode(CANJaguar.NeutralMode.kCoast);
            jagLeftMaster.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
            jagLeftMaster.configNeutralMode(CANJaguar.NeutralMode.kCoast);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }
}