/*
 * Competition code for 2011 Team 610 - CoyoBot XII
 * Written by Patrick White, Warfa Jibril, and Mr. Lim
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.Watchdog;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.image.ColorImage;

public class CoyoBotXII extends IterativeRobot {

    //Camera
    AxisCamera camMinibot;
    ColorImage camImage;
    //Gyro
    Gyro gyro;
    //Drivetrain Motors
    CANJaguar jagLeftMaster, jagLeftSlave,
            jagRightMaster, jagRightSlave;
    //Shoulder Motors
    CANJaguar jagShoulderOne, jagShoulderTwo;
    //Victor jagShoulderTwo;
    //Gripper Treads
    Victor vicGripperTop, vicGripperBottom;
    //Flux Capacitors
    Relay fluxCapacitorOne, fluxCapacitorTwo;
    //Compressor
    Compressor compressor;
    //Shifters
    Solenoid solShifterHigh, solShifterLow;
    //Arm Extension
    Solenoid solArmStageOneIn, solArmStageOneOut;
    Solenoid solArmStageTwoIn, solArmStageTwoOut;
    //Minibot Deployment
    Solenoid solMinibotA;
    Solenoid solMinibotB;
    //Driver Station Joysticks
    Joystick joyDriver;
    Joystick joyOperator;
    //WOOF!
    Watchdog watchdog;
    //Driver Station Display
    DriverStationLCD dsLCD;
    //Ultrasonic
    AnalogChannel anaUltraSonic;
    //Line Follower PID
    PIDLineController pidLineController;
    PIDLineSource pidLineError;
    PIDLineOutput pidLineOutput;
    //Autonomous Timer
    Timer autoTimer = new Timer();
    //Drive Mode: 0 = Tank, 1 = Arcade, 2 = Kaj, 3 = Line Follow, 4 = Tower
    int driveMode;
    //Arm State: 2 = Retracted, 1 = Middle, 0 = Extended
    int armState;
    //Arm Flip: 1 = Up/Down for Front, -1 = Up/Down for Back
    int armFlip;
    //TODO: Figure out what lights FC#s represent
    //Flux Capacitor: 0 = ?, 1 = Off, 2 = ?, 3 = ?
    int fluxState;
    //Autonomous Program Stage
    int autonomousStage;
    //Arm Extend Trigger States
    int gripreleaseStage;
    //Gripper Scoring Stage
    boolean lTriggerToggle;
    boolean rTriggerToggle;
    //Shifting State: True = Shifting, False = Shift Complete
    boolean shiftToggle;
    //Should PID State: False = Manual Control, True = PID Preset
    boolean shoulderPID;
    //CAN Fault Detection + ReInitialization Flag
    boolean canInitialized;
    //Tower Drive Mode: True = Tower Drive, False = Normal Drive
    boolean towerDrive;
    //-1 to 1 speeds to send to left and right motors
    double leftSpeed, rightSpeed;
    //Cartesian movement inputs from joystick or autonomous
    double xInput, yInput;
    //Current robot max speed in RPMs
    double maxSpeed;
    //Robot max speed in Low Gear
    double maxLowSpeed;
    //Robot max speed in High Gear
    double maxHighSpeed;
    //Save last line follower error state
    double prevLineError;
    //Conversion from Volts to Metres
    double vToM;
    //Drivetrain PID constants (Speed Control)
    double driveP = 0.2;
    double driveI = 0.004;
    double driveD = 0.0;
    //Shoulder/Arm PID constants (Position Control)
    double armP = -800;
    double armI = 0;
    double armD = 0;
    //Drivetrain PID constants (Position Control for Tower Drive)
    double tdriveP = 300;
    double tdriveI = 0;
    double tdriveD = 0;
    //Wheel rotations from autonomous start to scoring peg
    double rotationsToGrid = 8.2;
    //Autonomous drive speed: 1 = Max Speed
    double speed = 0.7;
    //CAN Exception Counter
    int canFaults = 0;
    //Autonomouse Acceleration Control
    boolean autonAccel = false;
    double autonSpeed = 0;
    double autonLeftSpeed = 0;
    double autonRightSpeed = 0;
    double autonUltraFactor = 1;
    // Grip Scoring Shoulder Position
    double shoulderpos;
    boolean autonTubeRelease;
    int posSet;
    //Autonomous switch mode: 1 = Disabled; 2 = 1 tube (line); 3 = 1 tube (deadreckonin6); 4 = 2 tube left; 5 = 2 tube right;
    int autoSwitch = 2;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        //Watchdog and driver station
        watchdog = Watchdog.getInstance();
        dsLCD = DriverStationLCD.getInstance();
        //Camera Initialization

        camMinibot = AxisCamera.getInstance();
        camMinibot.writeResolution(AxisCamera.ResolutionT.k320x240);
        camMinibot.writeBrightness(0);
        camMinibot.writeWhiteBalance(AxisCamera.WhiteBalanceT.fixedIndoor);
        camMinibot.writeColorLevel(100);
        camMinibot.writeExposureControl(AxisCamera.ExposureT.flickerfree60);
        camMinibot.writeExposurePriority(AxisCamera.ExposurePriorityT.frameRate);
        camMinibot.writeCompression(20);
        camMinibot.writeRotation(AxisCamera.RotationT.k0);
        camMinibot.writeMaxFPS(30);

        //All Jaguars
        try {
            //Drivetrain
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
            jagLeftMaster.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
            jagRightMaster.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
            //Shoulder
            jagShoulderOne = new CANJaguar(ElectricalMap.kJaguarShoulderOne);
            jagShoulderOne.setPositionReference(CANJaguar.PositionReference.kPotentiometer);
            jagShoulderOne.configPotentiometerTurns(1);
            jagShoulderTwo = new CANJaguar(ElectricalMap.kJaguarShoulderTwo);
            jagShoulderTwo.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
            //jagShoulderTwo = new Victor(ElectricalMap.kJaguarShoulderTwo);
            //Flag Jaguars as successfully initialized
            canInitialized = true;
        } catch (CANTimeoutException ex) {
            System.out.println(ex.toString());
            //Flag Jaguars for reinitialization
            canInitialized = false;
        }
        //Gyro
        gyro = new Gyro(1);
        gyro.setSensitivity(0.007);
        //Compressor
        compressor = new Compressor(ElectricalMap.kCompressorPressureSwitchChannel, ElectricalMap.kCompressorRelayChannel);
        compressor.start();
        //Shifters
        solShifterHigh = new Solenoid(ElectricalMap.kSolenoidModulePort, ElectricalMap.kSolenoidHighChannel);
        solShifterLow = new Solenoid(ElectricalMap.kSolenoidModulePort, ElectricalMap.kSolenoidLowChannel);
        //Arm Extension
        solArmStageOneIn = new Solenoid(ElectricalMap.kSolenoidModulePort, 3);
        solArmStageOneOut = new Solenoid(ElectricalMap.kSolenoidModulePort, 4);
        solArmStageTwoIn = new Solenoid(ElectricalMap.kSolenoidModulePort, 5);
        solArmStageTwoOut = new Solenoid(ElectricalMap.kSolenoidModulePort, 6);
        //Minibot Deployment
        solMinibotA = new Solenoid(ElectricalMap.kSolenoidModulePort, ElectricalMap.kSolenoidMinibotA);
        solMinibotB = new Solenoid(ElectricalMap.kSolenoidModulePort, ElectricalMap.kSolenoidMinibotB);
        //Gripper Tread Victors
        vicGripperTop = new Victor(ElectricalMap.kVictorGripperTopChannel);
        vicGripperBottom = new Victor(ElectricalMap.kVictorGripperBottomChannel);
        //Flux Capacitor
        fluxCapacitorOne = new Relay(ElectricalMap.kRelayFluxOneChannel);
        fluxCapacitorTwo = new Relay(ElectricalMap.kRelayFluxTwoChannel);
        //Driver Station Joysticks
        joyDriver = new Joystick(ElectricalMap.kJoystickDriverPort);
        joyOperator = new Joystick(ElectricalMap.kJoystickOperatorPort);
        //Ultrasonic Sensor
        anaUltraSonic = new AnalogChannel(ElectricalMap.kUltrasonicChannel);
        anaUltraSonic.setAverageBits(10);
        //anaUltraSonic.setOversampleBits(4);
        //Line Follower PID
        pidLineError = new PIDLineSource();
        pidLineOutput = new PIDLineOutput();
        pidLineController = new PIDLineController(-0.08, 0, 0.0, pidLineError, pidLineOutput);
        pidLineController.setInputRange(-4, 4);
        pidLineController.setOutputRange(-1, 1);
        pidLineController.setSetpoint(0);
        pidLineController.enable();
        //Tank Mode
        driveMode = 0;
        //Retract Arm Extension
        armState = 2;
        //Initialize Arm Extention at armStage = 2 (Retracted)
        solArmStageOneIn.set(false);
        solArmStageOneOut.set(true);
        solArmStageTwoIn.set(false);
        solArmStageTwoOut.set(true);
        //Regular arm up/down (Front)
        armFlip = 1;
        //No Flux Capacitance
        fluxState = 0;
        //Reset Autonomous Mode
        autonomousStage = 0;
        // Release Scoring Stage
        gripreleaseStage = 0;
        // Set Position Once
        posSet = 0;
        // Run Auton Grip Release
        autonTubeRelease = false;
        //Max Robot Low Gear Speed = 200 RPM
        maxLowSpeed = 200;
        //Max Robot High Gear Speed = 530 RPM
        maxHighSpeed = 530;
        //Reset Joystick Button States
        rTriggerToggle = false;
        lTriggerToggle = false;
        shiftToggle = false;
        //Manual Shoulder Control
        shoulderPID = false;
        //Disable Tower Drive
        towerDrive = false;
        //Set max speed for Low Gear
        maxSpeed = maxLowSpeed;
        //Assume no Line Error
        prevLineError = 0.0;
        //Set Ultrasonic Volts to Metres conversion
        vToM = 0.38582677165354330708661417322835;
    }

    public void disabledInit() {
        //Pause any software PIDs
        pidLineController.disable();
    }

    public void disabledPeriodic() {
        watchdog.feed();
        //Update Cam
        //Update DS
        updateDS();
        for (int i = 1; i < 6; i++) {
            if (joyDriver.getRawButton(i)) {
                autoSwitch = i;
            }
        }
    }

    public void autonomousInit() {
        //Resume any software PIDs
        pidLineController.enable();
    }

    public void autonomousPeriodic() {
        watchdog.feed();
        //Update DS
        updateDS();
        //Monitor CAN for exceptions and reinitialize if needed
        checkCANauton();
        /*
        //Acceleration Control
        if (autonAccel) {
        try {
        if (autonLeftSpeed < autonSpeed) {
        autonLeftSpeed += 5;
        jagLeftMaster.setX(autonLeftSpeed);
        }
        if (autonLeftSpeed > autonSpeed) {
        autonLeftSpeed -= 5;
        jagLeftMaster.setX(autonLeftSpeed);
        }
        if (autonRightSpeed < autonSpeed) {
        autonRightSpeed += 5;
        jagRightMaster.setX(autonRightSpeed);
        }
        if (autonRightSpeed > autonSpeed) {
        autonRightSpeed -= 5;
        jagRightMaster.setX(autonRightSpeed);
        }
        } catch (CANTimeoutException ex) {
        System.out.println(ex.toString());
        canInitialized = false;
        }
        }*/
    }

    public void autonomousContinuous() {
        switch (autoSwitch) {
            case 1:
                //Nothing
                break;
            case 2:
                //1 tube, line
                switch (autonomousStage) {
                    case 0:
                        try {
                            jagLeftMaster.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
                            jagRightMaster.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
                            jagLeftMaster.enableControl();
                            jagRightMaster.enableControl();
                        } catch (CANTimeoutException ex) {
                            System.out.println(ex.toString());
                            canInitialized = false;
                        }
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
                            jagLeftMaster.setX(leftSpeed);//changed from speed to percentvbus, old was maxSpeed * leftSpeed
                            jagRightMaster.setX(rightSpeed);
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
                            jagLeftMaster.setX(leftSpeed);
                            jagRightMaster.setX(rightSpeed);
                        } catch (CANTimeoutException ex) {
                            System.out.println(ex.toString());
                            canInitialized = false;
                        }
                        break;
                }
                break;
            case 3:
                //1 tube, deadreckonin6
                switch (autonomousStage) {
                    case 0:
                        //Reset Autonomous Timer
                        autoTimer.reset();
                        autoTimer.start();
                        //Configure Jaguars
                        try {
                            gyro.reset();
                            jagLeftMaster.changeControlMode(CANJaguar.ControlMode.kPosition);
                            jagRightMaster.changeControlMode(CANJaguar.ControlMode.kPosition);
                            jagLeftMaster.enableControl(0);
                            jagRightMaster.enableControl(0);
                            jagLeftMaster.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
                            jagRightMaster.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
                            jagLeftMaster.enableControl();
                            jagRightMaster.enableControl();
                            jagShoulderOne.changeControlMode(CANJaguar.ControlMode.kPosition);
                            jagShoulderOne.setPID(armP, armI, armD);
                            jagShoulderOne.enableControl();
                            shoulderPID = true;

                            //Move arm up to top peg
                            jagShoulderOne.setX(0.414);
                            /*
                            //Drive forward (under acceleration control)
                            autonAccel = false;
                            autonSpeed = 0;
                            autonLeftSpeed = 0;
                            autonRightSpeed = 0;
                             */
                            autonomousStage = 1;
                        } catch (CANTimeoutException ex) {
                            System.out.println(ex.toString());
                            canInitialized = false;
                        }
                        break;
                    case 1:
                        if (anaUltraSonic.getVoltage() / vToM > 2.2) {
                            autonUltraFactor = 1;
                        } else {
                            autonUltraFactor = (1.0 / 1.5) * ((anaUltraSonic.getVoltage() / vToM) - 0.7);
                        }
                        try {
                            jagLeftMaster.setX(1.0 * autonUltraFactor - gyro.getAngle() / 40);
                            jagRightMaster.setX(1.0 * autonUltraFactor + gyro.getAngle() / 40);
                        } catch (CANTimeoutException ex) {
                            System.out.println(ex.toString());
                            canInitialized = false;
                        }
                        if (autoTimer.get() > 0.5) {
                            //Arm Pickup
                            solArmStageOneIn.set(false);
                            solArmStageOneOut.set(true);
                            solArmStageTwoIn.set(true);
                            solArmStageTwoOut.set(false);
                        }
                        //Check if we have driven far enough
                        try {
                            if (autoTimer.get() >= 2.0) {
                                if ((anaUltraSonic.getVoltage() / vToM) < 1.2) {
                                    jagLeftMaster.setX(0);
                                    jagRightMaster.setX(0);
                                    //Rotate tube
                                    vicGripperTop.set(-1);
                                    vicGripperBottom.set(1);
                                    autoTimer.reset();
                                    autoTimer.start();
                                    autonomousStage = 2;
                                }
                            }
                        } catch (CANTimeoutException ex) {
                            System.out.println(ex.toString());
                            canInitialized = false;
                        }
                        break;
                    case 2:
                        if (autoTimer.get() >= 1.2) {
                            //Spit tube
                            vicGripperTop.set(1);
                            vicGripperBottom.set(1);
                            //Fully Retracted
                            solArmStageOneIn.set(false);
                            solArmStageOneOut.set(true);
                            solArmStageTwoIn.set(false);
                            solArmStageTwoOut.set(true);
                        }
                        if (autoTimer.get() >= 2.0) {
                            autonomousStage = 3;
                        }
                        break;
                    case 3:
                        try {
                            //Swing arm back over to pick up from back
                            jagShoulderOne.setX(0.867);
                            armState = 1;
                        } catch (CANTimeoutException ex) {
                            System.out.println(ex.toString());
                            canInitialized = false;
                        }
                        break;
                }
                break;
            case 4:
                //2 tubes left - copypasta'd from 5, with signs in gyro commands
                switch (autonomousStage) {
                    case 0:
                        //Reset Autonomous Timer
                        autoTimer.reset();
                        autoTimer.start();
                        //Configure Jaguars
                        try {
                            gyro.reset();
                            jagLeftMaster.changeControlMode(CANJaguar.ControlMode.kPosition);
                            jagRightMaster.changeControlMode(CANJaguar.ControlMode.kPosition);
                            jagLeftMaster.enableControl(0);
                            jagRightMaster.enableControl(0);
                            jagLeftMaster.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
                            jagRightMaster.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
                            jagLeftMaster.enableControl();
                            jagRightMaster.enableControl();
                            jagShoulderOne.changeControlMode(CANJaguar.ControlMode.kPosition);
                            jagShoulderOne.setPID(armP, armI, armD);
                            jagShoulderOne.enableControl();
                            shoulderPID = true;

                            //Move arm up to top peg
                            jagShoulderOne.setX(0.414);
                            /*
                            //Drive forward (under acceleration control)
                            autonAccel = false;
                            autonSpeed = 0;
                            autonLeftSpeed = 0;
                            autonRightSpeed = 0;
                             */
                            autonomousStage = 1;
                        } catch (CANTimeoutException ex) {
                            System.out.println(ex.toString());
                            canInitialized = false;
                        }
                        break;
                    case 1:
                        if (anaUltraSonic.getVoltage() / vToM > 2.2) {
                            autonUltraFactor = 1;
                        } else {
                            autonUltraFactor = (1.0 / 1.5) * ((anaUltraSonic.getVoltage() / vToM) - 0.7);
                        }
                        try {
                            jagLeftMaster.setX(1.0 * autonUltraFactor - gyro.getAngle() / 40);
                            jagRightMaster.setX(1.0 * autonUltraFactor + gyro.getAngle() / 40);
                        } catch (CANTimeoutException ex) {
                            System.out.println(ex.toString());
                            canInitialized = false;
                        }
                        if (autoTimer.get() > 0.5) {
                            //Arm Pickup
                            solArmStageOneIn.set(false);
                            solArmStageOneOut.set(true);
                            solArmStageTwoIn.set(true);
                            solArmStageTwoOut.set(false);
                        }
                        //Check if we have driven far enough
                        try {
                            if (autoTimer.get() >= 2.0) {
                                if ((anaUltraSonic.getVoltage() / vToM) < 1.2) {
                                    jagLeftMaster.setX(0);
                                    jagRightMaster.setX(0);
                                    //Rotate tube
                                    vicGripperTop.set(-1);
                                    vicGripperBottom.set(1);
                                    autoTimer.reset();
                                    autoTimer.start();
                                    autonomousStage = 2;
                                }
                            }
                        } catch (CANTimeoutException ex) {
                            System.out.println(ex.toString());
                            canInitialized = false;
                        }
                        break;
                    case 2:
                        if (autoTimer.get() >= 1.2) {
                            //Spit tube
                            vicGripperTop.set(1);
                            vicGripperBottom.set(1);
                            //Fully Retracted
                            solArmStageOneIn.set(false);
                            solArmStageOneOut.set(true);
                            solArmStageTwoIn.set(false);
                            solArmStageTwoOut.set(true);
                        }
                        if (autoTimer.get() >= 2.0) {
                            autonomousStage = 3;
                        }
                        break;
                    case 3:
                        try {
                            if (jagRightMaster.getPosition() > 4.9) {
                                autonUltraFactor = 1;
                            } else {
                                autonUltraFactor = (1.0 / 2.7) * (jagRightMaster.getPosition() - 2.2);
                            }
                            //Drive backwards
                            if (autoTimer.get() >= 2.3) {
                                //Arm Pickup
                                solArmStageOneIn.set(false);
                                solArmStageOneOut.set(true);
                                solArmStageTwoIn.set(true);
                                solArmStageTwoOut.set(false);
                            }
                            //if (autoTimer.get() >= 4.6) {
                            jagLeftMaster.setX((-1.0 * autonUltraFactor - (gyro.getAngle() - 18) / 40));
                            jagRightMaster.setX((-1.0 * autonUltraFactor + (gyro.getAngle() - 18) / 40));
                            //}
                            //Check if we have driven back to original position
                            if (jagRightMaster.getPosition() <= 2.5) {
                                autonomousStage = 4;
                                autoTimer.reset();
                            }
                            //Swing arm back over to pick up from back
                            jagShoulderOne.setX(0.88);
                            //Suck gripper
                            vicGripperTop.set(-1);
                            vicGripperBottom.set(-1);
                            pidLineController.enable();
                        } catch (CANTimeoutException ex) {
                            System.out.println(ex.toString());
                            canInitialized = false;
                        }
                        break;
                    case 4:
                        try {
                            vicGripperTop.set(0);
                            vicGripperBottom.set(0);
                            jagShoulderOne.setX(0.414);
                            if (autoTimer.get() > 0.5 && autoTimer.get() < 2.0) {
                                //Extend arm
                                solArmStageOneIn.set(true);
                                solArmStageOneOut.set(false);
                                solArmStageTwoIn.set(true);
                                solArmStageTwoOut.set(false);
                                //Rotate Piece Back
                                vicGripperTop.set(1);
                                vicGripperBottom.set(-1);
                            }
                            if (anaUltraSonic.getVoltage() / vToM > 2.3) {
                                autonUltraFactor = 1;
                            } else {
                                autonUltraFactor = (1.0 / 1.5) * ((anaUltraSonic.getVoltage() / vToM) - 0.8);
                            }
                            /*
                            yInput = -autonUltraFactor;
                            xInput = pidLineOutput.xValue;
                            octantJoystick();
                            jagLeftMaster.setX(leftSpeed);
                            jagRightMaster.setX(rightSpeed);
                             */
                            jagLeftMaster.setX(1.0 * autonUltraFactor - (gyro.getAngle() + 10) / 40);
                            jagRightMaster.setX(1.0 * autonUltraFactor + (gyro.getAngle() + 10) / 40);
                            if (autoTimer.get() >= 2.0) {
                                if ((anaUltraSonic.getVoltage() / vToM) < 1.1) {
                                    //Rotate tube
                                    vicGripperTop.set(-1);
                                    vicGripperBottom.set(1);
                                    autoTimer.reset();
                                    autoTimer.start();
                                    jagLeftMaster.setX(0);
                                    jagRightMaster.setX(0);
                                    autonomousStage = 5;
                                    autoTimer.reset();
                                }
                            }
                        } catch (CANTimeoutException ex) {
                            System.out.println(ex.toString());
                            canInitialized = false;
                        }
                        break;
                    case 5:
                        if (autoTimer.get() >= 1.7) {
                            //Stop tube
                            vicGripperTop.set(0);
                            vicGripperBottom.set(0);
                            try {
                                //Swing arm back over to pick up from back
                                jagShoulderOne.setX(0.867);
                                armState = 1;
                            } catch (CANTimeoutException ex) {
                                System.out.println(ex.toString());
                                canInitialized = false;
                            }
                        } else if (autoTimer.get() >= 1.2) {
                            //Spit tube
                            vicGripperTop.set(1);
                            vicGripperBottom.set(1);
                            //Arm Pickup
                            solArmStageOneIn.set(false);
                            solArmStageOneOut.set(true);
                            solArmStageTwoIn.set(true);
                            solArmStageTwoOut.set(false);
                        }
                        break;
                }
                break;
            case 5:
                //2 tubes, right
                switch (autonomousStage) {
                    case 0:
                        //Reset Autonomous Timer
                        autoTimer.reset();
                        autoTimer.start();
                        //Configure Jaguars
                        try {
                            gyro.reset();
                            jagLeftMaster.changeControlMode(CANJaguar.ControlMode.kPosition);
                            jagRightMaster.changeControlMode(CANJaguar.ControlMode.kPosition);
                            jagLeftMaster.enableControl(0);
                            jagRightMaster.enableControl(0);
                            jagLeftMaster.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
                            jagRightMaster.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
                            jagLeftMaster.enableControl();
                            jagRightMaster.enableControl();
                            jagShoulderOne.changeControlMode(CANJaguar.ControlMode.kPosition);
                            jagShoulderOne.setPID(armP, armI, armD);
                            jagShoulderOne.enableControl();
                            shoulderPID = true;

                            //Move arm up to top peg
                            jagShoulderOne.setX(0.414);
                            /*
                            //Drive forward (under acceleration control)
                            autonAccel = false;
                            autonSpeed = 0;
                            autonLeftSpeed = 0;
                            autonRightSpeed = 0;
                             */
                            autonomousStage = 1;
                        } catch (CANTimeoutException ex) {
                            System.out.println(ex.toString());
                            canInitialized = false;
                        }
                        break;
                    case 1:
                        if (anaUltraSonic.getVoltage() / vToM > 2.2) {
                            autonUltraFactor = 1;
                        } else {
                            autonUltraFactor = (1.0 / 1.5) * ((anaUltraSonic.getVoltage() / vToM) - 0.7);
                        }
                        try {
                            jagLeftMaster.setX(1.0 * autonUltraFactor - gyro.getAngle() / 40);
                            jagRightMaster.setX(1.0 * autonUltraFactor + gyro.getAngle() / 40);
                        } catch (CANTimeoutException ex) {
                            System.out.println(ex.toString());
                            canInitialized = false;
                        }
                        if (autoTimer.get() > 0.5) {
                            //Arm Pickup
                            solArmStageOneIn.set(false);
                            solArmStageOneOut.set(true);
                            solArmStageTwoIn.set(true);
                            solArmStageTwoOut.set(false);
                        }
                        //Check if we have driven far enough
                        try {
                            if (autoTimer.get() >= 2.0) {
                                if ((anaUltraSonic.getVoltage() / vToM) < 1.2) {
                                    jagLeftMaster.setX(0);
                                    jagRightMaster.setX(0);
                                    //Rotate tube
                                    vicGripperTop.set(-1);
                                    vicGripperBottom.set(1);
                                    autoTimer.reset();
                                    autoTimer.start();
                                    autonomousStage = 2;
                                }
                            }
                        } catch (CANTimeoutException ex) {
                            System.out.println(ex.toString());
                            canInitialized = false;
                        }
                        break;
                    case 2:
                        if (autoTimer.get() >= 1.2) {
                            //Spit tube
                            vicGripperTop.set(1);
                            vicGripperBottom.set(1);
                            //Fully Retracted
                            solArmStageOneIn.set(false);
                            solArmStageOneOut.set(true);
                            solArmStageTwoIn.set(false);
                            solArmStageTwoOut.set(true);
                        }
                        if (autoTimer.get() >= 2.0) {
                            autonomousStage = 3;
                        }
                        break;
                    case 3:
                        try {
                            if (jagRightMaster.getPosition() > 4.9) {
                                autonUltraFactor = 1;
                            } else {
                                autonUltraFactor = (1.0 / 2.7) * (jagRightMaster.getPosition() - 2.2);
                            }
                            //Drive backwards
                            if (autoTimer.get() >= 2.3) {
                                //Arm Pickup
                                solArmStageOneIn.set(false);
                                solArmStageOneOut.set(true);
                                solArmStageTwoIn.set(true);
                                solArmStageTwoOut.set(false);
                            }
                            //if (autoTimer.get() >= 4.6) {
                            jagLeftMaster.setX((-1.0 * autonUltraFactor - (gyro.getAngle() + 18) / 40));
                            jagRightMaster.setX((-1.0 * autonUltraFactor + (gyro.getAngle() + 18) / 40));
                            //}
                            //Check if we have driven back to original position
                            if (jagRightMaster.getPosition() <= 2.5) {
                                autonomousStage = 4;
                                autoTimer.reset();
                            }
                            //Swing arm back over to pick up from back
                            jagShoulderOne.setX(0.88);
                            //Suck gripper
                            vicGripperTop.set(-1);
                            vicGripperBottom.set(-1);
                            pidLineController.enable();
                        } catch (CANTimeoutException ex) {
                            System.out.println(ex.toString());
                            canInitialized = false;
                        }
                        break;
                    case 4:
                        try {
                            vicGripperTop.set(0);
                            vicGripperBottom.set(0);
                            jagShoulderOne.setX(0.414);
                            if (autoTimer.get() > 0.5 && autoTimer.get() < 2.0) {
                                //Extend arm
                                solArmStageOneIn.set(true);
                                solArmStageOneOut.set(false);
                                solArmStageTwoIn.set(true);
                                solArmStageTwoOut.set(false);
                                //Rotate Piece Back
                                vicGripperTop.set(1);
                                vicGripperBottom.set(-1);
                            }
                            if (anaUltraSonic.getVoltage() / vToM > 2.3) {
                                autonUltraFactor = 1;
                            } else {
                                autonUltraFactor = (1.0 / 1.5) * ((anaUltraSonic.getVoltage() / vToM) - 0.8);
                            }
                            /*
                            yInput = -autonUltraFactor;
                            xInput = pidLineOutput.xValue;
                            octantJoystick();
                            jagLeftMaster.setX(leftSpeed);
                            jagRightMaster.setX(rightSpeed);
                             */
                            jagLeftMaster.setX(1.0 * autonUltraFactor - (gyro.getAngle() - 10) / 40);
                            jagRightMaster.setX(1.0 * autonUltraFactor + (gyro.getAngle() - 10) / 40);
                            if (autoTimer.get() >= 2.0) {
                                if ((anaUltraSonic.getVoltage() / vToM) < 1.1) {
                                    //Rotate tube
                                    vicGripperTop.set(-1);
                                    vicGripperBottom.set(1);
                                    autoTimer.reset();
                                    autoTimer.start();
                                    jagLeftMaster.setX(0);
                                    jagRightMaster.setX(0);
                                    autonomousStage = 5;
                                    autoTimer.reset();
                                }
                            }
                        } catch (CANTimeoutException ex) {
                            System.out.println(ex.toString());
                            canInitialized = false;
                        }
                        break;
                    case 5:
                        if (autoTimer.get() >= 1.7) {
                            //Stop tube
                            vicGripperTop.set(0);
                            vicGripperBottom.set(0);
                            try {
                                //Swing arm back over to pick up from back
                                jagShoulderOne.setX(0.867);
                                armState = 1;
                            } catch (CANTimeoutException ex) {
                                System.out.println(ex.toString());
                                canInitialized = false;
                            }
                        } else if (autoTimer.get() >= 1.2) {
                            //Spit tube
                            vicGripperTop.set(1);
                            vicGripperBottom.set(1);
                            //Arm Pickup
                            solArmStageOneIn.set(false);
                            solArmStageOneOut.set(true);
                            solArmStageTwoIn.set(true);
                            solArmStageTwoOut.set(false);
                        }
                        break;
                }
                break;
        }

        //Synchronize slave Jaguars with Master values
        syncSlaves();
    }

    public void checkCANauton() {
        //Check to see if CANExceptions occur, if so, reinitialize CAN
        if (!canInitialized) {
            canFaults++;
            try {
                //Drivetrain
                jagLeftMaster.setSpeedReference(CANJaguar.SpeedReference.kEncoder);
                jagRightMaster.setSpeedReference(CANJaguar.SpeedReference.kEncoder);
                jagLeftMaster.setPositionReference(CANJaguar.PositionReference.kQuadEncoder);
                jagRightMaster.setPositionReference(CANJaguar.PositionReference.kQuadEncoder);
                jagLeftMaster.configEncoderCodesPerRev(256);
                jagRightMaster.configEncoderCodesPerRev(256);
                jagLeftSlave.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
                jagRightSlave.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
                jagLeftMaster.changeControlMode(CANJaguar.ControlMode.kSpeed);
                jagRightMaster.changeControlMode(CANJaguar.ControlMode.kSpeed);
                jagLeftMaster.setPID(driveP, driveI, driveD);
                jagRightMaster.setPID(driveP, driveI, driveD);
                //Shoulder
                jagShoulderOne.setPositionReference(CANJaguar.PositionReference.kPotentiometer);
                jagShoulderOne.configPotentiometerTurns(1);
                jagShoulderTwo.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
                //Start various PIDs for autonomous mode
                jagLeftMaster.setPID(driveP, driveI, driveD);
                jagRightMaster.setPID(driveP, driveI, driveD);
                jagLeftMaster.enableControl();
                jagRightMaster.enableControl();
                jagShoulderOne.changeControlMode(CANJaguar.ControlMode.kPosition);
                jagShoulderOne.setPID(armP, armI, armD);
                jagShoulderOne.enableControl();
                shoulderPID = true;
                canInitialized = true;
            } catch (CANTimeoutException ex) {
                System.out.println(ex.toString());
                canInitialized = false;
            }
        }
    }

    public void teleopInit() {
        try {
            //Set Jaguar PIDs
            jagLeftMaster.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
            jagRightMaster.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
        } catch (CANTimeoutException ex) {
            System.out.println(ex.toString());
            canInitialized = false;
        }
        //Reset autonomous
        autonomousStage = 0;
    }

    public void teleopPeriodic() {
        watchdog.feed(); //feed the watchdog
        //Reset autonomous
        autonomousStage = 0;
        //Deploy minibot if both driver buttons 5+6 are pressed
        if (joyDriver.getRawButton(5) && joyDriver.getRawButton(6)) {
            solMinibotA.set(true);
            solMinibotB.set(true);
        } else {
            solMinibotA.set(false);
            solMinibotB.set(false);
        }
        //Switch to Tower Drive if D-pad buttons are pressed
        if (Math.abs(joyDriver.getRawAxis(5)) >= 0.05 || Math.abs(joyDriver.getRawAxis(6)) >= 0.05) {
            if (towerDrive == false) {
                towerDrive = true;
                driveMode = 4;
                xInput = 0;
                yInput = 0;
                try {
                    jagLeftMaster.changeControlMode(CANJaguar.ControlMode.kPosition);
                    jagRightMaster.changeControlMode(CANJaguar.ControlMode.kPosition);
                    jagLeftMaster.setX(0);
                    jagRightMaster.setX(0);
                    jagLeftMaster.setPID(-tdriveP, -tdriveI, -tdriveD);
                    jagRightMaster.setPID(tdriveP, tdriveI, tdriveD);
                    jagLeftMaster.enableControl(0);
                    jagRightMaster.enableControl(0);
                } catch (CANTimeoutException ex) {
                    System.out.println(ex.toString());
                    canInitialized = false;
                }
            }
        }
        //Switch to regular drive if sticks are moved
        if (Math.abs(joyDriver.getRawAxis(2)) >= 0.1 || Math.abs(joyDriver.getRawAxis(4)) >= 0.1) {
            if (towerDrive == true) {
                towerDrive = false;
                driveMode = 0;
                try {
                    jagLeftMaster.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
                    jagRightMaster.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
                } catch (CANTimeoutException ex) {
                    System.out.println(ex.toString());
                    canInitialized = false;
                }
            }
        }
        /* For PID Tuning Only
        if (joyDriver.getRawButton(5)) {
        driveI -= 0.001;
        }
        if (joyDriver.getRawButton(6)) {
        driveI += 0.001;
        }
         */
        //Shift to high gear when driver button 8 is pressed
        if (joyDriver.getRawButton(8)) {
            solShifterHigh.set(true);
            solShifterLow.set(false);
            maxSpeed = maxHighSpeed;
            shiftToggle = true;
        } else if (joyDriver.getRawButton(7)) {
            //Shift to low gear when driver button 7 is pressed
            solShifterHigh.set(false);
            solShifterLow.set(true);
            maxSpeed = maxLowSpeed;
            shiftToggle = true;
        }
        //Suck/Spit gripper when trigger
        if (joyOperator.getRawAxis(3) > .05 || joyOperator.getRawAxis(3) < -.05) {
            vicGripperTop.set(joyOperator.getRawAxis(3));
            vicGripperBottom.set(joyOperator.getRawAxis(3));
        } else {
            //Rotate piece in gripper based on which side arm is on
            if (armFlip == 1) {
                vicGripperTop.set(-1 * (joyOperator.getRawAxis(2)));
                vicGripperBottom.set(joyOperator.getRawAxis(2));
            } else {
                vicGripperTop.set((joyOperator.getRawAxis(2)));
                vicGripperBottom.set(-1 * (joyOperator.getRawAxis(2)));
            }
        }
        // Run automated gripper release if Austin presses analog stick
        if (joyOperator.getRawButton(10) && autonTubeRelease == false) {
            // Start automated gripper release
            autonTubeRelease = true;
            try {
                shoulderpos = jagShoulderOne.getPosition();
            } catch (CANTimeoutException ex) {
                System.out.println(ex.toString());
                canInitialized = false;
            }
            gripreleaseStage = 0;
        }
        if (autonTubeRelease == true) {
            switch (gripreleaseStage) {
                case 0:
                    // Push the tube out
                    vicGripperTop.set(-1);
                    vicGripperBottom.set(-1);
                    try {
                        //Move arm down 5 degrees
                        jagShoulderOne.setX(jagShoulderOne.getPosition() - 0.0138 * armFlip);
                        // Check if arm is down
                        if (jagShoulderOne.getPosition() <= shoulderpos - 0.013 || jagShoulderOne.getPosition() >= shoulderpos + 0.013) {  // If down 5 degrees switch to next case
                            gripreleaseStage = 1;
                            //start a timer
                            autoTimer.reset();
                            autoTimer.start();
                        }
                    } catch (CANTimeoutException ex) {
                        System.out.println(ex.toString());
                        canInitialized = false;
                    }
                    break;
                case 1:
                    // Still pushing tube out just in case
                    vicGripperTop.set(-1);
                    vicGripperBottom.set(-1);
                    // Retract once
                    solArmStageOneIn.set(false);
                    solArmStageOneOut.set(true);
                    solArmStageTwoIn.set(true);
                    solArmStageTwoOut.set(false);
                    // Stop the automated gripper release
                    //check timer if > 0.5 secs
                    if (autoTimer.get() > 0.5) {
                        autonTubeRelease = false;
                    }
                    break;
            }
        } else {
            //Map buttons to arm setpoints
            if (joyOperator.getRawButton(1)) {
                //Pickup Front
                try {
                    jagShoulderOne.changeControlMode(CANJaguar.ControlMode.kPosition);
                    jagShoulderOne.setPID(armP, armI, armD);
                    jagShoulderOne.enableControl();
                    jagShoulderOne.setX(0.08);
                    shoulderPID = true;
                    armFlip = 1;
                    armState = 2;
                } catch (CANTimeoutException ex) {
                    System.out.println(ex.toString());
                    canInitialized = false;
                }
            }
            if (joyOperator.getRawButton(2)) {
                //Pickup Back
                try {
                    jagShoulderOne.changeControlMode(CANJaguar.ControlMode.kPosition);
                    jagShoulderOne.setPID(armP, armI, armD);
                    jagShoulderOne.enableControl();
                    jagShoulderOne.setX(0.88);
                    shoulderPID = true;
                    armFlip = -1;
                    armState = 2;
                } catch (CANTimeoutException ex) {
                    System.out.println(ex.toString());
                    canInitialized = false;
                }
            }
            if (joyOperator.getRawButton(3)) {
                //Top Front
                try {
                    jagShoulderOne.changeControlMode(CANJaguar.ControlMode.kPosition);
                    jagShoulderOne.setPID(armP, armI, armD);
                    jagShoulderOne.enableControl();
                    jagShoulderOne.setX(0.414);
                    shoulderPID = true;
                    armFlip = 1;
                } catch (CANTimeoutException ex) {
                    System.out.println(ex.toString());
                    canInitialized = false;
                }
            }
            if (joyOperator.getRawButton(4)) {
                //Top Back
                try {
                    jagShoulderOne.changeControlMode(CANJaguar.ControlMode.kPosition);
                    jagShoulderOne.setPID(armP, armI, armD);
                    jagShoulderOne.enableControl();
                    jagShoulderOne.setX(0.585);
                    shoulderPID = true;
                    armFlip = -1;
                } catch (CANTimeoutException ex) {
                    System.out.println(ex.toString());
                    canInitialized = false;
                }
            }
            //Update Arm Extension States from operator buttons 5+6
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
            //Move Arm Extension to specified arm state
            //NOTE: comments are right, solenoid object names are wrong
            switch (armState) {
                case 0: //Fully Extended
                    solArmStageOneIn.set(true);
                    solArmStageOneOut.set(false);
                    solArmStageTwoIn.set(true);
                    solArmStageTwoOut.set(false);
                    break;
                case 1: //Pickup
                    solArmStageOneIn.set(false);
                    solArmStageOneOut.set(true);
                    solArmStageTwoIn.set(true);
                    solArmStageTwoOut.set(false);
                    break;
                case 2: //Fully Retracted
                    solArmStageOneIn.set(false);
                    solArmStageOneOut.set(true);
                    solArmStageTwoIn.set(false);
                    solArmStageTwoOut.set(true);
                    break;
            }
            //Disable shoulder PID if shoulder joystick is moved
            if (shoulderPID) {
                try {
                    if (Math.abs(joyOperator.getRawAxis(5)) > 0.05) {
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
                    //Shoulder joint soft limits
                    //Competition: if (jagShoulderOne.getPosition() < 0.1) {
                    if (jagShoulderOne.getPosition() < 0.08) {
                        jagShoulderOne.setX(Math.min(0.0, armFlip * (joyOperator.getRawAxis(5)) * (joyOperator.getRawAxis(5)) * (joyOperator.getRawAxis(5))));
                        //Competition: } else if (jagShoulderOne.getPosition() > 0.9) {
                    } else if (jagShoulderOne.getPosition() > 0.88) {
                        jagShoulderOne.setX(Math.max(0.0, armFlip * (joyOperator.getRawAxis(5)) * (joyOperator.getRawAxis(5)) * (joyOperator.getRawAxis(5))));
                    } else {
                        //Manual shoulder control
                        jagShoulderOne.setX(armFlip * (joyOperator.getRawAxis(5)) * (joyOperator.getRawAxis(5)) * (joyOperator.getRawAxis(5)));
                    }
                } catch (CANTimeoutException ex) {
                    System.out.println(ex.toString());
                    canInitialized = false;
                }
            }
        }

        //Update flux capacitor
        for (int i = 1; i < 5; i++) {
            if (joyDriver.getRawButton(i)) {
                fluxState = i - 1;
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

        //Check to see if a shift is in progress
        //If so, cut drivetrain power
        if (shiftToggle && (joyDriver.getRawButton(7) || joyDriver.getRawButton(8))) {
            try {
                jagLeftSlave.setX(0);
                jagRightSlave.setX(0);
            } catch (CANTimeoutException ex) {
                System.out.println(ex.toString());
                canInitialized = false;
            }
        } else if (shiftToggle) {
            //Check to see if shift has finished
            //If so, restore drivetrain power
            try {
                shiftToggle = false;
                jagLeftMaster.enableControl();
                jagRightMaster.enableControl();
            } catch (CANTimeoutException ex) {
                System.out.println(ex.toString());
                canInitialized = false;
            }
        } else {
            //Normal driving conditions
            switch (driveMode) {
                case 0:
                    //dsLCD.println(DriverStationLCD.Line.kMain6, 1, "Drive mode: Tank  ");
                    try {
                        //Tank mode
                        if (Math.abs(joyDriver.getRawAxis(2)) > 0.05) {
                            jagRightMaster.setX(joyDriver.getRawAxis(2) * joyDriver.getRawAxis(2) * joyDriver.getRawAxis(2));
                        } else {
                            jagRightMaster.setX(0);
                        }
                        if (Math.abs(joyDriver.getRawAxis(4)) > 0.05) {
                            jagLeftMaster.setX(joyDriver.getRawAxis(4) * joyDriver.getRawAxis(4) * joyDriver.getRawAxis(4));
                        } else {
                            jagLeftMaster.setX(0);
                        }
                    } catch (CANTimeoutException ex) {
                        System.out.println(ex.toString());
                        canInitialized = false;
                    }
                    break;
                case 1:
                    //dsLCD.println(DriverStationLCD.Line.kMain6, 1, "Drive mode: Arcade");
                    try {
                        //Arcade mode
                        xInput = joyDriver.getRawAxis(1) * convertJoy(joyDriver.getRawAxis(1), -joyDriver.getRawAxis(2));
                        yInput = joyDriver.getRawAxis(2) * convertJoy(joyDriver.getRawAxis(1), -joyDriver.getRawAxis(2));
                        octantJoystick();
                        jagLeftMaster.setX(leftSpeed);
                        jagRightMaster.setX(rightSpeed);
                    } catch (CANTimeoutException ex) {
                        System.out.println(ex.toString());
                        canInitialized = false;
                    }
                    break;
                case 2:
                    //dsLCD.println(DriverStationLCD.Line.kMain6, 1, "Drive mode: Kaj   ");
                    try {
                        //Kaj mode
                        xInput = joyDriver.getRawAxis(3) * convertJoy(joyDriver.getRawAxis(3), -joyDriver.getRawAxis(2));
                        yInput = -joyDriver.getRawAxis(2) * convertJoy(joyDriver.getRawAxis(3), -joyDriver.getRawAxis(2));
                        octantJoystick();
                        jagLeftMaster.setX(leftSpeed);
                        jagRightMaster.setX(rightSpeed);
                    } catch (CANTimeoutException ex) {
                        System.out.println(ex.toString());
                        canInitialized = false;
                    }
                    break;
                case 4:
                    //dsLCD.println(DriverStationLCD.Line.kMain6, 1, "Drive mode: Tower   ");
                    //Tower drive
                    xInput += 0.02 * joyDriver.getRawAxis(6);
                    yInput += 0.02 * joyDriver.getRawAxis(5);
                    try {
                        jagLeftMaster.setX(-yInput - xInput);
                        jagRightMaster.setX(-yInput + xInput);
                    } catch (CANTimeoutException ex) {
                        System.out.println(ex.toString());
                        canInitialized = false;
                    }
                    break;
            }
        }
        //Check for CAN Faults
        checkCANteleop();
        //Synchronize slave Jaguars
        syncSlaves();
        //Update DS
        updateDS();
    }

    public void teleopContinuous() {

        //Line follower drive mode
        if (driveMode == 3) {
            //dsLCD.println(DriverStationLCD.Line.kMain6, 1, "Drive mode: Line   ");
            try {
                //Make sure to enable pidLineController before calling me!
                //Follow the line!
                xInput = pidLineOutput.xValue;
                yInput = joyDriver.getRawAxis(2);
                leftSpeed = -yInput + xInput;
                rightSpeed = -yInput - xInput;
                jagLeftMaster.setX(maxSpeed * leftSpeed);
                jagRightMaster.setX(maxSpeed * rightSpeed);
            } catch (CANTimeoutException ex) {
                System.out.println(ex.toString());
                canInitialized = false;
            }
        }
    }

    public void checkCANteleop() {
        //Check to see if CANExceptions occur, if so, reinitialize CAN
        if (!canInitialized) {
            canFaults++;
            try {
                //Drivetrain
                jagLeftMaster.setSpeedReference(CANJaguar.SpeedReference.kEncoder);
                jagRightMaster.setSpeedReference(CANJaguar.SpeedReference.kEncoder);
                jagLeftMaster.setPositionReference(CANJaguar.PositionReference.kQuadEncoder);
                jagRightMaster.setPositionReference(CANJaguar.PositionReference.kQuadEncoder);
                jagLeftMaster.configEncoderCodesPerRev(256);
                jagRightMaster.configEncoderCodesPerRev(256);
                jagLeftSlave.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
                jagRightSlave.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
                /*jagLeftMaster.changeControlMode(CANJaguar.ControlMode.kSpeed);
                jagRightMaster.changeControlMode(CANJaguar.ControlMode.kSpeed);
                jagLeftMaster.setPID(driveP, driveI, driveD);
                jagRightMaster.setPID(driveP, driveI, driveD);
                jagLeftMaster.enableControl();
                jagRightMaster.enableControl();*/
                jagLeftMaster.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
                jagRightMaster.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
                towerDrive = false;
                driveMode = 0;
                //Shoulder
                jagShoulderOne.setPositionReference(CANJaguar.PositionReference.kPotentiometer);
                jagShoulderOne.configPotentiometerTurns(1);
                jagShoulderTwo.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
                //Start various PIDs for autonomous mode
                jagShoulderOne.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
                shoulderPID = false;
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

            jagShoulderTwo.setX(jagShoulderOne.getOutputVoltage() / jagShoulderOne.getBusVoltage());
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
     * Line 1: CAN Faults
     * Line 2: Left Encoder Position
     * Line 3: Right Encoder Position
     * Line 4: Left Encoder Speed
     * Line 5: Right Encoder Speed
     * Line 6: Ultrasonic Range in Metres
     */
    public void updateDS() {
        try {
            dsLCD.println(DriverStationLCD.Line.kUser2, 1, "Left Pos: " + jagLeftMaster.getPosition() + "          ");
            dsLCD.println(DriverStationLCD.Line.kUser3, 1, "Right Pos: " + jagRightMaster.getPosition() + "          ");
            dsLCD.println(DriverStationLCD.Line.kUser6, 1, "USonic m: " + anaUltraSonic.getVoltage() / vToM + "          ");
            //dsLCD.println(DriverStationLCD.Line.kUser6, 1, "Gyro: " + gyro.getAngle() + "          ");
            switch (autoSwitch) {
                case 1:
                    dsLCD.println(DriverStationLCD.Line.kUser6, 1, "Auto mode: " + "Disabled       ");
                    break;
                case 2:
                    dsLCD.println(DriverStationLCD.Line.kUser6, 1, "Auto mode: " + "1 tube, line   ");
                    break;
                case 3:
                    dsLCD.println(DriverStationLCD.Line.kUser6, 1, "Auto mode: " + "1 tube, d/r    ");
                    break;
                case 4:
                    dsLCD.println(DriverStationLCD.Line.kUser6, 1, "Auto mode: " + "2 tubes, l rack");
                    break;
                case 5:
                    dsLCD.println(DriverStationLCD.Line.kUser6, 1, "Auto mode: " + "2 tubes, r rack");
                    break;
            }
            dsLCD.println(DriverStationLCD.Line.kUser4, 1, "Left Speed: " + jagLeftMaster.getSpeed() + "          ");
            dsLCD.println(DriverStationLCD.Line.kUser5, 1, "Right Speed " + jagRightMaster.getSpeed() + "          ");
            //dsLCD.println(DriverStationLCD.Line.kMain6, 1, "CAN Faults: " + autonomousStage + "          ");
        } catch (CANTimeoutException ex) {
            System.out.println(ex.toString());
            canInitialized = false;
        }
        dsLCD.updateLCD();
    }
}
