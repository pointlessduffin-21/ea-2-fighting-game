package dev.ea2.fightingGame.characters;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.awt.*;
import java.awt.event.KeyEvent;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ME extends CharacterBase {

    private String name;
    private String[] specialMoves;
    private KeyHandler keyHandler;

    public ME(int health, int x, int y, int speed, int height, int width, KeyHandler keyHandler, String name,
              String[] specialMoves) {
        super(health, x, y, speed, height, width);
        this.name = name;
        this.specialMoves = specialMoves;
        this.keyHandler = keyHandler;
        loadImages("ME");
    }



    @Override
    public void update() {
        if (keyHandler.isKeyDown(KeyEvent.VK_UP) && !isJumping) {
            action = "jump";
            isJumping = true;
            velocityY = -jumpStrength;
        }

        if (isJumping) {
            y += velocityY;
            velocityY += gravity;

            if (velocityY > 0 && y >= ground) {
                y = ground;
                action = "idle";
                isJumping = false;
                velocityY = 0;
            }
            System.out.println("Miles position: (" + x + ", " + y + ")");
        }

        if (keyHandler.isKeyDown(KeyEvent.VK_DOWN) && !isJumping) {
            System.out.println("Crouching");
            isCrouching = true;
            action = "crouch";

            if (keyHandler.isPunchKeyEPressed() && isCrouching) {
                System.out.println(name + ": Desk Slam");
                action = "low";
            } else {
                action = "crouch";
            }
        } else if (isJumping) {
            isCrouching = false;
            if (keyHandler.isKeyDown(KeyEvent.VK_LEFT) && x >= 0) {
                x -= speed;
                System.out.println("Miles position: (" + x + ", " + y + ")");
            }
            if (keyHandler.isKeyDown(KeyEvent.VK_RIGHT) && x <= 1165) {
                x += speed;
                System.out.println("Miles position: (" + x + ", " + y + ")");
            }
            if (keyHandler.isPunchKeyEPressed() && isJumping) {
                System.out.println(name + ": AHHHHHH!");
                action = "high";
            } else {
                action = "jump";
            }
        } else {
            isCrouching = false;
            action = "idle";
            if (keyHandler.isKeyDown(KeyEvent.VK_LEFT) && x >= 0) {
                x -= speed;
                System.out.println("Miles position: (" + x + ", " + y + ")");
                action = "back";
            }
            if (keyHandler.isKeyDown(KeyEvent.VK_RIGHT) && x <= 1165) {
                x += speed;
                System.out.println("Miles position: (" + x + ", " + y + ")");
                action = "forward";
            }
            if (keyHandler.isPunchKeyEPressed()) {
                if (keyHandler.isKeyDown(KeyEvent.VK_RIGHT) && specialTimer == 6) {
                    System.out.println("Big ol Finger");
                    action = "special";
                    specialTimer -= 3;
                } else {
                    System.out.println("Read");
                    action = "attack";
                }
            }

            frameCounter++;

            if (frameCounter >= 4) {
                if (specialTimer < 6) {
                    specialTimer++;
                    System.out.println("Timer: " + specialTimer);
                }
                frameCounter = 0;
            }
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        super.draw(g2);
    }
}

