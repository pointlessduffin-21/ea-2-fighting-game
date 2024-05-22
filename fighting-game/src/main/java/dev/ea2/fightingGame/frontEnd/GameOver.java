package dev.ea2.fightingGame.frontEnd;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class GameOver extends JFrame {

    private static GameOver instance = null;

    private GameOver() {
        initComponents();
    }

    public static GameOver getInstance() {
        if (instance == null) {
            instance = new GameOver();
        }
        return instance;
    }

    public static void screen() {
        GameOver.getInstance().setVisible(true);
    }

    private void initComponents() {



        setTitle("Game Over");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 720);
        setLayout(new BorderLayout());

        JLabel gameOverLabel = new JLabel("Game Over", SwingConstants.CENTER);
        gameOverLabel.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 36));
        gameOverLabel.setForeground(Color.WHITE);

        JButton mainMenuButton = new JButton("Main Menu");
        mainMenuButton.setFont(new Font("Dialog", Font.BOLD, 18));
        mainMenuButton.addActionListener(e -> {
            mainMenu mainMenu = new mainMenu();
            mainMenu.setVisible(true);
            setVisible(false);
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        buttonPanel.add(mainMenuButton);

        ImageIcon backgroundImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("/images/background.jpg")));
        JLabel backgroundLabel = new JLabel(backgroundImage);
        backgroundLabel.setLayout(new GridLayout(2, 1));
        backgroundLabel.add(gameOverLabel);
        backgroundLabel.add(buttonPanel);

        add(backgroundLabel);

        setLocationRelativeTo(null); // Center the frame on the screen
    }

    public static void display() {
        GameOver.getInstance().setVisible(true);
    }
}
