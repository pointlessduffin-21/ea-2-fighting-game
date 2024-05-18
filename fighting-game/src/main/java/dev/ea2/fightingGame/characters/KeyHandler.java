package dev.ea2.fightingGame.characters;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    private boolean[] keys;

    public KeyHandler() {
        keys = new boolean[256];
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
        return isKeyDown(KeyEvent.VK_Z); // Assuming 'Z' key for punch
    }

    public boolean isKickKeyPressed() {
        return isKeyDown(KeyEvent.VK_X); // Assuming 'X' key for kick
    }
}
// Path: ea-2-fighting-game/fighting-game/src/main/java/dev/ea2/fightingGame/characters/Character.java