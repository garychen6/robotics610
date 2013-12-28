package attendance;

import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.*;
import java.util.*;
import java.lang.*;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 *
 * @author Jamie
 * Problems: If a file has been made, but if the contents are erased, 
 */
public class Attendance_OldFinal extends JFrame{

    
    static public final String name = "Team 610 Attendance"; //Title of the program
    static public final int height = 1200; //Height of box
    static public final int width = 1650; // Width of box
    private JTextField nameBox;
    private Container contents;
    private JLabel title;
    private JLabel badName;
    private String nameEntry;
     String[] teamList = new String[]{
            "Abhinav Dhar", "Adam Murai", "Adrian Chan", "Aidan Oldershaw", "Alp Turkmen", "Avram Kachura", "Baron Alloway", "Charles Ju", "David Ferris", "Edwin Xu", "Elwyn Zhang", "Galen Frostad", "Gorav Menon", "Hugh McCauley", "Ian Donaldson", "Ian Lo", "Jacob Kachura", "Jake Fisher", "Jamie Kilburn", "Jamie Rose", "Jason Sauntry", "Jason Spevack", "Jeffrey Seto", "Jonathan Lau", "Jonathan Pearce", "Jordan Grant", "Joseph Kachura", "Mathhew Tory", "Matthew Lang", "Matthew Riley", "Max Liu", "Michael Hatsios", "Nathan Li", "Neal Ganguli", "Nick Haughton", "Nikesh Pandey", "Richard Robinson", "Ryan Fredrickson", "Ryan Tam", "Taran Ravindran", "Thomas Herring", "Timmy Seto", "Tyler Young", "Walter Raftus"
        };

    /**
     * @param args the command line arguments
     */
    
 
    public static void main(String[] args) throws IOException {
        Attendance_OldFinal Attendance_Final;
        Attendance_Final = new Attendance_OldFinal();
    }

    public Attendance_OldFinal() throws IOException {


        //The constant team size.
        setTitle(name); // sets the title
        setSize(width, height); //sets the size
        title = new JLabel("Please enter your full name.", SwingConstants.CENTER); //Instruction Label
        nameBox = new JTextField(20); //box for entry of info 
        badName = new JLabel();
        badName.setHorizontalAlignment(JLabel.CENTER);
        nameBox.setHorizontalAlignment(JTextField.CENTER);
        contents = getContentPane();
        contents.setLayout(new FlowLayout());
        contents.setBackground(Color.green.darker()); //make it a nice dark green        
        contents.add(title);
        contents.add(badName);
        contents.add(nameBox); 
        
        nameBox.addKeyListener(new KeyListener(){         //Checks if the enter key is pressed, if it is the entry is put into nameEntry, and the text is erased
              
            public void keyPressed(KeyEvent e){
                for(int i = 0; i< 44; i++){
                    if(e.getKeyCode() == KeyEvent.VK_ENTER){
                        nameEntry = nameBox.getText();                                             
                       
                      if(nameEntry.contains(teamList[i])){
                               nameBox.setText("");
                               break;   
                           }else{
                          nameBox.setText("Incorrect name.");
                      }
                      
                       
                    }       
                }      
                }
            public void keyTyped(KeyEvent ke) {                    
            }            
            public void keyReleased(KeyEvent ke) {            
        }
        
            });      
        setVisible(true); //Sets all the JLabels/JTextFields to be visible
        final int TEAM_SIZE = 44;
        String fileName = "";
       
        while (true) {
            System.out.println("running");
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

            } //In the case of the FileNotFoundException, a new file wil be created when writing.
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
          //  System.out.println("Enter your name:");
            //Save the input to a string.
            String name = nameEntry;
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
            if (!nameExists) {
                
        //        System.out.println("Your name was not found. Please check your spelling.");
            } else {
          //      System.out.println("Thank you for logging in.");
            }
        }
    }

    

    
}
