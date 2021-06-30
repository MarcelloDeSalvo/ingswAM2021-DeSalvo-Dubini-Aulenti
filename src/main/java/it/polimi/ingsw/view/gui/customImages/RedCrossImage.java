package it.polimi.ingsw.view.gui.customImages;


import it.polimi.ingsw.model.exceptions.ImageNotFound;
import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.gui.panels.BackgroundImagePanel;

import java.awt.*;
import java.awt.event.MouseEvent;

public class RedCrossImage extends SmartImagePanel {
    private int count = 0;
    private final String nick;
    private int offSet;

    public RedCrossImage(Gui gui, String path, Dimension imageDimension, Point absolutePosition, BackgroundImagePanel backGround, String nick, int offSet) throws ImageNotFound {
        super(gui, path, imageDimension, absolutePosition, backGround);
        this.offSet = offSet;
        this.nick = nick;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        getBackGroundPanel().getGraphics2D().setFont(new Font("Rubik", Font.PLAIN, 20));
        getBackGroundPanel().getGraphics2D().setColor(Color.BLUE);
        getBackGroundPanel().getGraphics2D().drawString(nick, getImagePosition().x,getImagePosition().y + offSet);

    }

    @Override
    public void onMouseClicked(MouseEvent e, Graphics2D graphics2D) {
    }

    @Override
    public void onHighlight(Graphics2D graphics2D) {
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

        getBackGroundPanel().repaint();

    }

    public String getNick() {
        return nick;
    }
}
