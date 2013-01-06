/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ksi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

/**
 *
 * @author Warfa
 */
public class TeamSheet {

    String teamName;
    double avgAuton = 0;
    double avgAccuracyAuton = 0;
    double avgTeleOp = 0;
    double avgAccuracyTeleop = 0;
    double avgDefenseRanking = 0;
    int numMatches = 0;

    TeamSheet(String teamName) {
        this.teamName = teamName;
    }

    public void addSheet(File f) throws FileNotFoundException {
        FileReader inFile = new FileReader(f);
        Scanner scan = new Scanner(inFile);
        String line = scan.nextLine();
        String[] info = line.split("@");
        for (int i = 0; i < info.length; i++) {
            System.out.println(info[i]);
        }
    }
}
