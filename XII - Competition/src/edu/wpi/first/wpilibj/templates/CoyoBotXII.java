package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.can.CANTimeoutException;

public class CoyoBotXII extends IterativeRobot {

    CANJaguar jagFrontLeftMaster, jagBackLeftSlave,
            jagFrontRightMaster, jagBackRightSlave;
    Victor vicGripperTop, vicGripperBottom;
    CANJaguar jagShoulder;
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
            jagFrontLeftMaster = new CANJaguar(3);
            jagBackLeftSlave = new CANJaguar(4);
            jagFrontRightMaster = new CANJaguar(1);
            jagBackRightSlave = new CANJaguar(2);
        } catch (CANTimeoutException ex) {
            System.out.println(ex.toString());
        }

        compressor = new Compressor(1,1);

        solShifterHigh = new Solenoid(7,1);
        solShifterLow = new Solenoid(7,2);

        joyDriver = new Joystick(1);
        joyOperator = new Joystick(2);

        //digLineLeft = new DigitalInput(1);
        //digLineMiddle = new DigitalInput(2);
        //digLineRight = new DigitalInput(3);

        anaUltraSonic = new AnalogChannel(1);

        driveMode = 0; //0 = Tank; 1 = Arcade; 2 = Kaj
        driveToggle = false;
        cruiseControl = false;

        compressor.start();
    }

    public void disabledPeriodic(){
        regulatePressure();
    }
    public void autonomousPeriodic() {
        regulatePressure();
    }

    public void teleopInit() {
    }

    public void teleopPeriodic() {
        regulatePressure();

        watchdog.feed(); //feed the watchdog

        //Check buttons & set shift - high is 3, low is 2
        if(joyDriver.getRawButton(3)){
            solShifterHigh.set(true);
            solShifterLow.set(false);
        }
        else if(joyDriver.getRawButton(2)){
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
                    jagFrontLeftMaster.setX(joyDriver.getRawAxis(2));
                    jagFrontRightMaster.setX(joyDriver.getRawAxis(4));
                } catch (CANTimeoutException ex) {
                    System.out.println(ex.toString());
                }
                break;
            case 1:
                dsLCD.println(DriverStationLCD.Line.kMain6, 1,
                        "Drive mode: Arcade");
                try {
                    jagFrontLeftMaster.setX(joyDriver.getRawAxis(2)
                            - joyDriver.getRawAxis(1));
                    jagFrontRightMaster.setX(joyDriver.getRawAxis(2)
                            + joyDriver.getRawAxis(1));
                } catch (CANTimeoutException ex) {
                    System.out.println(ex.toString());
                }
                break;
            case 2:
                dsLCD.println(DriverStationLCD.Line.kMain6, 1,
                        "Drive mode: Kaj   ");
                try {
                    jagFrontLeftMaster.setX(joyDriver.getRawAxis(2)
                            - joyDriver.getRawAxis(3));
                    jagFrontRightMaster.setX(joyDriver.getRawAxis(2)
                            + joyDriver.getRawAxis(3));
                } catch (CANTimeoutException ex) {
                    System.out.println(ex.toString());
                }
                break;
        }

        // Synchronize Slave Jaguars
        try {
            jagBackLeftSlave.setX(jagFrontLeftMaster.getX());
            jagBackRightSlave.setX(jagFrontRightMaster.getX());
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
    public void regulatePressure(){
        if(compressor.getPressureSwitchValue());//compressor.stop();
        else compressor.start();
    }
}
