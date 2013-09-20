package org.crescentschool.robotics.competition.subsystems;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.crescentschool.robotics.competition.constants.ElectricalConstants;

public class DriveTrain extends Subsystem {

    Gyro gyro;
    CANJaguar jagRightMaster;
    Victor victorRightSlaveMid;
    Victor victorRightSlaveBack;
    CANJaguar jagLeftMaster;
    Victor victorLeftSlaveMid;
    Victor victorLeftSlaveBack;
    double p = 10.0;
    double i = 0;
    double d = 0;
    double error = 0;
    double targetAngle = 0;
    int driveMode = 1;
    private static DriveTrain instance = null;
    private boolean canError = false;
    double errorI = 0;
    public boolean locked = false;
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

    public void initVBus() {
        driveMode = 1;
        initVBusRight();
        initVBusLeft();
    }

    public void initVBusRight() {
        try {
            jagRightMaster.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
            jagRightMaster.enableControl();
        } catch (CANTimeoutException ex) {
            canError = true;
            handleCANError();
            ex.printStackTrace();
        }
    }

    public void initVBusLeft() {
        try {
            jagLeftMaster.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
            jagLeftMaster.enableControl();
        } catch (CANTimeoutException ex) {
            canError = true;
            handleCANError();
            ex.printStackTrace();
        }
    }

    public void initPosition() {
        driveMode = 2;
        initPositionRight();
        initPositionLeft();
    }

    public void initPositionRight() {
        try {
            jagRightMaster.changeControlMode(CANJaguar.ControlMode.kPosition);
            jagRightMaster.setPositionReference(CANJaguar.PositionReference.kQuadEncoder);
            jagRightMaster.configEncoderCodesPerRev(256);
            jagRightMaster.setPID(p, i, d);
            jagRightMaster.enableControl(0);
        } catch (CANTimeoutException ex) {
            canError = true;
            handleCANError();
            ex.printStackTrace();
        }
    }

    public void initPositionLeft() {
        try {
            jagLeftMaster.changeControlMode(CANJaguar.ControlMode.kPosition);
            jagLeftMaster.setPositionReference(CANJaguar.PositionReference.kQuadEncoder);
            jagLeftMaster.configEncoderCodesPerRev(256);
            jagLeftMaster.setPID(-p, -i, -d);
            jagLeftMaster.enableControl(0);
        } catch (CANTimeoutException ex) {
            canError = true;
            handleCANError();
            ex.printStackTrace();
        }
    }

    public double getPositionRight() {
        try {
            return jagRightMaster.getPosition();
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
            handleCANError();
            return 42;
        }
    }

    public double getPositionLeft() {
        try {
            return jagLeftMaster.getPosition();
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

    public void setPositionLeft(double pos) {
        if (driveMode != 2) {
            initPosition();
        }
        try {
            jagLeftMaster.setX(-pos);
            syncSlaves(false,0);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
            handleCANError();
        }
    }

    public void setPositionRight(double pos) {
        if (driveMode != 2) {
            initPosition();
        }
        try {
            jagRightMaster.setX(pos);
            syncSlaves(false,0);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
            handleCANError();
        }
    }

    public void syncSlaves(boolean vbus, double power) {
        try {
            if (!vbus) {
                double getOutputVoltageRight = jagRightMaster.getOutputVoltage();
                double getOutputVoltageLeft = jagLeftMaster.getOutputVoltage();
                victorRightSlaveMid.set(getOutputVoltageRight / 9.0);
                victorRightSlaveBack.set(getOutputVoltageRight / 9.0);
                victorLeftSlaveMid.set(getOutputVoltageLeft / 9.0);
                victorLeftSlaveBack.set(getOutputVoltageLeft / 9.0);
            } else {
                victorRightSlaveMid.set(power);
                victorRightSlaveBack.set(power);
                victorLeftSlaveMid.set(power);
                victorLeftSlaveBack.set(power);
            }
        } catch (CANTimeoutException ex) {
            canError = true;
            handleCANError();
            ex.printStackTrace();
        }
    }
     public void syncSlavesRight(boolean vbus, double power) {
        try {
            if (!vbus) {
                double getOutputVoltageRight = jagRightMaster.getOutputVoltage();
                victorRightSlaveMid.set(getOutputVoltageRight / 9.0);
                victorRightSlaveBack.set(getOutputVoltageRight / 9.0);
            } else {
                victorRightSlaveMid.set(power);
                victorRightSlaveBack.set(power);
            }
        } catch (CANTimeoutException ex) {
            canError = true;
            handleCANError();
            ex.printStackTrace();
        }
    }
      public void syncSlavesLeft(boolean vbus, double power) {
        try {
            if (!vbus) {
                double getOutputVoltageRight = jagRightMaster.getOutputVoltage();
                double getOutputVoltageLeft = jagLeftMaster.getOutputVoltage();
                victorLeftSlaveMid.set(getOutputVoltageLeft / 9.0);
                victorLeftSlaveBack.set(getOutputVoltageLeft / 9.0);
            } else {
                victorLeftSlaveMid.set(power);
                victorLeftSlaveBack.set(power);
            }
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
        syncSlavesLeft(true,power);
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
        syncSlavesRight(true,power);
    }

    public void resetGyro() {
        gyro.reset();
    }
    //1: Left
    //2: Right
    //3: Both

    public void setAngle(double angle, boolean newOffset, int side) {
        if (newOffset) {
            targetAngle = gyro.getAngle() + angle;
        }
        double i = 0.005;
        double p = 0.1;
        error = targetAngle - gyro.getAngle();
        errorI += error;
        errorI = Math.min(1.0 / i, errorI);
        switch (side) {
            case 1:
                setLeftVBus(error * p + i * errorI);
                break;
            case 2:
                setRightVBus(error * p + i * errorI);
                break;
            case 3:
                setRightVBus(error * -p - i * errorI);
                setLeftVBus(error * p + i * errorI);
                break;
        }

        SmartDashboard.putNumber("Output", error * -p - i * errorI);
        SmartDashboard.putNumber("turnError", error);
        SmartDashboard.putNumber("Angle Wanted", angle);
    }

    public void setErrorI(double errorI) {
        this.errorI = errorI;
    }

    public void handleCANError() {
        if (canError) {
            SmartDashboard.putString("Messages", "CAN Error!");
            Logger.getLogger().debug("CAN Error!");
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
