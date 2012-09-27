/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.templates;


import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.camera.*;
import edu.wpi.first.wpilibj.image.*;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class KitBotXII extends IterativeRobot {
    Jaguar red;
    Jaguar black;
    Encoder leftEncoder;
    Encoder rightEncoder;
    Joystick left;
    Joystick right;
    Joystick gamePad;
    Watchdog watchdog;
    DriverStationLCD dsLCD;
    DigitalInput photoreceptorL;
    DigitalInput photoreceptorM;
    DigitalInput photoreceptorR;
    DigitalInput doesNotExist;
    AxisCamera camera;
    int driveMode;
    boolean driveToggle;
    boolean cruiseControl;
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        black = new Jaguar(4,1);
        red = new Jaguar(4,2);
        leftEncoder = new Encoder(4,5);
        rightEncoder = new Encoder(6,7);
        left = new Joystick(1);
        right = new Joystick(2);
        gamePad = new Joystick(3);
        watchdog = Watchdog.getInstance();
        dsLCD = DriverStationLCD.getInstance();
        photoreceptorL = new DigitalInput(4,1);
        photoreceptorM = new DigitalInput(4,2);
        photoreceptorR = new DigitalInput(4,3);
        camera = AxisCamera.getInstance();
        driveMode = 0; //0 = Tank; 1 = Arcade; 2 = Kaj
        driveToggle = false;
        cruiseControl = false;


        camera.writeResolution(AxisCamera.ResolutionT.k160x120);
        camera.writeWhiteBalance(AxisCamera.WhiteBalanceT.hold);
        camera.writeExposureControl(AxisCamera.ExposureT.hold);
        camera.writeExposurePriority(AxisCamera.ExposurePriorityT.frameRate);

        leftEncoder.start();
        rightEncoder.start();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        red.set(1);
        black.set(1);
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopInit() {
        ColorImage image;
        try{
            image = camera.getImage();
            image.write("unedited.jpg");
            BinaryImage bImage = image.thresholdRGB(160, 255, 160, 255, 160, 255);
            bImage.write("whitemask.jpg");
            MonoImage mImage = image.getLuminancePlane();
            mImage.write("luminancePlane.jpg");
            image.free();
            bImage.free();
        } catch (NIVisionException e){
            System.out.println("Error retrieving image: NIVisionException");
            e.printStackTrace();
        } catch (AxisCameraException e){
            System.out.println("Error retrieving image: AxisCameraException");
            e.printStackTrace();
        }

    }
    public void teleopPeriodic() {
        watchdog.feed(); //feed the watchdog
        //Toggle drive mode
        if(!driveToggle && left.getRawButton(2)){
            driveMode = (driveMode + 1)%3;
            driveToggle = true;
        } else if(driveToggle && !left.getRawButton(2))driveToggle = false;

        //Print drive mode to DS & send values to Jaguars
        switch (driveMode){
            case 0:
                dsLCD.println(DriverStationLCD.Line.kMain6, 1, "Drive mode: Tank  ");
                red.set(left.getY() + gamePad.getY());
                black.set(-right.getY() - gamePad.getThrottle());
                break;
            case 1:
                dsLCD.println(DriverStationLCD.Line.kMain6, 1, "Drive mode: Arcade");
                red.set(right.getX() - right.getY());
                black.set(right.getX() + right.getY());
                break;
            case 2:
                dsLCD.println(DriverStationLCD.Line.kMain6, 1, "Drive mode: Kaj   ");
                red.set(left.getX() - right.getY());
                black.set(left.getX() + right.getY());
                break;
        }/**/
        dsLCD.println(DriverStationLCD.Line.kUser3, 1, "1" + photoreceptorL.get() + "2" + photoreceptorM.get() + "3" + photoreceptorR.get());
        dsLCD.println(DriverStationLCD.Line.kUser4, 1, "Encoder in 4,5: " + leftEncoder.get() + "    ");
        dsLCD.println(DriverStationLCD.Line.kUser5, 1, "Encoder in 6,7: " + rightEncoder.get() + "    ");


        dsLCD.updateLCD();

    }
}
