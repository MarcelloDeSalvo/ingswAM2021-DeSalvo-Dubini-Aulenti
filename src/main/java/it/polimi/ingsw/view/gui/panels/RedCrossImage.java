package it.polimi.ingsw.view.gui.panels;


import it.polimi.ingsw.model.exceptions.ImageNotFound;
import it.polimi.ingsw.view.gui.Gui;

import java.awt.*;
import java.awt.event.MouseEvent;

public class RedCrossImage extends SmartImagePanel {
    private int count = 0;
    private String nick;

    public RedCrossImage(Gui gui, String path, Dimension imageDimension, Point absolutePosition, BackgroundImagePanel backGround, String nick) throws ImageNotFound {
        super(gui, path, imageDimension, absolutePosition, backGround);
        this.nick = nick;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (isHighlight())
            onHighlight((Graphics2D) g);
    }

    @Override
    public void onMouseClicked(MouseEvent e) {
        increasePos();
    }

    @Override
    public void onHighlight(Graphics2D graphics2D) {
        graphics2D.drawString(nick, getImagePosition().x,getImagePosition().y-20);
        repaint();
    }

    public void increasePos(){

        if (count<=1 || count>=4 && count<9 || count>= 11 && count < 16 || count>=18 && count <24) {
            this.setImagePosition(new Point(this.getImagePosition().x + 88, this.getImagePosition().y));
            count++;
        }else if (count < 5 || count >=16 && count <18){
            this.setImagePosition(new Point(this.getImagePosition().x, this.getImagePosition().y-88));
            count++;
        }else if(count<24){
            this.setImagePosition(new Point(this.getImagePosition().x, this.getImagePosition().y+88));
            count++;
        }

    }

    public String getNick() {
        return nick;
    }
}
