package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.Watchdog;
import edu.wpi.first.wpilibj.can.CANTimeoutException;

public class CoyoBotXII extends IterativeRobot {

    CANJaguar jagLeftMaster, jagLeftSlave,
            jagRightMaster, jagRightSlave;
    CANJaguar jagShoulder;
    Victor vicGripperTop, vicGripperBottom;
    Compressor compressor;
    Solenoid solShifterHigh, solShifterLow;
    Solenoid solArmStageOne, solArmStageTwo;
    Solenoid solDeploy;
    Joystick joyDriver;
    Joystick joyOperator;
    Watchdog watchdog;
    DriverStationLCD dsLCD;
    DigitalInput digLineLeft, digLineMiddle, digLineRight;
    AnalogChannel anaUltraSonic;
    int driveMode;
    boolean driveToggle;
    boolean cruiseControl;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {

        watchdog = Watchdog.getInstance();
        dsLCD = DriverStationLCD.getInstance();

        try {
            jagLeftMaster = new CANJaguar(ElectricalMap.kJaguarLeftMaster);
            jagLeftMaster.setSpeedReference(CANJaguar.SpeedReference.kEncoder);
            jagLeftSlave = new CANJaguar(ElectricalMap.kJaguarLeftSlave);
            jagRightMaster = new CANJaguar(ElectricalMap.kJaguarRightMaster);
            jagRightMaster.setSpeedReference(CANJaguar.SpeedReference.kEncoder);
            jagRightSlave = new CANJaguar(ElectricalMap.kJaguarRightSlave);
        } catch (CANTimeoutException ex) {
            System.out.println(ex.toString());
        }

        compressor = new Compressor(ElectricalMap.kCompressorPressureSwitchChannel,ElectricalMap.kCompressorRelayChannel);

        solShifterHigh = new Solenoid(ElectricalMap.kSolenoidModulePort,ElectricalMap.kSolenoidHighChannel);
        solShifterLow = new Solenoid(ElectricalMap.kSolenoidModulePort,ElectricalMap.kSolenoidLowChannel);

        joyDriver = new Joystick(ElectricalMap.kJoystickDriverPort);
        joyOperator = new Joystick(ElectricalMap.kJoystickOperatorPort);

        //digLineLeft = new DigitalInput(ElectricalMap.kLineSensor1Channel);
        //digLineMiddle = new DigitalInput(ElectricalMap.kLineSensor2Channel);
        //digLineRight = new DigitalInput(ElectricalMap.kLineSensor3Channel);

        anaUltraSonic = new AnalogChannel(ElectricalMap.kUltrasonicChannel);

        driveMode = 0; //0 = Tank; 1 = Arcade; 2 = Kaj
        driveToggle = false;
        cruiseControl = false;

        compressor.start();
    }
    public void disabledInit(){

    }

    public void disabledPeriodic(){

    }

    public void autonomousInit(){
        try {
            jagLeftMaster.changeControlMode(CANJaguar.ControlMode.kSpeed);
            jagRightMaster.changeControlMode(CANJaguar.ControlMode.kSpeed);
        } catch (CANTimeoutException ex){
            System.out.println(ex.toString());
        }
    }

    public void autonomousPeriodic() {
        watchdog.feed();
        /*try {
            //TODO: PID loop - possibly move to auto init
        } catch (CANTimeoutException ex){
            System.out.println(ex.toString());
        }/**/
    }

    public void teleopInit() {
        try {
            jagLeftMaster.changeControlMode(CANJaguar.ControlMode.kVoltage);
            jagRightMaster.changeControlMode(CANJaguar.ControlMode.kVoltage);
        } catch (CANTimeoutException ex){
            System.out.println(ex.toString());
        }
    }

    public void teleopPeriodic() {

        watchdog.feed(); //feed the watchdog

        //Check buttons & set shift - high is 8, low is 7
        if(joyDriver.getRawButton(8)){
            solShifterHigh.set(true);
            solShifterLow.set(false);
        }
        else if(joyDriver.getRawButton(7)){
            solShifterHigh.set(false);
            solShifterLow.set(true);
        }
        //Toggle drive mode
        
        if (!driveToggle && joyDriver.getRawButton(2)) {
            driveMode = (driveMode + 1) % 3;
            driveToggle = true;
        } else if (driveToggle && !joyDriver.getRawButton(2)) {
            driveToggle = false;
        }

        //Print drive mode to DS & send values to Jaguars
        switch (driveMode) {
            case 0:
                dsLCD.println(DriverStationLCD.Line.kMain6, 1,
                        "Drive mode: Tank  ");
                try {
                    jagLeftMaster.setX(joyDriver.getRawAxis(2));
                    jagRightMaster.setX(joyDriver.getRawAxis(4));
                } catch (CANTimeoutException ex) {
                    System.out.println(ex.toString());
                }
                break;
            case 1:
                dsLCD.println(DriverStationLCD.Line.kMain6, 1,
                        "Drive mode: Arcade");
                try {
                    jagLeftMaster.setX(joyDriver.getRawAxis(2)
                            - joyDriver.getRawAxis(1));
                    jagRightMaster.setX(joyDriver.getRawAxis(2)
                            + joyDriver.getRawAxis(1));
                } catch (CANTimeoutException ex) {
                    System.out.println(ex.toString());
                }
                break;
            case 2:
                dsLCD.println(DriverStationLCD.Line.kMain6, 1,
                        "Drive mode: Kaj   ");
                try {
                    jagLeftMaster.setX(joyDriver.getRawAxis(2)
                            - joyDriver.getRawAxis(3));
                    jagRightMaster.setX(joyDriver.getRawAxis(2)
                            + joyDriver.getRawAxis(3));
                } catch (CANTimeoutException ex) {
                    System.out.println(ex.toString());
                }
                break;
        }

        // Synchronize Slave Jaguars
        try {
            jagLeftSlave.setX(jagLeftMaster.getX());
            jagRightSlave.setX(jagRightMaster.getX());
        } catch (CANTimeoutException ex) {
            System.out.println(ex.toString());
        }

        // Update the Driver Station
       // dsLCD.println(DriverStationLCD.Line.kUser3, 1, "L:" + digLineLeft.get()
         //       + " M:" + digLineMiddle.get() + " R:" + digLineRight.get());

        dsLCD.println(DriverStationLCD.Line.kUser4, 1, "Range:"
                + anaUltraSonic.getVoltage() + "    ");

        dsLCD.updateLCD();

    }
}
