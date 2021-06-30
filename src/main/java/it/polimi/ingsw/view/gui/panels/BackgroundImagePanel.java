package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.model.exceptions.ImageNotFound;
import it.polimi.ingsw.view.ImageUtil;
import javax.swing.*;
import java.awt.*;

public class BackgroundImagePanel extends JPanel {

    private final Image backGroundImg;
    private final int xB;
    private final int yB;
    private Graphics2D graphics2D;
    private Point pivot;

    private int widthB = 0;
    private int heightB = 0;

    public BackgroundImagePanel(String path, int xB, int yB, boolean absolutePosition) throws ImageNotFound {
        this.xB = xB;
        this.yB = yB;

        pivot = new Point();
        pivot.x = xB;
        pivot.y = yB;

        backGroundImg = ImageUtil.loadImage(path);

        if (absolutePosition)
            setLayout(null);

        setOpaque(false);
    }

    public BackgroundImagePanel(String path, boolean absolutePosition) throws ImageNotFound {
        this.xB = 0;
        this.yB = 0;

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

        if(widthB == 0 || heightB == 0)
            graphics2D.drawImage(backGroundImg, xB, yB, this);
        else
            graphics2D.drawImage(backGroundImg, xB, yB, widthB, heightB, this);
    }

    public Graphics2D getGraphics2D(){
        return graphics2D;
    }

    public Point getPivot() {
        return pivot;
    }

    public void setWidth(int width) {
        this.widthB = width;
    }

    public void setHeight(int height) {
        this.heightB = height;
    }
}
