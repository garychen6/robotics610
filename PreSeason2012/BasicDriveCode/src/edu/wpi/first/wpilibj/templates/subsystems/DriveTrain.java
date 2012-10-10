
package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.templates.RobotMap;

/**
 *
 */
public class DriveTrain extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.\
    CANJaguar rightMaster;
    CANJaguar rightSlave;
    CANJaguar leftMaster;
    CANJaguar leftSlave;
    static DriveTrain instance;
    
    public static DriveTrain getInstance(){
        if(instance == null){
            instance = new DriveTrain();
        }
        return instance;
    }
            
            
    DriveTrain(){
        try {
            rightMaster = new CANJaguar(RobotMap.DriveRightMaster);
            rightSlave = new CANJaguar(RobotMap.DriveRightSlave);
            leftMaster = new CANJaguar(RobotMap.DriveLeftMaster);
            leftSlave = new CANJaguar(RobotMap.DriveLeftSlave);
            rightMaster.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
            rightSlave.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
            leftMaster.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
            leftSlave.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
            rightMaster.enableControl();
            rightSlave.enableControl();
            leftMaster.enableControl();
            leftSlave.enableControl();
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
        
    }
    public void setVbus(double power){
        try {
            leftMaster.setX(power);
            leftSlave.setX(power);
            rightMaster.setX(power);
            rightSlave.setX(power);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
        
    }
    
    
    
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}

