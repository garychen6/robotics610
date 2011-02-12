/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package org.robotics.crescentschool;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.SimpleRobot;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.can.CANTimeoutException;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SimpleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Diagnostic extends SimpleRobot {

    Timer timer = new Timer();

    /**
     * This function is called once each time the robot enters autonomous mode.
     */
    public void autonomous() {
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6, 1, "Starting Diagnostic               ");
        DriverStationLCD.getInstance().updateLCD();
        timer.delay(5);
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6, 1, "Starting Jaguar Diagnostic              ");
        DriverStationLCD.getInstance().updateLCD();
        timer.delay(5);
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6, 1, "Starting Left Master                    ");
        DriverStationLCD.getInstance().updateLCD();
        timer.delay(5);
        try {
            CANJaguar testingJag = new CANJaguar(ElectricalMap.kJaguarLeftMaster);
            testingJag.setX(1);
            timer.delay(5);
            testingJag.setX(0);
        } catch (CANTimeoutException ex) {
            DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6, 1, "Problem Left Master                    ");
            DriverStationLCD.getInstance().updateLCD();
            timer.delay(5);
        }
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6, 1, "Starting Left Slave                        ");
        DriverStationLCD.getInstance().updateLCD();
        timer.delay(5);
        try {
            CANJaguar testingJag = new CANJaguar(ElectricalMap.kJaguarLeftSlave);
            testingJag.setX(1);
            timer.delay(5);
            testingJag.setX(0);
        } catch (CANTimeoutException ex) {
            DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6, 1, "Problem Left Slave                     ");
            DriverStationLCD.getInstance().updateLCD();
            timer.delay(5);
        }
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6, 1, "Starting Right Master                      ");
        DriverStationLCD.getInstance().updateLCD();
        timer.delay(5);
        try {
            CANJaguar testingJag = new CANJaguar(ElectricalMap.kJaguarRightMaster);
            testingJag.setX(1);
            timer.delay(5);
            testingJag.setX(0);
        } catch (CANTimeoutException ex) {
            DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6, 1, "Problem Right Master                    ");
            DriverStationLCD.getInstance().updateLCD();
            timer.delay(5);
        }
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6, 1, "Starting Right Slave                     ");
        DriverStationLCD.getInstance().updateLCD();
        timer.delay(5);
        try {
            CANJaguar testingJag = new CANJaguar(ElectricalMap.kJaguarRightSlave);
            testingJag.setX(1);
            timer.delay(5);
            testingJag.setX(0);
        } catch (CANTimeoutException ex) {
            DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6, 1, "Problem Right Slave                   ");
            DriverStationLCD.getInstance().updateLCD();
            timer.delay(5);
        }
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6, 1, "Starting Shoulder One                     ");
        DriverStationLCD.getInstance().updateLCD();
        timer.delay(5);
        try {
            CANJaguar testingJag = new CANJaguar(ElectricalMap.kJaguarShoulderOne);
            testingJag.setX(1);
            timer.delay(5);
            testingJag.setX(0);
        } catch (CANTimeoutException ex) {
            DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6, 1, "Problem Shoulder One                   ");
            DriverStationLCD.getInstance().updateLCD();
            timer.delay(5);
        }
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6, 1, "Starting Shoulder Two                     ");
        DriverStationLCD.getInstance().updateLCD();
        timer.delay(5);
        try {
            CANJaguar testingJag = new CANJaguar(ElectricalMap.kJaguarShoulderTwo);
            testingJag.setX(1);
            timer.delay(5);
            testingJag.setX(0);
        } catch (CANTimeoutException ex) {
            DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6, 1, "Problem Shoulder Two                   ");
            DriverStationLCD.getInstance().updateLCD();
            timer.delay(5);
        }
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6, 1, "Analog Module                     ");
        DriverStationLCD.getInstance().updateLCD();
        timer.delay(5);

        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6, 1, "Gyro                     ");
        DriverStationLCD.getInstance().updateLCD();
        timer.delay(5);
        Gyro gytroll = new Gyro(ElectricalMap.kGyroChannel);
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6, 1, "Gyro" + gytroll.getAngle());
        DriverStationLCD.getInstance().updateLCD();
        timer.delay(1);
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6, 1, "Gyro" + gytroll.getAngle());
        DriverStationLCD.getInstance().updateLCD();
        timer.delay(1);
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6, 1, "Gyro" + gytroll.getAngle());
        DriverStationLCD.getInstance().updateLCD();
        timer.delay(1);
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6, 1, "Gyro" + gytroll.getAngle());
        DriverStationLCD.getInstance().updateLCD();
        timer.delay(1);
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6, 1, "Gyro" + gytroll.getAngle());
        DriverStationLCD.getInstance().updateLCD();
        timer.delay(1);
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6, 1, "Gyro" + gytroll.getAngle());
        DriverStationLCD.getInstance().updateLCD();
        timer.delay(1);
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6, 1, "Gyro" + gytroll.getAngle());
        DriverStationLCD.getInstance().updateLCD();
        timer.delay(1);
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6, 1, "Gyro" + gytroll.getAngle());
        DriverStationLCD.getInstance().updateLCD();
        timer.delay(1);
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6, 1, "Gyro" + gytroll.getAngle());
        DriverStationLCD.getInstance().updateLCD();
        timer.delay(1);
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6, 1, "Gyro" + gytroll.getAngle());
        DriverStationLCD.getInstance().updateLCD();
        timer.delay(1);
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6, 1, "Ultrasonic                     ");
        DriverStationLCD.getInstance().updateLCD();
        timer.delay(5);
        AnalogChannel anaUltrasonic = new AnalogChannel(ElectricalMap.kUltrasonicChannel);
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6, 1, "Ultrasonic" + anaUltrasonic.getVoltage());
        DriverStationLCD.getInstance().updateLCD();
        timer.delay(1);
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6, 1, "Ultrasonic" + anaUltrasonic.getVoltage());
        DriverStationLCD.getInstance().updateLCD();
        timer.delay(1);
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6, 1, "Ultrasonic" + anaUltrasonic.getVoltage());
        DriverStationLCD.getInstance().updateLCD();
        timer.delay(1);
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6, 1, "Ultrasonic" + anaUltrasonic.getVoltage());
        DriverStationLCD.getInstance().updateLCD();
        timer.delay(1);
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6, 1, "Ultrasonic" + anaUltrasonic.getVoltage());
        DriverStationLCD.getInstance().updateLCD();
        timer.delay(1);
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6, 1, "Ultrasonic" + anaUltrasonic.getVoltage());
        DriverStationLCD.getInstance().updateLCD();
        timer.delay(1);
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6, 1, "Ultrasonic" + anaUltrasonic.getVoltage());
        DriverStationLCD.getInstance().updateLCD();
        timer.delay(1);
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6, 1, "Ultrasonic" + anaUltrasonic.getVoltage());
        DriverStationLCD.getInstance().updateLCD();
        timer.delay(1);
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6, 1, "Ultrasonic" + anaUltrasonic.getVoltage());
        DriverStationLCD.getInstance().updateLCD();
        timer.delay(1);
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6, 1, "Ultrasonic" + anaUltrasonic.getVoltage());
        DriverStationLCD.getInstance().updateLCD();
        timer.delay(1);
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6, 1, "SolenoidMiddlePort                     ");
        DriverStationLCD.getInstance().updateLCD();
        timer.delay(5);
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6, 1, "Solenoid High Channel                     ");
        DriverStationLCD.getInstance().updateLCD();
        timer.delay(5);
        Solenoid solShifterHigh = new Solenoid(ElectricalMap.kSolenoidModulePort, ElectricalMap.kSolenoidHighChannel);
        solShifterHigh.set(true);
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6, 1, "Solenoid High Channel True" + solShifterHigh.get());
        DriverStationLCD.getInstance().updateLCD();
        timer.delay(5);
        solShifterHigh.set(false);
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6, 1, "Solenoid High Channel False" + solShifterHigh.get());
        DriverStationLCD.getInstance().updateLCD();
        timer.delay(5);
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6, 1, "Solenoid High Channel                     ");
        DriverStationLCD.getInstance().updateLCD();
        timer.delay(5);
        Solenoid solShifterLow = new Solenoid(ElectricalMap.kSolenoidModulePort, ElectricalMap.kSolenoidLowChannel);
        solShifterLow.set(true);
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6, 1, "Solenoid Low Channel True" + solShifterLow.get());
        DriverStationLCD.getInstance().updateLCD();
        timer.delay(5);
        solShifterLow.set(false);
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6, 1, "Solenoid Low Channel False" + solShifterLow.get());
        DriverStationLCD.getInstance().updateLCD();
        timer.delay(5);
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6, 1, "Digital Modular Port                     ");
        DriverStationLCD.getInstance().updateLCD();
        timer.delay(5);
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6, 1, "Line Sensors                     ");
        DriverStationLCD.getInstance().updateLCD();
        timer.delay(5);
        DigitalInput digLineLeft = new DigitalInput(ElectricalMap.kLightSensorLChannel);
        DigitalInput digLineMiddle = new DigitalInput(ElectricalMap.kLightSensorMChannel);
        DigitalInput digLineRight = new DigitalInput(ElectricalMap.kLightSensorRChannel);
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6, 1, "Light Sensor Left " + digLineLeft.get());
        DriverStationLCD.getInstance().updateLCD();
        timer.delay(1);
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6, 1, "Light Sensor Left " + digLineLeft.get());
        DriverStationLCD.getInstance().updateLCD();
        timer.delay(1);
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6, 1, "Light Sensor Left " + digLineLeft.get());
        DriverStationLCD.getInstance().updateLCD();
        timer.delay(1);
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6, 1, "Light Sensor Left " + digLineLeft.get());
        DriverStationLCD.getInstance().updateLCD();
        timer.delay(1);
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6, 1, "Light Sensor Left " + digLineLeft.get());
        DriverStationLCD.getInstance().updateLCD();
        timer.delay(1);
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6, 1, "Light Sensor Left " + digLineLeft.get());
        DriverStationLCD.getInstance().updateLCD();
        timer.delay(1);
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6, 1, "Light Sensor Middle " + digLineMiddle.get());
        DriverStationLCD.getInstance().updateLCD();
        timer.delay(1);
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6, 1, "Light Sensor Middle " + digLineMiddle.get());
        DriverStationLCD.getInstance().updateLCD();
        timer.delay(1);
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6, 1, "Light Sensor Middle " + digLineMiddle.get());
        DriverStationLCD.getInstance().updateLCD();
        timer.delay(1);
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6, 1, "Light Sensor Middle " + digLineMiddle.get());
        DriverStationLCD.getInstance().updateLCD();
        timer.delay(1);
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6, 1, "Light Sensor Middle " + digLineMiddle.get());
        DriverStationLCD.getInstance().updateLCD();
        timer.delay(1);
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6, 1, "Light Sensor Right " + digLineRight.get());
        DriverStationLCD.getInstance().updateLCD();
        timer.delay(1);
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6, 1, "Light Sensor Right" + digLineRight.get());
        DriverStationLCD.getInstance().updateLCD();
        timer.delay(1);
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6, 1, "Light Sensor Right" + digLineRight.get());
        DriverStationLCD.getInstance().updateLCD();
        timer.delay(1);
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6, 1, "Light Sensor Right" + digLineRight.get());
        DriverStationLCD.getInstance().updateLCD();
        timer.delay(1);
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6, 1, "Light Sensor Right" + digLineRight.get());
        DriverStationLCD.getInstance().updateLCD();
        timer.delay(1);
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6, 1, "Victor Gripper Top                     ");
        DriverStationLCD.getInstance().updateLCD();
        timer.delay(5);
        Victor vicGripperTop = new Victor(ElectricalMap.kVictorGripperTopChannel);
        vicGripperTop.set(1);
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6, 1, "VictorGripperTop +1" + vicGripperTop.get());
        DriverStationLCD.getInstance().updateLCD();
        timer.delay(5);
        vicGripperTop.set(-1);
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6, 1, "VictorGripperTop -1" + vicGripperTop.get());
        DriverStationLCD.getInstance().updateLCD();
        timer.delay(5);
        Victor vicGripperBottom = new Victor(ElectricalMap.kVictorGripperBottomChannel);
        vicGripperBottom.set(1);
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6, 1, "VictorGripperBottom +1" + vicGripperBottom.get());
        DriverStationLCD.getInstance().updateLCD();
        timer.delay(5);
        vicGripperBottom.set(-1);
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6, 1, "VictorGripperBottom -1" + vicGripperBottom.get());
        DriverStationLCD.getInstance().updateLCD();
        timer.delay(5);
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6, 1, "Compressor                    ");
        DriverStationLCD.getInstance().updateLCD();
        timer.delay(5);
        Compressor compressor = new Compressor(ElectricalMap.kCompressorPressureSwitchChannel, ElectricalMap.kCompressorRelayChannel);
        compressor.start();
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6, 1, "Compressor Start" + compressor.getPressureSwitchValue());
        DriverStationLCD.getInstance().updateLCD();
        timer.delay(5);
        compressor.stop();
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6, 1, "Compressor Stop" + compressor.getPressureSwitchValue());
        DriverStationLCD.getInstance().updateLCD();
        timer.delay(5);
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6, 1, "Diagnostic Complete                ");
        DriverStationLCD.getInstance().updateLCD();
        timer.delay(5);




    }

    /**
     * This function is called once each time the robot enters operator control.
     */
    public void operatorControl() {
    }
}
