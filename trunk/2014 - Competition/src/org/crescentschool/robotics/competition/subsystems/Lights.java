/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.subsystems;

import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.crescentschool.robotics.competition.constants.ElectricalConstants;

/**
 *
 * @author Adam Murai
 */
public class Lights extends Subsystem {

//    DigitalOutput bit1 = new DigitalOutput(ElectricalConstants.arduinoOut1),
//            bit2 = new DigitalOutput(ElectricalConstants.arduinoOut2),
//            bit3 = new DigitalOutput(ElectricalConstants.arduinoOut3);
    public static final int BLUE_PRE = 0;
    public static final int RED_PRE = 4;
    public static final int HOT_LEFT_BLUE = 1;
    public static final int HOT_RIGHT_BLUE = 2;
    public static final int HOT_LEFT_RED = 6;
    public static final int HOT_RIGHT_RED = 7;
//    public static final int SHOOT = 4;
    public static final int INTAKE = 5;
    public static final int TELE = 3;
    private static Lights instance;
    private DigitalOutput bit0;
    private DigitalOutput bit1;
    private DigitalOutput bit2;
    private int pattern = 0;
    private boolean redAlliance = true;

    public Lights() {
        bit0 = new DigitalOutput(8);
        bit1 = new DigitalOutput(9);
        bit2 = new DigitalOutput(10);

    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }

    public static Lights getInstance() {
        if (instance == null) {
            instance = new Lights();
        }
        return instance;
    }

    public void setBits(boolean b0, boolean b1, boolean b2) {
        bit0.set(b0);
        bit1.set(b1);
        bit2.set(b2);
//        System.out.println(b0 + " " + b1 + " "+b2);
    }

    public void setPattern(int pattern) {
        this.pattern = pattern;
        switch (pattern) {
            case BLUE_PRE:
                setBits(true, true, true);
                break;
            case HOT_LEFT_BLUE:
                setBits(true, false, false);
                break;
            case HOT_RIGHT_BLUE:
                setBits(false, false, true);
                break;
            case HOT_LEFT_RED:
                setBits(true, true, false);
                break;
            case HOT_RIGHT_RED:
                setBits(false, true, true);
                break;
            case RED_PRE:
                setBits(false, true, false);
                break;
            case INTAKE:
                setBits(true, false, true);
                break;
            case TELE:
                setBits(false, false, false);
                break;
            default:
                setBits(false, false, false);
                break;
        }
    }

    private void updateLights() {
        switch (pattern) {
            case BLUE_PRE:
                setBits(true, true, true);
                break;
            case HOT_LEFT_BLUE:
                setBits(true, false, false);
                break;
            case HOT_RIGHT_BLUE:
                setBits(false, false, true);
                break;
            case HOT_LEFT_RED:
                setBits(true, true, false);
                break;
            case HOT_RIGHT_RED:
                setBits(false, true, true);
                break;
            case RED_PRE:
                setBits(false, true, false);
                break;
            case INTAKE:
                setBits(true, false, true);
                break;
            case TELE:
                setBits(false, false, false);
                break;
            default:
                setBits(false, false, false);
                break;
        }
    }

    /**
     * @return the redAlliance
     */
    public boolean isRedAlliance() {
        return redAlliance;
    }

    /**
     * @param redAlliance the redAlliance to set
     */
    public void setRedAlliance(boolean redAlliance) {
        this.redAlliance = redAlliance;
    }
}
