package dev.ea2.fightingGame.characters;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;




@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ME extends Character {

    private String name;
    private String[] specialMoves;
    Game gp;
    private KeyHandler keyHandler;
    private boolean isJumping = false;
    private int velocityY;
    private final int maxJumpHeight = 50;
    private final int jumpStrength = 25;
    private final int gravity = 4;
    private final int ground = 600;

    public BufferedImage idle, forward, back, crouch, jump, low, high, attack, special;
    public String action = "idle";

    public ME(int health, int x, int y, int speed, int h, int w, KeyHandler keyHandler, String name,
            String[] specialMoves) {
        super(health, x, y, speed, h, w);
        this.name = name;
        this.specialMoves = specialMoves;
        this.keyHandler = keyHandler;
    }

    public void getImage() {
        try {

            idle = ImageIO.read(getClass().getResourceAsStream("/characters/ME/ME_idle.png"));
            forward = ImageIO.read(getClass().getResourceAsStream("/characters/ME/ME_forward.png"));
            back = ImageIO.read(getClass().getResourceAsStream("/characters/ME/ME_back.png"));
            crouch = ImageIO.read(getClass().getResourceAsStream("/characters/ME/ME_crouch.png"));
            jump = ImageIO.read(getClass().getResourceAsStream("/characters/ME/ME_jump.png"));
            low = ImageIO.read(getClass().getResourceAsStream("/characters/ME/ME_low.png"));
            high = ImageIO.read(getClass().getResourceAsStream("/characters/ME/ME_high.png"));
            attack = ImageIO.read(getClass().getResourceAsStream("/characters/ME/ME_attack.png"));
            special = ImageIO.read(getClass().getResourceAsStream("/characters/ME/ME_special.png"));
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update() {
        getImage();
        // Handle jumping
        if (keyHandler.isKeyDown(KeyEvent.VK_UP) && !isJumping) {
            action = "jump";
            isJumping = true;
            velocityY = -jumpStrength;
        }

        // Apply gravity if the character is jumping
        if (isJumping) {
            super.y += velocityY;
            velocityY += gravity; // Simulates the effect of gravity

            // Check if character has reached the maximum height or is coming down
            if (velocityY > 0 && super.y >= ground) { // Assuming groundLevel is the y-coordinate of the ground (50)
                super.y = ground; // Reset to ground level
                action = "idle";
                isJumping = false;
                velocityY = 0;
            }
            System.out.println("Phoenix position: (" + x + ", " + y + ")");
        }

        // Handle key inputs for other actions
        if (keyHandler.isKeyDown(KeyEvent.VK_DOWN)) {
            System.out.println("Crouching");
            action = "crouch";
            // Combined action example
            if (keyHandler.isPunchKeyPressed()) {
                System.out.println(name + ": Desk Slam");
                action = "low";
            
            }  else {
                action = "crouch";
            }
        } else if (isJumping) {
            if (keyHandler.isKeyDown(KeyEvent.VK_LEFT) && super.x >= 0) {
                super.x -= super.speed; // Move character left
                System.out.println("Phoenix position: (" + x + ", " + y + ")");

            }
            if (keyHandler.isKeyDown(KeyEvent.VK_RIGHT) && super.x <= 660) {
                super.x += super.speed; // Move character right
                System.out.println("Phoenix position: (" + x + ", " + y + ")");

            }
            if (keyHandler.isPunchKeyPressed()) {
                System.out.println(name + ": AHHHHHH!");
                action = "high";
            } else {
                action = "jump";
            }
        } else {

            action = "idle";
            if (keyHandler.isKeyDown(KeyEvent.VK_LEFT) && super.x >= 0) {
                super.x -= super.speed; // Move character left
                System.out.println("Phoenix position: (" + x + ", " + y + ")");
                action = "back";
            }
            if (keyHandler.isKeyDown(KeyEvent.VK_RIGHT) && super.x <= 660) {
                super.x += super.speed; // Move character right
                System.out.println("Phoenix position: (" + x + ", " + y + ")");
                action = "forward";
            }
            if (keyHandler.isPunchKeyPressed()) {

                if (keyHandler.isKeyDown(KeyEvent.VK_LEFT)) {
                    System.out.println("Big ol Finger");
                    action = "special";
                } else {
                    System.out.println("Read");
                    action = "attack";
                }
            }
        }
        // Other key inputs
    }
    
    
     @Override
    public void draw(Graphics2D g2) {

        // g2.setColor(Color.BLACK);

        // g2.fillRect(super.x, super.y, super.height, super.width);

        BufferedImage image = null;

        switch (action) {
            case "idle":
                image = idle;
                break;
            case "forward":
                image = forward;
                break;
            case "back":
                image = back;
                break;
            case "jump":
                image = jump;
                break;
            case "crouch":
                image = crouch;
                break;
            case "attack":
                image = attack;
                break;
            case "high":
                image = high;
                break;
            case "low":
                image = low;
                break;
            case "special":
                image = special;
                break;
        }

        int imageWidth = image.getWidth() * 2;
        int imageHeight = image.getHeight() * 2;
        
      // Calculate the drawing position based on the character's position and the image height
      int drawY = super.y + super.height - imageHeight;
        int drawx = super.x + super.width - imageWidth;

        // Draw the image with the same bottom position
        g2.drawImage(image, drawx, drawY, imageWidth, imageHeight, null);
    }
}


