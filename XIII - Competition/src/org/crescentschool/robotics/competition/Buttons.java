package org.crescentschool.robotics.competition;

import edu.wpi.first.wpilibj.Joystick;

/**
 * A utility class for making button use simpler
 * @author Patrick White
 */
public class Buttons {

    private static Button[] opButtons = new Button[0];
    private static Button[] drButtons = new Button[0];


    private static class Button {

        int id;
        boolean isPressed;
        boolean isHeld;
        boolean isReleased;
        Joystick gamepad;

        Button(int id, Joystick gamepad) {
            this.id = id;
            this.gamepad = gamepad;
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
            Button button = opButtons[i];
            if (button.isPressed) {
                button.isPressed = false;
            }
            if (button.isReleased) {
                button.isReleased = false;
            }
            if (button.gamepad.getRawButton(button.id) && !button.isHeld) {
                button.isHeld = true;
                button.isPressed = true;
            } else if (!button.gamepad.getRawButton(button.id) && button.isHeld) {
                button.isHeld = false;
                button.isReleased = true;
            }
        }
        for (int i = 0; i < drButtons.length; i++) {
            Button button = drButtons[i];
            if (button.isPressed) {
                button.isPressed = false;
            }
            if (button.isReleased) {
                button.isReleased = false;
            }
            if (button.gamepad.getRawButton(button.id) && !button.isHeld) {
                button.isHeld = true;
                button.isPressed = true;
            } else if (!button.gamepad.getRawButton(button.id) && button.isHeld) {
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
    public static void register(int buttonID, Joystick gamepad) {
        if(gamepad.equals(OI.getInstance().getDriver())){
            Button[] newButtons = new Button[drButtons.length + 1];
            System.arraycopy(drButtons, 0, newButtons, 0, drButtons.length);
            drButtons = newButtons;
            drButtons[drButtons.length - 1] = new Button(buttonID, gamepad);
        } else {
            Button[] newButtons = new Button[opButtons.length + 1];
            System.arraycopy(opButtons, 0, newButtons, 0, opButtons.length);
            opButtons = newButtons;
            opButtons[opButtons.length - 1] = new Button(buttonID, gamepad);
        }
    }

    /**
     * Returns whether or not the button has been pressed since the last call to update()
     * @param buttonID the number of the button
     * @param gamepad the gamepad the button is on
     * @return whether the button has been pressed since the last update
     */
    public static boolean isPressed(int buttonID, Joystick gamepad) {
        if(gamepad.equals(OI.getInstance().getDriver())){
            for (int i = 0; i < drButtons.length; i++) {
                Button button = drButtons[i];
                if (button.id == buttonID && button.gamepad.equals(gamepad)) {
                    return button.isPressed;
                }
            }
            return false;
        } else {
            for (int i = 0; i < drButtons.length; i++) {
                Button button = drButtons[i];
                if (button.id == buttonID && button.gamepad.equals(gamepad)) {
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
    public static boolean isReleased(int buttonID, Joystick gamepad) {
        if(gamepad.equals(OI.getInstance().getDriver())){
            for (int i = 0; i < drButtons.length; i++) {
                Button button = drButtons[i];
                if (button.id == buttonID && button.gamepad.equals(gamepad)) {
                    return button.isReleased;
                }
            }
            return false;
        } else {
            for (int i = 0; i < drButtons.length; i++) {
                Button button = drButtons[i];
                if (button.id == buttonID && button.gamepad.equals(gamepad)) {
                    return button.isReleased;
                }
            }
            return false;
        }
    }
}