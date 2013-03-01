/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ksiserver;

import java.io.File;
import java.io.FileWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author billylo
 */
class WorkerThread extends Thread {

    private byte[] data = new byte[255];
    private Socket socket;
    private String[] info = new String[16];

    public WorkerThread(Socket socket) {

        for (int i = 0; i < data.length; i++) {
            data[i] = (byte) i;
        }
        this.socket = socket;
    }

    public void run() {
        try {
            Scanner scan = new Scanner(socket.getInputStream());
            while (scan.hasNext()) {
                    for (int i = 0; i < info.length; i++) {
                        info[i] = scan.nextLine();
                    }
                    SocketServer.write(socket.getInetAddress().getHostAddress() + " added info about " + info[0] + " for Match: " + info[1]);
                    if (!SocketServer.containsTeam(info[0])) {
                        SocketServer.addTeam(info[0]);
                        File dir = new File(SocketServer.getDataBase() + "\\" + info[0]);
                        dir.mkdir();
                        dir.setReadable(true);
                        File commentFile = new File(SocketServer.getDataBase() + "\\" + info[0] + "\\" + "Comments.txt");
                        commentFile.createNewFile();
                        commentFile.setReadable(true);
                        commentFile.setWritable(true);
                    }
                    FileWriter fl = new FileWriter(SocketServer.getDataBase() + "\\" + info[0] + "\\" + info[0] + "-" + info[1] + ".txt");
                    for (int i = 0; i < info.length - 1; i++) {
                        fl.write(info[i] + "\r\n");
                    }
                    fl.close();
                    fl = new FileWriter(SocketServer.getDataBase() + "\\" + info[0] + "\\" + "Comments.txt", true);
                    fl.write(info[info.length - 1] + "\r\n");
                    fl.close();
            }
             SocketServer.write(socket.getInetAddress().getHostAddress() + " disconnected!");
             socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SocketServer.decrementThreadCount();
        }
    }
}