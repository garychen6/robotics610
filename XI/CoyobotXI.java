/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.crescentschool.robotics.coyobotxi;


import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.camera.*;
import org.crescentschool.robotics.coyobotxi.util.ElectricalMap;
//import edu.wpi.first.wpilibj.image.*;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class CoyobotXI extends IterativeRobot {

    private static Joystick leftStick, rightStick;
    private static Joystick gamePad;
    private static Jaguar rightRear, rightFront, leftRear, leftFront;
    private static Victor frontRoller;
    private static Compressor compressor;
    private static DigitalInput leftsensor,rightsensor;
    private static Gyro gyro;
    private static AxisCamera camera;
    private static Encoder leftEncoder, rightEncoder;
    private static Solenoid lock, load, footAdjust, pushDown, pushUp;
    private static DriverStationLCD driverstation;
    private boolean button2 = false;
    private static double timer;
    private static boolean primed = false;
    private static boolean loaded;
    private static int leftVal=-1;
    private static int rightVal=-1;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public int timesThree(int number){
        int result;
        result = number * 3;
        return result;
    }
    public void robotInit() {
        camera = AxisCamera.getInstance();
        camera.writeResolution(AxisCamera.ResolutionT.k320x240);
        camera.writeWhiteBalance(AxisCamera.WhiteBalanceT.automatic);
        /*
        Timer.delay(2);
        try{
        ColorImage im=camera.getImage();
        im.write("C:/testimage.jpg");
        }
        catch(NIVisionException e) {
            e.printStackTrace();
        }
        catch(AxisCameraException e) {
            e.printStackTrace();
        }
   
   */
        leftStick = new Joystick(ElectricalMap.kLeftJoystickPort);
        rightStick = new Joystick(ElectricalMap.kRightJoystickPort);
        gamePad = new Joystick(ElectricalMap.kGamePadPort);

        rightRear = new Jaguar(ElectricalMap.kRearRightJaguar);
        rightFront = new Jaguar(ElectricalMap.kFrontRightJaguar);
        leftRear = new Jaguar(ElectricalMap.kRearLeftJaguar);
        leftFront = new Jaguar(ElectricalMap.kFrontLeftJaguar);

        leftsensor=new DigitalInput(ElectricalMap.kLeftLightSensor);
        rightsensor=new DigitalInput(ElectricalMap.kRightLightSensor);

        frontRoller = new Victor(ElectricalMap.kRollerVictor);

        compressor = new Compressor(ElectricalMap.kPressureSwitch, ElectricalMap.kCompressorChannel);

        gyro = new Gyro(ElectricalMap.kGyroChannel);

        driverstation = DriverStationLCD.getInstance();

        leftEncoder = new Encoder(ElectricalMap.kLeftAEncoder, ElectricalMap.kLeftBEncoder);
        rightEncoder = new Encoder(ElectricalMap.kRightAEncoder, ElectricalMap.kRightBEncoder);
        leftEncoder.setDistancePerPulse(0.008749);
        rightEncoder.setDistancePerPulse(0.008749);
        lock = new Solenoid(ElectricalMap.kLockSolenoid);
        load = new Solenoid(ElectricalMap.kLoadSolenoid);
        footAdjust = new Solenoid(ElectricalMap.kFootAdjustSolenoid);
        pushDown = new Solenoid(ElectricalMap.kPushDownSolenoid);
        pushUp = new Solenoid(ElectricalMap.kPushUpSolenoid);

        loaded = false;
    }

    public void autonomousInit() {
        getWatchdog().setEnabled(false);
        leftEncoder.reset();
        rightEncoder.reset();
        leftEncoder.start();
        rightEncoder.start();
        leftEncoder.setReverseDirection(true);
        gyro.reset();
        gyro.setSensitivity(0.00647);
       // autonomous_timer.reset();
        //autonomous_timer.start();

    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        System.out.println("left value: "+leftsensor.get()+ "\n"+"right value: "+rightsensor.get());
        driverstation.println(DriverStationLCD.Line.kMain6, 1, "Left encoder: "+leftEncoder.getDistance());
        driverstation.println(DriverStationLCD.Line.kUser2, 1, "Right encoder: "+rightEncoder.getDistance());
        driverstation.println(DriverStationLCD.Line.kUser3, 1, "Left gamepad button 6: "+gamePad.getRawButton(6));
        driverstation.updateLCD();
        /*
        leftFront.set(-1*leftEncoder.getDistance()/0.08);
        leftRear.set(-1*leftEncoder.getDistance()/0.08);
        rightFront.set(rightEncoder.getDistance()/0.08);
        rightRear.set(rightEncoder.getDistance()/0.08);

         /*
        System.out.println(gyro.getAngle());

        if (autonomous_timer.get() < 1e6) {
        
        leftFront.set(1);
        leftRear.set(1);
        rightFront.set(1);
        rightRear.set(1);
        }
        if (autonomous_timer.get()>1e6 && autonomous_timer.get() < 1.1e6) {
            
             
        leftFront.set(-0.8);
        leftRear.set(-0.8);
        rightFront.set(0.8);
        rightRear.set(0.8);
        }
        if (autonomous_timer.get() > 1.2e6) {
            if (camera.freshImage()) {
                
                EllipseMatch2 matches=FindBall.find(camera.getImage());
                tot_matches.add(matches[0]);
            }
            
            
        }
        if (gyro.getAngle() < -90) {
            start camera drive thread;
        }
        drivetrain.drive(1,0);
        Timer.delay(1);
        drivetrain.drive(0,0);
        drivetrain.drive(0.8,-0.8);
        Process.start();


        drive 1 second;
        turn 90 left;

        getbestmatch() in the meanwhile;
        get angle of best match, turn towards it;
        update drive angle until distance gets to threshold "x";
        drive blind;
        when light sensor on {
            if (far field) turn to 0 degrees, high kick ball;
            if (near field) turn, search for target, kick ball;
        }
        */


    }
    public static void standstill() {

       leftFront.set((leftEncoder.get()-leftVal)/10000.0);
       rightFront.set((rightEncoder.get()-rightVal)/10000.0);
    }
    public void teleopInit() {
        getWatchdog().setEnabled(true);
        compressor.start();
        frontRoller.set(0);
        timer = 0;
        primed = false;
    }


    public void teleopPeriodic() {
        System.out.println("left value: "+leftsensor.get()+ "\n"+"right value: "+rightsensor.get());
        getWatchdog().feed();
        timer += 0.05;
        if(!loaded) { 
            loadKicker();
            loaded = true;
        }
        if ((leftStick.getTrigger() || gamePad.getRawButton(5)) && primed) { // Low kick
            footAdjust.set(false);
            timer = 0;
            primed = false;
        } else if ((rightStick.getTrigger() || gamePad.getRawButton(6)) && primed){ //High kick
            frontRoller.set(0);
            footAdjust.set(true);
            timer = 0;
            primed = false;
        }
        fire();
        //COMPRESSOR
        if(!compressor.getPressureSwitchValue()) compressor.start();
        else if(compressor.getPressureSwitchValue()) compressor.stop();
        //TANK DRIVE
        double left = leftStick.getY();
        double right = rightStick.getY();
        if(leftStick.getRawButton(5)) {//WE WANT WHEELS TO BE IN HIGHEST POSITION
            pushUp.set(true);
            pushDown.set(false);
        }
        if(leftStick.getRawButton(3)) {//WE WANT WHEELS TO BE IN MIDDLE POSITION
            pushUp.set(false);
            pushDown.set(false);
        }
        if (leftStick.getRawButton(4)) {//WE WANT WHEELS TO BE IN LOWEST POSITION
            pushUp.set(false);
            pushDown.set(true);
        }
        if (leftStick.getRawButton(2)) {
            if (leftVal!=-1) leftVal=leftEncoder.get();
            if (rightVal!=-1) rightVal=rightEncoder.get();
        }
        if(ElectricalMap.kInvertedSide.equals("RIGHT")) {
            right *= -1;
        }
        else if(ElectricalMap.kInvertedSide.equals("LEFT")){
            left *= -1;
        }
        //DRIVE
        leftFront.set(left);
        leftRear.set(left);
        rightFront.set(right);
        rightRear.set(right);
        //DIAGNOSTICS
        //SEND NEW MESSAGES
        driverstation.updateLCD();
        //BUTTON 2 ON RIGHT STICK TOGGLES ROLLER
        if((rightStick.getRawButton(2) || gamePad.getRawButton(2)) && !button2){
            if(frontRoller.get() == 0){
                frontRoller.set(-1);
            } else {
                frontRoller.set(0);
            }
            button2 = true;
        } else if (!rightStick.getRawButton(2) && gamePad.getRawButton(2)){
            button2 = false;
        }
       // System.out.println("Lightsensor value: "+lightsensor.getVoltage());
    }


    public void fire(){
        if(timer <= 0.2) lock.set(true);
        if(timer >= 2.4 && timer <= 2.6) load.set(false);
        if(timer >= 3.4 && timer <= 3.5) lock.set(false);
        if(timer >= 3.6){
            load.set(true);
            primed = true;
        }
    }

    public void loadKicker() {
        //if(timer <= 0.2) lock.set(true);
        if(timer >= 2.4 && timer <= 2.6) load.set(false);
        if(timer >= 3.4 && timer <= 3.5) lock.set(false);
        if(timer >= 3.6){
            load.set(true);
            primed = true;
        }
    }
}