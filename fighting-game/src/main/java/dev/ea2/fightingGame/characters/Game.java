package dev.ea2.fightingGame.characters;

import dev.ea2.fightingGame.frontEnd.pauseMenu;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Game extends JPanel{

    public int PWHealth = 5;
    public int MEHealth = 5;

    private BufferedImage pwHeart;
    private BufferedImage mwHeart;

    private KeyHandler keyHandler = new KeyHandler();
    private BufferedImage backgroundImage;
    PlayerCharacter player = new PlayerCharacter(5, 50, 250, 5, 100, 100, keyHandler, "Hero", new String[]{"Punch", "Kick"});
    PW pw = new PW(PWHealth, 30, 600, 15, 100, 100, keyHandler, "Phoenix Wright", new String[] { "Objection!", "Present" });
    ME me = new ME(MEHealth, 1100, 600, 15, 100, 100, keyHandler, "Miles Edgeworth", new String[]{"Objection!", "Present"});

    public static void gago(String imageFile) {
        Game game = new Game();
        game.start(imageFile);
    }

    private void start(String imageFile) {

        JFrame frame = new JFrame("Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1280, 720);
        frame.setLayout(new BorderLayout());
     
        JPanel panel = new JPanel();
        panel.setFocusable(true);
        panel.addKeyListener(keyHandler);
        frame.add(panel, BorderLayout.CENTER);
        frame.add(this);
      
        frame.setVisible(true);

        try {
            backgroundImage = ImageIO.read(getClass().getResourceAsStream(imageFile));
            pwHeart = ImageIO.read(getClass().getResourceAsStream("/images/icons/pwHeart.png"));
            mwHeart = ImageIO.read(getClass().getResourceAsStream("/images/icons/mwHeart.png"));
           
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

        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    Game.this.setVisible(false);
                    pauseMenu.pauseActual(imageFile);
                }
            }
        });
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

        // Draw hearts for PW
        for (int i = 0; i < PWHealth; i++) {
            g2.drawImage(pwHeart, i * pwHeart.getWidth(), 0, this);
        }

        // Draw hearts for ME
        for (int i = 0; i < MEHealth; i++) {
            g2.drawImage(mwHeart, this.getWidth() - (i + 1) * mwHeart.getWidth(), 0, this);
        }

        g2.dispose();
    }
}
