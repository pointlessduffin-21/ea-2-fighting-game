package dev.ea2.fightingGame.characters;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Game extends JPanel{

    private KeyHandler keyHandler = new KeyHandler();
    private BufferedImage backgroundImage;
    PlayerCharacter player = new PlayerCharacter(5, 50, 250, 5, 100, 100, keyHandler, "Hero", new String[]{"Punch", "Kick"});
    PW pw = new PW(5, 30, 600, 15, 100, 100, keyHandler, "Phoenix Wright", new String[] { "Objection!", "Present" });
    ME me = new ME(5, 1100, 600, 15, 100, 100, keyHandler, "Miles Edgeworth", new String[]{"Objection!", "Present"});



    public static void gago(String imageFile) {
        Game game = new Game();
        game.start("/characters/PW/PW_idle.png");
    }

    private void start(String imageFile) {

        JFrame frame = new JFrame("Character Movement Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1280, 720);
        frame.setLayout(new BorderLayout());
        
        // Add the Game panel to the frame
     
        JPanel panel = new JPanel();
        panel.setFocusable(true);
        panel.addKeyListener(keyHandler); // Register KeyHandler to the panel
        frame.add(panel, BorderLayout.CENTER);
        frame.add(this); // 'this' refers to the current Game JPanel
      
        frame.setVisible(true);

        try {
            backgroundImage = ImageIO.read(getClass().getResourceAsStream(imageFile));
           
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // Timer to call update periodically for both characters
        Timer timer = new Timer(60, e -> {
            pw.update();
            me.update();
            repaint();
        });
        timer.start();
    }
    
    public void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        
    
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            System.out.print("sex");
        }
        pw.draw(g2);
        me.draw(g2);

        g2.dispose();
    }
}
