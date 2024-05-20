package dev.ea2.fightingGame.frontEnd;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import dev.ea2.fightingGame.extensions.ImagePanel;

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

        titleLabel = new JLabel("Order in the Court", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        add(titleLabel);

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

        KeyStroke pKeyStroke = KeyStroke.getKeyStroke('p');

        Action openAboutUsAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainMenu.this.setVisible(false);
                aboutus.deez();
            }
        };

        getRootPane().getActionMap().put("openAboutUs", openAboutUsAction);

        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(pKeyStroke, "openAboutUs");
        quitPanel.add(quitButton);
        add(quitPanel);

        setVisible(true);
    }
}