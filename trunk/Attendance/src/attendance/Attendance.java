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
        
      String []rawList = new String[50];  //their names + y'n
      String []nameList = new String[50];
      String []hereList = new String[50];
      
      String []parts = new String[50];
     
        BufferedReader in = new BufferedReader(new FileReader("Attendance122_1.csv"));
        Scanner sc = new Scanner(System.in);
        for(int i=0; i<20;i++){
          
          rawList[i] = in.readLine();
           parts = rawList[i].split(",");
            hereList[i] = parts[1];
            nameList[i] = parts[0];
            
           
           
        //    System.out.println(list3);
        }
        
        
        String[] here;
        
     //   here = sc.nextLine();
        String[] list;
        list = new String[]{
            "Ryan Tam", "Taran Ravindran", "Matthew Riley", "Jonathan Pearce", "Jacob Kachura", "Avram Kachura", "Thomas Herring", "Jason Sauntry", "Jonathan Lau", "Max Liu", "Abhinav Dhar", "Jamie Rose", "Timmy Seto", "Walter Raftus", "Richard Robinson", "Adrian Chan", "Hugh McCauley", "Jake Fisher", "Ian Donaldson", "Nikesh Pandey", "Matthew Lang", "Joseph Kachura", "Alp Turkmen", "Tyler Young", "Nathan Li", "Michael Hatsios", "Nick Haughton", "David Ferris", "Ian Lo", "Jamie Kilburn", "Aidan Oldershaw", "Jeffrey Seto", "Elwyn Zhang", "Charles Ju", "Adam Murai", "Mathhew Tory", "Jason Spevack", "Neal Ganguli", "Gorav Menon", "Ryan Fredrickson", "Edwin Xu", "Galen Frostad", "Jordan Grant", "Baron Alloway\n"
        };

        System.out.println("Enter your name");
        String name = sc.nextLine();    

        if (Arrays.asList(list).contains(name)) {
            System.out.println("Thanks for logging on!");
            //now add a Y next to their name
        } else {
            System.out.println("Sorry, check your spelling and make sure you're using your full name + Caps!");
        }
        int length = Array.getLength(list);
        
        
        
for(int i=0; i<20; i++){
    if (list[i].equals(name)) {
 //       here[i] = "Y\n";
    }
    
    
}
        FileWriter outFile = new FileWriter("Attendance122.csv");

        
        for(int i=0; i<20; i++){
    if (nameList[i].equals(name)) {
             hereList[i] = "Y\n";
             System.out.println(hereList[i]);
    }
    
    
}
        for(int i=0; i<20; i++){
            outFile.write(hereList[i]);
            
                   
           
 //           outFile.write(here[i] + ",");
        }
      
        


        outFile.close();
        







        // TODO code application logic here
    }
}
