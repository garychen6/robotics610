/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.subsystems;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.crescentschool.robotics.competition.constants.ElectricalConstants;
import org.crescentschool.robotics.competition.constants.PIDConstants;

/**
 * Subsystem for the drivetrain
 * @author Warfa Jibril, Patrick White, Mr. Lim
 */
public class DriveTrain extends Subsystem {

    private static DriveTrain instance = null;
    private double pPos, iPos, dPos, pSpeed, iSpeed, dSpeed;
    // 1 = %VBus, 2 = Speed, 3 = Position
    private int controlMode = 1;
    private boolean canError = false;
    private int count = 0;
    private CANJaguar jagRightMaster;
    private CANJaguar jagLeftMaster;
    private CANJaguar jagRightSlave;
    private CANJaguar jagLeftSlave;
    private CoyoBotGyro gyro;
    private Accelerometer accel;
    private PIDController posControllerRight;
    private PIDController posControllerLeft;
    private PIDSource rightPosIn = new PIDSource() {

        public double pidGet() {
            try {
                return jagRightMaster.getPosition();
            } catch (CANTimeoutException ex) {
                canError = true;
                handleCANError();
                ex.printStackTrace();
                return 0;
            }
        }
    };
    private PIDOutput rightPosOut = new PIDOutput() {

        public void pidWrite(double output) {
            try {
                // Jag is in Speed Control Mode
                // Output should be in rpm
                // PID is tuned to only reach 1/2 the setpoint
                // Setpoint should be twice the desire mount
                jagRightMaster.setX(-2 * output + gyro.getAngle() / PIDConstants.gyroP);
                syncSlaves();
            } catch (CANTimeoutException ex) {
                canError = true;
                handleCANError();
                ex.printStackTrace();
            }


        }
    };
    private PIDSource leftPosIn = new PIDSource() {

        public double pidGet() {
            try {
                return jagLeftMaster.getPosition();
            } catch (CANTimeoutException ex) {
                canError = true;
                handleCANError();
                ex.printStackTrace();
                return 0;
            }
        }
    };
    private PIDOutput leftPosOut = new PIDOutput() {

        public void pidWrite(double output) {
            try {
                jagLeftMaster.setX(2 * output + gyro.getAngle() / PIDConstants.gyroP);
                syncSlaves();
            } catch (CANTimeoutException ex) {
                ex.printStackTrace();
            }

        }
    };

    /**
     * Sets the default command for the drivetrain.
     */
    public void initDefaultCommand() {
        // No default command for the drivetrain
    }

    /**
     * Ensures only one drivetrain is instantiated.
     * @return The singleton drivetrain instance.
     */
    public static DriveTrain getInstance() {
        if (instance == null) {
            instance = new DriveTrain();
        }
        return instance;
    }

    private DriveTrain() {
        pPos = PIDConstants.drivePositionP;
        iPos = PIDConstants.drivePositionI;
        dPos = PIDConstants.drivePositionD;
        pSpeed = PIDConstants.driveSpeedP;
        iSpeed = PIDConstants.driveSpeedI;
        dSpeed = PIDConstants.driveSpeedD;
        try {
            jagRightMaster = new CANJaguar(ElectricalConstants.DriveRightMaster);
            jagLeftMaster = new CANJaguar(ElectricalConstants.DriveLeftMaster);
            jagRightSlave = new CANJaguar(ElectricalConstants.DriveRightSlave);
            jagLeftSlave = new CANJaguar(ElectricalConstants.DriveLeftSlave);
            posControllerRight = new PIDController(PIDConstants.drivePositionP,
                    PIDConstants.drivePositionI, PIDConstants.drivePositionD, rightPosIn, rightPosOut, 0.020);
            posControllerLeft = new PIDController(PIDConstants.drivePositionP,
                    PIDConstants.drivePositionI, PIDConstants.drivePositionD, leftPosIn, leftPosOut, 0.020);
            posControllerRight.setInputRange(-20, 20);
            posControllerRight.setOutputRange(-200, 200);
            posControllerLeft.setInputRange(-20, 20);
            posControllerLeft.setOutputRange(-200, 200);
            posControllerLeft.setTolerance(5);
        } catch (CANTimeoutException ex) {
            canError = true;
            handleCANError();
            ex.printStackTrace();
        }
        gyro = new CoyoBotGyro(ElectricalConstants.GyroPort);
        gyro.setSensitivity(0.007);
        accel = new Accelerometer(2);
        initPosMode();
    }

    private void initPosMode() {
        SmartDashboard.putString("DriveMode", "Position");
        controlMode = 3;
        posControllerRight.setPID(-pPos, -iPos, -dPos);
        posControllerLeft.setPID(pPos, iPos, dPos);
        gyro.reset();
        try {
            jagLeftMaster.configFaultTime(0.5);
            jagRightMaster.configFaultTime(0.5);
            jagLeftMaster.configNeutralMode(CANJaguar.NeutralMode.kCoast);
            jagRightMaster.configNeutralMode(CANJaguar.NeutralMode.kCoast);
            jagLeftSlave.configFaultTime(0.5);
            jagRightSlave.configFaultTime(0.5);
            jagLeftSlave.configNeutralMode(CANJaguar.NeutralMode.kCoast);
            jagRightSlave.configNeutralMode(CANJaguar.NeutralMode.kCoast);
            jagLeftMaster.setSpeedReference(CANJaguar.SpeedReference.kQuadEncoder);
            jagRightMaster.setSpeedReference(CANJaguar.SpeedReference.kQuadEncoder);
            jagLeftMaster.setPositionReference(CANJaguar.PositionReference.kQuadEncoder);
            jagRightMaster.setPositionReference(CANJaguar.PositionReference.kQuadEncoder);
            jagLeftMaster.configEncoderCodesPerRev(ElectricalConstants.DriveEncoderCounts);
            jagRightMaster.configEncoderCodesPerRev(ElectricalConstants.DriveEncoderCounts);
            jagLeftMaster.changeControlMode(CANJaguar.ControlMode.kPosition);
            jagRightMaster.changeControlMode(CANJaguar.ControlMode.kPosition);
            jagLeftMaster.enableControl(0);
            jagRightMaster.enableControl(0);
            jagLeftMaster.changeControlMode(CANJaguar.ControlMode.kSpeed);
            jagRightMaster.changeControlMode(CANJaguar.ControlMode.kSpeed);
            jagLeftMaster.setPID(pSpeed, iSpeed, dSpeed);
            jagRightMaster.setPID(-pSpeed, -iSpeed, -dSpeed);
            jagLeftMaster.enableControl(0);
            jagRightMaster.enableControl(0);
            jagLeftSlave.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
            jagRightSlave.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
            jagLeftSlave.enableControl(0);
            jagRightSlave.enableControl(0);
        } catch (CANTimeoutException ex) {
            canError = true;
            handleCANError();
            ex.printStackTrace();
        }
        posControllerLeft.reset();
        posControllerRight.reset();
        posControllerLeft.enable();
        posControllerRight.enable();
    }

    private void initSpeedMode() {
        posControllerLeft.disable();
        posControllerRight.disable();
        SmartDashboard.putString("DriveMode", "Speed Mode");
        controlMode = 2;
        try {
            jagLeftMaster.configFaultTime(0.5);
            jagRightMaster.configFaultTime(0.5);
            jagLeftMaster.configNeutralMode(CANJaguar.NeutralMode.kCoast);
            jagRightMaster.configNeutralMode(CANJaguar.NeutralMode.kCoast);
            jagLeftSlave.configFaultTime(0.5);
            jagRightSlave.configFaultTime(0.5);
            jagLeftSlave.configNeutralMode(CANJaguar.NeutralMode.kCoast);
            jagRightSlave.configNeutralMode(CANJaguar.NeutralMode.kCoast);
            jagLeftMaster.setSpeedReference(CANJaguar.SpeedReference.kQuadEncoder);
            jagRightMaster.setSpeedReference(CANJaguar.SpeedReference.kQuadEncoder);
            jagLeftMaster.setPositionReference(CANJaguar.PositionReference.kQuadEncoder);
            jagRightMaster.setPositionReference(CANJaguar.PositionReference.kQuadEncoder);
            jagLeftMaster.configEncoderCodesPerRev(ElectricalConstants.DriveEncoderCounts);
            jagRightMaster.configEncoderCodesPerRev(ElectricalConstants.DriveEncoderCounts);
            jagLeftMaster.changeControlMode(CANJaguar.ControlMode.kSpeed);
            jagRightMaster.changeControlMode(CANJaguar.ControlMode.kSpeed);
            jagLeftMaster.setPID(pSpeed, iSpeed, dSpeed);
            jagRightMaster.setPID(-pSpeed, -iSpeed, -dSpeed);
            jagLeftMaster.enableControl(0);
            jagRightMaster.enableControl(0);
            jagLeftSlave.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
            jagRightSlave.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
            jagLeftSlave.enableControl(0);
            jagRightSlave.enableControl(0);
        } catch (CANTimeoutException ex) {
            canError = true;
            handleCANError();
            ex.printStackTrace();
        }
    }

    private void initVBusMode() {
        posControllerLeft.disable();
        posControllerRight.disable();
        SmartDashboard.putString("DriveMode", "VBus");
        controlMode = 1;
        try {
            jagLeftMaster.configFaultTime(0.5);
            jagRightMaster.configFaultTime(0.5);
            jagLeftMaster.configNeutralMode(CANJaguar.NeutralMode.kCoast);
            jagRightMaster.configNeutralMode(CANJaguar.NeutralMode.kCoast);
            jagLeftSlave.configFaultTime(0.5);
            jagRightSlave.configFaultTime(0.5);
            jagLeftSlave.configNeutralMode(CANJaguar.NeutralMode.kCoast);
            jagRightSlave.configNeutralMode(CANJaguar.NeutralMode.kCoast);
            jagLeftMaster.setSpeedReference(CANJaguar.SpeedReference.kQuadEncoder);
            jagRightMaster.setSpeedReference(CANJaguar.SpeedReference.kQuadEncoder);
            jagLeftMaster.setPositionReference(CANJaguar.PositionReference.kQuadEncoder);
            jagRightMaster.setPositionReference(CANJaguar.PositionReference.kQuadEncoder);
            jagLeftMaster.configEncoderCodesPerRev(ElectricalConstants.DriveEncoderCounts);
            jagRightMaster.configEncoderCodesPerRev(ElectricalConstants.DriveEncoderCounts);
            jagLeftMaster.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
            jagRightMaster.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
            jagLeftMaster.enableControl(0);
            jagRightMaster.enableControl(0);
            jagLeftSlave.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
            jagRightSlave.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
            jagLeftSlave.enableControl(0);
            jagRightSlave.enableControl(0);
        } catch (CANTimeoutException ex) {
            canError = true;
            handleCANError();
            ex.printStackTrace();
        }
    }

    private void syncSlaves() {
        try {
            jagLeftSlave.setX(jagLeftMaster.getOutputVoltage() / 12);
            jagRightSlave.setX(jagRightMaster.getOutputVoltage() / 12);
        } catch (CANTimeoutException ex) {
            canError = true;
            handleCANError();
            ex.printStackTrace();
        }
    }

    /**
     * Sets the target position for the left side of the drivetrain.
     * @param setpoint 
     */
    public void setLeftPos(double setpoint) {
        if (controlMode != 3) {
            initPosMode();
        }
        posControllerLeft.setSetpoint(setpoint);
    }

    /**
     * Sets the target position for the right side of the drivetrain.
     * @param setpoint 
     */
    public void setRightPos(double setpoint) {
        if (controlMode != 3) {
            initPosMode();
        }
        posControllerRight.setSetpoint(-setpoint);
    }

    /**
     * Sets the target position for the both sides of the drivetrain.
     * @param setpoint 
     */
    public void setPos(double setpoint) {
        setLeftPos(setpoint);
        setRightPos(setpoint);
        System.out.println("SetPoint " + setpoint);
    }

    /**
     * Gets the target position for the right side of the drivetrain.
     */
    public double getLeftPosSetpoint() {
        return posControllerLeft.getSetpoint();
    }

    /**
     * Gets the target position for the right side of the drivetrain.
     */
    public double getRightPosSetpoint() {
        return posControllerRight.getSetpoint();
    }

    /**
     * Gets the target position for the left side of the drivetrain.
     */
    public double getPosSetpoint() {
        return (posControllerRight.getSetpoint() + posControllerLeft.getSetpoint()) / 2;
    }

    /**
     * Gets the current position for left side of the drivetrain.
     */
    public double getLeftPos() {
        try {
            return (jagLeftMaster.getPosition());
        } catch (CANTimeoutException ex) {
            canError = true;
            handleCANError();
            ex.printStackTrace();
            return 0;
        }
    }

    /**
     * Gets the current position for right side of the drivetrain.
     */
    public double getRightPos() {
        try {
            return (jagRightMaster.getPosition());
        } catch (CANTimeoutException ex) {
            canError = true;
            handleCANError();
            ex.printStackTrace();
            return 0;
        }
    }

    /**
     * Gets the current position for both sides of the drivetrain.
     */
    public double getPos() {
        try {
            return (jagLeftMaster.getPosition() + jagRightMaster.getPosition()) / 2;
        } catch (CANTimeoutException ex) {
            canError = true;
            handleCANError();
            ex.printStackTrace();
            return 0;
        }
    }
     /**
     * Gets the current Acceleration of the drivetrain.
     */
    public double getAccel() {
      return accel.getAcceleration();
    }

    /**
     * Sets the target speed for the left side of the drivetrain.
     * @param setpoint The target speed in ft/s.
     */
    public void setLeftSpeed(double setpoint) {
        if (controlMode != 2) {
            initSpeedMode();
        }
        try {
            jagLeftMaster.setX(2 * setpoint);
            syncSlaves();
            count++;
        } catch (CANTimeoutException ex) {
            canError = true;
            handleCANError();
            ex.printStackTrace();
        }
    }

    /**
     * Sets the target speed for the right side of the drivetrain.
     * @param setpoint The target speed in ft/s.
     */
    public void setRightSpeed(double setpoint) {
        if (controlMode != 2) {
            initSpeedMode();
        }
        try {
            jagRightMaster.setX(-2 * setpoint);
            syncSlaves();
        } catch (CANTimeoutException ex) {
            canError = true;
            handleCANError();
            ex.printStackTrace();
        }
    }

    /**
     * Sets the target speed for the both sides of the drivetrain.
     * @param setpoint The target speed in ft/s.
     */
    public void setSpeed(double setpoint) {
        setLeftSpeed(setpoint);
        setRightSpeed(setpoint);
    }

    /**
     * Gets the target speed for the left side of the drivetrain.
     * @return The target speed in ft/s.
     */
    public double getLeftSpeedSetpoint() {
        try {
            return jagLeftMaster.getX();
        } catch (CANTimeoutException ex) {
            canError = true;
            handleCANError();
            ex.printStackTrace();
            return 0;
        }
    }

    /**
     * Gets the target speed for the right side of the drivetrain.
     * @return The target speed in ft/s.
     */
    public double getRightSpeedSetpoint() {
        try {
            return jagRightMaster.getX();
        } catch (CANTimeoutException ex) {
            canError = true;
            handleCANError();
            ex.printStackTrace();
            return 0;
        }
    }

    /**
     * Gets the target speed for both sides of the drivetrain.
     * @return The target speed in ft/s.
     */
    public double getSpeedSetpoint() {
        try {
            return (jagLeftMaster.getX() + jagRightMaster.getX()) / 2;
        } catch (CANTimeoutException ex) {
            canError = true;
            handleCANError();
            ex.printStackTrace();
            return 0;
        }
    }

    /**
     * Gets the current speed for the left side of the drivetrain.
     * @return The target speed in ft/s.
     */
    public double getLeftSpeed() {
        try {
            return (jagLeftMaster.getSpeed());
        } catch (CANTimeoutException ex) {
            canError = true;
            handleCANError();
            ex.printStackTrace();
            return 0;
        }
    }

    /**
     * Gets the current speed for the right side of the drivetrain.
     * @return The target speed in ft/s.
     */
    public double getRightSpeed() {
        try {
            return (jagRightMaster.getSpeed());
        } catch (CANTimeoutException ex) {
            canError = true;
            handleCANError();
            ex.printStackTrace();
            return 0;
        }
    }

    /**
     * Gets the current speed for both sides of the drivetrain.
     * @return The target speed in ft/s.
     */
    public double getSpeed() {
        try {
            return (jagLeftMaster.getSpeed() + jagRightMaster.getPosition()) / 2;
        } catch (CANTimeoutException ex) {
            canError = true;
            handleCANError();
            ex.printStackTrace();
            return 0;
        }
    }

    /**
     * Sets the target normalized voltage for the left side of the drivetrain.
     * @param setpoint The target normalized voltage from -1 to 1.
     */
    public void setLeftVBus(double setpoint) {
        if (controlMode != 1) {
            initVBusMode();
        }
        try {
            jagLeftMaster.setX(setpoint);

        } catch (CANTimeoutException ex) {
            canError = true;
            handleCANError();
            ex.printStackTrace();
        }
        syncSlaves();
    }

    /**
     * Sets the target normalized voltage for the left side of the drivetrain.
     * @param setpoint The target normalized voltage from -1 to 1.
     */
    public void setRightVBus(double setpoint) {
        if (controlMode != 1) {
            initVBusMode();
        }
        try {
            jagRightMaster.setX(setpoint);

        } catch (CANTimeoutException ex) {
            canError = true;
            handleCANError();
            ex.printStackTrace();
        }
        syncSlaves();
    }

    public void handleCANError() {
        if (canError) {
            System.out.println("CAN Error!");
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            canError = false;
            switch (controlMode) {
                case 1:
                    initVBusMode();
                    break;
                case 2:
                    initSpeedMode();
                    break;
                case 3:
                    initPosMode();
                    break;
            }
        }
    }

    /**
     * Increments the position mode P constant.
     */
    public void incPPos() {
        pPos += 1;
        printPIDPos();
    }

    /**
     * Decrements the position mode P constant.
     */
    public void decPPos() {
        pPos -= 1;
        printPIDPos();
    }

    /**
     * Increments the position mode I constant.
     */
    public void incIPos() {
        iPos += 0.1;
        printPIDPos();
    }

    /**
     * Decrements the position mode I constant.
     */
    public void decIPos() {
        iPos -= 0.1;
        printPIDPos();
    }

    /**
     * Increments the speed mode P constant.
     */
    public void incPSpeed() {
        pSpeed += 0.01;
        printPIDSpeed();
    }

    /**
     * Decrements the speed mode P constant.
     */
    public void decPSpeed() {
        pSpeed -= 0.01;
        printPIDSpeed();
    }

    /**
     * Increments the speed mode P constant.
     */
    public void incISpeed() {
        dSpeed += 0.1;
        printPIDSpeed();
    }

    /**
     * Decrements the speed mode P constant.
     */
    public void decISpeed() {
        dSpeed -= 0.1;
        printPIDSpeed();
    }

    /**
     * Returns the drivetrain's gyro.
     * @return The drivetrain's gyro.
     */
    public CoyoBotGyro getGyro() {
        return gyro;
    }

    /**
     * Prints the current PID constants for position mode.
     */
    public void printPIDPos() {
        System.out.println("Pos P: " + pPos + " Pos I: " + iPos + " Pos D: " + dPos);
    }

    /**
     * Prints the current PID constants for speed mode.
     */
    public void printPIDSpeed() {
        System.out.println("Speed P: " + pSpeed + " Speed I: " + iSpeed + " Speed D: " + dSpeed);
    }

    /**
     * Re-initializes the drivetrain based on the current drive mode.
     */
    public void reInit() {
        switch (controlMode) {
            case 2:
                initSpeedMode();
                break;
            case 3:
                initPosMode();
                break;
            default:
            case 1:
                initVBusMode();
                break;
        }
    }
}
