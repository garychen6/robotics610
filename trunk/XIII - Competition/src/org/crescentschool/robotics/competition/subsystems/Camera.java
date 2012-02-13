/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.camera.AxisCameraException;
import edu.wpi.first.wpilibj.image.BinaryImage;
import edu.wpi.first.wpilibj.image.ColorImage;
import edu.wpi.first.wpilibj.image.NIVisionException;
import edu.wpi.first.wpilibj.image.ParticleAnalysisReport;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.crescentschool.robotics.competition.constants.ImagingConstants;

/**
 *
 * @author Warfa, Patrick, Mr. Lim
 */
public class Camera extends Subsystem {

    private ColorImage colorImage;
    private ParticleAnalysisReport s_particles[];
    private static Camera instance = null;
    private static AxisCamera camera = AxisCamera.getInstance();
    private double xOffset = 0;
    private ParticleAnalysisReport topTarget = null;

    private Camera() {
        resetCamera();
    }

    /**
     * Ensures only one camera is instantiated.
     * @return The singleton camera instance.
     */
    public static Camera getInstance() {
        if (instance == null) {
            instance = new Camera();
        }
        return instance;
    }

    /**
     * Returns the top target in Particle Analysis Report form.
     * @return the particle analysis report of the top target
     */
    public ParticleAnalysisReport getTopTarget(){
        return topTarget;
    }
    
    /**
     * Gets the normalized position for the top square on the net.
     * @return The normalized position for the top square as a value between -1 and 1.
     */
    public double getX() {

        return xOffset;
    }

    /**
     * Updates the camera image if there is a new image. Identifies reflective rectangles. Calculates X offset of highest rectangle.
     */
    public void processCamera() {


        if (camera.freshImage()) {
            try {
                colorImage = camera.getImage(); // get the image from the camera

                //TODO: Tune these HSL values at the venue!
                BinaryImage binImage = colorImage.thresholdHSV(ImagingConstants.kHThresholdMin, ImagingConstants.kHThresholdMax, ImagingConstants.kSThresholdMin, ImagingConstants.kSThresholdMax, ImagingConstants.kLThresholdMin, ImagingConstants.kLThresholdMax);
                s_particles = binImage.getOrderedParticleAnalysisReports(4);
                colorImage.free();
                binImage.free();

                if (s_particles.length > 0) {
                    int lowestY = 0;
                    for (int i = 0; i < s_particles.length; i++) {
                        ParticleAnalysisReport circ = s_particles[i];
                        //Find the highest rectangle (will have the lowest Y coordinate)
                        if (s_particles[lowestY].center_mass_y > circ.center_mass_y) {
                            lowestY = i;
                        }
                    }
                    topTarget = s_particles[lowestY];
                    //Send bounding rectangle info to SmartDashboard
                    xOffset = ((topTarget.boundingRectLeft + topTarget.boundingRectWidth / 2) - 160.0) / 160.0;
//                    if (Math.abs(xOffset) < 0.05) {
//                        xOffset = 0;
//                    }
                } else {
                    xOffset = 0;
                }
            } catch (AxisCameraException ex) {
                ex.printStackTrace();
            } catch (NIVisionException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * The default command for the camera.
     */
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }

    /**
     * 
     */
    public void resetCamera() {
        // Do not configure the camera, it SHOULD be persistent!!!
//        camera.writeResolution(AxisCamera.ResolutionT.k320x240);
//        camera.writeCompression(30);
//        camera.writeWhiteBalance(AxisCamera.WhiteBalanceT.fixedOutdoor1);
//        camera.writeExposureControl(AxisCamera.ExposureT.flickerfree60);
//        camera.writeExposurePriority(AxisCamera.ExposurePriorityT.frameRate);
//        camera.writeBrightness(0);
//        camera.writeColorLevel(50);
//        camera.writeMaxFPS(30);
//        camera.writeRotation(AxisCamera.RotationT.k0);
        //sharpness should be 0, but there is no method for it
    }
}
