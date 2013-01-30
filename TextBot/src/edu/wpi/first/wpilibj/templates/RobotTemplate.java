/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.templates;

import com.sun.squawk.microedition.io.FileConnection;
import edu.wpi.first.wpilibj.IterativeRobot;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.microedition.io.Connector;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotTemplate extends IterativeRobot {

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        try {
            FileConnection fc;
            InputStreamReader reader;
            char[] word = new char[100000];
            char check = 0;
            int count = 0;
            int length;
            int k;
            int decimal;
            double[] constants = new double[50];

            fc = (FileConnection) Connector.open("file:///" + "Constants.txt", Connector.READ);
            reader = new InputStreamReader(fc.openInputStream());
            reader.read(word);
            k = 0;
            while (check != '*') {
                while (check != '=') {
                    check = word[count];
                    count++;
                }
                length = word[count];

                count += 2;

                decimal = 0;
                
                for (int i = 1; i <= length; i++) {
                    if(word[count] == '.'){
                        decimal = length - i;
                        constants[k] /= 10;
                    }
                    constants[k] += tenPower(length - i) * (double)(word[count++]);
                }
                constants[k] /= tenPower(decimal);
                k++;
                check = word[++count];
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
    }

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    }
    
    public double tenPower(double power){
        int product = 1;
        for(int i = 0; i < power; i ++){
            product *= 10;
        }
        return product;
    }
    
}
