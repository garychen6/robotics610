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
    public static DriveTrain getInstance(){
        if(instance == null){
            instance = new DriveTrain();
        }
        return instance;
    }
    DriveTrain() {
        try {
            jagRightMaster = new CANJaguar(ElectricalConstants.jagRightMaster);
            jagLeftMaster = new CANJaguar(ElectricalConstants.jagLeftMaster);
            jagRightMaster.configNeutralMode(CANJaguar.NeutralMode.kCoast);
            jagLeftMaster.configNeutralMode(CANJaguar.NeutralMode.kCoast);
            vr1 = new Victor(ElectricalConstants.victorRightSlaveMid);
            vr2 = new Victor(ElectricalConstants.victorRightSlaveBack);
            vl1 = new Victor(ElectricalConstants.victorLeftSlaveMid);
            vl2 = new Victor(ElectricalConstants.victorLeftSlaveBack);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }
    void initVBus(){
        try {
            jagRightMaster.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
            jagLeftMaster.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
            jagRightMaster.enableControl();
            jagLeftMaster.enableControl();
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }
    
    void initPosition(){
        try {
                jagRightMaster.changeControlMode(CANJaguar.ControlMode.kSpeed);
                jagLeftMaster.changeControlMode(CANJaguar.ControlMode.kSpeed);
                jagRightMaster.setPositionReference(CANJaguar.PositionReference.kQuadEncoder);
                jagLeftMaster.setPositionReference(CANJaguar.PositionReference.kQuadEncoder);
                jagRightMaster.configEncoderCodesPerRev(256);
                jagLeftMaster.configEncoderCodesPerRev(256);
                jagRightMaster.changeControlMode(CANJaguar.ControlMode.kVoltage);
                jagLeftMaster.changeControlMode(CANJaguar.ControlMode.kVoltage);
                jagRightMaster.enableControl(0);
                jagLeftMaster.enableControl(0);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }
   

    void syncSlaves(){
        try {
            vr1.set(jagRightMaster.getOutputVoltage()/jagRightMaster.getBusVoltage());
            vr2.set(jagRightMaster.getOutputVoltage()/jagRightMaster.getBusVoltage());
            vl1.set(jagLeftMaster.getOutputVoltage()/jagLeftMaster.getBusVoltage());
            vl2.set(jagLeftMaster.getOutputVoltage()/jagLeftMaster.getBusVoltage());
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }
    public void setLeftVBus(double power){
        try{
            jagLeftMaster.setX(power);
        } catch (CANTimeoutException e){     
        }
        syncSlaves();
    }
    public void setRightVBus(double power){
        try{
            jagRightMaster.setX(power);
        } catch (CANTimeoutException e){
        }
        syncSlaves();
    }
}
