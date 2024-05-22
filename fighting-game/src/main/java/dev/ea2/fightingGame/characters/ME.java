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

    // Hit boxes for different attacks
    private Rectangle shortHitBox = new Rectangle(400, 600, 70, 80); // Middle short range attack
    private Rectangle lowHitBox = new Rectangle(200, 450, 50, 50); // Lower attack, below max jump
    private Rectangle highHitBox = new Rectangle(150, 350, 100, 100); // Big AOE hitbox, in the middle part of the range
    private Rectangle specialHitBox = new Rectangle(100, 100, 200, 200); // Longer range for special attack


    public ME(int health, int x, int y, int speed, int height, int width, KeyHandler keyHandler, String name,
              String[] specialMoves) {
        super(health, x, y, speed, height, width);
        this.name = name;
        this.specialMoves = specialMoves;
        this.keyHandler = keyHandler;
        loadImages("ME");
    }

    // Update the character's position and actions
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
                jumpAttackTime = 6;
                velocityY = 0;
            }
            System.out.println("Miles position: (" + x + ", " + y + ")");
        }

        if (keyHandler.isKeyDown(KeyEvent.VK_DOWN) && !isJumping) {
            System.out.println("Crouching");
            isCrouching = true;
            action = "crouch";

            if (keyHandler.isPunchKeyEPressed() && isCrouching && crouchAttackTime >=2) {
                System.out.println(name + ": Desk Slam");
                action = "low";
                crouchAttackTime--;
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
            if (keyHandler.isPunchKeyEPressed() && isJumping && jumpAttackTime >=3) {
                System.out.println(name + ": AHHHHHH!");
                jumpAttackTime--;
                action = "high";
            } else {
                action = "jump";
            }
        } else {
            isCrouching = false;
            crouchAttackTime = 6;
            if (action != "hit") {
                action = "idle";
                System.out.print("idle");
            } else {
                
            }
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
                if (keyHandler.isKeyDown(KeyEvent.VK_RIGHT)) {
                    if (specialTimer == 6){
                        specialTimer = 0 ;
                    }
                    if (specialTimer <= 1) {
                        System.out.println("Big ol Finger");
                        action = "special";
                    }
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
        updateHitBoxes();
    }

    private void updateHitBoxes() {
        int imageWidth = getWidth() * 2;
        int imageHeight = getHeight() * 2;
        int drawY = getY() + getHeight() - imageHeight;
        int drawX = getX() + getWidth() - imageWidth;

        // Update hit box positions based on character actions
        switch (getAction()) {
            case "attack":
                shortHitBox.setBounds(drawX - 95, drawY - 20, imageWidth - 40, imageHeight - 40);
                break;
            case "low":
                lowHitBox.setBounds(drawX - 170, drawY + 70, imageWidth + 40, imageHeight - 70);
                break;
            case "high":
                highHitBox.setBounds(drawX - 130, drawY - 50, imageWidth + 100, imageHeight + 100);
                break;
            case "special":
                specialHitBox.setBounds(drawX - 280, drawY - 50, imageWidth + 100, imageHeight - 60);
                break;
            default:
                // Reset hit boxes if not in an attack action
                shortHitBox.setBounds(0, 0, 0, 0);
                lowHitBox.setBounds(0, 0, 0, 0);
                highHitBox.setBounds(0, 0, 0, 0);
                specialHitBox.setBounds(0, 0, 0, 0);
                break;
        }
    }



    @Override
    public void draw(Graphics2D g2, String name) {
        super.draw(g2, name);

        // Draw hit boxes for debugging purposes
        g2.setColor(Color.YELLOW);
        g2.draw(shortHitBox);
        g2.setColor(Color.RED);
        g2.draw(lowHitBox);
        g2.setColor(Color.GREEN);
        g2.draw(highHitBox);
        g2.setColor(Color.BLUE);
        g2.draw(specialHitBox);
    }

}

