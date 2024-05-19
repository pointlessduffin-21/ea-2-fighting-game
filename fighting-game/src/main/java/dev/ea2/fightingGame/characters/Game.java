package dev.ea2.fightingGame.characters;

import javax.swing.*;
import java.awt.*;

public class Game extends JPanel{

    private KeyHandler keyHandler = new KeyHandler();
    PlayerCharacter player = new PlayerCharacter(5, 50, 250, 5, 100, 100, keyHandler, "Hero", new String[]{"Punch", "Kick"});
    PW pw = new PW(5, 30, 250, 15, 100, 100, keyHandler, "Phoenix Wright", new String[]{"Objection!", "Present"});


    public static void main(String[] args) {
        // Create the Game instance to access its non-static members
        Game game = new Game();
        game.start();
    }

    private void start() {

        JFrame frame = new JFrame("Character Movement Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());
        
        // Add the Game panel to the frame
     
        JPanel panel = new JPanel();
        panel.setFocusable(true);
        panel.addKeyListener(keyHandler); // Register KeyHandler to the panel
        frame.add(panel, BorderLayout.CENTER);
        frame.add(this); // 'this' refers to the current Game JPanel

        frame.setVisible(true);

        // Timer to call update periodically for both characters
        Timer timer = new Timer(60, e -> {
            pw.update();
            repaint();
        });
        timer.start();
    }
    
    public void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        pw.draw(g2);

        g2.dispose();
    }
}
