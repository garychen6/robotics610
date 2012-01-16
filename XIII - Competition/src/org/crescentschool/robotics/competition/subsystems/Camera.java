/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import com.sun.squawk.util.Arrays;
import com.sun.squawk.util.Comparer;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.camera.AxisCameraException;
import edu.wpi.first.wpilibj.image.BinaryImage;
import edu.wpi.first.wpilibj.image.ColorImage;
import edu.wpi.first.wpilibj.image.NIVisionException;
import edu.wpi.first.wpilibj.image.ParticleAnalysisReport;

/**
 *
 * @author Warfa
 */
public class Camera extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    AxisCamera camera;
    private ColorImage colorImage;
    private ParticleAnalysisReport s_particles[];
    private final ParticleComparer particleComparer = new ParticleComparer();

    /**
     * Constructor for the sample program.
     * Get an instance of the axis camera.
     */
    public Camera() {
        camera = AxisCamera.getInstance();
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
            try {
                colorImage = camera.getImage(); // get the image from the camera

                /**
                 * The color threshold operation returns a bitmap (BinaryImage) where pixels are present
                 * when the corresponding pixels in the source (HSL) image are in the specified
                 * range of H, S, and L values.
                 */
                BinaryImage binImage = colorImage.thresholdHSL(242, 255, 36, 255, 25, 255);

                /**
                 * Find blobs (groupings) of pixels that were identified in the color threshold operation
                 */
                s_particles = binImage.getOrderedParticleAnalysisReports();

                /**
                 * Free the underlying color and binary images.
                 * You must do this since the image is actually stored
                 * as a C++ data structure in the underlying implementation to maximize processing speed.
                 */
                colorImage.free();
                binImage.free();

                /**
                 * print the number of detected particles (color blobs)
                 */
                System.out.println("Particles: " + s_particles.length);

                if (s_particles.length > 0) {
                    /**
                     * sort the particles using the custom comparitor class (see below)
                     */
                    Arrays.sort(s_particles, particleComparer);

                    for (int i = 0; i < s_particles.length; i++) {
                        ParticleAnalysisReport circ = s_particles[i];

                        /**
                         * Compute the number of degrees off center based on the camera image size
                         */
                        double degreesOff = -((54.0 / 640.0) * ((circ.imageWidth / 2.0) - circ.center_mass_x));
                        switch (i) {
                            case 0:
                                System.out.print("Best Particle:     ");
                                break;
                            case 1:
                                System.out.print("2nd Best Particle: ");
                                break;
                            case 2:
                                System.out.print("3rd Best Particle: ");
                                break;
                            default:
                                System.out.print((i + 1) + "th Best Particle: ");
                                break;
                        }
                        System.out.print("    X: " + circ.center_mass_x
                                + "     Y: " + circ.center_mass_y
                                + "     Degrees off Center: " + degreesOff
                                + "     Size: " + circ.particleArea);
                        System.out.println();

                    }
                }
                System.out.println();
                System.out.println();
                Timer.delay(4);
            } catch (AxisCameraException ex) {
                ex.printStackTrace();
            } catch (NIVisionException ex) {
                ex.printStackTrace();
            }
        }
        Timer.delay(0.01);
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
