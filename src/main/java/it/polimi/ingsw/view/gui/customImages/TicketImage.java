package it.polimi.ingsw.view.gui.customImages;

import it.polimi.ingsw.model.exceptions.ImageNotFound;
import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.gui.panels.BackgroundImagePanel;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class TicketImage extends SmartImagePanel {

    private final ArrayList<String> playerThatGotTheTicket;

    public TicketImage(Gui gui, String path, Dimension imageDimension,
                       Point absolutePosition, BackgroundImagePanel backGround,
                       ArrayList<String> playerThatGotTheTicket) throws ImageNotFound {
        super(gui, path, imageDimension, absolutePosition, backGround);
        this.playerThatGotTheTicket = playerThatGotTheTicket;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

    }

    @Override
    public void onMouseClicked(MouseEvent e, Graphics2D graphics2D) {

    }

    @Override
    public void onHighlight(Graphics2D graphics2D) {
        graphics2D.setFont(new Font("Rubik", Font.PLAIN, 20));
        graphics2D.setColor(Color.RED);
        int offsetNick=120;
        for (String nick: playerThatGotTheTicket) {
            graphics2D.drawString(nick, getImagePosition().x,getImagePosition().y + offsetNick);
            offsetNick+=15;
        }

        getBackGroundPanel().repaint();
    }

    public ArrayList<String> getPlayerThatGotTheTicket() {
        return playerThatGotTheTicket;
    }
}
