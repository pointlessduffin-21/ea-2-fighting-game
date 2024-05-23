package dev.ea2.fightingGame.characters;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * The CharacterBase class serves as a blueprint for creating characters in a fighting game.
 * It includes attributes and methods for character properties, actions, and rendering.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class CharacterBase {

    protected static final Logger logger = Logger.getLogger(CharacterBase.class.getName());


    private boolean invulnerable;

    // Character attributes
    protected int health;
    protected int x;
    protected int y;
    protected int speed;
    protected int height;
    protected int width;

    // Character states
    protected boolean isJumping = false;
    protected boolean isCrouching = false;
    protected boolean isHit = false;
    protected int velocityY;
    protected final int maxJumpHeight = 50;
    protected final int jumpStrength = 40;
    protected final int gravity = 4;
    protected final int ground = 600;
    protected int specialTimer = 6;
    protected int frameCounter = 0;
    protected int jumpAttackTime = 6;
    protected int crouchAttackTime = 6;

    // Getter for the hitbox
    @Getter
    protected final Rectangle hitbox = new Rectangle();

    // Character images
    protected BufferedImage idle, forward, back, crouch, jump, low, high, attack, special, hit;
    protected String action = "idle"; // Default action is idle

    /**
     * Constructor to initialize the character with specific attributes.
     *
     * @param health Initial health of the character.
     * @param x      Initial x-coordinate of the character.
     * @param y      Initial y-coordinate of the character.
     * @param speed  Speed at which the character moves.
     * @param height Height of the character.
     * @param width  Width of the character.
     */
    public CharacterBase(int health, int x, int y, int speed, int height, int width) {
        this.health = health;
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.height = height;
        this.width = width;
        this.invulnerable = false; // Initialize invulnerability to false

    }

    /**
     * Loads the character images from the resources folder based on the character's name.
     *
     * @param characterName The name of the character whose images are to be loaded.
     */
    protected void loadImages(String characterName) {
        try {
            hit = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/characters/" + characterName + "/" + characterName + "_hit.png")));
            idle = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/characters/" + characterName + "/" + characterName + "_idle.png")));
            forward = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/characters/" + characterName + "/" + characterName + "_forward.png")));
            back = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/characters/" + characterName + "/" + characterName + "_back.png")));
            crouch = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/characters/" + characterName + "/" + characterName + "_crouch.png")));
            jump = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/characters/" + characterName + "/" + characterName + "_jump.png")));
            low = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/characters/" + characterName + "/" + characterName + "_low.png")));
            high = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/characters/" + characterName + "/" + characterName + "_high.png")));
            attack = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/characters/" + characterName + "/" + characterName + "_attack.png")));
            special = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/characters/" + characterName + "/" + characterName + "_special.png")));
        } catch (IOException e) {
            logger.severe("Error loading images for " + characterName);
        }
    }

    /**
     * Abstract method to update the character's state. This method should be implemented
     * by subclasses to define specific behavior for each character.
     */
    public abstract void update();

    /**
     * Draws the character on the screen based on its current action.
     *
     * @param g2   The Graphics2D object used for drawing.
     * @param name The name of the character, used to determine specific drawing parameters.
     */
    public void draw(Graphics2D g2, String name) {
        BufferedImage image = null;

        // Select the appropriate image based on the current action
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
            case "hit":
                image = hit;
                break;
        }

        if (image != null) {
            // Calculate the position and size to draw the character sprite
            int imageWidth = image.getWidth() * 2;
            int imageHeight = image.getHeight() * 2;
            int boxWidth = 260;
            int boxHeight = 240;
            int boxX;
            int drawY = this.y + this.height - imageHeight;
            int boxY = this.y + this.height - imageHeight;
            int drawX;

            if (name.equals("Miles Edgeworth")) {
                drawX = this.x + this.width - imageWidth;
                boxX = this.x - 120;
            } else {
                drawX = this.x;
                boxX = this.x;
            }

            // Adjust hitbox dimensions based on the character's action
            switch (action) {
                case "idle":
                case "jump":
                    boxHeight = 260;
                    break;
                case "crouch":
                case "low":
                    boxHeight = 160;
                    boxY = this.y;
                    break;
            }

            // Set the hitbox dimensions to match the character sprite
            hitbox.setBounds(boxX, boxY, boxWidth, boxHeight);

            // Draw the hitbox (for debugging purposes)
            g2.setColor(Color.WHITE); // Set the color for the hitbox outline
            g2.draw(hitbox); // Draw the hitbox outline

            // Draw the character sprite
            g2.drawImage(image, drawX, drawY, imageWidth, imageHeight, null);
        }
    }

    public void setInvulnerable(boolean invulnerable) {
        this.invulnerable = invulnerable;
    }

}
