package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.view.gui.Gui;

import javax.swing.*;
import java.awt.*;

public class PlayerBoardPanel extends BackgroundImagePanel {

    private final Gui gui;

    public PlayerBoardPanel(Gui gui) {
        super("/images/PLayerBoard.jpg", false);
        this.gui = gui;

        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder(50,50,50,50));
    }
}
