package dev.ea2.fightingGame.characters;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.awt.Graphics2D;
import java.awt.Rectangle;
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
    private KeyHandler keyHandler;
    private boolean isJumping = false;
    private boolean isCrouching = false;
    private boolean isHit = false;
    private int velocityY;
    private final int maxJumpHeight = 50;
    private final int jumpStrength = 25;
    private final int gravity = 4;
    private final int ground = 600;
    private int specialTimer = 6;
    private int frameCounter = 0;

    public BufferedImage idle, forward, back, crouch, jump, low, high, attack, special;
    public String action = "idle";

    public Rectangle hitbox = new Rectangle(0, 0, 0, 0);

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
        if (keyHandler.isKeyDown(KeyEvent.VK_UP) && !isJumping) {
            action = "jump";
            isJumping = true;
            velocityY = -jumpStrength;
        }

        if (isJumping) {
            super.y += velocityY;
            velocityY += gravity; 

            if (velocityY > 0 && super.y >= ground) { 
                super.y = ground; 
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
            
            }  else {
                action = "crouch";
            }
        } else if (isJumping) {
            isCrouching = false;
            if (keyHandler.isKeyDown(KeyEvent.VK_LEFT) && super.x >= 0) {
                super.x -= super.speed; 
                System.out.println("Miles position: (" + x + ", " + y + ")");

            }
            if (keyHandler.isKeyDown(KeyEvent.VK_RIGHT) && super.x <= 1165) {
                super.x += super.speed; 
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
            if (keyHandler.isKeyDown(KeyEvent.VK_LEFT) && super.x >= 0) {
                super.x -= super.speed; 
                System.out.println("Miles position: (" + x + ", " + y + ")");
                action = "back";
            }
            if (keyHandler.isKeyDown(KeyEvent.VK_RIGHT) && super.x <= 1165) {
                super.x += super.speed; 
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
                frameCounter = 0; // Reset the frame counter
            }
        }
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
                image = back;
                break;
            case "back":
                image = forward;
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
        
      int drawY = super.y + super.height - imageHeight;
      int drawx = super.x + super.width - imageWidth;

        g2.drawImage(image, drawx, drawY, imageWidth, imageHeight, null);
    }
}


