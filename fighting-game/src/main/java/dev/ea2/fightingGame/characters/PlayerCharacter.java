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
    Game gp;
    private KeyHandler keyHandler;
    private boolean isJumping = false;
    private int velocityY;
    private final int maxJumpHeight = 40;
    private final int jumpStrength = 15;
    private final int gravity = 3;

    public PlayerCharacter(int health, int x, int y, int speed, int h, int w, KeyHandler keyHandler, String name, String[] specialMoves) {
        super(health, x, y, speed, h, w);
        this.name = name;
        this.specialMoves = specialMoves;
        this.keyHandler = keyHandler;
    }

}
