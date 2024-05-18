package dev.ea2.fightingGame.characters;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlayerCharacter extends Character {

    private String name;
    private String[] specialMoves;

    public PlayerCharacter(int health, int x, int y, KeyHandler keyHandler, String name, String[] specialMoves) {
        super(health, x, y, keyHandler);
        this.name = name;
        this.specialMoves = specialMoves;
    }

}
