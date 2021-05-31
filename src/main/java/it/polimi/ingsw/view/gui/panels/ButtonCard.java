package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.model.cards.Status;
import it.polimi.ingsw.model.exceptions.ImageNotFound;
import it.polimi.ingsw.view.ImageUtil;
import it.polimi.ingsw.view.gui.Gui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ButtonCard extends JButton {

    private final int cardID;
    private final Gui gui;

    /**
     * ButtonCard with an automatic image resize
     */
    public ButtonCard(Gui gui, String path, Dimension scaledDimension, int id) throws ImageNotFound {
        super();

        this.gui = gui;
        this.cardID = id;

        BufferedImage originalImage = ImageUtil.loadImage(path);
        Image dimg = originalImage.getScaledInstance(scaledDimension.width, scaledDimension.height, Image.SCALE_SMOOTH);
        ImageIcon imageIcon = new ImageIcon(dimg);

        this.setIcon(imageIcon);
        this.setBorderPainted(false);
        this.setContentAreaFilled(false);
        this.setPreferredSize(new Dimension(scaledDimension.width, scaledDimension.height));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if(gui.getMyHand().getStatusFromSpecificLeaderCard(cardID) == Status.ACTIVE) {
            this.setBorder(BorderFactory.createLineBorder(Color.RED, 5));
            this.setBorderPainted(true);
        }
    }
}
