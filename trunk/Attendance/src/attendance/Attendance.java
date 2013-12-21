/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package attendance;

import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.Array;
import java.util.*;
import java.lang.*;

/**
 *
 * @author jamiekilburn
 */
public class Attendance {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        while (true) {
            String[] rawList = new String[50];  //their names + y'n
            String[] nameList = new String[50];
            String[] hereList = new String[50];

            String[] parts = new String[50];

            BufferedReader in = new BufferedReader(new FileReader("AttendanceForm.csv"));
            Scanner sc = new Scanner(System.in);
            for (int i = 0; i < 44; i++) {

                rawList[i] = in.readLine();
                parts = rawList[i].split(",");
                hereList[i] = parts[1];
                nameList[i] = parts[0];
            }
            String[] list;
            list = new String[]{
                "Ryan Tam", "Taran Ravindran", "Matthew Riley", "Jonathan Pearce", "Jacob Kachura", "Avram Kachura", "Thomas Herring", "Jason Sauntry", "Jonathan Lau", "Max Liu", "Abhinav Dhar", "Jamie Rose", "Timmy Seto", "Walter Raftus", "Richard Robinson", "Adrian Chan", "Hugh McCauley", "Jake Fisher", "Ian Donaldson", "Nikesh Pandey", "Matthew Lang", "Joseph Kachura", "Alp Turkmen", "Tyler Young", "Nathan Li", "Michael Hatsios", "Nick Haughton", "David Ferris", "Ian Lo", "Jamie Kilburn", "Aidan Oldershaw", "Jeffrey Seto", "Elwyn Zhang", "Charles Ju", "Adam Murai", "Mathhew Tory", "Jason Spevack", "Neal Ganguli", "Gorav Menon", "Ryan Fredrickson", "Edwin Xu", "Galen Frostad", "Jordan Grant", "Baron Alloway"
            };

            System.out.println("Enter your name");
            String name = sc.nextLine();
            if (Arrays.asList(list).contains(name)) {
                System.out.println("Thanks for logging on!");
                //now add a Y next to their name
            } else {
                System.out.println("Sorry, check your spelling and make sure you're using your full name!");
            }
            FileWriter outFile = new FileWriter("AttendanceForm.csv");
            for (int i = 0; i < 44; i++) {
                if (list[i].equals(name)) {
                    hereList[i] = "Y";
                    System.out.println(hereList[i]);
                }
                if (hereList[i] == "Y") {
                    hereList[i] = "Y";
                }
                outFile.write(list[i] + "," + hereList[i] + "\n");                              
            }
            outFile.close();
        }
        
    }
}
