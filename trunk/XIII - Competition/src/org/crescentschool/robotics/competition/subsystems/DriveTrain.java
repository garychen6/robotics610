/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.subsystems;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.command.Subsystem;
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

    private double pPos, iPos, dPos, pSpeed, iSpeed, dSpeed;
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
    private CoyoBotGyro gyro;
    private double PIDRightOutput;
    private double PIDLeftOutput;
    public PIDController posControllerRight;
    public PIDController posControllerLeft;
    private static DriveTrain instance = null;
    private PIDSource rightPosIn = new PIDSource() {

        public double pidGet() {
            try {
                //System.out.println(jagRightMaster.getPosition());
                return jagRightMaster.getPosition();
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
            try {
                // Jag is in Speed Control Mode
                // Output should be in rpm
                //SmartDashboard.putData("right", posControllerRight);
                jagRightMaster.setX(-2*output + gyro.getAngle()/ PIDConstants.gyroP);
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
                //return jagLeftMaster.getPosition();
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
                //SmartDashboard.putData("left", posControllerLeft);
                //PIDLeftOutput = 300 * output;
                jagLeftMaster.setX(2*output + gyro.getAngle()/ PIDConstants.gyroP);
                syncSlaves();
            } catch (CANTimeoutException ex) {
                ex.printStackTrace();
            }

        }
    };

    public void initDefaultCommand() {
        setDefaultCommand(new TankDrive());
    }

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
            //TODO: These PID Controllers may need to run faster than 50ms
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
        gyro = new CoyoBotGyro(1);
        gyro.setSensitivity(0.007);
        shifterHigh = new Solenoid(2);
        shifterLow = new Solenoid(3);
        shifterLow.set(false);
        shifterHigh.set(true);
        initPosMode();
        //initSpeedMode();
    }

    public void initPosMode() {
        System.out.println("Pos Mode");
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
            //jagLeftMaster.setX(0);
            //jagRightMaster.setX(0);
            //jagLeftMaster.setPID(PIDConstants.driveSpeedP, PIDConstants.driveSpeedI, PIDConstants.driveSpeedD);
            //jagRightMaster.setPID(-PIDConstants.driveSpeedP, -PIDConstants.driveSpeedI, -PIDConstants.driveSpeedD);
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
        posControllerLeft.enable();
        posControllerRight.enable();
    }

    public void initSpeedMode() {
        System.out.println("Speed Mode");
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
            jagLeftMaster.setX(0);
            jagRightMaster.setX(0);
            //jagLeftMaster.setPID(PIDConstants.driveSpeedP, PIDConstants.driveSpeedI, PIDConstants.driveSpeedD);
            //jagRightMaster.setPID(-PIDConstants.driveSpeedP, -PIDConstants.driveSpeedI, -PIDConstants.driveSpeedD);
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
        System.out.println("Why the hell are we running VBus Mode!");
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
            jagLeftMaster.setPID(PIDConstants.driveSpeedP, PIDConstants.driveSpeedI, PIDConstants.driveSpeedD);
            jagRightMaster.setPID(-PIDConstants.driveSpeedP, -PIDConstants.driveSpeedI, -PIDConstants.driveSpeedD);
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

    public double PIDPosROutput() {
        return PIDRightOutput;
    }

    public double PIDPosLOutput() {
        return PIDLeftOutput;
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
        posControllerRight.setSetpoint(-setpoint);
    }

    public void leftPosSetpoint(double setpoint) {
        if (controlMode != 3) {
            initPosMode();
        }
        posControllerLeft.setSetpoint(setpoint);
    }

    public void posSetpoint(double setpoint) {
        leftPosSetpoint(setpoint);
        rightPosSetpoint(setpoint);
        System.out.println("SetPoint "+setpoint );
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

    public double getRPos() {
        try {
            return (jagRightMaster.getPosition());
        } catch (CANTimeoutException ex) {
            canError = true;
            handleCANError();
            ex.printStackTrace();
            return 0;
        }
    }

    public double getLPos() {
        try {
            return (jagLeftMaster.getPosition());
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
            jagRightMaster.setX(-2*setpoint);
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

            jagLeftMaster.setX(2*setpoint);
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
            return (jagLeftMaster.getSpeed() + jagLeftMaster.getPosition()) / 2;
        } catch (CANTimeoutException ex) {
            canError = true;
            handleCANError();
            ex.printStackTrace();
            return 0;
        }
    }

    public double getLSpeed() {
        try {
            return (jagLeftMaster.getSpeed());
        } catch (CANTimeoutException ex) {
            canError = true;
            handleCANError();
            ex.printStackTrace();
            return 0;
        }
    }

    public double getRSpeed() {
        try {
            return (jagRightMaster.getSpeed());
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

    public void incPPos() {
        pPos += 1;
        printPIDPos();
    }

    public void decPPos() {
        pPos -= 1;
        printPIDPos();
    }

    public void incIPos() {
        iPos += 0.1;
        printPIDPos();
    }

    public void decIPos() {
        iPos -= 0.1;
        printPIDPos();
    }

    public void incPSpeed() {
        pSpeed += 0.01;
        printPIDSpeed();
    }

    public void decPSpeed() {
        pSpeed -= 0.01;
        printPIDSpeed();
    }

    public void incISpeed() {
        dSpeed += 0.1;
        printPIDSpeed();
    }

    public void decISpeed() {
        dSpeed -= 0.1;
        printPIDSpeed();
    }

    public CoyoBotGyro getGyro() {
        return gyro;
    }

    private void printPIDPos() {
        System.out.println("Pos P: " + pPos + " Pos I: " + iPos + " Pos D: " + dPos);
    }

    private void printPIDSpeed() {
        System.out.println("Speed P: " + pSpeed + " Speed I: " + iSpeed + " Speed D: " + dSpeed);
    }
}