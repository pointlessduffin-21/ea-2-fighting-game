package dev.ea2.fightingGame.frontEnd;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GameOver extends JFrame {

    private static GameOver instance = null;
    private JLabel winnerLabel;
    private JLabel gameOverLabel;

    private GameOver() {
        initComponents();
    }

    public static GameOver getInstance() {
        if (instance == null) {
            instance = new GameOver();
        }
        return instance;
    }

    public static void screen(String winnerName) {
        GameOver gameOver = GameOver.getInstance();
        gameOver.setWinner(winnerName);
        gameOver.setVisible(true);
        gameOver.fadeInLabels();
    }

    private void initComponents() {
        setTitle("Game Over");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 720);
        setLayout(new BorderLayout());

        gameOverLabel = new JLabel(" Game Over ", SwingConstants.CENTER);
        gameOverLabel.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 60));
        gameOverLabel.setForeground(new Color(255, 255, 255, 0)); // Start with fully transparent

        winnerLabel = new JLabel("", SwingConstants.CENTER);
        winnerLabel.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 40));
        winnerLabel.setForeground(new Color(255, 255, 255, 0)); // Start with fully transparent

        JButton mainMenuButton = new JButton("🏠 Main Menu");
        mainMenuButton.setFont(new Font("Dialog", Font.BOLD, 18));
        mainMenuButton.addActionListener(e -> {
            mainMenu mainMenu = new mainMenu();
            mainMenu.setVisible(true);
            setVisible(false);
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        buttonPanel.setOpaque(false);
        buttonPanel.add(mainMenuButton);

        JLabel backgroundLabel = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(0, 0, Color.DARK_GRAY, getWidth(), getHeight(), Color.BLACK);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        backgroundLabel.setLayout(new GridLayout(3, 1));
        backgroundLabel.add(gameOverLabel);
        backgroundLabel.add(winnerLabel);
        backgroundLabel.add(buttonPanel);

        add(backgroundLabel);



        // Disable Enter and Space keys
        KeyAdapter keyAdapter = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_SPACE) {
                    e.consume(); // Ignore the key event
                }
            }
        };

        addKeyListener(keyAdapter);
        mainMenuButton.addKeyListener(keyAdapter);
        setFocusable(true);
        requestFocusInWindow();
    }

    public void setWinner(String winnerName) {
        winnerLabel.setText(" Winner: " + winnerName  );
    }

    public static void display(String winnerName) {
        GameOver.getInstance().setWinner(winnerName);
        GameOver.getInstance().setVisible(true);
        GameOver.getInstance().fadeInLabels();
    }

    private void fadeInLabels() {
        Timer timer = new Timer(20, new ActionListener() {
            float opacity = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                opacity += 0.05f;
                if (opacity > 1) {
                    opacity = 1;
                    ((Timer) e.getSource()).stop();
                }
                gameOverLabel.setForeground(new Color(255, 255, 255, (int) (opacity * 255)));
                winnerLabel.setForeground(new Color(255, 255, 255, (int) (opacity * 255)));
            }
        });
        timer.start();
    }


    public void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        this.setVisible(false); // Hide the current window
        new mainMenu();
    }
}
