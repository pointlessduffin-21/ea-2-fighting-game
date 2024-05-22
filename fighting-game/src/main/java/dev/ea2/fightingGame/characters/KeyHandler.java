package dev.ea2.fightingGame.characters;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    private final boolean[] keys;

    public KeyHandler() {
        keys = new boolean[256];
    }

    public boolean isKeyDown(int keyCode) {
        if (keyCode >= 0 && keyCode < keys.length) {
            return keys[keyCode];
        }
        return false;
    }

    public boolean isPunchKeyPressed() {
        return isKeyDown(KeyEvent.VK_SPACE); // ATTACK FOR PLAYER 1
    }

    public boolean isPunchKeyEPressed() {
        return isKeyDown(KeyEvent.VK_ENTER); //  ATTACK FOR PLAYER 2
    }

    @Override
    public void keyTyped(KeyEvent e) {
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
