package org.crescentschool.robotics.competition.commands;

import com.sun.squawk.util.MathUtils;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.crescentschool.robotics.competition.OI;
import org.crescentschool.robotics.competition.subsystems.DriveTrain;
import org.crescentschool.robotics.competition.subsystems.Pneumatics;
import org.crescentschool.robotics.competition.subsystems.*;

/**
 *
 * @author bradmiller
 */
public class LockOn extends Command {

    InputStream is;
    OutputStream os;
    InputStream is2;
    OutputStream os2;
    String data;
    private double height;
    Joystick joyDriver;
    OI oi;
    private double offset;
    Gyro gyro;
    double area;
    DriveTrain driveTrain;
    double angleTurn = 0;
    double prevOffset = 0;
    boolean pressed = false;
    boolean finished = false;
    boolean sideLocked = false;
    boolean locked = false;
    Pneumatics pneumatics;
    double angle;
    double error = 0;
    double errorI = 0;
    Preferences constantsTable = Preferences.getInstance();
    OurTimer time;

    public LockOn() throws IOException {
        Socket.startSocket();
        driveTrain = DriveTrain.getInstance();
        oi = OI.getInstance();
        pneumatics = Pneumatics.getInstance();
        requires(driveTrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        is = Socket.getInStream();
        os = Socket.getOutStream();
        driveTrain.resetGyro();

        System.out.println("Trying to track");
        if (Socket.getConnected()) {
            data = "";
            byte[] msg = new byte[1000];

            try {
                os.write("<request><get_variable>OFFSET</get_variable></request>".getBytes());
                is.read(msg);
                String message = new String(msg);
                offset = retrieveVal(message, "OFFSET");
                System.out.println(message);

            } catch (IOException ex) {
                ex.printStackTrace();
            }
//        if (offset < 0) {
//            offset = Math.max(offset, -0.2);
//        } else {
//            offset = Math.min(offset, 0.2);
//        }
            SmartDashboard.putNumber("OFFSET", offset);
//        if(Math.abs(offset)< 0.03){
//            finished = true;
//        }
        } else {
            SmartDashboard.putString("Messages", "Aborting LockON");
            finished = true;
        }


        angle = Math.toDegrees(MathUtils.atan(offset * Math.tan(Math.toRadians(28.5))));

    }

    public double getOffset() {
        return offset;
    }
    // Called repeatedly when this Command is scheduled to run

    protected void execute() {
                OurTimer time = OurTimer.getTimer("LockOn");

//        if (offset != prevOffset) {
//            errorI = 0;
//            //driveTrain.setAngle(angleTurn, offset != prevOffset, 3);
//        }
        //i = 0.003
        //p = 0.05
        //window = 25
        double ff = 0;
        double i = 0.06;
        double p = 0.04;
        error = angle - driveTrain.getGyro().getAngle();

        //Window I = 10
        if (Math.abs(error) < constantsTable.getDouble("windowI", 0)) {
            errorI += error;
        }

        //errorI = Math.min(0.4 / i, errorI);

        if (Math.abs(error) < Math.abs(Math.toDegrees(MathUtils.atan(0.015 * Math.tan(Math.toRadians(28.5)))))) {
            errorI = 0;
            error = 0;
        }

        driveTrain.setRightVBus(Math.max(-0.3, (error * -p - i * errorI - ff * angle)));
        driveTrain.setLeftVBus(Math.min(0.3, (error* p + i * errorI + ff * angle)));

        System.out.println("Angle: " + angle + " Gyro: " + driveTrain.getGyro().getAngle() + " error: " + error + " errorI: " + errorI);
        System.out.println("Tracking");
        prevOffset = offset;
        time.stop();
    }

    public static double retrieveVal(String msg, String variable) {
        String subMsg = msg.substring(msg.indexOf(variable), msg.indexOf(variable, msg.indexOf(variable) + 1));
        subMsg = subMsg.substring(subMsg.indexOf(">") + 1, subMsg.length() - 2);
        return (Double.parseDouble(subMsg));
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return finished;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
