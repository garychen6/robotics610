
package org.crescentschool.robotics.BrRobot.subsystems;

import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.crescentschool.robotics.BrRobot.OI;
import org.crescentschool.robotics.BrRobot.commands.Tank;

/**
 *
 */
public class DriveTrain extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    private Victor rBack;
    private Victor rFront;
    private Victor lBack;
    private Victor lFront;
    private static DriveTrain instance = null;
    
    public static DriveTrain getInstance(){
        if(instance == null){
            instance = new DriveTrain();
          instance.setDefaultCommand(new Tank());
        }
     return instance;
    }
    
    private DriveTrain(){
        rBack = new Victor(1);
        rFront = new Victor(3);
        lBack = new Victor(2);
        lFront = new Victor(4);
    }
    public void tank(){
        rBack.set(OI.getInstance().getJoyDriver().getRawAxis(1));
        rFront.set(OI.getInstance().getJoyDriver().getRawAxis(1));
        lBack.set(OI.getInstance().getJoyDriver().getRawAxis(2));
        lFront.set(OI.getInstance().getJoyDriver().getRawAxis(2));
    }
       public void stop(){
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}

