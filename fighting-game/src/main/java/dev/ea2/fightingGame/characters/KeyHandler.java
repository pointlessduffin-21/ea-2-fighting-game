package dev.ea2.fightingGame.characters;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    private boolean[] keys;
    private boolean[] keysPressed;
    private boolean[] keysFired;

    public KeyHandler() {
        keys = new boolean[256];
        keysPressed = new boolean[KeyEvent.KEY_LAST + 1];
        keysFired = new boolean[KeyEvent.KEY_LAST + 1];
    }

    public boolean isKeyPressed(int keyCode) {
        if (keyCode >= 0 && keyCode < keysPressed.length) {
            boolean keyDown = keysPressed[keyCode];
            boolean keyFired = keysFired[keyCode];

            // If key is pressed and has not been fired yet, return true
            if (keyDown && !keyFired) {
                keysFired[keyCode] = true; // Mark the key as fired
                return true;
            } else if (!keyDown) {
                keysFired[keyCode] = false; // Reset fired state if key is released
            }
        }
        return false;
    }


    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode >= 0 && keyCode < keys.length) {
            keys[keyCode] = true;
        }
       
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode >= 0 && keyCode < keys.length) {
            keys[keyCode] = false;
        }
   
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used
    }

    public boolean isKeyDown(int keyCode) {
        if (keyCode >= 0 && keyCode < keys.length) {
            return keys[keyCode];
        }
        return false;
    }

    public boolean isPunchKeyPressed() {
        return isKeyDown(KeyEvent.VK_SPACE); // Assuming 'Z' key for punch
    }

    public boolean isPunchKeyEPressed() {
        return isKeyDown(KeyEvent.VK_ENTER); // Assuming 'Z' key for punch
    }
    
}
// Path: ea-2-fighting-game/fighting-game/src/main/java/dev/ea2/fightingGame/characters/Character.java