package it.polimi.ingsw.view.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class BackgroundImagePanel extends JPanel {

    private final String path;
    private Image backGroundImg;
    private final int x;
    private final int y;
    private Graphics2D graphics2D;
    private Point pivot;

    public BackgroundImagePanel(String path, int x, int y, boolean absolutePosition) {
        this.path = path;
        this.x = x;
        this.y = y;

        pivot = new Point();
        pivot.x = x;
        pivot.y = y;

        try {
            backGroundImg = ImageIO.read(getClass().getResourceAsStream(path));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (absolutePosition)
            setLayout(null);
    }

    public BackgroundImagePanel(String path, boolean absolutePosition) {
        this.path = path;
        this.x = 0;
        this.y = 0;

        if (absolutePosition)
            setLayout(null);
    }

    public Dimension getPreferredSize() {
        return backGroundImg == null ?  new Dimension(200, 200) : new Dimension(backGroundImg.getWidth(this), backGroundImg.getHeight(this));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        graphics2D = (Graphics2D) g.create();
        graphics2D.drawImage(backGroundImg, x, y, this);
    }

    public Graphics2D getGraphics2D(){
        return graphics2D;
    }

    public Point getPivot() {
        return pivot;
    }
}
