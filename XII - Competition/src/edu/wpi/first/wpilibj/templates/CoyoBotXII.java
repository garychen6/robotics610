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
    double pConstant = 0.1, iConstant = 0, dConstant = 0;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {

        watchdog = Watchdog.getInstance();
        dsLCD = DriverStationLCD.getInstance();

        try {
            jagLeftMaster = new CANJaguar(ElectricalMap.kJaguarLeftMaster);
            jagRightMaster = new CANJaguar(ElectricalMap.kJaguarRightMaster);
            jagLeftMaster.setSpeedReference(CANJaguar.SpeedReference.kEncoder);
            jagRightMaster.setSpeedReference(CANJaguar.SpeedReference.kEncoder);
            jagLeftMaster.configEncoderCodesPerRev(512);
            jagRightMaster.configEncoderCodesPerRev(512);
            jagLeftSlave = new CANJaguar(ElectricalMap.kJaguarLeftSlave);
            jagRightSlave = new CANJaguar(ElectricalMap.kJaguarRightSlave);
        } catch (CANTimeoutException ex) {
            System.out.println(ex.toString());
        }

        try {
            jagLeftMaster.changeControlMode(CANJaguar.ControlMode.kSpeed);
            jagRightMaster.changeControlMode(CANJaguar.ControlMode.kSpeed);
            jagLeftMaster.setPID(0.1, 0, 0);
            jagRightMaster.setPID(0.1, 0, 0);
            jagLeftMaster.enableControl();
            jagRightMaster.enableControl();
        } catch (CANTimeoutException ex){
            System.out.println(ex.toString());
        }
        compressor = new Compressor(ElectricalMap.kCompressorPressureSwitchChannel,ElectricalMap.kCompressorRelayChannel);

        solShifterHigh = new Solenoid(ElectricalMap.kSolenoidModulePort,ElectricalMap.kSolenoidHighChannel);
        solShifterLow = new Solenoid(ElectricalMap.kSolenoidModulePort,ElectricalMap.kSolenoidLowChannel);

        joyDriver = new Joystick(ElectricalMap.kJoystickDriverPort);
        joyOperator = new Joystick(ElectricalMap.kJoystickOperatorPort);

        digLineLeft = new DigitalInput(ElectricalMap.kLightSensorLChannel);
        digLineMiddle = new DigitalInput(ElectricalMap.kLightSensorMChannel);
        digLineRight = new DigitalInput(ElectricalMap.kLightSensorRChannel);

        //anaUltraSonic = new AnalogChannel(ElectricalMap.kUltrasonicChannel);

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
            jagLeftMaster.setX(-60);
            jagRightMaster.setX(-60);
        } catch (CANTimeoutException ex){
            System.out.println(ex.toString());
        }
    }

    public void autonomousPeriodic(){
        watchdog.feed();
        updateDS();
    }
    
    public void autonomousContinuous() {
        syncSlaves();
    }

    public void teleopInit() {
        try {
            //jagLeftMaster.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
            //jagRightMaster.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
            jagLeftMaster.enableControl();
            jagRightMaster.enableControl();
            jagLeftMaster.setPID(pConstant, iConstant, dConstant);
            jagRightMaster.setPID(pConstant, iConstant, dConstant);
            jagLeftMaster.setX(60);
            jagRightMaster.setX(60);
        } catch (CANTimeoutException ex){
            System.out.println(ex.toString());
        }
         
    }

    public void teleopPeriodic(){
        watchdog.feed();
        syncSlaves();
        if(joyDriver.getRawButton(5))pConstant += 0.01;
        if(joyDriver.getRawButton(7))pConstant -= 0.01;
        if(joyDriver.getRawButton(4))iConstant += 0.001;
        if(joyDriver.getRawButton(2))iConstant -= 0.001;
        if(joyDriver.getRawButton(6))dConstant += 0.01;
        if(joyDriver.getRawButton(8))dConstant -= 0.01;

        //Check buttons & set shift - high is 8, low is 7
        if(joyDriver.getRawButton(10)){
            solShifterHigh.set(true);
            solShifterLow.set(false);
        }
        else if(joyDriver.getRawButton(9)){
            solShifterHigh.set(false);
            solShifterLow.set(true);
        }
        updateDS();
    }
    public void NOTteleopPeriodic() {

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
                    jagLeftMaster.setX(120*(-joyDriver.getRawAxis(2)));
                    jagRightMaster.setX(120*(-joyDriver.getRawAxis(4)));
                } catch (CANTimeoutException ex) {
                    System.out.println(ex.toString());
                }
                break;
            case 1:
                dsLCD.println(DriverStationLCD.Line.kMain6, 1,
                        "Drive mode: Arcade");
                try {
                    jagLeftMaster.setX(120*(-joyDriver.getRawAxis(2)
                            + joyDriver.getRawAxis(1)));
                    jagRightMaster.setX(120*(-joyDriver.getRawAxis(2)
                            - joyDriver.getRawAxis(1)));
                } catch (CANTimeoutException ex) {
                    System.out.println(ex.toString());
                }
                break;
            case 2:
                dsLCD.println(DriverStationLCD.Line.kMain6, 1,
                        "Drive mode: Kaj   ");
                try {
                    jagLeftMaster.setX(120*(-joyDriver.getRawAxis(2)
                            + joyDriver.getRawAxis(3)));
                    jagRightMaster.setX(120*(-joyDriver.getRawAxis(2)
                            - joyDriver.getRawAxis(3)));
                } catch (CANTimeoutException ex) {
                    System.out.println(ex.toString());
                }
                break;
        }

        syncSlaves();

        updateDS();

        // Update the Driver Station
       // dsLCD.println(DriverStationLCD.Line.kUser3, 1, "L:" + digLineLeft.get()
         //       + " M:" + digLineMiddle.get() + " R:" + digLineRight.get());

//        dsLCD.println(DriverStationLCD.Line.kUser4, 1, "Range:"
  //              + anaUltraSonic.getVoltage() + "    ");


    }

    public void syncSlaves(){
        try {
            jagLeftSlave.setX(jagLeftMaster.getOutputVoltage());
            jagRightSlave.setX(jagRightMaster.getOutputVoltage());
        } catch (CANTimeoutException ex){
            System.out.println(ex.toString());
        }
    }

    public void updateDS(){
        try{
            dsLCD.println(DriverStationLCD.Line.kUser2, 1, "Left Enc: " + (int)jagLeftMaster.getSpeed());
            dsLCD.println(DriverStationLCD.Line.kUser3, 1, "Right Enc: " + (int)jagRightMaster.getSpeed());
            dsLCD.println(DriverStationLCD.Line.kUser4, 1, "P: " + pConstant);
            dsLCD.println(DriverStationLCD.Line.kUser5, 1, "I: " + iConstant);
            dsLCD.println(DriverStationLCD.Line.kUser6, 1, "D: " + dConstant);
        } catch (CANTimeoutException ex){
            System.out.println(ex.toString());
        }
        dsLCD.updateLCD();
    }
}
