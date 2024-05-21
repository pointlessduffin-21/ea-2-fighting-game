    package dev.ea2.fightingGame.characters;

    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.NoArgsConstructor;

    import javax.imageio.ImageIO;
    import java.awt.*;
    import java.awt.image.BufferedImage;
    import java.io.IOException;
    import java.util.Objects;
    import java.util.logging.Logger;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public abstract class CharacterBase {

        protected static final Logger logger = Logger.getLogger(ME.class.getName());


        protected int health;
        protected int x;
        protected int y;
        protected int speed;
        protected int height;
        protected int width;


        protected boolean isJumping = false;
        protected boolean isCrouching = false;
        protected boolean isHit = false;
        protected int velocityY;
        protected final int maxJumpHeight = 50;
        protected final int jumpStrength = 25;
        protected final int gravity = 4;
        protected final int ground = 600;
        protected int specialTimer = 6;
        protected int frameCounter = 0;

        protected Rectangle hitbox = new Rectangle(0, 0, 0, 0);

        protected BufferedImage idle, forward, back, crouch, jump, low, high, attack, special;
        protected String action = "idle";

        public CharacterBase(int health, int x, int y, int speed, int height, int width) {
            this.health = health;
            this.x = x;
            this.y = y;
            this.speed = speed;
            this.height = height;
            this.width = width;
        }

        protected void loadImages(String characterName) {
            try {
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


        public abstract void update();

        public void draw(Graphics2D g2) {
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

            if (image != null) {
                int imageWidth = image.getWidth() * 2;
                int imageHeight = image.getHeight() * 2;
                int drawY = this.y + this.height - imageHeight;
                int drawX = this.x + this.width - imageWidth;
                g2.drawImage(image, drawX, drawY, imageWidth, imageHeight, null);
            }
        }

    }
