package it.polimi.ingsw.view.gui;

import java.awt.*;
import java.awt.event.MouseEvent;

public class RedCrossImage extends SmartImagePanel{
    private int count = 0;
    private String nick;

    public RedCrossImage(Gui gui, String path, Dimension imageDimension, Point absolutePosition, BackgroundImagePanel backGround, String nick) {
        super(gui, path, imageDimension, absolutePosition, backGround);
        this.nick = nick;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
    }

    @Override
    public void onMouseClicked(MouseEvent e) {
        increasePos();
    }

    @Override
    public void onHighlight() {
        getBackGroundPanel().getGraphics2D().drawString(nick, 0,40);
    }

    public void increasePos(){
        this.setImagePosition(new Point(this.getImagePosition().x + 50, this.getImagePosition().y));
    }
}
