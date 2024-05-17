package dev.ea2.fightingGame.extensions;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImagePanel extends JPanel {

    private Image backgroundImage;

    // Some code to initialize the background image.
    // Here, we're reading from a file.
    public ImagePanel(String fileName) throws IOException {
        backgroundImage = ImageIO.read(getClass().getResource(fileName));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the background image.
        g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), this);
    }
}