package it.polimi.ingsw.view.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class BackgroundImagePanel extends JPanel {

    private final String path;
    private final int x;
    private final int y;

    public BackgroundImagePanel(String path, int x, int y) {
        super();
        this.path = path;
        this.x = x;
        this.y = y;
    }

    public BackgroundImagePanel(String path) {
        super();
        this.path = path;
        this.x = 0;
        this.y = 0;
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        try {
            Image image = ImageIO.read(getClass().getResourceAsStream(path));
            g.drawImage(image, x, y, this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
