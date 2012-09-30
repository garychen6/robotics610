/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Main.OI;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.constants.ElectricalConstants;
import edu.wpi.first.wpilibj.constants.PIDConstants;
import edu.wpi.first.wpilibj.templates.commands.ArmPresets;

/**
 * Warfa Jibril
 * Mr. Lim
 * ICS3U
 * March 6, 2012
 */
public class Arm extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
   
    // Initializing all robot systems
    CANJaguar armMaster;
    CANJaguar armSlave;
    DoubleSolenoid stageOne;
    DoubleSolenoid stageTwo;
    OI oi;
    private int controlMode = 1; 
    public static Arm instance = null;

      // Allows commands and Subsystems to use the current instance of Arm
    public static Arm getArm() {
        if (instance == null) {
            instance = new Arm();
        }
        return instance;
    }
    // Arm Constructor (runs once) after an arm object has been created
    public Arm() {
        oi = OI.getInstance();
        try {
            // initialize all the systems
            armMaster = new CANJaguar(ElectricalConstants.kJaguarArmMaster);
            //armSlave = new CANJaguar(ElectricalConstants.kJaguarArmSlave);
            stageOne = new DoubleSolenoid(ElectricalConstants.kSolenoidModulePort, ElectricalConstants.kStageOneForward, ElectricalConstants.kStageOneBackward);
            stageTwo = new DoubleSolenoid(ElectricalConstants.kSolenoidModulePort, ElectricalConstants.kStageTwoForward, ElectricalConstants.kStageTwoBackward);
             //Catches Jag CANTimeout Error
        } catch (CANTimeoutException ex) {
           // Prints the location of the code that caused the errror
            ex.printStackTrace();
            
        }
        //initialize all jaguars into position mode
        initPos();
    }
    
    public void initVbus(){
        controlMode=1;
        try{
            armMaster.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
            armMaster.configFaultTime(0.5);
            armMaster.configNeutralMode(CANJaguar.NeutralMode.kCoast);
            armMaster.enableControl();
        }catch (CANTimeoutException ex){
            ex.printStackTrace();
            try {
                Thread.sleep(500);
                initVbus();
            } catch (InterruptedException ex1) {
                ex1.printStackTrace();
            }   
        }
    }
    
    public void setVbus(double setpoint) {
        if(controlMode!=1){
            initVbus();
        }
        try{
        armMaster.setX(setpoint);
    
        }
        catch (CANTimeoutException ex){
            ex.printStackTrace();
        }
    }
 
    public void initPos() {
        controlMode=2;
        try {
              //initialize all jaguars into position mode
            armMaster.changeControlMode(CANJaguar.ControlMode.kPosition);
            armMaster.setPositionReference(CANJaguar.PositionReference.kPotentiometer);
            armMaster.configFaultTime(0.5);
            armMaster.configNeutralMode(CANJaguar.NeutralMode.kBrake);
            armMaster.configPotentiometerTurns(1);
            armMaster.setPID(PIDConstants.armP, PIDConstants.armI, PIDConstants.armD);
            //armSlave.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
            //armSlave.configFaultTime(0.5);
            ///armSlave.configNeutralMode(CANJaguar.NeutralMode.kCoast);
            armMaster.enableControl();
            //armSlave.enableControl();
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
            try {
                Thread.sleep(500);
                initPos();
            } catch (InterruptedException ex1) {
                ex1.printStackTrace();
            }
        }
    }

// sets the position for the arm with reference to the potentiometer 
    public void setPos(double pos) {
        if(controlMode!=2){
            initPos();
        }
        try {
            armMaster.setX(pos);
            //syncSlaves();
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
            initPos();
        }
    }
    
    public double getPos(){
        double currentPos=0;
        if(controlMode!=2){
            initPos();
        }
        try{
            currentPos = armMaster.getX();
        }
        catch (CANTimeoutException ex){
            ex.printStackTrace();
            initPos();
        }
        return currentPos;
    }
   
    // Sets the arm solenoids depending on wanted arm state
    public void setArmExtend(int mode) {
         // 1 fully retracted, 2 middle , 3 fully extended
        switch (mode) {
            case 1:
                stageOne.set(DoubleSolenoid.Value.kReverse);
                stageTwo.set(DoubleSolenoid.Value.kReverse);
                break;
            case 2:
                stageOne.set(DoubleSolenoid.Value.kReverse);
                stageTwo.set(DoubleSolenoid.Value.kForward);
                break;
            case 3:
                stageOne.set(DoubleSolenoid.Value.kForward);
                stageTwo.set(DoubleSolenoid.Value.kForward);
                break;
        }
    }
    //Synchronizes the secondary motor
    private void syncSlaves() {
        try {
            armSlave.setX(armMaster.getOutputVoltage() / armMaster.getBusVoltage());
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }
        // Set the default command for a subsystem here.
    public void initDefaultCommand() {

        //setDefaultCommand(new MySpecialCommand());
        setDefaultCommand(new ArmPresets());
    }
}
