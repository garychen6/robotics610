/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.subsystems;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SendablePIDController;
import org.crescentschool.robotics.competition.commands.TankDrive;
import org.crescentschool.robotics.competition.commands.TowerDrive;
import org.crescentschool.robotics.competition.constants.ElectricalConstants;
import org.crescentschool.robotics.competition.constants.PIDConstants;

/**
 *
 * @author Warfa
 */
public class DriveTrain extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    private double p, i, d;
    // 1 = %VBus, 2 = Speed, 3 = Position
    public int controlMode = 1;
    private boolean canError = false;
    private CANJaguar jagRightMaster;
    private int count = 0;
    private CANJaguar jagLeftMaster;
    private CANJaguar jagRightSlave;
    private CANJaguar jagLeftSlave;
    private Solenoid shifterHigh;
    private Solenoid shifterLow;
    private Gyro gyro;
    private SendablePIDController posControllerRight;
    private SendablePIDController posControllerLeft;
    private static DriveTrain instance = null;
    private PIDSource rightPosIn = new PIDSource() {

        public double pidGet() {
            try {
                return jagRightMaster.getPosition() - PIDConstants.gyroP * gyro.getAngle();
                //return jagRightMaster.getPosition();
            } catch (CANTimeoutException ex) {
                canError = true;
                handleCANError();
                ex.printStackTrace();
                return 0;
            }
        }
    };
    int count1 = 0;
    private PIDOutput rightPosOut = new PIDOutput() {

        public void pidWrite(double output) {

            // Jag is in Speed Control Mode
            // Output should be in rpm
          
            runJagRight(output);
            syncSlaves();
            count++;

        }
    };
    private PIDSource leftPosIn = new PIDSource() {

        public double pidGet() {
            try {
                //return jagLeftMaster.getPosition() + PIDConstants.gyroP * gyro.getAngle();
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

            
            runJagLeft(output);
            syncSlaves();

        }
    };

    public void initDefaultCommand() {
        setDefaultCommand(new TowerDrive());
    }

    public static DriveTrain getInstance() {
        if (instance == null) {
            instance = new DriveTrain();
        }
        return instance;
    }

    private DriveTrain() {

        p = PIDConstants.posP;
        i = PIDConstants.posI;
        d = PIDConstants.posD;

        try {
            jagRightMaster = new CANJaguar(ElectricalConstants.DriveRightMaster);
            jagLeftMaster = new CANJaguar(ElectricalConstants.DriveLeftMaster);
            jagRightSlave = new CANJaguar(ElectricalConstants.DriveRightSlave);
            jagLeftSlave = new CANJaguar(ElectricalConstants.DriveLeftSlave);
            //TODO: These PID Controllers may need to run faster than 50ms
            posControllerRight = new SendablePIDController(PIDConstants.posP,
                    PIDConstants.posI, PIDConstants.posD, rightPosIn, rightPosOut);
            posControllerLeft = new SendablePIDController(PIDConstants.posP,
                    PIDConstants.posI, PIDConstants.posD, leftPosIn, leftPosOut);
        } catch (CANTimeoutException ex) {
            canError = true;
            handleCANError();
            ex.printStackTrace();
        }

        shifterHigh = new Solenoid(2);
        shifterLow = new Solenoid(3);
        shifterHigh.set(true);
        initPosMode();
    }

    public void initPosMode() {
        System.out.println("Pos Mode");
        controlMode = 3;
        posControllerRight.setPID(p, i, d);
        posControllerLeft.setPID(p, i, d);
        try {
            jagLeftMaster.configFaultTime(0.5);
            jagRightMaster.configFaultTime(0.5);
            jagLeftMaster.configNeutralMode(CANJaguar.NeutralMode.kCoast);
            jagRightMaster.configNeutralMode(CANJaguar.NeutralMode.kCoast);
            jagLeftMaster.setSpeedReference(CANJaguar.SpeedReference.kQuadEncoder);
            jagRightMaster.setSpeedReference(CANJaguar.SpeedReference.kQuadEncoder);
            jagLeftMaster.setPositionReference(CANJaguar.PositionReference.kQuadEncoder);
            jagRightMaster.setPositionReference(CANJaguar.PositionReference.kQuadEncoder);
            jagLeftMaster.configEncoderCodesPerRev(ElectricalConstants.DriveEncoderCounts);
            jagRightMaster.configEncoderCodesPerRev(ElectricalConstants.DriveEncoderCounts);
            jagLeftMaster.changeControlMode(CANJaguar.ControlMode.kSpeed);
            jagRightMaster.changeControlMode(CANJaguar.ControlMode.kSpeed);
            //jagLeftMaster.setX(0);
            //jagRightMaster.setX(0);
            jagLeftMaster.setPID(PIDConstants.speedP, PIDConstants.speedI, PIDConstants.speedD);
            jagRightMaster.setPID(-PIDConstants.speedP, -PIDConstants.speedI, -PIDConstants.speedD);
            //jagLeftMaster.setPID(p, i, d);
            //jagRightMaster.setPID(-p, -i, -d);
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

    public void initSpeedMode() {
        System.out.println("Speed Mode");
        controlMode = 2;
        try {
            jagLeftMaster.configFaultTime(0.5);
            jagRightMaster.configFaultTime(0.5);
            jagLeftMaster.configNeutralMode(CANJaguar.NeutralMode.kCoast);
            jagRightMaster.configNeutralMode(CANJaguar.NeutralMode.kCoast);
            jagLeftMaster.setSpeedReference(CANJaguar.SpeedReference.kQuadEncoder);
            jagRightMaster.setSpeedReference(CANJaguar.SpeedReference.kQuadEncoder);
            jagLeftMaster.setPositionReference(CANJaguar.PositionReference.kQuadEncoder);
            jagRightMaster.setPositionReference(CANJaguar.PositionReference.kQuadEncoder);
            jagLeftMaster.configEncoderCodesPerRev(ElectricalConstants.DriveEncoderCounts);
            jagRightMaster.configEncoderCodesPerRev(ElectricalConstants.DriveEncoderCounts);
            jagLeftMaster.changeControlMode(CANJaguar.ControlMode.kSpeed);
            jagRightMaster.changeControlMode(CANJaguar.ControlMode.kSpeed);
            jagLeftMaster.setX(0);
            jagRightMaster.setX(0);
            jagLeftMaster.setPID(PIDConstants.speedP, PIDConstants.speedI, PIDConstants.speedD);
            jagRightMaster.setPID(-PIDConstants.speedP, -PIDConstants.speedI, -PIDConstants.speedD);
            //jagLeftMaster.setPID(p, i, d);
            //jagRightMaster.setPID(-p, -i, -d);
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
        System.out.println("VBus Mode Temp");
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
            jagLeftMaster.setX(0);
            jagRightMaster.setX(0);
            jagLeftMaster.setPID(PIDConstants.speedP, PIDConstants.speedI, PIDConstants.speedD);
            jagRightMaster.setPID(-PIDConstants.speedP, -PIDConstants.speedI, -PIDConstants.speedD);
            jagLeftMaster.enableControl(0);
            jagRightMaster.enableControl(0);
            //jagRightMaster.
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
            jagLeftSlave.setX(jagLeftMaster.getOutputVoltage() / jagLeftMaster.getBusVoltage());
            jagRightSlave.setX(jagRightMaster.getOutputVoltage() / jagRightMaster.getBusVoltage());
            // System.out.print("Left Slave: "+ jagLeftMaster.getOutputCurrent());
            // System.out.println(" Right Slave: "+ jagRightMaster.getOutputCurrent());
        } catch (CANTimeoutException ex) {
            canError = true;
            handleCANError();
            ex.printStackTrace();
        }
    }

    public void runJagRight(double speed) {
        try {
            jagRightMaster.setX(speed);
        } catch (CANTimeoutException ex) {
            canError = true;
            handleCANError();
            ex.printStackTrace();
        }
    }

    public void runJagLeft(double speed) {
        try {
            jagLeftMaster.setX(speed);
        } catch (CANTimeoutException ex) {
            canError = true;
            handleCANError();
            ex.printStackTrace();
        }
    }

    public void rightPosSetpoint(double setpoint) {
        if (controlMode != 3) {
            initPosMode();
        }
        try {
            jagRightMaster.enableControl(0);
        } catch (CANTimeoutException ex) {
            canError = true;
            handleCANError();
            ex.printStackTrace();
        }
        posControllerRight.setSetpoint(setpoint);
        posControllerRight.enable();
    }

    public void leftPosSetpoint(double setpoint) {
        if (controlMode != 3) {
            initPosMode();
        }
        try {
            
            jagLeftMaster.enableControl(0);
        } catch (CANTimeoutException ex) {
            canError = true;
            handleCANError();
            ex.printStackTrace();
        }
        posControllerLeft.setSetpoint(setpoint);
        posControllerLeft.enable();
    }

    public void posSetpoint(double setpoint) {
        leftPosSetpoint(setpoint);
        rightPosSetpoint(setpoint);
    }

    public double getRightPosSetpoint() {
        return posControllerRight.getSetpoint();
    }

    public double getLeftPosSetpoint() {
        return posControllerLeft.getSetpoint();
    }

    public double getPosSetpoint() {
        return (posControllerRight.getSetpoint() + posControllerLeft.getSetpoint()) / 2;
    }

    public double getPos() {
        try {
            return (jagLeftMaster.getPosition() + jagLeftMaster.getPosition()) / 2;
        } catch (CANTimeoutException ex) {
            canError = true;
            handleCANError();
            ex.printStackTrace();
            return 0;
        }
    }

    public void rightSpeedSetpoint(double setpoint) {
        if (controlMode != 2) {
            initSpeedMode();
        }
        try {
            //System.out.print(setpoint);
            //System.out.print("," + jagRightMaster.getSpeed());
            jagRightMaster.setX(-setpoint);
            syncSlaves();
            count++;
        } catch (CANTimeoutException ex) {
            canError = true;
            handleCANError();
            ex.printStackTrace();
        }
    }

    public void leftSpeedSetpoint(double setpoint) {
        if (controlMode != 2) {
            initSpeedMode();
        }
        try {
            //System.out.print("," + setpoint);
            //System.out.println("," + jagLeftMaster.getSpeed());

            jagLeftMaster.setX(setpoint);
            syncSlaves();
            count++;
        } catch (CANTimeoutException ex) {
            canError = true;
            handleCANError();
            ex.printStackTrace();
        }
    }

    public void speedSetpoint(double setpoint) {
        leftSpeedSetpoint(setpoint);
        rightSpeedSetpoint(setpoint);
    }

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

    public double getSpeed() {
        try {
            return (jagLeftMaster.getSpeed());// + jagLeftMaster.getPosition()) / 2;
        } catch (CANTimeoutException ex) {
            canError = true;
            handleCANError();
            ex.printStackTrace();
            return 0;
        }
    }

    public void leftVBusSetpoint(double setpoint) {
        if (controlMode != 1) {
            initVBusMode();
        }
        try {
            //System.out.print("Left " + jagLeftMaster.getSpeed());
            jagLeftMaster.setX(setpoint);

        } catch (CANTimeoutException ex) {
            canError = true;
            handleCANError();
            ex.printStackTrace();
        }
        syncSlaves();
    }

    public void rightVBusSetpoint(double setpoint) {
        if (controlMode != 1) {
            initVBusMode();
        }
        try {

            //System.out.println("Right " + jagRightMaster.getSpeed());

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
            if (canError) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                handleCANError();
            }
        }
    }

    public void incP() {
        p += 10;
        printPID();
    }

    public void decP() {
        p -= 10;
        printPID();
    }

    public void incI() {
        i += 10;
        printPID();
    }

    public void decI() {
        i -= 10;
        printPID();
    }

    private void printPID() {
        System.out.println("P: " + p + " I: " + i + " D: " + d);
    }
}