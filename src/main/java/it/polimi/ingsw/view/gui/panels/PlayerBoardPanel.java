package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.view.gui.Gui;

import javax.swing.*;

public class PlayerBoardPanel extends JLayeredPane {

    private final Gui gui;

    public PlayerBoardPanel(Gui gui) {
        super();
        this.gui = gui;

        this.setBounds(0, 0, 1920, 980);
        this.setLayout(null);

        BackgroundImagePanel pl1 = new BackgroundImagePanel("/images/PlayerBoard.jpg", 0, 0, false);
        pl1.setBounds(50, 0, 1802, 887);

        this.add(pl1, 0);
    }
}
