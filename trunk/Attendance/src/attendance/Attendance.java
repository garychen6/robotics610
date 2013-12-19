/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package attendance;

import java.io.*;
import java.util.*;

/**
 *
 * @author jamiekilburn
 */
public class Attendance {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        String[] List;
        List = new String[]{
            "Ryan Tam", "Taran Ravindran", "Matthew Riley", "Jonathan Pearce", "Jacob Kachura", "Avram Kachura", "Thomas Herring", "Jason Sauntry", "Jonathan Lau", "Max Liu", "Abhinav Dhar", "Jamie Rose", "Timmy Seto", "Walter Raftus", "Richard Robinson", "Adrian Chan", "Hugh McCauley", "Jake Fisher", "Ian Donaldson", "Nikesh Pandey", "Matthew Lang", "Joseph Kachura", "Alp Turkmen", "Tyler Young", "Nathan Li", "Michael Hatsios", "Nick Haughton", "David Ferris", "Ian Lo", "Jamie Kilburn", "Aidan Oldershaw", "Jeffrey Seto", "Elwyn Zhang", "Charles Ju", "Adam Murai", "Mathhew Tory", "Jason Spevack", "Neal Ganguli", "Gorav Menon", "Ryan Fredrickson", "Edwin Xu", "Galen Frostad", "Jordan Grant", "Baron Alloway"
        };

        System.out.println("Enter your name");
        String name = sc.nextLine();

        if (Arrays.asList(List).contains(name)) {
            System.out.println("Thanks for logging on!");
            //now add a Y next to their name
        } else {
            System.out.println("Sorry, check your spelling and make sure you're using your full name + Caps!");
        }
        




        FileWriter outFile = new FileWriter("Attendance12.csv");

        outFile.write(Arrays.toString(List));
        outFile.write("N");


        outFile.close();







        // TODO code application logic here
    }
}
