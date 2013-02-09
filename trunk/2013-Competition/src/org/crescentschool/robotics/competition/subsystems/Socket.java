/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.SocketConnection;
import org.crescentschool.robotics.competition.commands.KinectDriveTest;

/**
 *
 * @author Warfa
 */
public class Socket extends Subsystem{
    static SocketConnection sc = null;
    static InputStream is;
    static OutputStream os;

   public static void startSocket() throws IOException{
       if(sc == null){
           sc =  (SocketConnection)Connector.open("socket://10.6.12.11:6060");
           sc.setSocketOption(SocketConnection.LINGER, 1);
           is = sc.openInputStream();
           os = sc.openOutputStream();
       }
   }
   
  public static InputStream getInStream(){
      return is;
  }
  public static OutputStream getOutStream(){
      return os;
  }

    protected void initDefaultCommand() {
    }

  
}
