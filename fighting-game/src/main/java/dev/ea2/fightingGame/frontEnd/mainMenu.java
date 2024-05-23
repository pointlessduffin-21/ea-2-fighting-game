package dev.ea2.fightingGame.frontEnd;

import dev.ea2.fightingGame.extensions.ImagePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class mainMenu extends JFrame {
    private JButton playButton;
    private JButton quitButton;
    private JLabel titleLabel;
    private characterSelect1 characterSelectWindow;

    public mainMenu() {
        setTitle("Contempt in Court");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 720);
        setLayout(new GridLayout(3, 1));

        try {
            setContentPane(new ImagePanel("/images/background.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        setLayout(new GridLayout(3, 1));


        // Load the logo image from a file
        ImageIcon logoIcon = new ImageIcon("src/main/resources/images/logo.png");

        // Create a JLabel to display the logo
        JLabel logoLabel = new JLabel(logoIcon);
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center the logo horizontally

        // Add padding and adjust the logo position
        logoLabel.setBorder(BorderFactory.createEmptyBorder(150, 0, 50, 0)); // Adjusted padding to move the logo down
        // Add the logo JLabel to the container
        add(logoLabel);


        JPanel playPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 165));
        playPanel.setOpaque(false);
        playButton = new JButton("Play");
        playButton.setPreferredSize(new Dimension(100, 50));
        characterSelectWindow = new characterSelect1();

        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainMenu.this.setVisible(false); // Hide the current window
                characterSelectWindow.setVisible(true); // Show the characterSelect1 window
            }
        });
        playPanel.add(playButton);
        add(playPanel);

        JPanel quitPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        quitPanel.setOpaque(false);
        quitButton = new JButton("Quit");
        quitButton.setPreferredSize(new Dimension(100, 50));
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        quitPanel.add(quitButton);
        add(quitPanel);


        // leadership button
        JPanel leadershipPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        leadershipPanel.setOpaque(false);
        JButton leadershipButton = new JButton("Leadership");
        leadershipButton.setPreferredSize(new Dimension(100, 50));

        // href to localhost:6969

        leadershipButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    java.awt.Desktop.getDesktop().browse(java.net.URI.create("http://localhost:6969"));
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });

        quitPanel.add(leadershipButton);
        add(quitPanel);

        // about us button

        JButton aboutUsButton = new JButton("About Us");
        aboutUsButton.setPreferredSize(new Dimension(100, 50));
        aboutUsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainMenu.this.setVisible(false);
                new aboutus().setVisible(true);
            }
        });

        quitPanel.add(aboutUsButton);
        add(quitPanel);





        setVisible(true);
    }
}