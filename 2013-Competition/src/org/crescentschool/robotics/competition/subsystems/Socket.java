package org.crescentschool.robotics.competition.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.SocketConnection;

/**
 *
 * @author Warfa
 */
public class Socket extends Subsystem {

    static SocketConnection sc = null;
    static InputStream is;
    static OutputStream os;
    static boolean socketConnected = false;

    public static void startSocket() {
        if (sc == null) {
            try {
                sc = (SocketConnection) Connector.open("socket://10.6.10.11:6060");
                sc.setSocketOption(SocketConnection.LINGER, 1);
                is = sc.openInputStream();
                os = sc.openOutputStream();
                socketConnected = true;
            } catch (IOException ex) {
                Logger.getLogger().debug("Can't Connect to Socket");
                SmartDashboard.putString("Messages", "Can't Connect to Socket");
                socketConnected = false;
            }
        }
    }

    public static boolean getConnected() {
        return socketConnected;
    }

    public static InputStream getInStream() {
        return is;
    }

    public static OutputStream getOutStream() {
        return os;
    }

    protected void initDefaultCommand() {
    }
}
