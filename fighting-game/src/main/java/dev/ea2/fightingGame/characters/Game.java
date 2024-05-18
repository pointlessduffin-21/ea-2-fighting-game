package dev.ea2.fightingGame.characters;

import javax.swing.*;
import java.awt.*;

public class Game {

    public static void main(String[] args) {
        KeyHandler keyHandler = new KeyHandler();

        PlayerCharacter player = new PlayerCharacter(100, 50, 50, keyHandler, "Hero", new String[]{"Punch", "Kick"});

        JFrame frame = new JFrame("Character Movement Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setFocusable(true);
        panel.addKeyListener(keyHandler);
        frame.add(panel, BorderLayout.CENTER);

        frame.setVisible(true);

        Timer timer = new Timer(100, e -> player.update());
        timer.start();
    }
}
