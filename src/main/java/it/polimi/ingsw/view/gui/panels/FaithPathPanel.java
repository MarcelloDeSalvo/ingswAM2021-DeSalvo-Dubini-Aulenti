package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.liteModel.LiteFaithPath;
import it.polimi.ingsw.model.exceptions.ImageNotFound;
import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.gui.customImages.RedCrossImage;
import it.polimi.ingsw.view.gui.customImages.TicketImage;

import java.awt.*;
import java.util.ArrayList;

public class FaithPathPanel extends BackgroundImagePanel {

    private final Gui gui;
    private final ArrayList<Point> ticketPoints;

    private final ArrayList<RedCrossImage> redCrossImages;
    private final ArrayList<TicketImage> ticketImages;
    private int ticketsCounter;

    /**
     * Creates the panel used to display the common faithpath the players share
     */
    public FaithPathPanel(Gui gui, LiteFaithPath liteFaithPath) throws ImageNotFound {
        super("/images/backgrounds/FaithPath.jpg", 50, 250, true);
        this.gui=gui;

        ArrayList<Point> points = new ArrayList<>();
        ticketPoints = new ArrayList<>();
        ticketImages = new ArrayList<>();

        points.add(new Point(80, 256));
        points.add(new Point(115, 256));
        points.add(new Point(80, 296));
        points.add(new Point(115, 296));

        ticketPoints.add(new Point(450, 190));
        ticketPoints.add(new Point(900, 100));
        ticketPoints.add(new Point(1420, 190));


        redCrossImages = new ArrayList<>();

        int i = 0;
        int off = 50;
        for (String nickname: liteFaithPath.getNicknames()) {
            RedCrossImage redCrossImage = new RedCrossImage(gui, "/images/others/redCross.png",
                    new Dimension(30, 30), points.get(i), this, nickname, off);
            off+=20;

            redCrossImages.add(redCrossImage);
            add(redCrossImage);

            incRedCrossImages(nickname, liteFaithPath.getPositions(nickname));

            i++;
        }

    }

    /**
     * This method adds to the faithpath the newly "flipped" Papal Favour and all the players that earned it
     */
    public void printTicket(int point, ArrayList<String> playerThatGotThePoint){

        if(ticketImages.size()<=point-2){
            TicketImage ticketImage = new TicketImage(gui,"/images/others/"+point+"_Points.png",
                    new Dimension(100, 100 ), ticketPoints.get(ticketsCounter), this, playerThatGotThePoint);

            ticketImages.add(ticketImage);
            ticketsCounter++;
            this.revalidate();
            this.repaint();
            add(ticketImage);
        }else{
            for (String nick : playerThatGotThePoint) {
                ticketImages.get(point-2).getPlayerThatGotTheTicket().add(nick);
            }

        }

    }

    /**
     * Increases the position of senderNick in the faithPath
     * @param senderNick player that moved forward
     */
    public void incRedCrossImages(String senderNick, int qty) {
        for (RedCrossImage red: redCrossImages) {
            if (red.getNick().equals(senderNick))
                for (int i = 0; i<qty; i++)
                    red.increasePos();
        }
    }

    /**
     * Increases the position of everyone BUT senderNick in the faithPath
     * @param senderNick player that discarded resources
     */

    public void incOtherRedCrossImages(String senderNick, int qty) {
        for (RedCrossImage red: redCrossImages) {
            if (!red.getNick().equals(senderNick))
                for (int i = 0; i<qty; i++)
                    red.increasePos();
        }
    }
}
