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
public class PW extends CharacterBase {

    private String name;
    private String[] specialMoves;
    private KeyHandler keyHandler;

    public PW(int health, int x, int y, int speed, int height, int width, KeyHandler keyHandler, String name,
              String[] specialMoves) {
        super(health, x, y, speed, height, width);
        this.name = name;
        this.specialMoves = specialMoves;
        this.keyHandler = keyHandler;
        loadImages("PW");
    }



    @Override
    public void update() {
        if (keyHandler.isKeyDown(KeyEvent.VK_W) && !isJumping) {
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
            System.out.println("Phoenix position: (" + x + ", " + y + ")");
        }

        if (keyHandler.isKeyDown(KeyEvent.VK_S) && !isJumping) {
            System.out.println("Crouching");
            isCrouching = true;
            action = "crouch";
            if (keyHandler.isPunchKeyPressed() && isCrouching) {
                System.out.println(name + ": Desk Slam");
                action = "low";
            } else {
                action = "crouch";
            }
        } else if (isJumping) {
            isCrouching = false;
            if (keyHandler.isKeyDown(KeyEvent.VK_A) && x >= 0) {
                x -= speed;
                System.out.println("Phoenix position: (" + x + ", " + y + ")");
            }
            if (keyHandler.isKeyDown(KeyEvent.VK_D) && x <= 1165) {
                x += speed;
                System.out.println("Phoenix position: (" + x + ", " + y + ")");
            }
            if (keyHandler.isPunchKeyPressed() && isJumping) {
                System.out.println(name + ": AHHHHHH!");
                action = "high";
            } else {
                action = "jump";
            }
        } else {
            isCrouching = false;
            action = "idle";
            if (keyHandler.isKeyDown(KeyEvent.VK_A) && x >= 0) {
                x -= speed;
                System.out.println("Phoenix position: (" + x + ", " + y + ")");
                action = "back";
            }
            if (keyHandler.isKeyDown(KeyEvent.VK_D) && x <= 1165) {
                x += speed;
                System.out.println("Phoenix position: (" + x + ", " + y + ")");
                action = "forward";
            }
            if (keyHandler.isPunchKeyPressed()) {
                System.out.println("Timer: " + specialTimer);
                if (keyHandler.isKeyDown(KeyEvent.VK_A) && specialTimer == 6) {
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
