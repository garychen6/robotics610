/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package matchsimulation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JOptionPane;

/**
 *
 * @author Ian
 */
public class MatchSimulation {

    /**
     * @param args the command line arguments
     */
    static int[] teams;
    static double[] kprs;
    static int[][] matches;
    static double[][] kprMatches;
    static int[][] qs;
    static int numMatches;
    static String path;
    static int[][] originalQs;
    static int played;

    public static void main(String[] args) {
        readFiles();
        MatchSimulationFrame frame = new MatchSimulationFrame();
        frame.setResizable(false);
        frame.setBounds(100, 100, 100, 80);

        played = Integer.parseInt(JOptionPane.showInputDialog("How many matches have been completed?"));

        simulate(played);
        Date date1 = new Date();
        System.out.println(date1 + " Done Simulating");

        JButton update = new JButton("Update");
        update.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateRankings();
                Date date = new Date();
                System.out.println(date + " Done Updating");
            }
        });
        update.setBounds(10, 10, 100, 30);
        frame.add(update);
        frame.setVisible(true);

    }

    public static void updateRankings() {
        try {
            File results = new File(path + "\\MatchResults.csv");
            for (int i = 0; i < teams.length; i++) {
                qs[getIndex(teams[i])][0] = originalQs[getIndex(teams[i])][0];
                qs[getIndex(teams[i])][1] = originalQs[getIndex(teams[i])][1];
                qs[getIndex(teams[i])][2] = originalQs[getIndex(teams[i])][2];
            }

            Scanner sc = new Scanner(results);
            ArrayList<String> lines = new ArrayList();
            sc.nextLine();
            while (sc.hasNextLine()) {
                lines.add(sc.nextLine());
            }
            for (int i = 0; i < lines.size(); i++) {
                sc = new Scanner(lines.get(i));
                sc.useDelimiter(",");
                sc.next();
                sc.next();
                sc.next();
                sc.next();
                sc.next();
                sc.next();
                sc.next();
                sc.next();
                String winner = sc.next();
                switch (winner) {
                    case "Red":
                        qs[getIndex(matches[played - 1 + i][0])][1] += 2;
                        qs[getIndex(matches[played - 1 + i][1])][1] += 2;
                        qs[getIndex(matches[played - 1 + i][2])][1] += 2;
                        break;
                    case "Blue":
                        qs[getIndex(matches[played - 1 + i][4])][1] += 2;
                        qs[getIndex(matches[played - 1 + i][5])][1] += 2;
                        qs[getIndex(matches[played - 1 + i][6])][1] += 2;
                        break;
                }
                if (sc.hasNext()) {
                    String string = (sc.next());
                    if (!string.equals("")) {
                        int auton = Integer.parseInt(string);
                        qs[getIndex(matches[played - 1 + i][0])][2] += auton;
                        qs[getIndex(matches[played - 1 + i][1])][2] += auton;
                        qs[getIndex(matches[played - 1 + i][2])][2] += auton;
                    }
                }
                if (sc.hasNext()) {
                    String string = (sc.next());
                    if (!string.equals("")) {
                        int auton2 = Integer.parseInt(string);
                        qs[getIndex(matches[played - 1 + i][4])][2] += auton2;
                        qs[getIndex(matches[played - 1 + i][5])][2] += auton2;
                        qs[getIndex(matches[played - 1 + i][6])][2] += auton2;
                    }

                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MatchSimulation.class.getName()).log(Level.SEVERE, null, ex);
        }
        writeRankings();
    }

    public static void simulate(int played) {
        File dir1 = new File(path);
        File results = new File(path + "\\MatchResults.csv");
        FileWriter f1;

        try {
            results.createNewFile();

            results.setReadable(true);
            results.setWritable(true);
            f1 = new FileWriter(results);

            f1.write("Red 1, Red 2, Red 3, Red Sum, Blue 1, Blue 2, Blue 3, Blue Sum\r\n");

            for (int i = played - 1; i < numMatches; i++) {
                String line = "";
                line += matches[i][0] + ",";
                line += matches[i][1] + ",";
                line += matches[i][2] + ",";
                line += Math.round(kprMatches[i][3] * 10) / 10.0 + ",";
                line += matches[i][4] + ",";
                line += matches[i][5] + ",";
                line += matches[i][6] + ",";
                line += Math.round(kprMatches[i][7] * 10) / 10.0 + ",";

                f1.write(line);
                if (Math.abs(kprMatches[i][3] - kprMatches[i][7]) < 10) {
                    f1.write("Undecided\r\n");
                } else if (kprMatches[i][3] > kprMatches[i][7]) {
                    f1.write("Red\r\n");

                } else if (kprMatches[i][3] < kprMatches[i][7]) {
                    f1.write("Blue\r\n");

                }


            }
            f1.close();
            updateRankings();
        } catch (IOException ex) {
        }

        writeRankings();




    }

    public static void writeRankings() {
        ArrayList<Integer> qsList = new ArrayList();
        ArrayList<Integer> qsTeams = new ArrayList();
        ArrayList<Integer> qsHP = new ArrayList();

        for (int i = 0; i < teams.length; i++) {
            qsList.add(qs[i][1]);
            qsTeams.add(qs[i][0]);
            qsHP.add(qs[i][2]);
        }
        ArrayList<String> newRankings = new ArrayList();
        while (qsList.size() > 0) {
            int largestIndex = 0;
            int largest = -1;
            for (int i = 0; i < qsList.size(); i++) {

                if (qsList.get(i) > largest) {
                    largest = qsList.get(i);
                    largestIndex = i;
                }
                if (qsList.get(i) == largest && qsHP.get(i) > qsHP.get(largestIndex)) {
                    largest = qsList.get(i);
                    largestIndex = i;
                }
            }
            newRankings.add(qsTeams.get(largestIndex) + "," + qsList.get(largestIndex) + "," + qsHP.get(largestIndex) + "\r\n");
            qsList.remove(largestIndex);
            qsHP.remove(largestIndex);
            qsTeams.remove(largestIndex);
        }

        File dir = new File(path);
        File newR = new File(path + "\\" + "NewRankings.csv");
        try {
            newR.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(MatchSimulation.class.getName()).log(Level.SEVERE, null, ex);
        }
        newR.setReadable(true);
        newR.setWritable(true);
        try {
            FileWriter fw = new FileWriter(path + "\\" + "NewRankings.csv");
            fw.write("Team,QS,HP\r\n");
            while (newRankings.size() > 0) {
                fw.write(newRankings.get(0));
                newRankings.remove(0);
            }
            fw.close();
        } catch (IOException ex) {
            Logger.getLogger(MatchSimulation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void readFiles() {
        path = JOptionPane.showInputDialog("Enter your file path.");
        File dir = new File(path);
        File[] files = dir.listFiles();
        File matchSchedule = files[0];
        File rankings = files[0];
        File kpr = files[0];
        for (File file : files) {
            if (file.getName().equals("MatchSchedule.csv")) {
                matchSchedule = file;
            }
            if (file.getName().equals("Rankings.csv")) {
                rankings = file;
            }
            if (file.getName().equals("Kpr.txt")) {
                kpr = file;
            }
        }

        try {

            FileReader kprRead = new FileReader(kpr.getAbsoluteFile());
            Scanner kprScan = new Scanner(kprRead);
            ArrayList<String> lines = new ArrayList();
            int numTeams = 0;
            while (kprScan.hasNextLine()) {
                numTeams++;
                lines.add(kprScan.nextLine());
            }


            teams = new int[numTeams];
            kprs = new double[numTeams];
            qs = new int[numTeams][3];
            originalQs = new int[numTeams][3];

            for (int i = 0; i < numTeams; i++) {

                Scanner sc = new Scanner(lines.get(i));
                teams[i] = (int) sc.nextInt();
                kprs[i] = Math.round(sc.nextDouble() * 10) / 10.0;
            }


            FileReader matchRead = new FileReader(matchSchedule.getAbsoluteFile());
            Scanner sc = new Scanner(matchRead);
            lines = new ArrayList();
            numMatches = -1;
            sc.nextLine();
            while (sc.hasNextLine()) {
                numMatches++;
                lines.add(sc.nextLine());
            }

            kprMatches = new double[numMatches][8];
            matches = new int[numMatches][8];
            for (int i = 0; i < numMatches; i++) {

                sc = new Scanner(lines.get(i));
                sc.useDelimiter(",");

                sc.next();
                sc.next();
                matches[i][0] = Integer.parseInt(sc.next());
                matches[i][1] = Integer.parseInt(sc.next());
                matches[i][2] = Integer.parseInt(sc.next());
                matches[i][4] = Integer.parseInt(sc.next());
                matches[i][5] = Integer.parseInt(sc.next());
                matches[i][6] = Integer.parseInt(sc.next());

                kprMatches[i][0] = kprs[getIndex(matches[i][0])];
                kprMatches[i][1] = kprs[getIndex(matches[i][1])];
                kprMatches[i][2] = kprs[getIndex(matches[i][2])];

                kprMatches[i][4] = kprs[getIndex(matches[i][4])];
                kprMatches[i][5] = kprs[getIndex(matches[i][5])];
                kprMatches[i][6] = kprs[getIndex(matches[i][6])];

                kprMatches[i][3] = kprMatches[i][0] + kprMatches[i][1] + kprMatches[i][2];
                kprMatches[i][7] = kprMatches[i][4] + kprMatches[i][5] + kprMatches[i][6];

            }
            FileReader rankRead = new FileReader(rankings.getAbsoluteFile());
            sc = new Scanner(rankRead);
            sc.useDelimiter(",");
            sc.nextLine();

            for (int i = 0; i < teams.length; i++) {
                String line = sc.nextLine();
                Scanner sc2 = new Scanner(line);
                sc2.useDelimiter(",");
                sc2.next();
                int team = Integer.parseInt(sc2.next());
                qs[getIndex(team)][0] = team;
                qs[getIndex(team)][1] = Integer.parseInt(sc2.next());
                qs[getIndex(team)][2] = Integer.parseInt(sc2.next());
                originalQs[getIndex(team)][0] = qs[getIndex(team)][0];
                originalQs[getIndex(team)][1] = qs[getIndex(team)][1];
                originalQs[getIndex(team)][2] = qs[getIndex(team)][2];
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(MatchSimulation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static int getIndex(int team) {
        for (int i = 0; i < teams.length; i++) {
            if (teams[i] == team) {
                return i;
            }
        }
        return -1;
    }
}
