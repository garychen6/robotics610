package org.crescentschool.robotics.competition.commands;

import com.sun.squawk.util.MathUtils;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.crescentschool.robotics.competition.OI;
import org.crescentschool.robotics.competition.constants.InputConstants;
import org.crescentschool.robotics.competition.subsystems.DriveTrain;
import org.crescentschool.robotics.competition.subsystems.Socket;

/**
 *
 * @author bradmiller
 */
public class KinectDriveTest extends Command {

    InputStream is;
    OutputStream os;
    String data;
    Joystick joyDriver;
    OI oi;
    double offset;
    Gyro gyro;
    double area;
    DriveTrain driveTrain;
    double angleTurn = 0;
    double prevOffset = 0;
    boolean pressed = false;
    
    public KinectDriveTest() throws IOException {
        Socket.startSocket();
        driveTrain = DriveTrain.getInstance();
        oi = OI.getInstance();
        //Gyro gyro = new Gyro(ElectricalConstants.gyroAnalogInput);
        //requires(driveTrain);
        //joyDriver = oi.getDriver();
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);

    }

    // Called just before this Command runs the first time
    protected void initialize() {
        is = Socket.getInStream();
        os = Socket.getOutStream();
    }

    public double getOffset() {
        return offset;
    }
    // Called repeatedly when this Command is scheduled to run

    protected void execute() {

        if (OI.getInstance().getOperator().getRawButton(InputConstants.squareButton)) {
            if(!pressed){
                driveTrain.resetGyro();
            }
            pressed = true;
            data = "";
            byte[] msg = new byte[1000];

            try {
                os.write("<request><get_variable>OFFSET</get_variable></request>".getBytes());
                is.read(msg);
                String message = new String(msg);
                offset = retrieveVal(message, "OFFSET");


            } catch (IOException ex) {
                ex.printStackTrace();
            }
            SmartDashboard.putNumber("OFFSET", offset);
            angleTurn = Math.toDegrees(MathUtils.atan(offset * Math.tan(Math.toRadians(28.5))));
            
            SmartDashboard.putNumber("AngleTurn", angleTurn);
            driveTrain.setAngle(angleTurn,offset!=prevOffset);
            prevOffset = offset;
        }else{
            pressed = false;
        }

    }

    public static double retrieveVal(String msg, String variable) {
        String subMsg = msg.substring(msg.indexOf(variable), msg.indexOf(variable, msg.indexOf(variable) + 1));
        subMsg = subMsg.substring(subMsg.indexOf(">") + 1, subMsg.length() - 2);
        return (Double.parseDouble(subMsg));
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
