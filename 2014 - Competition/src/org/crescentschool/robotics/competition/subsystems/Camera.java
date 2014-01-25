/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.subsystems;

import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.camera.AxisCameraException;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.image.BinaryImage;
import edu.wpi.first.wpilibj.image.ColorImage;
import edu.wpi.first.wpilibj.image.NIVisionException;
import edu.wpi.first.wpilibj.image.ParticleAnalysisReport;
import java.util.Calendar;
import java.util.Date;
import org.crescentschool.robotics.competition.constants.ImagingConstants;

/**
 *
 * @author ianlo
 */
public class Camera extends Subsystem {

    private AxisCamera camera;
    private int offset = 0;

    private Camera() {
        //Get the camera and save the reference.
        camera = AxisCamera.getInstance();


    }

    public void processCamera() {
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
                    int leftParticles = 0;
                    int rightParticles = 0;
                    //Iterate through the particles
                    for (int i = 0; i < analysis.length; i++) {
                        //If the particle area is more than 50
                        if (analysis[i].particleArea > 50) {
                            //Add it to the left or the right particle count
                            if (analysis[i].center_mass_x - width / 2.0 > 0) {
                                rightParticles++;
                            } else {
                                leftParticles++;
                            }
                        }

                    }
                    //If one side has more particles, set the offset to 1 or -1. If they're equal, set the offset to 0.
                    if (rightParticles > leftParticles) {
                        offset = 1;
                    } else if (leftParticles > rightParticles) {
                        offset = -1;
                    } else {
                        offset = 0;
                    }

                } else {
//                    Display a message if no particles were found
//                    System.out.println("Goal not found");
                }
            }
        } catch (AxisCameraException ex) {
            ex.printStackTrace();
        } catch (NIVisionException ex) {
            ex.printStackTrace();
        }
    }
    //runs processcamera and then returns the offset given out.
    public int getOffset() {
        processCamera();
        return offset;
    }

    protected void initDefaultCommand() {
    }
}
