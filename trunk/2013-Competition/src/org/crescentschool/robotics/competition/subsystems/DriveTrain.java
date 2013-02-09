package org.crescentschool.robotics.competition.subsystems;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.crescentschool.robotics.competition.OI;
import org.crescentschool.robotics.competition.constants.ElectricalConstants;

public class DriveTrain extends Subsystem {

    Gyro gyro;
    CANJaguar jagRightMaster;
    Victor victorRightSlaveMid;
    Victor victorRightSlaveBack;
    CANJaguar jagLeftMaster;
    Victor victorLeftSlaveMid;
    Victor victorLeftSlaveBack;
    double p = 0;
    double i = 0;
    double d = 0;
    double error = 0;
    double targetAngle = 0;
    int driveMode = 1;
    private static DriveTrain instance = null;
    private boolean canError = false;
    double errorI = 0;
    Preferences constantsTable;

    public void initDefaultCommand() {
    }

    public static DriveTrain getInstance() {
        if (instance == null) {
            instance = new DriveTrain();
        }
        return instance;
    }

    DriveTrain() {
        try {
            gyro = new Gyro(1);
            victorRightSlaveMid = new Victor(ElectricalConstants.victorRightSlaveFront);
            victorRightSlaveBack = new Victor(ElectricalConstants.victorRightSlaveBack);
            victorLeftSlaveMid = new Victor(ElectricalConstants.victorLeftSlaveFront);
            victorLeftSlaveBack = new Victor(ElectricalConstants.victorLeftSlaveBack);
            jagLeftMaster = new CANJaguar(ElectricalConstants.jagLeftMaster);
            jagRightMaster = new CANJaguar(ElectricalConstants.jagRightMaster);
            jagRightMaster.configNeutralMode(CANJaguar.NeutralMode.kCoast);
            jagLeftMaster.configNeutralMode(CANJaguar.NeutralMode.kCoast);
            constantsTable = Preferences.getInstance();
        } catch (CANTimeoutException ex) {
            canError = true;
            handleCANError();
            ex.printStackTrace();
        }
    }

    public Gyro getGyro() {
        return gyro;
    }

    void initVBus() {
        try {
            System.out.println("VBus");
            driveMode = 1;
            jagLeftMaster.changeControlMode(CANJaguar.ControlMode.kPercentVbus);

            jagRightMaster.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
            jagRightMaster.enableControl();
            jagLeftMaster.enableControl();
        } catch (CANTimeoutException ex) {
            canError = true;
            handleCANError();
            ex.printStackTrace();
        }
    }

    void initPosition() {
        try {
            System.out.println("Position");
            driveMode = 2;
            jagRightMaster.changeControlMode(CANJaguar.ControlMode.kPosition);
            jagLeftMaster.changeControlMode(CANJaguar.ControlMode.kPosition);
            jagRightMaster.setPositionReference(CANJaguar.PositionReference.kQuadEncoder);
            jagLeftMaster.setPositionReference(CANJaguar.PositionReference.kQuadEncoder);
            jagRightMaster.configEncoderCodesPerRev(256);
            jagLeftMaster.configEncoderCodesPerRev(256);
            jagRightMaster.setPID(p, i, d);
            jagLeftMaster.setPID(p, i, d);
            jagRightMaster.enableControl(0);
            jagLeftMaster.enableControl(0);
        } catch (CANTimeoutException ex) {
            canError = true;
            handleCANError();
            ex.printStackTrace();
        }
    }

    public double getPosition() {
        try {
            return jagRightMaster.getPosition();
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
            handleCANError();
            return 42;
        }
    }

    public void setPID(double p, double i, double d) {
        this.p = p;
        this.i = i;
        this.d = d;
        initPosition();
    }

    public void setPosition(double pos) {
        if (driveMode != 2) {
            initPosition();
        }
        try {
            System.out.println("Setpoint: " + pos);
            jagLeftMaster.setX(-pos);
            jagRightMaster.setX(pos);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
            handleCANError();
        }
        syncSlaves();
    }

    void syncSlaves() {
        try {
            double getOutputVoltageRight = jagRightMaster.getOutputVoltage();
            double getOutputVoltageLeft = jagLeftMaster.getOutputVoltage();
            victorRightSlaveMid.set(getOutputVoltageRight / 12.0);
            victorRightSlaveBack.set(getOutputVoltageRight/12.0);
            victorLeftSlaveMid.set(getOutputVoltageLeft/12.0);
            victorLeftSlaveBack.set(getOutputVoltageLeft/12.0);
        } catch (CANTimeoutException ex) {
            canError = true;
            handleCANError();
            ex.printStackTrace();
        }
    }

    public void setLeftVBus(double power) {
        if (driveMode != 1) {
            initVBus();
        }
        try {
            jagLeftMaster.setX(power);
        } catch (CANTimeoutException e) {
            canError = true;
            handleCANError();
            e.printStackTrace();
        }
        syncSlaves();
    }

    public void setRightVBus(double power) {
        if (driveMode != 1) {
            initVBus();
        }
        try {
            jagRightMaster.setX(power);
        } catch (CANTimeoutException e) {
            canError = true;
            handleCANError();
            e.printStackTrace();
        }
        syncSlaves();
    }

    public void resetGyro() {
        gyro.reset();
    }

    public void setAngle(double angle, boolean newOffset) {
        if (newOffset) {
            targetAngle = gyro.getAngle() + angle;
        }
        error = targetAngle - gyro.getAngle();
        errorI += error;
        double i = constantsTable.getDouble("turnI", 0);
        double p = constantsTable.getDouble("turnP", 0);
        setRightVBus(0.1);
        errorI = Math.min(1.0 / i, errorI);
        setRightVBus(-0.2);
        setLeftVBus(error * p + i * errorI);
        SmartDashboard.putNumber("Output", error * -p - i * errorI);
        SmartDashboard.putNumber("turnError", error);
        SmartDashboard.putNumber("Angle Wanted", angle);
    }

    public void setAngleOnSpot(double angle) {
        double error = (angle - gyro.getAngle());
        errorI += error;
        double i = constantsTable.getDouble("turnI", 0);
        double p = constantsTable.getDouble("turnP", 0);
        setRightVBus(error * p + i * errorI);
        setLeftVBus(error * -p - i * errorI);
        SmartDashboard.putNumber("turnError", error);
    }

    public void setErrorI(double errorI) {
        this.errorI = errorI;
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
            switch (driveMode) {
                case 1:
                    initVBus();
                    break;
                case 2:
                    initPosition();
                    break;
            }
        }
    }
}
