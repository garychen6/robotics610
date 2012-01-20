package org.crescentschool.robotics.kitBot.subsystems;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.crescentschool.robotics.kitBot.OI;
import org.crescentschool.robotics.kitBot.commands.TankDrive;

/**
 *
 */
public class DriveTrain extends Subsystem {

    private  CANJaguar jagLeft, jagRight;
    private static DriveTrain instance = null;
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public static DriveTrain getInstance() {
        if (instance == null) {
            instance = new DriveTrain();
        }
        return instance;

    }
    private DriveTrain()
    {
        try {
            jagLeft = new CANJaguar(3);
            jagRight = new CANJaguar(2);
            jagLeft.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
            jagRight.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
        
        
    }
    public void KajDrive(){
        try{
           jagLeft.setX(-OI.getInstance().getDriver().getRawAxis(4) + OI.getInstance().getDriver().getRawAxis(2));
           jagRight.setX((-OI.getInstance().getDriver().getRawAxis(4) - OI.getInstance().getDriver().getRawAxis(2)));
        }catch(CANTimeoutException ex){
            ex.printStackTrace();
        }
    }
       public void TankDrive(){
        try{
           jagLeft.setX(OI.getInstance().getDriver().getRawAxis(2) );
           jagRight.setX(- OI.getInstance().getDriver().getRawAxis(5));
        }catch(CANTimeoutException ex){
            ex.printStackTrace();
        }
    }
  
    
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
        setDefaultCommand(new TankDrive());
    }
}
