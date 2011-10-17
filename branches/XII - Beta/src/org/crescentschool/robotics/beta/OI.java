/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.crescentschool.robotics.beta;

public class OI {
    
    private static OI instance = null;
    
    private OI() {
    }
    
    public static OI getInstance() {
    if (instance == null) 
            instance = new OI();
        return instance;
    }
}

