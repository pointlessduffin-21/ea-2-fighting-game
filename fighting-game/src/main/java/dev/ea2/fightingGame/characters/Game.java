package dev.ea2.fightingGame.characters;

import dev.ea2.fightingGame.frontEnd.GameOver;
import dev.ea2.fightingGame.frontEnd.pauseMenu;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * The Game class handles the main game logic, rendering, and state management for the fighting game.
 * It extends JPanel and includes methods for starting the game, pausing, resuming, quitting, and handling collisions.
 */
public class Game extends JPanel {

    private static final Logger logger = Logger.getLogger(Game.class.getName());

    private final int PWHealth;
    private final int MEHealth;
    private final String imageFile;

    private final KeyHandler keyHandler = new KeyHandler();
    private final Phoenix phoenix;
    private final Miles me;

    private BufferedImage backgroundImage;
    private BufferedImage pwHeart;
    private BufferedImage mwHeart;

    private Timer timer;
    private JFrame frame;

    /**
     * Constructor to initialize the Game with character health and background image file.
     *
     * @param PWHealth Initial health for Phoenix Wright.
     * @param MEHealth Initial health for Miles Edgeworth.
     * @param imageFile Path to the background image file.
     */
    public Game(int PWHealth, int MEHealth, String imageFile) {
        this.PWHealth = PWHealth;
        this.MEHealth = MEHealth;
        this.imageFile = imageFile;

        // Initialize characters with health, position, speed, size, and actions
        phoenix = new Phoenix(PWHealth, 50, 600, 15, 100, 100, keyHandler, "Phoenix Wright", new String[]{"Objection!", "Present"});
        me = new Miles(MEHealth, 1100, 600, 15, 100, 100, keyHandler, "Miles Edgeworth", new String[]{"Objection!", "Present"});
    }

    /**
     * Static method to start the game with a specified background image file.
     *
     * @param imageFile Path to the background image file.
     */
    public static void StartGameWithImage(String imageFile) {
        Game game = new Game(5, 5, imageFile);
        game.start();
    }

    /**
     * Pauses the game, stops the timer, and shows the pause menu.
     */
    public void pause() {
        timer.stop();
        setVisible(false);
        pauseMenu.pauseActual(this, imageFile, PWHealth, MEHealth);
    }

    /**
     * Resumes the game, starts the timer, and makes the game visible.
     */
    public void resume() {
        timer.start();
        setVisible(true);
    }

    /**
     * Quits the game by disposing of the frame.
     */
    public void quit() {
        if (frame != null) {
            frame.dispose();
        }
    }

    /**
     * Starts the game by setting up the JFrame, loading images, and starting the game loop timer.
     */
    private void start() {
        frame = new JFrame("Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1280, 720);
        frame.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setFocusable(true);
        panel.addKeyListener(keyHandler);
        frame.add(panel, BorderLayout.CENTER);
        frame.add(this);

        loadImages();

        // Timer for game loop, updates game state every 60 ms
        timer = new Timer(60, e -> {
            phoenix.update();
            me.update();
            checkCollisions();
            repaint();
            checkGameOver();
        });
        timer.start();

        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    pause();
                }
            }
        });

        frame.setVisible(true);
    }

    /**
     * Loads images for the background and health indicators.
     */
    private void loadImages() {
        try {
            backgroundImage = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(imageFile)));
            pwHeart = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/images/icons/pwHeart.png")));
            mwHeart = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/images/icons/mwHeart.png")));
        } catch (IOException e) {
            logger.severe("Error loading images for " + imageFile + ": " + e.getMessage());
        }
    }

    /**
     * Sends a POST request to the server with the game result.
     *
     * @param playerName Name of the player.
     * @param result Result of the game ("Won" or "Lost").
     */
    private void sendPostRequest(String playerName, String result) {
        try {
            URL url = new URL("http://localhost:6969/api/add");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            String formattedDateTime = now.format(formatter);

            String jsonInputString = String.format("{\"playerName\": \"%s\", \"result\": \"%s\", \"datePlayed\": \"%s\"}",
                    playerName, result, formattedDateTime);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                logger.info("Response from server: " + response);
            }
        } catch (IOException e) {
            logger.severe("Error sending POST request: " + e.getMessage());
        }
    }

    /**
     * Paints the game components including the background, characters, and health indicators.
     *
     * @param g Graphics object used for painting.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }

        phoenix.draw(g2, phoenix.getName());
        me.draw(g2, me.getName());

        drawHealth(g2);

        g2.dispose();
    }

    /**
     * Draws the health indicators for both characters.
     *
     * @param g2 Graphics2D object used for painting.
     */
    private void drawHealth(Graphics2D g2) {
        for (int i = 0; i < phoenix.getHealth(); i++) {
            g2.drawImage(pwHeart, i * pwHeart.getWidth(), 0, this);
        }
        for (int i = 0; i < me.getHealth(); i++) {
            g2.drawImage(mwHeart, this.getWidth() - (i + 1) * mwHeart.getWidth(), 0, this);
        }
    }

    /**
     * Checks for collisions between characters and handles the resulting interactions.
     */
    private void checkCollisions() {
        handleCollision(phoenix, me, me.getShortHitBox());
        handleCollision(me, phoenix, phoenix.getShortHitBox());

        handleCollision(phoenix, me, me.getLowHitBox());
        handleCollision(me, phoenix, phoenix.getLowHitBox());

        handleCollision(phoenix, me, me.getHighHitBox());
        handleCollision(me, phoenix, phoenix.getHighHitBox());

        handleCollision(phoenix, me, me.getSpecialHitBox());
        handleCollision(me, phoenix, phoenix.getSpecialHitBox());

        if (phoenix.getShortHitBox().intersects(me.getShortHitBox()) && me.getShortHitBox().intersects(phoenix.getShortHitBox())) {
            knockBack(phoenix, me);
        }

        if (phoenix.getLowHitBox().intersects(me.getLowHitBox()) && me.getLowHitBox().intersects(phoenix.getLowHitBox())) {
            knockBack(phoenix, me);
        }

        if (phoenix.getHighHitBox().intersects(me.getHighHitBox()) && me.getHighHitBox().intersects(phoenix.getHighHitBox())) {
            knockBack(phoenix, me);
        }

        if (phoenix.getSpecialHitBox().intersects(me.getSpecialHitBox()) && me.getSpecialHitBox().intersects(phoenix.getSpecialHitBox())) {
            knockBack(phoenix, me);
        }

        if (me.getShortHitBox().intersects(phoenix.getShortHitBox()) && phoenix.getShortHitBox().intersects(me.getShortHitBox())) {
            knockBack(me, phoenix);
        }

        if (me.getLowHitBox().intersects(phoenix.getLowHitBox()) && phoenix.getLowHitBox().intersects(me.getLowHitBox())) {
            knockBack(me, phoenix);
        }

        if (me.getHighHitBox().intersects(phoenix.getHighHitBox()) && phoenix.getHighHitBox().intersects(me.getHighHitBox())) {
            knockBack(me, phoenix);
        }

        if (me.getSpecialHitBox().intersects(phoenix.getSpecialHitBox()) && phoenix.getSpecialHitBox().intersects(me.getSpecialHitBox())) {
            knockBack(me, phoenix);
        }
    }

    /**
     * Handles collision between an attacker and a defender within a specified hit box.
     *
     * @param attacker The character initiating the attack.
     * @param defender The character receiving the attack.
     * @param hitBox The hit box where the collision is checked.
     */
    private void handleCollision(CharacterBase attacker, CharacterBase defender, Rectangle hitBox) {
        if (attacker.getHitbox().intersects(hitBox)) {
            attacker.setHealth(attacker.getHealth() - 1);
            knockBack(attacker, defender);
            attacker.setAction("hit");

        }
    }

    /**
     * Applies knockback effect to the attacker based on the defender's position.
     *
     * @param attacker The character receiving the knockback.
     * @param defender The character causing the knockback.
     */
    private void knockBack(CharacterBase attacker, CharacterBase defender) {
        int knockBackDistance = 200;

        if (attacker.getX() < defender.getX()) {
            attacker.setX(attacker.getX() - knockBackDistance);
        } else {
            attacker.setX(attacker.getX() + knockBackDistance);
        }
    }

    /**
     * Checks if the game is over and handles the end game logic, including sending results to the server.
     */
    private void checkGameOver() {
        if (phoenix.getHealth() <= 0 || me.getHealth() <= 0) {
            timer.stop();
            setVisible(false);
            GameOver.screen();

            if (phoenix.getHealth() <= 0) {
                sendPostRequest(phoenix.getName(), "Lost");
            } else {
                sendPostRequest(phoenix.getName(), "Won");
            }

            if (me.getHealth() <= 0) {
                sendPostRequest(me.getName(), "Lost");
            } else {
                sendPostRequest(me.getName(), "Won");
            }
        }
    }
}
