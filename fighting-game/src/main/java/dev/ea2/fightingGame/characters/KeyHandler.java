package dev.ea2.fightingGame.characters;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * The KeyHandler class implements KeyListener to handle keyboard input for the game.
 * It tracks the state of key presses using a boolean array.
 */
public class KeyHandler implements KeyListener {

    private final boolean[] keys;

    /**
     * Constructor initializes the keys array to track the state of all keys.
     */
    public KeyHandler() {
        keys = new boolean[256];
    }

    /**
     * Checks if a key is currently pressed.
     *
     * @param keyCode the code of the key to check
     * @return true if the key is pressed, false otherwise
     */
    public boolean isKeyDown(int keyCode) {
        if (keyCode >= 0 && keyCode < keys.length) {
            return keys[keyCode];
        }
        return false;
    }

    /**
     * Checks if the punch key for player 1 (Space) is pressed.
     *
     * @return true if the punch key is pressed, false otherwise
     */
    public boolean isPunchKeyPressed() {
        return isKeyDown(KeyEvent.VK_SPACE); // ATTACK FOR PLAYER 1
    }

    /**
     * Checks if the punch key for player 2 (Enter) is pressed.
     *
     * @return true if the punch key is pressed, false otherwise
     */
    public boolean isPunchKeyEPressed() {
        return isKeyDown(KeyEvent.VK_ENTER); // ATTACK FOR PLAYER 2
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // This method is not used, but must be implemented as part of KeyListener
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Set the key to true if it is pressed
        if (e.getKeyCode() >= 0 && e.getKeyCode() < keys.length) {
            keys[e.getKeyCode()] = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Set the key to false if it is released
        if (e.getKeyCode() >= 0 && e.getKeyCode() < keys.length) {
            keys[e.getKeyCode()] = false;
        }
    }
}
