/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.subsystems;

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.camera.AxisCameraException;
import edu.wpi.first.wpilibj.image.BinaryImage;
import edu.wpi.first.wpilibj.image.ColorImage;
import edu.wpi.first.wpilibj.image.NIVisionException;
import edu.wpi.first.wpilibj.image.ParticleAnalysisReport;
import org.crescentschool.robotics.competition.constants.ElectricalConstants;
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
    private static Relay camLight;
    private BinaryImage binImage;
    private ParticleAnalysisReport circ;
    private Turret turret;
    private double curTurretPot;
    boolean freshImage;

    private Camera() {
        resetCamera();
        turret = Turret.getInstance();
        camLight = new Relay(ElectricalConstants.camLight);
        setLight(true);
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
    public ParticleAnalysisReport getTopTarget() {
        return topTarget;
    }

    /**
     * Returns the height of the top bounding box as given by the particle analysis report
     * @return the height of the top bounding box
     */
    public double getHeight() {
        if (topTarget == null) {
            //TODO: Find new default height
            return 40.0;
        }
        return (topTarget.boundingRectTop + 0.5 * topTarget.boundingRectHeight);
    }

    /**
     * Gets the normalized position for the top square on the net.
     * @return The normalized position for the top square as a value between -1 and 1.
     */
    public double getX() {

        return xOffset;
    }

    /**
     * Says if we are using a new Image
     * @return whether we are using a new image
     */
    public boolean newImage() {
        if (freshImage) {
            freshImage = false;
            return true;
        }
        return false;

    }

    /**
     * Sets On or Off For the Camera Light
     */
    public void setLight(boolean on) {
        if (on) {
            camLight.set(Relay.Value.kForward);
        } else {
            camLight.set(Relay.Value.kOff);
        }
    }

    /**
     * Updates the camera image if there is a new image. Identifies reflective rectangles. Calculates X offset of highest rectangle.
     */
    public void processCamera() {

        if (camera.freshImage()) {
            try {
                //Take a snapshot of the current turret pot position
                curTurretPot = turret.getPos();
                colorImage = camera.getImage(); // get the image from the camera
                freshImage = true;
                //TODO: Tune these HSL values at the venue!
                binImage = colorImage.thresholdHSV(ImagingConstants.kHThresholdMin, ImagingConstants.kHThresholdMax, ImagingConstants.kSThresholdMin, ImagingConstants.kSThresholdMax, ImagingConstants.kLThresholdMin, ImagingConstants.kLThresholdMax);
                s_particles = binImage.getOrderedParticleAnalysisReports(4);

                colorImage.free();
                binImage.free();

                if (s_particles.length > 0) {
                    int lowestY = 0;
                    for (int i = 1; i < s_particles.length; i++) {
                        circ = s_particles[i];
                        //Find the highest rectangle (will have the lowest Y coordinate)
                        if (s_particles[lowestY].boundingRectTop > circ.boundingRectTop) {
                            if ((circ.boundingRectWidth > 20) && (circ.boundingRectHeight > 20)) {
                                lowestY = i;
                            }
                        }
                    }
                    topTarget = s_particles[lowestY];
                    //Send bounding rectangle info to SmartDashboard
                    // Check if the best top blob is bigger than 20
                    if (topTarget.particleArea > 20)
                    {
                        xOffset = ((topTarget.boundingRectLeft + topTarget.boundingRectWidth / 2) - 160.0) / 160.0;
                    }
                    else
                    {
                        xOffset = 0;
                        topTarget = null;
                    }
                } else {
                    xOffset = 0;
                    topTarget = null;
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
    
    public double getTurretPot() {
        return curTurretPot;
    }
}
