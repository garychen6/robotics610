
package edu.wpi.first.wpilibj.templates.subsystems;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.SocketConnection;

/**
 *
 */
public class Socket  {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    static SocketConnection sc = null;
    static InputStream is;
    static OutputStream os;

   public static void getInstance() throws IOException{
       if(sc == null){
           sc =  (SocketConnection)Connector.open("socket://10.6.10.49:6060");
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
}

