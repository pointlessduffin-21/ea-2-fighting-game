package dev.ea2.fightingGame.frontEnd;

import dev.ea2.fightingGame.characters.Game;

import java.awt.*;

public class pauseMenu extends javax.swing.JFrame {

    private Game game;
    private String imageFile;
    private int PWHealth;
    private int MEHealth;

    public pauseMenu(Game game, String imageFile, int PWHealth, int MEHealth) {
        initComponents(imageFile);
        this.game = game;
        this.imageFile = imageFile;
        this.PWHealth = PWHealth;
        this.MEHealth = MEHealth;
    }

    @SuppressWarnings("unchecked")
    private void initComponents(String imageFile) {

        jLabel2 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setSize(1280, 720);
        setLayout(new BorderLayout());

        jLabel2.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 36)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Game Paused");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(530, 240, 250, 60);

        jButton2.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jButton2.setText("Resume");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pauseMenu.this.setVisible(false);
                game.resume(); // Resume the game
            }
        });
        getContentPane().add(jButton2);
        jButton2.setBounds(580, 310, 150, 50);

        jButton1.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jButton1.setText("Quit");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mainMenu mainMenu = new mainMenu();
                mainMenu.setVisible(true);
                pauseMenu.this.setVisible(false);
                game.quit(); // Quit the game
            }


        });
        getContentPane().add(jButton1);
        jButton1.setBounds(580, 370, 150, 50);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/background.jpg"))); // NOI18N
        getContentPane().add(jLabel1);
        jLabel1.setBounds(0, 0, 1280, 720);

        pack();
    }

    public static void pauseActual(Game game, String imageFile, int PWHealth, int MEHealth) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new pauseMenu(game, imageFile, PWHealth, MEHealth).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    // End of variables declaration
}
