package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.Watchdog;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.can.CANTimeoutException;

public class CoyoBotXII extends IterativeRobot {

    CANJaguar jagLeftMaster, jagLeftSlave,
            jagRightMaster, jagRightSlave;
    CANJaguar jagShoulderOne; //TODO: Comp is , jagShoulderTwo;
    Victor jagShoulderTwo; //TODO: Remove me for Comp
    Victor vicGripperTop, vicGripperBottom;
    Relay fluxCapacitorOne, fluxCapacitorTwo;
    Compressor compressor;
    Solenoid solShifterHigh, solShifterLow;
    Solenoid solArmStageOneIn, solArmStageOneOut;
    Solenoid solArmStageTwoIn, solArmStageTwoOut;
    Solenoid solDeploy;
    Solenoid solMinibotA;
    Solenoid solMinibotB;
    Joystick joyDriver;
    Joystick joyOperator;
    Watchdog watchdog;
    Timer timer;
    DriverStationLCD dsLCD;
    DigitalInput digLineLeft, digLineMiddle, digLineRight;
    AnalogChannel anaUltraSonic;
    PIDLineController pidLineController;
    PIDLineSource pidLineError;
    PIDLineOutput pidLineOutput;
    Timer autoTimer = new Timer();
    int driveMode;
    int joyMode; //Terrible name
    int armState;
    int armFlip; // Reverses up/down on arm depending on last preset pressed
    int fluxState;
    int autonomousStage;
    boolean driveToggle;
    boolean joyToggle; //Terrible name
    boolean lTriggerToggle;
    boolean rTriggerToggle;
    boolean parkmode;
    boolean shiftToggle;
    boolean shoulderPID;
    boolean canInitialized;
    boolean towerDrive;
    double leftSpeed, rightSpeed;
    double xInput, yInput;
    double maxSpeed;
    double maxLowSpeed;
    double maxHighSpeed;
    double prevLineError;
    double vToM;
    //double driveP = 0.9;
    //double driveI = 0.0018;
    //double driveD = 0.0;
    double driveP = 3.0;
    double driveI = 0.004;
    double driveD = 0.0;
    double armP = -800;
    double armI = 0;
    double armD = 0;
    double tdriveP = 0.01;
    double tdriveI = 0;
    double tdriveD = 0;


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
            jagLeftMaster.setPositionReference(CANJaguar.PositionReference.kQuadEncoder);
            jagRightMaster.setPositionReference(CANJaguar.PositionReference.kQuadEncoder);
            jagLeftMaster.configEncoderCodesPerRev(256);
            jagRightMaster.configEncoderCodesPerRev(256);
            jagLeftSlave = new CANJaguar(ElectricalMap.kJaguarLeftSlave);
            jagRightSlave = new CANJaguar(ElectricalMap.kJaguarRightSlave);
            jagLeftSlave.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
            jagRightSlave.changeControlMode(CANJaguar.ControlMode.kPercentVbus);

            jagShoulderOne = new CANJaguar(ElectricalMap.kJaguarShoulderOne);
            jagShoulderOne.setPositionReference(CANJaguar.PositionReference.kPotentiometer);
            jagShoulderOne.configPotentiometerTurns(1);

            //TODO: Comp is jagShoulderTwo = new CANJaguar(ElectricalMap.kJaguarShoulderTwo);
            // jagShoulderTwo.changeControlMode(CANJaguar.ControlMode.kPercentVbus);

            jagShoulderTwo = new Victor(ElectricalMap.kJaguarShoulderTwo); //TODO: Remove me for Comp

            canInitialized = true;

        } catch (CANTimeoutException ex) {
            System.out.println(ex.toString());
            canInitialized = false;
        }

        try {
            jagLeftMaster.changeControlMode(CANJaguar.ControlMode.kSpeed);
            jagRightMaster.changeControlMode(CANJaguar.ControlMode.kSpeed);
            jagLeftMaster.setPID(driveP, driveI, driveD);
            jagRightMaster.setPID(driveP, driveI, driveD);
        } catch (CANTimeoutException ex) {
            System.out.println(ex.toString());
            canInitialized = false;
        }

        compressor = new Compressor(ElectricalMap.kCompressorPressureSwitchChannel, ElectricalMap.kCompressorRelayChannel);
        compressor.start();

        solShifterHigh = new Solenoid(ElectricalMap.kSolenoidModulePort, ElectricalMap.kSolenoidHighChannel);
        solShifterLow = new Solenoid(ElectricalMap.kSolenoidModulePort, ElectricalMap.kSolenoidLowChannel);

        solArmStageOneIn = new Solenoid(ElectricalMap.kSolenoidModulePort, 3);
        solArmStageOneOut = new Solenoid(ElectricalMap.kSolenoidModulePort, 4);
        solArmStageTwoIn = new Solenoid(ElectricalMap.kSolenoidModulePort, 5);
        solArmStageTwoOut = new Solenoid(ElectricalMap.kSolenoidModulePort, 6);

        solArmStageOneIn.set(true);
        solArmStageOneOut.set(false);
        solArmStageTwoIn.set(true);
        solArmStageTwoOut.set(false);

        solMinibotA = new Solenoid(ElectricalMap.kSolenoidModulePort, ElectricalMap.kSolenoidMinibotA);
        solMinibotB = new Solenoid(ElectricalMap.kSolenoidModulePort, ElectricalMap.kSolenoidMinibotB);

        vicGripperTop = new Victor(ElectricalMap.kVictorGripperTopChannel);
        vicGripperBottom = new Victor(ElectricalMap.kVictorGripperBottomChannel);
        fluxCapacitorOne = new Relay(ElectricalMap.kRelayFluxOneChannel);
        fluxCapacitorTwo = new Relay(ElectricalMap.kRelayFluxTwoChannel);

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
        pidLineController = new PIDLineController(-0.08, 0, 0.0, pidLineError, pidLineOutput);
        pidLineController.setInputRange(-4, 4);
        pidLineController.setOutputRange(-1, 1);
        pidLineController.setSetpoint(0);
        pidLineController.enable();

        driveMode = 0; //0 = Tank; 1 = Arcade; 2 = Kaj; 3 = LineTrack
        armState = 2;//0,1,2 = retracted, middle, extended
        joyMode = 0;
        armFlip = 1; // Regular arm up/down
        fluxState = 0;
        autonomousStage = 0;//MUST GO EEEEVERYWHERE

        maxLowSpeed = 200;
        maxHighSpeed = 530;

        driveToggle = false;
        joyToggle = false;
        parkmode = false;
        rTriggerToggle = false;
        lTriggerToggle = false;
        shiftToggle = false;
        shoulderPID = false;
        towerDrive = false;

        maxSpeed = maxLowSpeed;
        prevLineError = 0.0;
        vToM = 0.38582677165354330708661417322835;
    }

    public void disabledInit() {
        autonomousStage = 0;
    }

    public void disabledPeriodic() {
        updateDS();
        autonomousStage = 0;
    }

    public void autonomousInit() {
        try {
            jagLeftMaster.changeControlMode(CANJaguar.ControlMode.kSpeed);
            jagRightMaster.changeControlMode(CANJaguar.ControlMode.kSpeed);
            jagLeftMaster.setPID(driveP, driveI, driveD);
            jagRightMaster.setPID(driveP, driveI, driveD);
            jagLeftMaster.enableControl();
            jagRightMaster.enableControl();
            jagShoulderOne.changeControlMode(CANJaguar.ControlMode.kPosition);
            jagShoulderOne.enableControl();
        } catch (CANTimeoutException ex) {
            System.out.println(ex.toString());
            canInitialized = false;
        }
        autonomousStage = 0;
        pidLineController.enable();
        try {
            jagShoulderOne.setX(0.414);
        } catch (CANTimeoutException ex) {
            System.out.println(ex.toString());
            canInitialized = false;
        }
        shoulderPID = true;
        autoTimer.reset();
        autoTimer.start();
    }

    public void autonomousPeriodic() {
        watchdog.feed();
        updateDS();
        if (autoTimer.get() > 3 && autoTimer.get() < 5) {
            vicGripperTop.set(-1);
            vicGripperBottom.set(1);
            solArmStageOneIn.set(true);
            solArmStageOneOut.set(false);
            solArmStageTwoIn.set(true);
            solArmStageTwoOut.set(false);
        } else {
            //vicGripperTop.set(0);
            //vicGripperBottom.set(0);
        }
    }

    public void autonomousContinuous() {

        syncSlaves();

        if (digLineLeft.get() && digLineMiddle.get() && digLineRight.get()) {
            pidLineError.lineError = prevLineError * 2;
        } else if (!digLineLeft.get() && !digLineMiddle.get() && !digLineRight.get()) {
            pidLineError.lineError = 0;
            prevLineError = 0;
        } else if (!digLineLeft.get() && digLineMiddle.get() && !digLineRight.get()) {
            pidLineError.lineError = 0;
            prevLineError = 0;
        } else if (digLineLeft.get() && !digLineMiddle.get() && digLineRight.get()) {
            pidLineError.lineError = 0;
            prevLineError = 0;
        } else if (!digLineLeft.get() && !digLineMiddle.get() && digLineRight.get()) {
            pidLineError.lineError = -1;
            prevLineError = -1;
        } else if (digLineLeft.get() && !digLineMiddle.get() && !digLineRight.get()) {
            pidLineError.lineError = 1;
            prevLineError = 1;
        } else if (!digLineLeft.get() && digLineMiddle.get() && digLineRight.get()) {
            pidLineError.lineError = -2;
            prevLineError = -2;
        } else if (digLineLeft.get() && digLineMiddle.get() && !digLineRight.get()) {
            pidLineError.lineError = 2;
            prevLineError = 2;
        }

        switch (autonomousStage) {
            case 0:
                if (anaUltraSonic.getVoltage() < 0.5 * vToM && autoTimer.get() > 5) {
                    autonomousStage = 1;
                }
                vicGripperTop.set(0);
                vicGripperBottom.set(0);
                fluxCapacitorOne.set(Relay.Value.kOff);
                fluxCapacitorTwo.set(Relay.Value.kReverse);
                try {
                    xInput = pidLineOutput.xValue;
                    yInput = -0.6;
                    octantJoystick();
                    jagLeftMaster.setX(maxSpeed * leftSpeed);
                    jagRightMaster.setX(maxSpeed * rightSpeed);
                } catch (CANTimeoutException ex) {
                    System.out.println(ex.toString());
                    canInitialized = false;
                }
                break;
            case 1:
                vicGripperTop.set(1);
                vicGripperBottom.set(1);
                fluxCapacitorOne.set(Relay.Value.kOff);
                fluxCapacitorTwo.set(Relay.Value.kForward);
                try {
                    xInput = 0;
                    yInput = 0.4;
                    octantJoystick();
                    jagLeftMaster.setX(maxSpeed * leftSpeed);
                    jagRightMaster.setX(maxSpeed * rightSpeed);
                } catch (CANTimeoutException ex) {
                    System.out.println(ex.toString());
                    canInitialized = false;
                }
                break;
        }
    }

    public void teleopInit() {
        try {
            jagLeftMaster.changeControlMode(CANJaguar.ControlMode.kSpeed);
            jagRightMaster.changeControlMode(CANJaguar.ControlMode.kSpeed);
            jagLeftMaster.setPID(driveP, driveI, driveD);
            jagRightMaster.setPID(driveP, driveI, driveD);
            jagLeftMaster.enableControl();
            jagRightMaster.enableControl();

            //jagLeftMaster.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
            //jagRightMaster.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
            //jagLeftMaster.disableControl();
            //jagRightMaster.disableControl();
        } catch (CANTimeoutException ex) {
            System.out.println(ex.toString());
            canInitialized = false;
        }
        autonomousStage = 0;
    }

    public void teleopPeriodic() {
        watchdog.feed(); //feed the watchdog
        autonomousStage = 0;
        /*
        //Check buttons & set shift - high is 8, low is 7
        if (joyDriver.getRawButton(5) && joyDriver.getRawButton(6)){
        solMinibotA.set(true);
        solMinibotB.set(true);
        }
        else
        {
        solMinibotA.set(false);
        solMinibotB.set(false);
        }
         */

         if(towerDrive)
        {
           try {
            jagLeftMaster.changeControlMode(CANJaguar.ControlMode.kPosition);
            jagRightMaster.changeControlMode(CANJaguar.ControlMode.kPosition);
            jagLeftMaster.setPID(tdriveP, tdriveI, tdriveD);
            jagRightMaster.setPID(tdriveP, tdriveI, tdriveD);
        } catch (CANTimeoutException ex) {
            System.out.println(ex.toString());
            canInitialized = false;
        }
        }
         if(Math.abs(joyDriver.getRawAxis(5)) >= 0.1||Math.abs(joyDriver.getRawAxis(6)) >= 0.1)towerDrive = true;
        if (joyDriver.getRawButton(5)) {
            tdriveP -= 0.001;
        }
        if (joyDriver.getRawButton(6)) {
            tdriveP += 0.001;
        }

        if (joyDriver.getRawButton(8)) {
            solShifterHigh.set(true);
            solShifterLow.set(false);
            maxSpeed = maxHighSpeed;
            shiftToggle = true;
            try {
                jagLeftMaster.disableControl();
                jagRightMaster.disableControl();
            } catch (CANTimeoutException ex) {
                System.out.println(ex.toString());
            }
        } else if (joyDriver.getRawButton(7)) {
            solShifterHigh.set(false);
            solShifterLow.set(true);
            maxSpeed = maxLowSpeed;
            shiftToggle = true;
            try {
                jagLeftMaster.disableControl();
                jagRightMaster.disableControl();
            } catch (CANTimeoutException ex) {
                System.out.println(ex.toString());
            }
        }
        /*
        if(joyDriver.getRawButton(10))
        {
        if(parkmode == false)parkmode = true;
        if(parkmode == true)parkmode = false;
        }
        if(parkmode == true){
        dsLCD.println(DriverStationLCD.Line.kMain6, 1, "Parkmode: " +parkmode + "     ");
        try{
        jagLeftMaster.changeControlMode(CANJaguar.ControlMode.kPosition);
        jagRightMaster.changeControlMode(CANJaguar.ControlMode.kPosition);
        jagLeftMaster.setX(0);
        jagRightMaster.setX(0);

        }
        catch(CANTimeoutException ex) {
        System.out.println(ex.toString());
        }
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
            for (int i = 1; i < 5; i++) {
                if (joyDriver.getRawButton(i)) {
                    fluxState = i - 1;
                }
            }
        }
        switch (fluxState) {
            case 0:
                fluxCapacitorOne.set(Relay.Value.kOff);
                fluxCapacitorTwo.set(Relay.Value.kReverse);
                break;
            case 1:
                fluxCapacitorOne.set(Relay.Value.kOff);
                fluxCapacitorTwo.set(Relay.Value.kOff);
                break;
            case 2:
                fluxCapacitorOne.set(Relay.Value.kOff);
                fluxCapacitorTwo.set(Relay.Value.kForward);
                break;
            case 3:
                fluxCapacitorOne.set(Relay.Value.kForward);
                fluxCapacitorTwo.set(Relay.Value.kOff);
                break;
        }

        // Map buttons to arm setpoints
        if (joyOperator.getRawButton(1)) {
            // Pickup Front
            //Competition: setpointVal = 0.1;
            try {
                jagShoulderOne.changeControlMode(CANJaguar.ControlMode.kPosition);
                jagShoulderOne.setPID(armP, armI, armD);
                jagShoulderOne.enableControl();
                jagShoulderOne.setX(0.11);
            } catch (CANTimeoutException ex) {
                System.out.println(ex.toString());
                canInitialized = false;
            }
            shoulderPID = true;
            armFlip = 1;
            armState = 2;
        }
        if (joyOperator.getRawButton(2)) {
            // Pickup Back
            //Competition: setpointVal = 0.88;
            try {
                jagShoulderOne.changeControlMode(CANJaguar.ControlMode.kPosition);
                jagShoulderOne.setPID(armP, armI, armD);
                jagShoulderOne.enableControl();
                jagShoulderOne.setX(0.86);
            } catch (CANTimeoutException ex) {
                System.out.println(ex.toString());
                canInitialized = false;
            }
            shoulderPID = true;
            armFlip = -1;
            armState = 2;
        }
        if (joyOperator.getRawButton(3)) {
            // Top Front
            try {
                jagShoulderOne.changeControlMode(CANJaguar.ControlMode.kPosition);
                jagShoulderOne.setPID(armP, armI, armD);
                jagShoulderOne.enableControl();
                jagShoulderOne.setX(0.414);
            } catch (CANTimeoutException ex) {
                System.out.println(ex.toString());
                canInitialized = false;
            }
            shoulderPID = true;
            armFlip = 1;

        }

        if (joyOperator.getRawButton(4)) {
            // Top Back
            try {
                jagShoulderOne.changeControlMode(CANJaguar.ControlMode.kPosition);
                jagShoulderOne.setPID(armP, armI, armD);
                jagShoulderOne.enableControl();
                jagShoulderOne.setX(0.585);
            } catch (CANTimeoutException ex) {
                System.out.println(ex.toString());
                canInitialized = false;
            }
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

        if (shoulderPID) {
            try {
                if (Math.abs(joyOperator.getRawAxis(5)) > 0.2) {
                    jagShoulderOne.disableControl();
                    jagShoulderOne.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
                    shoulderPID = false;
                }
            } catch (CANTimeoutException ex) {
                System.out.println(ex.toString());
                canInitialized = false;
            }
        } else {
            try {
                //Competition: if (jagShoulderOne.getPosition() < 0.1) {
                if (jagShoulderOne.getPosition() < 0.11) {
                    jagShoulderOne.setX(Math.min(0.0, armFlip * (joyOperator.getRawAxis(5)) * (joyOperator.getRawAxis(5)) * (joyOperator.getRawAxis(5))));
                    //Competition: } else if (jagShoulderOne.getPosition() > 0.9) {
                } else if (jagShoulderOne.getPosition() > 0.86) {
                    jagShoulderOne.setX(Math.max(0.0, armFlip * (joyOperator.getRawAxis(5)) * (joyOperator.getRawAxis(5)) * (joyOperator.getRawAxis(5))));
                } else {
                    jagShoulderOne.setX(armFlip * (joyOperator.getRawAxis(5)) * (joyOperator.getRawAxis(5)) * (joyOperator.getRawAxis(5)));
                }
            } catch (CANTimeoutException ex) {
                System.out.println(ex.toString());
                canInitialized = false;
            }

        }

        //Print drive mode to DS & send values to Jaguars
        switch (driveMode) {
            case 0:
                dsLCD.println(DriverStationLCD.Line.kMain6, 1,
                        "Drive mode: Tank  ");
                try {
                    jagRightMaster.setX(maxSpeed * (joyDriver.getRawAxis(2)));
                    jagLeftMaster.setX(maxSpeed * (joyDriver.getRawAxis(4)));
                    pidLineController.disable();
                } catch (CANTimeoutException ex) {
                    System.out.println(ex.toString());
                    canInitialized = false;
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
                    canInitialized = false;
                }
                break;
            case 2:
                dsLCD.println(DriverStationLCD.Line.kMain6, 1,
                        "Drive mode: Kaj   ");
                try {

                    xInput = joyDriver.getRawAxis(3);
                    yInput = -joyDriver.getRawAxis(2);
                    octantJoystick();
                    jagLeftMaster.setX(maxSpeed * leftSpeed);
                    jagRightMaster.setX(maxSpeed * rightSpeed);
                    pidLineController.disable();
                } catch (CANTimeoutException ex) {
                    System.out.println(ex.toString());
                    canInitialized = false;
                }
                break;

        }

        updateDS();
    }

    public void teleopContinuous() {
        syncSlaves();
        /*
        try{
        jagLeftMaster.changeControlMode(CANJaguar.ControlMode.kSpeed);
        jagRightMaster.changeControlMode(CANJaguar.ControlMode.kSpeed);
        jagLeftMaster.setPID(driveP, driveI, driveD);
        jagRightMaster.setPID(driveP, driveI, driveD);
        jagLeftMaster.enableControl();
        jagRightMaster.enableControl();
        }
        catch(CANTimeoutException ex)
        {
        System.out.println(ex.toString());
        }
         */
        if (shiftToggle && (joyDriver.getRawButton(7) || joyDriver.getRawButton(8))) {
            try {
                jagLeftSlave.setX(0);
                jagRightSlave.setX(0);
            } catch (CANTimeoutException ex) {
                System.out.println(ex.toString());
            }

        } else if (shiftToggle) {
            try {
                shiftToggle = false;
                jagLeftMaster.enableControl();
                jagRightMaster.enableControl();
            } catch (CANTimeoutException ex) {
                System.out.println(ex.toString());
                canInitialized = false;
            }
        }
        if (digLineLeft.get() && digLineMiddle.get() && digLineRight.get()) {
            pidLineError.lineError = prevLineError * 2;
        } else if (!digLineLeft.get() && !digLineMiddle.get() && !digLineRight.get()) {
            pidLineError.lineError = 0;
            prevLineError = 0;
        } else if (!digLineLeft.get() && digLineMiddle.get() && !digLineRight.get()) {
            pidLineError.lineError = 0;
            prevLineError = 0;
        } else if (digLineLeft.get() && !digLineMiddle.get() && digLineRight.get()) {
            pidLineError.lineError = 0;
            prevLineError = 0;
        } else if (!digLineLeft.get() && !digLineMiddle.get() && digLineRight.get()) {
            pidLineError.lineError = -1;
            prevLineError = -1;
        } else if (digLineLeft.get() && !digLineMiddle.get() && !digLineRight.get()) {
            pidLineError.lineError = 1;
            prevLineError = 1;
        } else if (!digLineLeft.get() && digLineMiddle.get() && digLineRight.get()) {
            pidLineError.lineError = -2;
            prevLineError = -2;
        } else if (digLineLeft.get() && digLineMiddle.get() && !digLineRight.get()) {
            pidLineError.lineError = 2;
            prevLineError = 2;
        }
        if (driveMode == 3) {
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
                canInitialized = false;
            }
        }

        if (!canInitialized) {
            try {
                jagLeftMaster.setSpeedReference(CANJaguar.SpeedReference.kEncoder);
                jagRightMaster.setSpeedReference(CANJaguar.SpeedReference.kEncoder);
                jagLeftMaster.configEncoderCodesPerRev(256);
                jagRightMaster.configEncoderCodesPerRev(256);
                jagLeftSlave.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
                jagRightSlave.changeControlMode(CANJaguar.ControlMode.kPercentVbus);

                jagShoulderOne.setPositionReference(CANJaguar.PositionReference.kPotentiometer);
                jagShoulderOne.configPotentiometerTurns(1);

                //TODO: Comp is
                // jagShoulderTwo.changeControlMode(CANJaguar.ControlMode.kPercentVbus);


                canInitialized = true;

            } catch (CANTimeoutException ex) {
                System.out.println(ex.toString());
                canInitialized = false;
            }
        }
    }

    /**
     * Used for converting to 'smart' arcade drive.
     * Sets leftSpeed and rightSpeed based on the x and y inputs (not
     * necessarily from joysticks as the name suggests).
     */
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

    /**
     * Synchronizes the 'slave' Jaguars with the 'master' Jaguars
     */
    public void syncSlaves() {
        try {
            jagLeftSlave.setX(jagLeftMaster.getOutputVoltage() / jagLeftMaster.getBusVoltage());
            jagRightSlave.setX(jagRightMaster.getOutputVoltage() / jagRightMaster.getBusVoltage());
            //TODO: Comp is setX
            jagShoulderTwo.set(jagShoulderOne.getOutputVoltage() / jagShoulderOne.getBusVoltage());


        } catch (CANTimeoutException ex) {
            System.out.println(ex.toString());
            canInitialized = false;


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

    /**
     * Sends diagnostic information to the driver station.
     * Line 1: Drive mode
     * Line 2: Left encoder speed
     * Line 3: Shoulder PID target
     * Line 4: Shoulder PID proportional error
     * Line 5: Shoulder potentiometer value
     * Line 6: Ultrasonic distance (in metres)
     */
    public void updateDS() {
        try {
            dsLCD.println(DriverStationLCD.Line.kUser2, 1, "Left Speed: " + jagLeftMaster.getSpeed() + "     ");
            dsLCD.println(DriverStationLCD.Line.kUser3, 1, "Right Speed: " + jagRightMaster.getSpeed() + "     ");
            dsLCD.println(DriverStationLCD.Line.kUser6, 1, "TPID P: " + tdriveP + "     ");


        } catch (CANTimeoutException ex) {
            System.out.println(ex.toString());
            canInitialized = false;


        }
        try {
            dsLCD.println(DriverStationLCD.Line.kUser4, 1, "zValue: " + jagShoulderOne.getOutputVoltage());
            dsLCD.println(DriverStationLCD.Line.kUser5, 1, " potValue: " + (jagShoulderOne.getPosition()));
            dsLCD.updateLCD();


        } catch (CANTimeoutException ex) {
            dsLCD.println(DriverStationLCD.Line.kUser5, 1, " p0tValue: problem");
            dsLCD.updateLCD();
            canInitialized = false;


        } //dsLCD.println(DriverStationLCD.Line.kUser6, 1, "USonic m: " + anaUltraSonic.getVoltage() / vToM);
        dsLCD.updateLCD();

    }
}
