/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.crescentschool.robotics.competition.OI;
import org.crescentschool.robotics.competition.subsystems.Socket;

/**
 *
 * @author Warfa
 */
public class AutoSpeed extends Command {

    InputStream is;
    OutputStream os;
    String data;
    Joystick joyDriver;
    OI oi;
    double depth, x, y;

    public AutoSpeed() throws IOException {
        Socket.getInstance();
        oi = OI.getInstance();
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
        // if(joyDriver.getRawButton(4)){
        try {
            os.write("<request><get_variables>Distance</get_variables></request>".getBytes());
            is.read(msg);
            String message = new String(msg);
            depth = retrieveVal(message, "Distance");
            SmartDashboard.putNumber("Depth:",depth);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        //  }
    }

    public static double retrieveVal(String msg, String variable) {
        int indexOfFirstVar = msg.indexOf(variable);
        String subMsg = msg.substring(indexOfFirstVar, msg.indexOf(variable, indexOfFirstVar + 1));
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
