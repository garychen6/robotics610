package org.crescentschool.robotics.competition;

import edu.wpi.first.wpilibj.Joystick;

/**
 * A utility class for making button use simpler
 * @author Patrick White
 */
public class Buttons {

    private static Button[] opButtons = new Button[10];
    private static int nOB = 0;
    private static Button[] drButtons = new Button[10];
    private static int nDB = 0;
    public static final int kDriver = 1;
    public static final int kOperator = 2;
    public static final Joystick kDJoy = new Joystick(kDriver);
    public static final Joystick kOJoy = new Joystick(kOperator);
    
    static Button button;


    private static class Button {

        int id;
        boolean isPressed;
        boolean isHeld;
        boolean isReleased;
        int gamepadID;

        Button(int id, int gamepadID) {
            this.id = id;
            this.gamepadID = gamepadID;
            isPressed = false;
            isHeld = false;
            isReleased = false;
        }
    }

    /**
     * Updates the status of the buttons. Must be called every periodic loop.
     */
    public static void update() {
        for (int i = 0; i < opButtons.length; i++) {
            button = opButtons[i];
            if (button.isPressed) {
                button.isPressed = false;
            }
            if (button.isReleased) {
                button.isReleased = false;
            }
            if (kOJoy.getRawButton(button.id) && !button.isHeld) {
                button.isHeld = true;
                button.isPressed = true;
            } else if (!kOJoy.getRawButton(button.id) && button.isHeld) {
                button.isHeld = false;
                button.isReleased = true;
            }
        }
        for (int i = 0; i < drButtons.length; i++) {
            button = drButtons[i];
            if (button.isPressed) {
                button.isPressed = false;
            }
            if (button.isReleased) {
                button.isReleased = false;
            }
            if (kDJoy.getRawButton(button.id) && !button.isHeld) {
                button.isHeld = true;
                button.isPressed = true;
            } else if (!kDJoy.getRawButton(button.id) && button.isHeld) {
                button.isHeld = false;
                button.isReleased = true;
            }
        }
    }

    /**
     * Registers a button for use later. Each button must be registered before use.
     * @param buttonID the number of the button
     * @param gamepad the gamepad the button is on
     */
    public static void register(int buttonID, int gamepadID) {
        if(gamepadID == kDriver){
            drButtons[nDB] = new Button(buttonID, gamepadID);
            nDB++;
        } else {
            opButtons[nOB] = new Button(buttonID, gamepadID);
            nOB++;
        }
    }

    /**
     * Returns whether or not the button has been pressed since the last call to update()
     * @param buttonID the number of the button
     * @param gamepad the gamepad the button is on
     * @return whether the button has been pressed since the last update
     */
    public static boolean isPressed(int buttonID, int gamepadID) {
        if(gamepadID == kDriver){
            for (int i = 0; i < drButtons.length; i++) {
                button = drButtons[i];
                if (button.id == buttonID) {
                    return button.isPressed;
                }
            }
            return false;
        } else {
            for (int i = 0; i < opButtons.length; i++) {
                button = opButtons[i];
                if (button.id == buttonID) {
                    return button.isPressed;
                }
            }
            return false;
        }
    }

    /**
     * Returns whether or not the button is held down. This is identical to gamepad.getRawButton(buttonID).
     * @param buttonID the number of the button
     * @param gamepad the gamepad the button is on
     * @return whether the button is held down
     */
    public static boolean isHeld(int buttonID, Joystick gamepad) {
        return gamepad.getRawButton(buttonID);
        /*for (int i = 0; i < buttons.length; i++) {
            Button button = buttons[i];
            if (button.id == buttonID && button.gamepad.equals(gamepad)) {
                return button.isHeld;
            }
        }*/
        //return false;
    }

    /**
     * Returns whether or not the button has been released since the last call to update()
     * @param buttonID the number of the button
     * @param gamepad the gamepad the button is on
     * @return whether the button has been released since the last update
     */
    public static boolean isReleased(int buttonID, int gamepadID) {
        if(gamepadID == kDriver){
            for (int i = 0; i < drButtons.length; i++) {
                button = drButtons[i];
                if (button.id == buttonID) {
                    return button.isReleased;
                }
            }
            return false;
        } else {
            for (int i = 0; i < opButtons.length; i++) {
                button = opButtons[i];
                if (button.id == buttonID) {
                    return button.isReleased;
                }
            }
            return false;
        }
    }
}