package dev.ea2.fightingGame.characters;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.Graphics2D;

@Data // Generates getters, setters, toString, equals, and hashCode methods
@AllArgsConstructor // Generates a constructor with all arguments
@NoArgsConstructor // Generates a no-args constructor
public class Character {

    private int health;
    public int x;
    public int y;
    public int speed;
    public int height;
    public int width;

    
 
    // This method will be used to update the character's position
    public void update() {
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    public void draw(Graphics2D g) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'paintComponent'");
    }
    
}
// Path: ea-2-fighting-game/fighting-game/src/main/java/dev/ea2/fightingGame/characters/EnemyCharacter.java
