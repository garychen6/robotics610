/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cleanupdata;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Ian
 */
public class CleanUpData {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        String dir = "C:\\Users\\Ian\\Desktop\\Waterloo data";
        File dr = new File(dir);
        File[] folders = dr.listFiles();
        ArrayList<File[]> files = new ArrayList();
        for (int i = 0; i < folders.length; i++) {
            files.add(folders[i].listFiles());
        }
        File newData = new File(dir + "\\newData.csv");
        FileWriter outFile = new FileWriter(newData);;

        outFile.write("Team Number,Match Number,Starting Position,Autonomous Points,Teleop Pyramid Goal Discs Attempted,Teleop High Goal Discs Attempted,Teleop Middle Goal Discs Attempted,Teleop Low Goal Discs Attempted,Teleop Pyramid Goal Scored,Teleop High Goal Scored, Teleop Middle Goal Scored, Teleop Low Goal Scored, Defense, Hang Level, Hang Time\r\n");
        for (int i = 0; i < files.size(); i++) {
            String line = "";
            for (File f : files.get(i)) {
                if (!f.getName().equals("Comments.txt")) {
                    FileReader inFile = new FileReader(f);
                    Scanner sc = new Scanner(inFile);
                    while (sc.hasNext()) {
                        line += sc.nextLine() + ",";
                    }
                    line += "\r\n";
                }

            }

            outFile.write(line);
        }
        outFile.close();
    }
}
