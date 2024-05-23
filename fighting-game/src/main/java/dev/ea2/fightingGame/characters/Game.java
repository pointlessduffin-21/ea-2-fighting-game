package dev.ea2.fightingGame.characters;

import dev.ea2.fightingGame.frontEnd.GameOver;
import dev.ea2.fightingGame.frontEnd.pauseMenu;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.*;
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
    private final Miles Miles;

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
        Miles = new Miles(MEHealth, 1100, 600, 15, 100, 100, keyHandler, "Miles Edgeworth", new String[]{"Objection!", "Present"});
    }

    /**
     * Static method to start the game with a specified background image file.
     *
     * @param imageFile Path to the background image file.
     */


    /**
     * Pauses the game, stops the timer, and shows the pause menu.
     */

    // start the game

    public static void StartGameWithImage(String imageFile) {
        SwingUtilities .invokeLater(() -> {
            Game game = new Game(5, 5, imageFile);
            game.start();
        });
    }


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

    private void playBackgroundMusic() {
        try {
            // Fetch music file from Cloudinary
            URL cloudinaryUrl = new URL("https://res.cloudinary.com/ddemtlxll/video/upload/v1716451718/bg-music_p4sk8o.wav");
            HttpURLConnection connection = (HttpURLConnection) cloudinaryUrl.openConnection();
            InputStream inputStream = connection.getInputStream();

            // Create a temporary file to store the music
            File tempFile = File.createTempFile("bg-music", ".wav");
            FileOutputStream outputStream = new FileOutputStream(tempFile);
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            // Close streams
            inputStream.close();
            outputStream.close();

            // Create audio input stream from the temporary file
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(tempFile);

            // Create clip for background music
            Clip backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioInputStream);
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
            backgroundMusic.start();

            // Adjust volume
            FloatControl gainControl = (FloatControl) backgroundMusic.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(-10.0f); // Adjust volume as needed

            // Delete temporary file when the application exits
            tempFile.deleteOnExit();
        } catch (Exception e) {
            logger.severe("Error playing background music: " + e.getMessage());
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

        playBackgroundMusic();
        loadImages();

        // Timer for game loop, updates game state every 60 ms
        timer = new Timer(60, e -> {
            phoenix.update();
            Miles.update();
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
        Miles.draw(g2, Miles.getName());

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
        for (int i = 0; i < Miles.getHealth(); i++) {
            g2.drawImage(mwHeart, this.getWidth() - (i + 1) * mwHeart.getWidth(), 0, this);
        }
    }

    /**
     * Checks for collisions between characters and handles the resulting interactions.
     */
    private void checkCollisions() {
        handleCollision(phoenix, Miles, Miles.getShortHitBox());
        handleCollision(Miles, phoenix, phoenix.getShortHitBox());

        handleCollision(phoenix, Miles, Miles.getLowHitBox());
        handleCollision(Miles, phoenix, phoenix.getLowHitBox());

        handleCollision(phoenix, Miles, Miles.getHighHitBox());
        handleCollision(Miles, phoenix, phoenix.getHighHitBox());

        handleCollision(phoenix, Miles, Miles.getSpecialHitBox());
        handleCollision(Miles, phoenix, phoenix.getSpecialHitBox());

        if (phoenix.getShortHitBox().intersects(Miles.getShortHitBox()) && Miles.getShortHitBox().intersects(phoenix.getShortHitBox())) {
           // increase health for both PE


            knockBack(phoenix, Miles);
        }

        if (phoenix.getLowHitBox().intersects(Miles.getLowHitBox()) && Miles.getLowHitBox().intersects(phoenix.getLowHitBox())) {
            knockBack(phoenix, Miles);
        }

        if (phoenix.getHighHitBox().intersects(Miles.getHighHitBox()) && Miles.getHighHitBox().intersects(phoenix.getHighHitBox())) {
            knockBack(phoenix, Miles);
        }

        if (phoenix.getSpecialHitBox().intersects(Miles.getSpecialHitBox()) && Miles.getSpecialHitBox().intersects(phoenix.getSpecialHitBox())) {
            knockBack(phoenix, Miles);
        }

        if (Miles.getShortHitBox().intersects(phoenix.getShortHitBox()) && phoenix.getShortHitBox().intersects(Miles.getShortHitBox())) {

            knockBack(Miles, phoenix);
        }

        if (Miles.getLowHitBox().intersects(phoenix.getLowHitBox()) && phoenix.getLowHitBox().intersects(Miles.getLowHitBox())) {
            knockBack(Miles, phoenix);
        }

        if (Miles.getHighHitBox().intersects(phoenix.getHighHitBox()) && phoenix.getHighHitBox().intersects(Miles.getHighHitBox())) {
            knockBack(Miles, phoenix);
        }

        if (Miles.getSpecialHitBox().intersects(phoenix.getSpecialHitBox()) && phoenix.getSpecialHitBox().intersects(Miles.getSpecialHitBox())) {
            knockBack(Miles, phoenix);
        }



    }

    /**
     * Handles collision between an attacker and a defender within a specified hit box.
     * If the defender is not invulnerable, subtracts health from the attacker,
     * sets the defender's action to "hit", applies knockback to the defender,
     * and makes the defender invulnerable for a short duration.
     *
     * @param attacker The character initiating the attack.
     * @param defender The character receiving the attack.
     * @param hitBox   The hit box where the collision is checked.
     *
     *                 attack = phoenix
     *                 defender = Miles
     */
    private void handleCollision(CharacterBase attacker, CharacterBase defender, Rectangle hitBox) {
        // Check if the attacker's hitbox intersects with the defender's hitbox
        if (attacker.getHitbox().intersects(hitBox)) {
            // Check if the defender is not invulnerable
            if (!defender.isInvulnerable()) {
                // Subtract health from the attacker
                attacker.setHealth(attacker.getHealth() - 1);

                // Check if it's a short attack hit box and increment the attacker's health
                if (hitBox.equals(defender.getShortHitBox())) {
                    defender.setHealth(defender.getHealth() + 1);
                    // limit to 5

                    if (defender.getHealth() > 5) {
                        defender.setHealth(5);
                    }
                }

                // Set the defender's action to "hit" to display the hit animation
                defender.setAction("hit");
                // Apply knockback effect to the defender
                knockBack(attacker, defender);

                // Create a timer to reset the defender's action and invulnerability after a short delay
                Timer timer = new Timer(500, e -> {
                    // Reset the attacker's action to "idle" the one toke the damage
                    attacker.setAction("idle");
                    // Make the defender vulnerable again
                    defender.setInvulnerable(false);
                });
                timer.setRepeats(false); // Set to execute only once
                timer.start();

                // Set the attacker's action to "hit" to display the hit animation
                attacker.setAction("hit");
                // Make the defender invulnerable during the delay
                defender.setInvulnerable(true);
            }
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

        // if knockback still limit the character out of border

        if (attacker.getX() < 0) {
            attacker.setX(0);
        } else if (attacker.getX() > 1165) {
            attacker.setX(1165);
        } else  if (attacker.getY() < 0) {
            attacker.setY(0);
        } else if (attacker.getY() > 600) {
            attacker.setY(600);
        }



    }

    /**
     * Checks if the game is over and handles the end game logic, including sending results to the server.
     */
    private void checkGameOver() {
        if (phoenix.getHealth() <= 0 || Miles.getHealth() <= 0) {
            timer.stop();
            setVisible(false);

            String winnerName;
            if (phoenix.getHealth() <= 0) {
                winnerName = Miles.getName();
                sendPostRequest(phoenix.getName(), "Lost");
                sendPostRequest(Miles.getName(), "Won");
            } else {
                winnerName = phoenix.getName();
                sendPostRequest(phoenix.getName(), "Won");
                sendPostRequest(Miles.getName(), "Lost");
            }

            GameOver.screen(winnerName);
        }
    }

}

