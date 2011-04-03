 /*
 * This class contains a number of constants of electrical slots and channels
 * to be referred to instead of in the code - this should directly reflect the
 * electrcial map found at http://projects.zoho.com/portal/dmorrison33 under
 * Wiki - [Electrical] Competition Wiring Map
 */
package edu.wpi.first.wpilibj.templates;

public class ElectricalMap {

    //Analog Module
    public static final int kAnalogModulePort = 1;
    //Gyro
    public static final int kGyroChannel = 1;
    //Accelerometer
    public static final int kAccelerometerChannelA = 2;
    public static final int kAccelerometerChannelB = 3;
    //Ultrasonic
    public static final int kUltrasonicChannel = 4;
    //Solenoid Module
    public static final int kSolenoidModulePort = 7;
    //Shifter
    public static final int kSolenoidLowChannel = 2;
    public static final int kSolenoidHighChannel = 1;
    //Minibot Deployment
    public static final int kSolenoidMinibotA = 7;
    public static final int kSolenoidMinibotB = 8;//TODO: NO
    //Gripper Release
    public static final int kSolenoidGripperChannel = 8;
    //Digital Sidecar
    public static final int kDigitalModulePort = 4;
    //Line Follower Light Sensors
    public static final int kLightSensorRChannel = 2;
    public static final int kLightSensorMChannel = 3;
    public static final int kLightSensorLChannel = 4;
    //Gripper Treads
    public static final int kVictorGripperTopChannel = 5;
    public static final int kVictorGripperBottomChannel = 6;
    //Flux Capacitor
    public static final int kRelayFluxOneChannel = 2;
    public static final int kRelayFluxTwoChannel = 3;
    //Compressor
    public static final int kCompressorRelayChannel = 1;
    public static final int kCompressorPressureSwitchChannel = 1;
    //CAN Adapters
    //Drivetrain Motors
    public static final int kJaguarRightMaster = 2;
    public static final int kJaguarRightSlave = 1;
    public static final int kJaguarLeftMaster = 4;
    public static final int kJaguarLeftSlave = 3;
    //Shoulder Motors
    public static final int kJaguarShoulderOne = 7;
    public static final int kJaguarShoulderTwo = 8;
    //Driver Station
    public static final int kJoystickDriverPort = 1;
    public static final int kJoystickOperatorPort = 2;
}
