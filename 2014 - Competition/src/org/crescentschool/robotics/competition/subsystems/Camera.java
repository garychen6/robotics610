/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.subsystems;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.camera.AxisCameraException;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.image.BinaryImage;
import edu.wpi.first.wpilibj.image.ColorImage;
import edu.wpi.first.wpilibj.image.NIVisionException;
import edu.wpi.first.wpilibj.image.ParticleAnalysisReport;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.util.Calendar;
import java.util.Date;
import org.crescentschool.robotics.competition.constants.ElectricalConstants;
import org.crescentschool.robotics.competition.constants.ImagingConstants;

/**
 *
 * @author ianlo
 */
public class Camera extends Subsystem {

    private AxisCamera camera;
    private int offset = 0;
    private AnalogChannel ultrasonic;
    private static Camera instance = null;
    private Relay ringLight;
    private int widthThreshold = 10;
    private double ultrasonicReading = 0;

    private Camera() {
        //Get the camera and save the reference.
        camera = AxisCamera.getInstance();
        ringLight = new Relay(ElectricalConstants.cameraRingLight);
        ultrasonic = new AnalogChannel(ElectricalConstants.ultrasonicChannel);
        ultrasonic.setAverageBits(8);

    }

    public static Camera getInstance() {

        if (instance == null) {
            instance = new Camera();
        }

        return instance;
    }

//    public double getUltrasonicInches() {
//        double newValue = ultrasonic.getAverageVoltage() * ElectricalConstants.ultrasonicVtoF;
//        return newValue;
//
//    }
    public void processCamera() {
        camera = AxisCamera.getInstance();

        //Create a particle analysis report for the particles
        ParticleAnalysisReport[] analysis = null;
        //Offset >0 is on the right, offset <0 is on the left.
        offset = 0;

        try {
            //Run if the camera has a new image
            if (camera.freshImage()) {

                //Get the image and save the width
                ColorImage colorImage = camera.getImage();
                int width = colorImage.getWidth();
                //Get the current time
                Date startTime1 = Calendar.getInstance().getTime();
                //Threshold the image using the HSl values that are tuned.

                BinaryImage binImage = colorImage.thresholdHSV(ImagingConstants.HThresholdMin, ImagingConstants.HThresholdMax, ImagingConstants.SThresholdMin, ImagingConstants.SThresholdMax, ImagingConstants.VThresholdMin, ImagingConstants.VThresholdMax);
                //Get the analysis of particles
                analysis = binImage.getOrderedParticleAnalysisReports(5);
                //Save the finishing time
                Date endTime1 = Calendar.getInstance().getTime();
                //Release the images from memory
                binImage.free();
                colorImage.free();
                //Save the time required to process the image.
                long time = endTime1.getTime() - startTime1.getTime();

                //If particles were found, 
                if (analysis != null && analysis.length > 0) {
                    //Count how many particles are on the left or right.
                    int rightArea = 0;
                    //Iterate through the particles
                    for (int i = 0; i < analysis.length; i++) {
                        
//                                                    System.out.println(i + " " + analysis[i].particleArea + " " + analysis[i].boundingRectWidth);

                        //If the particle area is more than 50
                        if (analysis[i].particleArea > 20 && analysis[i].boundingRectWidth > widthThreshold) {
                            //Add it to the left or the area  count
                            offset = -1;
                            i = analysis.length;
                            break;
                        } else if (analysis[i].particleArea > 20 && analysis[i].boundingRectWidth <= widthThreshold) {
                            offset = 1;
                        }
                    }

                    //If one side has more particles, set the offset to 1 or -1. If they're equal, set the offset to 0.


                } else {
//                    Display a message if no particles were found
//                    System.out.println("Goal not found");
                }

                SmartDashboard.putNumber("offset", offset);
            }

        } catch (AxisCameraException ex) {
            ex.printStackTrace();
        } catch (NIVisionException ex) {
            ex.printStackTrace();
        }


    }

    public void setRingLight(boolean on) {
        if (on) {
            ringLight.set(Relay.Value.kForward);
        } else {
            ringLight.set(Relay.Value.kOff);

        }
    }
    //runs processcamera and then returns the offset given out.

    public int getOffset(int widthThreshold) {
//        this.widthThreshold = widthThreshold;
        processCamera();
        return offset;
    }

    protected void initDefaultCommand() {
    }
}
