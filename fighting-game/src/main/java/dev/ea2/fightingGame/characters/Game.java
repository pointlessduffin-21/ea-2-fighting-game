package dev.ea2.fightingGame.characters;

import dev.ea2.fightingGame.frontEnd.gameOver;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.logging.Logger;

public class Game extends JPanel {

    private static final Logger logger = Logger.getLogger(Game.class.getName());

    private int PWHealth;
    private int MEHealth;

    private final String imageFile;

    private final KeyHandler keyHandler = new KeyHandler();
    private final PW pw;
    private final ME me;

    private BufferedImage backgroundImage;
    private BufferedImage pwHeart;
    private BufferedImage mwHeart;

    private Timer timer;

    // Constructor
    public Game(int PWHealth, int MEHealth, String imageFile) {
        this.PWHealth = PWHealth;
        this.MEHealth = MEHealth;
        this.imageFile = imageFile;

        // Initialize characters
        pw = new PW(PWHealth, 50, 600, 15, 100, 100, keyHandler, "Phoenix Wright", new String[]{"Objection!", "Present"});
        me = new ME(MEHealth, 1100, 600, 15, 100, 100, keyHandler, "Miles Edgeworth", new String[]{"Objection!", "Present"});
    }

    // Method to start the game
    public static void StartGameWithImage(String imageFile) {
        Game game = new Game(5, 5, imageFile);
        game.start(imageFile);
    }

    // Method to pause the game
    public void pause() {
        timer.stop();
        setVisible(false);
        pauseMenu.pauseActual(this, imageFile, PWHealth, MEHealth);
    }

    // Method to initialize and start the game
    private void start(String imageFile) {
        // Create JFrame
        JFrame frame = new JFrame("Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1280, 720);
        frame.setLayout(new BorderLayout());

        // Add key listener to JPanel
        JPanel panel = new JPanel();
        panel.setFocusable(true);
        panel.addKeyListener(keyHandler);
        frame.add(panel, BorderLayout.CENTER);
        frame.add(this);

        // Load images
        try {
            backgroundImage = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(imageFile)));
            pwHeart = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/images/icons/pwHeart.png")));
            mwHeart = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/images/icons/mwHeart.png")));
        } catch (IOException e) {
            logger.severe("Error loading images for " + imageFile);
        }

        // Set timer to update game state periodically
        timer = new Timer(60, e -> {
            pw.update();
            me.update();
            checkCollisions();
            repaint();

            // Check for game over conditions
            if (pw.getHealth() <= 0 || me.getHealth() <= 0) {
                timer.stop();
                setVisible(false);
                gameOver.screen();
                if (PWHealth <= 0) {
                    sendPostRequest(pw.getName(), "Lost");
                } else {
                    sendPostRequest(pw.getName(), "Won");
                }

                if (MEHealth <= 0) {
                    sendPostRequest(me.getName(), "Lost");
                } else {
                    sendPostRequest(me.getName(), "Won");
                }
            }
        });
        timer.start();

        // Add key listener to pause the game
        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    setVisible(false);
                    pause();
                }
            }
        });

        // Show JFrame
        frame.setVisible(true);
    }

    private void sendPostRequest(String playerName, String result) {
        try {
            URL url = new URL("http://localhost:6969/api/add"); // Replace with your actual API endpoint
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            // Get current date and time
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            String formattedDateTime = now.format(formatter);

            String jsonInputString = String.format("{\"playerName\": \"%s\", \"result\": \"%s\", \"datePlayed\": \"%s\"}",
                    playerName, result, formattedDateTime);

            try(OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            try(BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                System.out.println(response.toString()); // Log the response for debugging
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Method to paint components on JPanel
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Draw background image
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }

        // Draw characters
        pw.draw(g2, pw.getName());
        me.draw(g2, me.getName());

        // Draw hearts for PW
        for (int i = 0; i < pw.getHealth(); i++) {
            g2.drawImage(pwHeart, i * pwHeart.getWidth(), 0, this);
        }

        // Draw hearts for ME
        for (int i = 0; i < me.getHealth(); i++) {
            g2.drawImage(mwHeart, this.getWidth() - (i + 1) * mwHeart.getWidth(), 0, this);
        }

        g2.dispose();
    }

    // Method to check collisions and handle health deduction and knockback
    private void checkCollisions() {
        if (pw.getHitbox().intersects(me.getShortHitBox())) {
            pw.setHealth(pw.getHealth() - 1);
            knockback(pw, me);
        }
        if (me.getHitbox().intersects(pw.getShortHitBox())) {
            me.setHealth(me.getHealth() - 1);
            knockback(me, pw);
        }
        // Check other hitboxes similarly
    }

    private void knockback(CharacterBase attacker, CharacterBase defender) {
        int knockbackDistance = 500;
        if (defender.getX() > attacker.getX()) {
            defender.setX(defender.getX() + knockbackDistance);
        } else {
            defender.setX(defender.getX() - knockbackDistance);
        }
    }
}
