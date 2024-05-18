package dev.ea2.fightingGame.characters;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data // Generates getters, setters, toString, equals, and hashCode methods
@AllArgsConstructor // Generates a constructor with all arguments
public class EnemyCharacter extends Character {

    // this class will be used to create the enemy character
    // it will extend the character class and will have additional
    // information such as the character's name and special moves
    // this class will be used to create the enemy character

    // name of the character
    private String name;
    // special moves of the character
    private String[] specialMoves;

    // No need to manually define constructor, getters, and setters
}
