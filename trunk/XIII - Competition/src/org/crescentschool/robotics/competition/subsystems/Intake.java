/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.subsystems;

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 * @author Warfa
 */
public class Intake extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    Relay inBot;
    static Intake instance = null;

    public static Intake getInstance() {
        if (instance == null) {
            instance = new Intake();
        }
       return instance;
    }

    private Intake() {
        inBot = new Relay(1);
    }
    
    public void setInbotForward(boolean on){
        if(on)inBot.set(Relay.Value.kForward);
        else inBot.set(Relay.Value.kOff);
    }
       public void setInbotBackward(boolean on){
        if(on)inBot.set(Relay.Value.kReverse);
        else inBot.set(Relay.Value.kOff);
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}
