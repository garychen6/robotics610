/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package attendance;

import java.io.*;
import java.util.*;
import java.lang.*;
import java.text.SimpleDateFormat;

/**
 *
 * @author jamiekilburn
 */
public class Attendance {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        //The constant team size.
        final int TEAM_SIZE = 44;
        String fileName = "";
        String[] teamList
                = new String[]{
                    "Abhinav Dhar", "Adam Murai", "Adrian Chan", "Aidan Oldershaw", "Alp Turkmen", "Avram Kachura", "Baron Alloway", "Charles Ju", "David Ferris", "Edwin Xu", "Elwyn Zhang", "Galen Frostad", "Gorav Menon", "Hugh McCauley", "Ian Donaldson", "Ian Lo", "Jacob Kachura", "Jake Fisher", "Jamie Kilburn", "Jamie Rose", "Jason Sauntry", "Jason Spevack", "Jeffrey Seto", "Jonathan Lau", "Jonathan Pearce", "Jordan Grant", "Joseph Kachura", "Mathhew Tory", "Matthew Lang", "Matthew Riley", "Max Liu", "Michael Hatsios", "Nathan Li", "Neal Ganguli", "Nick Haughton", "Nikesh Pandey", "Richard Robinson", "Ryan Fredrickson", "Ryan Tam", "Taran Ravindran", "Thomas Herring", "Timmy Seto", "Tyler Young", "Walter Raftus"

                };
        while (true) {
            //Get the current date.
            Calendar currentDate = Calendar.getInstance();
            //Format the date into a format that will work for us. 
            SimpleDateFormat sdf = new SimpleDateFormat("dd_MM_yyyy");
            //Create the filename according to the date. The file format will be csv.
            fileName = "attendance_" + sdf.format(currentDate.getTime()) + ".csv";

            //Array that will contain whether the team member is here or not. The first value will be the name, the index 1 in the second dimension will be whether they are present or not.
            String[][] attendanceList = new String[TEAM_SIZE][2];

            //An error will not be thrown if the file already exists. i.e. a file has already been created for that day.
            try {
                //Create a fileReader for the attendancesheet that already exists.
                FileReader in = new FileReader(fileName);
                //Create a scanner that will read the names and their attendance values.
                //The values will be separated by a comma each time. This is why a comma is needed at the end of each line as well.
                Scanner inScan = new Scanner(in).useDelimiter(",");
                //Iterate through the attendance array.
                for (int i = 0; i < TEAM_SIZE; i++) {
                    //Set the first value to the name, the next to the attendance value.
                    //Use the constant teamList Array to avoid any weird characters sneaking in when reading the file.
                    attendanceList[i][0] = teamList[i];
                    inScan.next();
                    //Use the substring to only get the first character. This eliminates any \n nonsense that may have been read.
                    attendanceList[i][1] = inScan.next().substring(0, 1);

                }

            } 
//In the case of the FileNotFoundException, a new file wil be created when writing.
            //Start the arrays from scratch.
            catch (FileNotFoundException e) {
                //Iterate through the attendance array.
                for (int i = 0; i < TEAM_SIZE; i++) {
                    //Set the first value to the name, the next to the attendance value.
                    attendanceList[i][0] = teamList[i];
                    attendanceList[i][1] = "N";
                }
            }
            //Create a new scanner to take the user's input.
            Scanner sc = new Scanner(System.in);
            //Prompt the user.
            System.out.println("Enter your name:");
            //Save the input to a string.
            String name = sc.nextLine();
            //Create a filewriter for our file using the fileName. Old files will be overwritten and if the file doesn't exist, a new file will be created.
            FileWriter outFile = new FileWriter(fileName);
            //Check if the name exists.
            boolean nameExists = false;
            //Iterate through the array.
            for (int i = 0; i < TEAM_SIZE; i++) {
                //If the input matches a name,
                if (attendanceList[i][0].equals(name)) {
                    //Set the attendance value to Y
                    attendanceList[i][1] = "Y";
                    nameExists = true;
                }
                //Write it to the csv file.
                outFile.write(attendanceList[i][0] + "," + attendanceList[i][1] + ",\r\n");
            }
            //Close the csv file to ensure nothing is corrupted between attendance inputs.
            outFile.close();
            if(!nameExists){
                System.out.println("Your name was not found. Please check your spelling.");
            } else {
                System.out.println("Thank you for logging in.");
            }
        }

    }
}
