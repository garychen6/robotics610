/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import com.sun.squawk.util.Comparer;
import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.camera.AxisCameraException;
import edu.wpi.first.wpilibj.image.BinaryImage;
import edu.wpi.first.wpilibj.image.ColorImage;
import edu.wpi.first.wpilibj.image.NIVisionException;
import edu.wpi.first.wpilibj.image.ParticleAnalysisReport;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author Warfa
 */
public class Camera extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    private ColorImage colorImage;
    private ParticleAnalysisReport s_particles[];
    private final ParticleComparer particleComparer = new ParticleComparer();
    private static Camera instance = null;
    private static AxisCamera camera = AxisCamera.getInstance();
    int frames = 0;
    long time = 0;
    long lastTime = 0;

    /**
     * Constructor for the sample program. Get an instance of the axis camera.
     */
    public Camera() {
        camera.writeResolution(AxisCamera.ResolutionT.k320x240);
        camera.writeCompression(30);
        camera.writeWhiteBalance(AxisCamera.WhiteBalanceT.fixedOutdoor1);
        camera.writeExposureControl(AxisCamera.ExposureT.flickerfree60);
        camera.writeExposurePriority(AxisCamera.ExposurePriorityT.frameRate);
        camera.writeBrightness(0);
        camera.writeColorLevel(50);
        camera.writeMaxFPS(30);
        camera.writeRotation(AxisCamera.RotationT.k0);
        //sharp0

    }

    public static Camera getInstance() {
        if (instance == null) {
            instance = new Camera();
        }
        return instance;
    }

    /**
     * the sample code runs during the autonomous period.
     *
     */
    public void processCamera() {

        /**
         * Run while the robot is enabled
         */
        if (camera.freshImage()) {	    // check if there is a new image
//            if (frames > 30) {
//                System.out.println(frames);
//                frames = 0;
//            }
//            frames++;
//            long curTime = System.currentTimeMillis();
//            time += curTime - lastTime;
//            lastTime = curTime;
//            frames++;
           try {
                colorImage = camera.getImage(); // get the image from the camera

                /**
                 * The color threshold operation returns a bitmap (BinaryImage)
                 * where pixels are present when the corresponding pixels in the
                 * source (HSL) image are in the specified range of H, S, and L
                 * values.
                 */
                BinaryImage binImage = colorImage.thresholdHSL(50, 100, 0, 255, 179, 255);

                /**
                 * Find blobs (groupings) of pixels that were identified in the
                 * color threshold operation
                 */
                s_particles = binImage.getOrderedParticleAnalysisReports(4);

                /**
                 * Free the underlying color and binary images. You must do this
                 * since the image is actually stored as a C++ data structure in
                 * the underlying implementation to maximize processing speed.
                 */
                colorImage.free();
                binImage.free();

                /**
                 * print the number of detected particles (color blobs)
                 */
                //System.out.println("Particles (max 4): " + s_particles.length);
                if (s_particles.length > 0) {
                    /**
                     * sort the particles using the custom comparitor class (see
                     * below)
                     */
                    //Particles already sorted by area
                    //Arrays.sort(s_particles, particleComparer);
                    SmartDashboard.putString("bounddata", s_particles[0].boundingRectLeft + ";" + s_particles[0].boundingRectTop + ";" + s_particles[0].boundingRectWidth + ";" + s_particles[0].boundingRectHeight);
                    for (int i = 0; i < s_particles.length; i++) {
                        ParticleAnalysisReport circ = s_particles[i];

                        /**
                         * Compute the number of degrees off center based on the
                         * camera image size
                         */
//                        double degreesOff = -((54.0 / 640.0) * ((circ.imageWidth / 2.0) - circ.center_mass_x));
//                        switch (i) {
//                            case 0:
//                                System.out.print("1st Particle: ");
//                                break;
//                            case 1:
//                                System.out.print("2nd Particle: ");
//                                break;
//                            case 2:
//                                System.out.print("3rd Particle: ");
//                                break;
//                            default:
//                                System.out.print((i + 1) + "th Particle: ");
//                                break;
//                        }
                        //System.out.print("X: " + circ.center_mass_x
                        //        + " Y: " + circ.center_mass_y
                        //        + " Size: " + circ.particleArea
                        //        + " Width: " + circ.boundingRectWidth
                        //        + " Height: " + circ.boundingRectHeight);
                        //System.out.println();
                    }
//                    if (frames == 100) {
//                        frames = 0;
//                        System.out.println(time / 100);
//                        time = 0;
//                    }
                  }
            } catch (AxisCameraException ex) {
                ex.printStackTrace();
            } catch (NIVisionException ex) {
                ex.printStackTrace();
            }
            //    }
        }
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}

/**
 * compare two particles
 *
 */
class ParticleComparer implements Comparer {

    public int compare(ParticleAnalysisReport p1, ParticleAnalysisReport p2) {
        float p1Ratio = p1.boundingRectWidth / p1.boundingRectHeight;
        float p2Ratio = p2.boundingRectWidth / p2.boundingRectHeight;

        if (Math.abs(p1Ratio - p2Ratio) < 0.1) {
            return -(Math.abs((p1.imageWidth / 2) - p1.center_mass_x))
                    - Math.abs(((p2.imageWidth / 2) - p2.center_mass_x));
        } else {
            if (Math.abs(p1Ratio - 1) < Math.abs(p2Ratio - 1)) {
                return 1;
            } else {
                return -1;
            }
        }
    }

    // overloaded method because the comparitor uses Objects (not Particles)
    public int compare(Object o1, Object o2) {
        return compare((ParticleAnalysisReport) o1, (ParticleAnalysisReport) o2);
    }
}
