package edu.wpi.first.wpilibj.templates.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.templates.OI;
import edu.wpi.first.wpilibj.templates.subsystems.Socket;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author bradmiller
 */
public class HandleKinect extends Command {

    InputStream is;
    OutputStream os;
    String data;
    Joystick joyDriver;
    OI oi;
    double depth,x,y;
    public HandleKinect() throws IOException {
        Socket.getInstance();
        oi = oi.getInstance();
        joyDriver = oi.getDriver();
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        is = Socket.getInStream();
        os = Socket.getOutStream();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        data = "";
        byte[] msg = new byte[1000];
        if(joyDriver.getRawButton(4)){
        try {
             os.write("<request><get_variable>depth,x,y</get_variable></request>".getBytes());
              is.read(msg);
              String message = new String(msg);
              depth = retrieveVal(message,"depth");
              x = retrieveVal(message,"x");
              y = retrieveVal(message,"y");
              System.out.println("X: "+x+" Y: "+y+" Depth: "+depth);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        }
    }
    public static double retrieveVal(String msg,String variable){
         String subMsg = msg.substring(msg.indexOf(variable),msg.indexOf(variable,msg.indexOf(variable)+1));
         subMsg = subMsg.substring(subMsg.indexOf(">"), subMsg.length()-2);
         return(Double.parseDouble(subMsg));
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
