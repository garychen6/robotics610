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
    PIDLineController pidLineController;
    PIDLineSource pidLineError;
    PIDLineOutput pidLineOutput;
    double distance;
    int driveMode;
    boolean driveToggle;
    boolean cruiseControl;
    double pConstant = 0.1, iConstant = 0, dConstant = 0;
    double leftSpeed, rightSpeed;
    double xInput, yInput;
    double maxSpeed;
    double maxLowSpeed = 180;
    double maxHighSpeed = 530;
    double lineError = 0;

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
            jagLeftMaster.configEncoderCodesPerRev(256);
            jagRightMaster.configEncoderCodesPerRev(256);
            jagLeftSlave = new CANJaguar(ElectricalMap.kJaguarLeftSlave);
            jagRightSlave = new CANJaguar(ElectricalMap.kJaguarRightSlave);
        } catch (CANTimeoutException ex) {
            System.out.println(ex.toString());
        }

        try {
            jagLeftMaster.changeControlMode(CANJaguar.ControlMode.kSpeed);
            jagRightMaster.changeControlMode(CANJaguar.ControlMode.kSpeed);
            jagLeftMaster.setPID(0.1, 0.0002, 0);
            jagRightMaster.setPID(0.1, 0.0002, 0);
        } catch (CANTimeoutException ex) {
            System.out.println(ex.toString());
        }

        compressor = new Compressor(ElectricalMap.kCompressorPressureSwitchChannel, ElectricalMap.kCompressorRelayChannel);

        solShifterHigh = new Solenoid(ElectricalMap.kSolenoidModulePort, ElectricalMap.kSolenoidHighChannel);
        solShifterLow = new Solenoid(ElectricalMap.kSolenoidModulePort, ElectricalMap.kSolenoidLowChannel);

        joyDriver = new Joystick(ElectricalMap.kJoystickDriverPort);
        joyOperator = new Joystick(ElectricalMap.kJoystickOperatorPort);

        digLineLeft = new DigitalInput(ElectricalMap.kLightSensorLChannel);
        digLineMiddle = new DigitalInput(ElectricalMap.kLightSensorMChannel);
        digLineRight = new DigitalInput(ElectricalMap.kLightSensorRChannel);

        anaUltraSonic = new AnalogChannel(ElectricalMap.kUltrasonicChannel);
        anaUltraSonic.setAverageBits(8);
        anaUltraSonic.setOversampleBits(4);

        pidLineError = new PIDLineSource();
        pidLineOutput = new PIDLineOutput();
        pidLineController = new PIDLineController(-0.15, -0.0020, 0.0, pidLineError, pidLineOutput);
        pidLineController.setInputRange(-2, 2);
        pidLineController.setOutputRange(-1, 1);
        pidLineController.setSetpoint(0);
        pidLineController.enable();

        driveMode = 2; //0 = Tank; 1 = Arcade; 2 = Kaj; 3 = LineTrack
        driveToggle = false;
        cruiseControl = false;
        vicGripperTop = new Victor(ElectricalMap.kVictorGripperTopChannel);
        vicGripperBottom = new Victor(ElectricalMap.kVictorGripperBottomChannel);


        maxSpeed = maxLowSpeed;

        compressor.start();
    }

    public void disabledInit() {
    }

    public void disabledPeriodic() {
        updateDS();
    }

    public void autonomousInit() {
        try {
            jagLeftMaster.enableControl();
            jagRightMaster.enableControl();
            jagLeftMaster.setX(-60);
            jagRightMaster.setX(-60);
        } catch (CANTimeoutException ex) {
            System.out.println(ex.toString());
        }
    }

    public void autonomousPeriodic() {
        watchdog.feed();
        updateDS();
    }

    public void autonomousContinuous() {
        syncSlaves();
    }

    public void teleopInit() {
        try {
            jagLeftMaster.enableControl();
            jagRightMaster.enableControl();
        } catch (CANTimeoutException ex) {
            System.out.println(ex.toString());
        }
    }

    public void ALSONOTteleopPeriodic() {
        watchdog.feed();
        syncSlaves();
        if (joyDriver.getRawButton(5)) {
            pConstant += 0.01;
        }
        if (joyDriver.getRawButton(7)) {
            pConstant -= 0.01;
        }
        if (joyDriver.getRawButton(4)) {
            iConstant += 0.0001;
        }
        if (joyDriver.getRawButton(2)) {
            iConstant -= 0.0001;
        }
        if (joyDriver.getRawButton(6)) {
            dConstant += 0.01;
        }
        if (joyDriver.getRawButton(8)) {
            dConstant -= 0.01;
        }

        //Check buttons & set shift - high is 8, low is 7
        if (joyDriver.getRawButton(10)) {
            solShifterHigh.set(true);
            solShifterLow.set(false);
        } else if (joyDriver.getRawButton(9)) {
            solShifterHigh.set(false);
            solShifterLow.set(true);
        }
        updateDS();
    }

    public void teleopPeriodic() {
        watchdog.feed(); //feed the watchdog

        //Check buttons & set shift - high is 8, low is 7
        if (joyDriver.getRawButton(8)) {
            solShifterHigh.set(true);
            solShifterLow.set(false);
            maxSpeed = maxHighSpeed;
        } else if (joyDriver.getRawButton(7)) {
            solShifterHigh.set(false);
            solShifterLow.set(true);
            maxSpeed = maxLowSpeed;
        }
        if (joyOperator.getRawButton(4))
        {
            vicGripperTop.set(1);
            vicGripperBottom.set(1);
        }
        else if(joyOperator.getRawButton(1))
        {
            vicGripperTop.set(-1);
            vicGripperBottom.set(-1);
        }
        else
        {
            if(joyOperator.getRawAxis(2) > 0)
          {
            vicGripperTop.set(-1 * (joyOperator.getRawAxis(2)));
            vicGripperBottom.set(joyOperator.getRawAxis(2));
          }
        }
        if (!driveToggle && joyDriver.getRawButton(2)) {
            driveMode = (driveMode + 1) % 4;
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
                    jagLeftMaster.setX(maxSpeed * (-joyDriver.getRawAxis(2)));
                    jagRightMaster.setX(maxSpeed * (-joyDriver.getRawAxis(4)));
                    pidLineController.disable();
                } catch (CANTimeoutException ex) {
                    System.out.println(ex.toString());
                }
                break;
            case 1:
                dsLCD.println(DriverStationLCD.Line.kMain6, 1,
                        "Drive mode: Arcade");
                try {
                    xInput = joyDriver.getRawAxis(1);
                    yInput = joyDriver.getRawAxis(2);
                    octantJoystick();
                    jagLeftMaster.setX(maxSpeed * leftSpeed);
                    jagRightMaster.setX(maxSpeed * rightSpeed);
                    pidLineController.disable();
                } catch (CANTimeoutException ex) {
                    System.out.println(ex.toString());
                }
                break;
            case 2:
                dsLCD.println(DriverStationLCD.Line.kMain6, 1,
                        "Drive mode: Kaj   ");
                try {
                    xInput = joyDriver.getRawAxis(3);
                    yInput = joyDriver.getRawAxis(2);
                    octantJoystick();
                    jagLeftMaster.setX(maxSpeed * leftSpeed);
                    jagRightMaster.setX(maxSpeed * rightSpeed);
                    pidLineController.disable();
                } catch (CANTimeoutException ex) {
                    System.out.println(ex.toString());
                }
                break;
           
        }


        updateDS();

        // Update the Driver Station
        // dsLCD.println(DriverStationLCD.Line.kUser3, 1, "L:" + digLineLeft.get()
        //       + " M:" + digLineMiddle.get() + " R:" + digLineRight.get());

//        dsLCD.println(DriverStationLCD.Line.kUser4, 1, "Range:"
        //              + anaUltraSonic.getVoltage() + "    ");


    }

    public void teleopContinuous() {
        if (!digLineLeft.get() && !digLineMiddle.get() && !digLineRight.get()) {
            pidLineError.lineError = 0;
        }
        if (!digLineLeft.get() && digLineMiddle.get() && !digLineRight.get()) {
            pidLineError.lineError = 0;
        }
        if (digLineLeft.get() && !digLineMiddle.get() && digLineRight.get()) {
            pidLineError.lineError = 0;
        }
        if (!digLineLeft.get() && !digLineMiddle.get() && digLineRight.get()) {
            pidLineError.lineError = -1;
        }
        if (digLineLeft.get() && !digLineMiddle.get() && !digLineRight.get()) {
            pidLineError.lineError = 1;
        }
        if (!digLineLeft.get() && digLineMiddle.get() && digLineRight.get()) {
            pidLineError.lineError = -2;
        }
        if (digLineLeft.get() && digLineMiddle.get() && !digLineRight.get()) {
            pidLineError.lineError = 2;
        }

        switch (driveMode)
        {
         case 3:
                dsLCD.println(DriverStationLCD.Line.kMain6, 1,
                        "Drive mode: Line   ");
                try {
                    xInput = pidLineOutput.xValue;
                    yInput = joyDriver.getRawAxis(2);
                    octantJoystick();
                    jagLeftMaster.setX(maxSpeed * leftSpeed);
                    jagRightMaster.setX(maxSpeed * rightSpeed);
                    pidLineController.enable();
                } catch (CANTimeoutException ex) {
                    System.out.println(ex.toString());
                }
                break;
        }
        syncSlaves();
    }

    public void octantJoystick() {
        if (xInput >= 0 && yInput >= 0) {
            if (xInput > yInput) {
                //Quadrant 8
                leftSpeed = xInput - yInput;
                rightSpeed = -xInput;
            } else {
                //Quadrant 7
                leftSpeed = xInput - yInput;
                rightSpeed = -yInput;
            }
        }
        if (xInput >= 0 && yInput <= 0) {
            if (xInput > -yInput) {
                //Quadrant 1
                leftSpeed = xInput;
                rightSpeed = -(yInput + xInput);
            } else {
                //Quadrant 2
                leftSpeed = -yInput;
                rightSpeed = -(yInput + xInput);
            }
        }
        if (xInput <= 0 && yInput <= 0) {
            if (xInput < yInput) {
                //Quadrant 4
                leftSpeed = xInput - yInput;
                rightSpeed = -xInput;
            } else {
                //Quadrant 3
                leftSpeed = xInput - yInput;
                rightSpeed = -yInput;
            }
        }
        if (xInput <= 0 && yInput >= 0) {
            if (-xInput > yInput) {
                //Quadrant 5
                leftSpeed = xInput;
                rightSpeed = -(xInput + yInput);
            } else {
                //Quadrant 6
                leftSpeed = -yInput;
                rightSpeed = -(xInput + yInput);
            }
        }
    }

    public void syncSlaves() {
        try {
            jagLeftSlave.setX(jagLeftMaster.getOutputVoltage());
            jagRightSlave.setX(jagRightMaster.getOutputVoltage());
        } catch (CANTimeoutException ex) {
            System.out.println(ex.toString());
        }
    }
    /**
     * Convert the joystick values from polar to cartesian coordinates.
     * Only call when using the circle-based joysticks.
     * @param x the x-value of the joystick
     * @param y the y-value of the joystick
     * @return the ratio to multiply both x and y by
     */
    public double convertJoy(double x, double y){
        return Math.sqrt(1.0 + (x/y)*(x/y));
    }
    public void updateDS() {
        try {
            dsLCD.println(DriverStationLCD.Line.kUser2, 1, "Left Enc: " + (int) jagLeftMaster.getSpeed() + "     ");
            dsLCD.println(DriverStationLCD.Line.kUser3, 1, "Right Enc: " + (int) jagRightMaster.getSpeed() + "     ");
            dsLCD.println(DriverStationLCD.Line.kUser6, 1, "PidX: " + pidLineOutput.xValue + "     ");

            //dsLCD.println(DriverStationLCD.Line.kUser4, 1, "P: " + pConstant);
            //dsLCD.println(DriverStationLCD.Line.kUser5, 1, "I: " + iConstant);
            //dsLCD.println(DriverStationLCD.Line.kUser6, 1, "D: " + dConstant);
        } catch (CANTimeoutException ex) {
            System.out.println(ex.toString());
        }
        dsLCD.println(DriverStationLCD.Line.kUser4, 1, "L: " + !digLineLeft.get() + " M: " + !digLineMiddle.get());
        dsLCD.println(DriverStationLCD.Line.kUser4, 20, " R: " + !digLineRight.get());
        dsLCD.println(DriverStationLCD.Line.kUser5, 1, "Distance: " + (anaUltraSonic.getAverageVoltage() / 0.3858267716535433));
        dsLCD.updateLCD();
    }
}
