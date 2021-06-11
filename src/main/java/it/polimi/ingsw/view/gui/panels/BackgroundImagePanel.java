package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.model.exceptions.ImageNotFound;
import it.polimi.ingsw.view.ImageUtil;

import javax.swing.*;
import java.awt.*;

public class BackgroundImagePanel extends JPanel {

    private final Image backGroundImg;
    private final int x;
    private final int y;
    private Graphics2D graphics2D;
    private Point pivot;

    private int width = 0;
    private int height = 0;

    public BackgroundImagePanel(String path, int x, int y, boolean absolutePosition) throws ImageNotFound {
        this.x = x;
        this.y = y;

        pivot = new Point();
        pivot.x = x;
        pivot.y = y;

        backGroundImg = ImageUtil.loadImage(path);

        if (absolutePosition)
            setLayout(null);

        setOpaque(false);
    }

    public BackgroundImagePanel(String path, boolean absolutePosition) throws ImageNotFound {
        this.x = 0;
        this.y = 0;

        backGroundImg = ImageUtil.loadImage(path);

        if (absolutePosition)
            setLayout(null);

        setOpaque(false);
    }

    public Dimension getPreferredSize() {
        return backGroundImg == null ?  new Dimension(200, 200) : new Dimension(backGroundImg.getWidth(this), backGroundImg.getHeight(this));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        graphics2D = (Graphics2D) g.create();

        if(width == 0 || height == 0)
            graphics2D.drawImage(backGroundImg, x, y, this);
        else
            graphics2D.drawImage(backGroundImg, x, y, width, height, this);
    }

    public Graphics2D getGraphics2D(){
        return graphics2D;
    }

    public Point getPivot() {
        return pivot;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
