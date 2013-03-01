/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Warfa
 */
public class ServerSockets {
    ServerSocket sc;
    
   ServerSockets(){
        try {
            sc = new ServerSocket(610);
            
        } catch (IOException ex) {
            Logger.getLogger(ServerSockets.class.getName()).log(Level.SEVERE, null, ex);
        }
   }
}
