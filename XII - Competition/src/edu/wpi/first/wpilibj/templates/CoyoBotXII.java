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
    CANJaguar jagShoulderOne, jagShoulderTwo;
    Victor vicGripperTop, vicGripperBottom;
    Compressor compressor;
    Solenoid solShifterHigh, solShifterLow;
    Solenoid solArmStageOneIn, solArmStageOneOut;
    Solenoid solArmStageTwoIn, solArmStageTwoOut;
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
    PIDShoulderController pidShoulderController;
    PIDShoulderSource pidShoulderSource;
    PIDShoulderOutput pidShoulderOutput;
    double distance;
    int driveMode;
    int joyMode; //Terrible name
    int armState;
    int armFlip; // Reverses up/down on arm depending on last preset pressed
    boolean driveToggle;
    boolean joyToggle; //Terrible name
    boolean lTriggerToggle;
    boolean rTriggerToggle;
    boolean cruiseControl;
    boolean shoulderPID = false;
    double pConstant = 0.1, iConstant = 0, dConstant = 0;
    double leftSpeed, rightSpeed;
    double xInput, yInput;
    double maxSpeed;
    double pVal = -0.08;
    double iVal;
    double maxLowSpeed = 180;
    double maxHighSpeed = 530;
    double lineError = 0;
    double prevLineError = 0.0;
    double shoulderError = 0;
    double prevShoulderError = 0;
    double setpointVal = 0.416;
    double prevsetpointVal = 0.267;

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

            jagShoulderOne = new CANJaguar(ElectricalMap.kJaguarShoulderOne);
            jagShoulderOne.setPositionReference(CANJaguar.PositionReference.kPotentiometer);
            jagShoulderOne.configPotentiometerTurns(1);

            jagShoulderTwo = new CANJaguar(ElectricalMap.kJaguarShoulderTwo);

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

        solArmStageOneIn = new Solenoid(8, 1);
        solArmStageOneOut = new Solenoid(8, 2);
        solArmStageTwoIn = new Solenoid(8, 3);
        solArmStageTwoOut = new Solenoid(8, 4);

        solArmStageOneIn.set(true);
        solArmStageOneOut.set(false);
        solArmStageTwoIn.set(true);
        solArmStageTwoOut.set(false);


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
        pidLineController = new PIDLineController(pVal, 0, 0.0, pidLineError, pidLineOutput);
        pidLineController.setInputRange(-4, 4);
        pidLineController.setOutputRange(-1, 1);
        pidLineController.setSetpoint(0);
        pidLineController.enable();
        pidShoulderSource = new PIDShoulderSource();
        pidShoulderOutput = new PIDShoulderOutput();
        pidShoulderController = new PIDShoulderController(-12, 0, -5, pidShoulderSource, pidShoulderOutput);
        pidShoulderController.setInputRange(-1, 1);
        pidShoulderController.setOutputRange(-1, 1);
        pidShoulderController.setSetpoint(0);
        driveMode = 2; //0 = Tank; 1 = Arcade; 2 = Kaj; 3 = LineTrack
        armState = 0;//0,1,2 = retracted, middle, extended
        driveToggle = false;
        cruiseControl = false;
        joyToggle = false;
        pidShoulderController.enable();
        rTriggerToggle = false;
        lTriggerToggle = false;
        joyMode = 0;
        vicGripperTop = new Victor(ElectricalMap.kVictorGripperTopChannel);
        vicGripperBottom = new Victor(ElectricalMap.kVictorGripperBottomChannel);
        armState = 2;

        maxSpeed = maxLowSpeed;

        armFlip = 1; // Regular arm up/down

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
        pidLineController.enable();
        try {
            jagLeftMaster.setX(60);
            jagRightMaster.setX(60);
        } catch (CANTimeoutException ex) {
            System.out.println(ex.toString());
        }
        if (digLineLeft.get() && digLineMiddle.get() && digLineRight.get()) {
            pidLineError.lineError = 0;
        }
        if (!digLineLeft.get() && digLineMiddle.get() && !digLineRight.get()) {
            pidLineError.lineError = 0;
        }
        if (digLineLeft.get() && !digLineMiddle.get() && digLineRight.get()) {
            pidLineError.lineError = 0;
        }
        if (digLineLeft.get() && digLineMiddle.get() && !digLineRight.get()) {
            pidLineError.lineError = -1;
        }
        if (!digLineLeft.get() && digLineMiddle.get() && digLineRight.get()) {
            pidLineError.lineError = 1;
        }
        if (digLineLeft.get() && !digLineMiddle.get() && !digLineRight.get()) {
            pidLineError.lineError = -2;
        }
        if (!digLineLeft.get() && !digLineMiddle.get() && digLineRight.get()) {
            pidLineError.lineError = 2;
        }



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
        /* P- Value tuned already
        if (joyDriver.getRawButton(4)) {
        pVal -= 0.01;
        pidLineController.setPID(pVal, 0, 0);

        } else if (joyDriver.getRawButton(1)) {
        pVal += 0.01;
        pidLineController.setPID(pVal, 0, 0);
        }
         */
        //GRIPPER: Out, in, or rotate
        if (joyOperator.getRawAxis(3) > .05 || joyOperator.getRawAxis(3) < -.05) {
            vicGripperTop.set(joyOperator.getRawAxis(3));
            vicGripperBottom.set(joyOperator.getRawAxis(3));
        } else {
            if (armFlip == 1) {
                vicGripperTop.set(-1 * (joyOperator.getRawAxis(2)));
                vicGripperBottom.set(joyOperator.getRawAxis(2));
            } else {
                vicGripperTop.set((joyOperator.getRawAxis(2)));
                vicGripperBottom.set(-1 * (joyOperator.getRawAxis(2)));
            }
        }

        // Map buttons to arm setpoints
        if (joyOperator.getRawButton(1)) {
            // Pickup Front
            setpointVal = 0.1;
            shoulderPID = true;
            armFlip = 1;
            armState = 2;
        }
        if (joyOperator.getRawButton(2)) {
            // Pickup Back
            setpointVal = 0.88;
            shoulderPID = true;
            armFlip = -1;
            armState = 2;
        }
        if (joyOperator.getRawButton(3)) {
            // Top Front
            setpointVal = 0.414;
            shoulderPID = true;
            armFlip = 1;

        }
        if (joyOperator.getRawButton(4)) {
            // Top Back
            setpointVal = 0.585;
            shoulderPID = true;
            armFlip = -1;

        }

        //Arm States
        if (!lTriggerToggle && joyOperator.getRawButton(5)) {
            armState = Math.min(armState + 1, 2);
            lTriggerToggle = true;
        } else if (lTriggerToggle && !joyOperator.getRawButton(5)) {
            lTriggerToggle = false;
        }
        if (!rTriggerToggle && joyOperator.getRawButton(6)) {
            armState = Math.max(armState - 1, 0);
            rTriggerToggle = true;
        } else if (rTriggerToggle && !joyOperator.getRawButton(6)) {
            rTriggerToggle = false;
        }
        switch (armState) {
            case 0:
                solArmStageOneIn.set(true);
                solArmStageOneOut.set(false);
                solArmStageTwoIn.set(true);
                solArmStageTwoOut.set(false);
                break;
            case 1:
                solArmStageOneIn.set(false);
                solArmStageOneOut.set(true);
                solArmStageTwoIn.set(true);
                solArmStageTwoOut.set(false);
                break;
            case 2:
                solArmStageOneIn.set(false);
                solArmStageOneOut.set(true);
                solArmStageTwoIn.set(false);
                solArmStageTwoOut.set(true);
                break;
        }
        if (!driveToggle && joyDriver.getRawButton(2)) {
            driveMode = (driveMode + 1) % 4;
            driveToggle = true;
        } else if (driveToggle && !joyDriver.getRawButton(2)) {
            driveToggle = false;
        }

        if (!joyToggle && joyDriver.getRawButton(5)) {
            joyMode = (joyMode + 1) % 2;
            joyToggle = true;
        } else if (driveToggle && !joyDriver.getRawButton(2)) {
            joyToggle = false;
        }
        /*
        if (joyOperator.getRawButton(1) && shoulderPID == false) {
        shoulderPID = true;
        pidShoulderController.enable();
        } else if (joyOperator.getRawButton(1) && shoulderPID == true) {
        shoulderPID = false;
        pidShoulderController.disable();
        }
        try {

        if (shoulderPID == true) {
         */

        if (shoulderPID) {
            if (Math.abs(joyOperator.getRawAxis(5)) > 0.2) {
                shoulderPID = false;
                pidShoulderController.disable();
            } else {
                pidShoulderController.enable();

                try {

                    jagShoulderOne.setX((pidShoulderOutput.zValue));

                } catch (CANTimeoutException ex) {
                    System.out.println(ex.toString());
                }
            }
        } else {
            try {
                if (jagShoulderOne.getPosition() < 0.1) {
                    jagShoulderOne.setX(Math.min(0.0, armFlip * (joyOperator.getRawAxis(5)) * (joyOperator.getRawAxis(5)) * (joyOperator.getRawAxis(5))));
                } else if (jagShoulderOne.getPosition() > 0.9) {
                    jagShoulderOne.setX(Math.max(0.0, armFlip * (joyOperator.getRawAxis(5)) * (joyOperator.getRawAxis(5)) * (joyOperator.getRawAxis(5))));
                } else {
                    jagShoulderOne.setX(armFlip * (joyOperator.getRawAxis(5)) * (joyOperator.getRawAxis(5)) * (joyOperator.getRawAxis(5)));
                }
            } catch (CANTimeoutException ex) {
                System.out.println(ex.toString());
            }
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
                    if (armFlip == 1) {
                        xInput = joyDriver.getRawAxis(3);
                        yInput = joyDriver.getRawAxis(2);
                        octantJoystick();
                        jagLeftMaster.setX(maxSpeed * leftSpeed);
                        jagRightMaster.setX(maxSpeed * rightSpeed);
                        pidLineController.disable();
                    } else {
                        xInput = joyDriver.getRawAxis(3);
                        yInput = -joyDriver.getRawAxis(2);
                        octantJoystick();
                        jagLeftMaster.setX(maxSpeed * leftSpeed);
                        jagRightMaster.setX(maxSpeed * rightSpeed);
                        pidLineController.disable();
                    }

                } catch (CANTimeoutException ex) {
                    System.out.println(ex.toString());


                }
                break;



        } /*if(!joyToggle && joyDriver.getRawButton(5)) {
        xInput = joyDriver.getRawAxis(3)*convertJoy(joyDriver.getRawAxis(3),joyDriver.getRawAxis(4));
        joyToggle = true;
        } else if (joyToggle && !joyDriver.getRawButton(5)) {
        joyToggle = false;
        } //Button values to be fixed/**/

        updateDS();

        // Update the Driver Station
        // dsLCD.println(DriverStationLCD.Line.kUser3, 1, "L:" + digLineLeft.get()
        //       + " M:" + digLineMiddle.get() + " R:" + digLineRight.get());

//        dsLCD.println(DriverStationLCD.Line.kUser4, 1, "Range:"
        //              + anaUltraSonic.getVoltage() + "    ");




    }

    public void teleopContinuous() {
        if (digLineLeft.get() && digLineMiddle.get() && digLineRight.get()) {

            pidLineError.lineError = prevLineError * 2;


        }
        if (!digLineLeft.get() && !digLineMiddle.get() && !digLineRight.get()) {
            pidLineError.lineError = 0;
            prevLineError = 0;


        }
        if (!digLineLeft.get() && digLineMiddle.get() && !digLineRight.get()) {
            pidLineError.lineError = 0;
            prevLineError = 0;


        }
        if (digLineLeft.get() && !digLineMiddle.get() && digLineRight.get()) {
            pidLineError.lineError = 0;
            prevLineError = 0;


        }
        if (!digLineLeft.get() && !digLineMiddle.get() && digLineRight.get()) {
            pidLineError.lineError = -1;
            prevLineError = -1;


        }
        if (digLineLeft.get() && !digLineMiddle.get() && !digLineRight.get()) {
            pidLineError.lineError = 1;
            prevLineError = 1;


        }
        if (!digLineLeft.get() && digLineMiddle.get() && digLineRight.get()) {
            pidLineError.lineError = -2;
            prevLineError = -2;


        }
        if (digLineLeft.get() && digLineMiddle.get() && !digLineRight.get()) {
            pidLineError.lineError = 2;
            prevLineError = 2;


        }
        try {
            if (jagShoulderOne.getPosition() != setpointVal) {
                pidShoulderSource.shoulderError = (jagShoulderOne.getPosition() - setpointVal);
            }
        } catch (CANTimeoutException ex) {
        }

        switch (driveMode) {
            case 3:
                dsLCD.println(DriverStationLCD.Line.kMain6, 1,
                        "Drive mode: Line   ");


                try {
                    xInput = pidLineOutput.xValue;
                    yInput = joyDriver.getRawAxis(2);
                    //octantJoystick();
                    leftSpeed = -yInput + xInput;
                    rightSpeed = -yInput - xInput;
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
            jagShoulderTwo.setX(jagShoulderOne.getX());


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
    public double convertJoy(double x, double y) {
        return Math.sqrt(1.0 + (x / y) * (x / y));


    }

    public void updateDS() {
        try {
            dsLCD.println(DriverStationLCD.Line.kUser2, 1, "Left Enc: " + (int) jagLeftMaster.getSpeed() + "     ");
            dsLCD.println(DriverStationLCD.Line.kUser3, 1, "SetpointVal: " + setpointVal + "     ");
            //dsLCD.println(DriverStationLCD.Line.kUser6, 1, "PIDX: " + pidLineOutput.xValue + "     ");

            //dsLCD.println(DriverStationLCD.Line.kUser4, 1, "P: " + pConstant);
            //dsLCD.println(DriverStationLCD.Line.kUser5, 1, "I: " + iConstant);
            //dsLCD.println(DriverStationLCD.Line.kUser6, 1, "D: " + dConstant);


        } catch (CANTimeoutException ex) {
            System.out.println(ex.toString());


        }
        dsLCD.println(DriverStationLCD.Line.kUser4, 1, "zValue: " + pidShoulderOutput.zValue);


        try {

            dsLCD.println(DriverStationLCD.Line.kUser5, 1, " potValue: " + (jagShoulderOne.getPosition()));
            dsLCD.updateLCD();


        } catch (CANTimeoutException ex) {
            dsLCD.println(DriverStationLCD.Line.kUser5, 1, " p0tValue: We have a problem matey");
            dsLCD.updateLCD();


        }

        dsLCD.println(DriverStationLCD.Line.kMain6, 1, "USonic m: " + anaUltraSonic.getVoltage() * 0.3804473);
        dsLCD.println(DriverStationLCD.Line.kUser6, 1, "setpointBool: " + shoulderPID);
        dsLCD.updateLCD();


    }
}
