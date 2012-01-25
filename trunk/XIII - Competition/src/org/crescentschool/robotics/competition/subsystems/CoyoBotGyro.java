/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008-2012. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package org.crescentschool.robotics.competition.subsystems;

import edu.wpi.first.wpilibj.AccumulatorResult;
import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.SensorBase;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.parsing.ISensor;
import org.crescentschool.robotics.competition.constants.ElectricalConstants;

/**
 * Use a rate gyro to return the robots heading relative to a starting position.
 * The Gyro class tracks the robots heading based on the starting position. As the robot
 * rotates the new heading is computed by integrating the rate of rotation returned
 * by the sensor. When the class is instantiated, it does a short calibration routine
 * where it samples the gyro while at rest to determine the default offset. This is
 * subtracted from each sample to determine the heading.
 */
public class CoyoBotGyro extends SensorBase implements PIDSource, ISensor {

    static final int kOversampleBits = 10;
    static final int kAverageBits = 0;
    static final double kSamplesPerSecond = 50.0;
    static final double kCalibrationSampleTime = 5.0;
    static final double kDefaultVoltsPerDegreePerSecond = 0.007;
    AnalogChannel m_analog;
    double m_voltsPerDegreePerSecond;
    double m_offset;
    boolean m_channelAllocated;
    AccumulatorResult result;

    /**
     * Initialize the gyro.
     * Calibrate the gyro by running for a number of samples and computing the center value for this
     * part. Then use the center value as the Accumulator center value for subsequent measurements.
     * It's important to make sure that the robot is not moving while the centering calculations are
     * in progress, this is typically done when the robot is first turned on while it's sitting at
     * rest before the competition starts.
     */
    private void initGyro() {
        result = new AccumulatorResult();
        if (m_analog == null) {
            System.out.println("Null m_analog");
        }
        m_voltsPerDegreePerSecond = kDefaultVoltsPerDegreePerSecond;
        m_analog.setAverageBits(kAverageBits);
        m_analog.setOversampleBits(kOversampleBits);
        double sampleRate = kSamplesPerSecond * (1 << (kAverageBits + kOversampleBits));
        m_analog.getModule().setSampleRate(sampleRate);

        Timer.delay(1.0);
        m_analog.initAccumulator();

        Timer.delay(kCalibrationSampleTime);

        m_analog.getAccumulatorOutput(result);

        int center = (int) ((double)result.value / (double)result.count + .5);

        m_offset = ((double)result.value / (double)result.count) - (double)center;

        m_analog.setAccumulatorCenter(center+ElectricalConstants.GyroAccumulatorCenter);

        m_analog.setAccumulatorDeadband(0); ///< TODO: compute / parameterize this
        m_analog.resetAccumulator();
    }
     
    public CoyoBotGyro(int slot, int channel) {
        m_analog = new AnalogChannel(slot, channel);
        m_channelAllocated = true;
        initGyro();
    }

    public CoyoBotGyro(int channel) {
        m_analog = new AnalogChannel(channel);
        m_channelAllocated = true;
        initGyro();
    }

    public CoyoBotGyro(AnalogChannel channel) {
        m_analog = channel;
        if (m_analog == null) {
            System.err.println("Analog channel supplied to Gyro constructor is null");
        } else {
            m_channelAllocated = false;
            initGyro();
        }
    }

    public void reset() {
        if (m_analog != null) {
            m_analog.resetAccumulator();
        }
    }

    public void free() {
        if (m_analog != null && m_channelAllocated) {
            m_analog.free();
        }
        m_analog = null;
    }

    public double getAngle() {
        if (m_analog == null) {
            return 0.0;
        } else {
            m_analog.getAccumulatorOutput(result);

            long value = result.value - (long) (result.count * m_offset);

            double scaledValue = value * 1e-9 * m_analog.getLSBWeight() * (1 << m_analog.getAverageBits()) /
                    (m_analog.getModule().getSampleRate() * m_voltsPerDegreePerSecond);

            return scaledValue;
        }
    }

    public void setSensitivity(double voltsPerDegreePerSecond) {
        m_voltsPerDegreePerSecond = voltsPerDegreePerSecond;
    }

    public double pidGet() {
        return getAngle();
    }
}
