package dev.ea2.fightingGame.characters;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.event.KeyEvent;

@Data // Generates getters, setters, toString, equals, and hashCode methods
@AllArgsConstructor // Generates a constructor with all arguments
@NoArgsConstructor // Generates a no-args constructor
public class Character {

    private int health;
    private int x;
    private int y;
    private KeyHandler keyHandler;

    // This method will be used to update the character's position
    public void update() {
        if (keyHandler.isKeyDown(KeyEvent.VK_UP)) {
            y -= 1; // Move character up
        }
        if (keyHandler.isKeyDown(KeyEvent.VK_DOWN)) {
            y += 1; // Move character down
        }
        if (keyHandler.isKeyDown(KeyEvent.VK_LEFT)) {
            x -= 1; // Move character left
        }
        if (keyHandler.isKeyDown(KeyEvent.VK_RIGHT)) {
            x += 1; // Move character right
        }
        if (keyHandler.isPunchKeyPressed()) {
            System.out.println("Punch!");
        }
        if (keyHandler.isKickKeyPressed()) {
            System.out.println("Kick!");
        }
        System.out.println("Character position: (" + x + ", " + y + ")");
    }
}
// Path: ea-2-fighting-game/fighting-game/src/main/java/dev/ea2/fightingGame/characters/EnemyCharacter.java
