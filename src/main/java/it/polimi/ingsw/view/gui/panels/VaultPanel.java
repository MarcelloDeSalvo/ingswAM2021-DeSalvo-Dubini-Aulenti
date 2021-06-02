package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.liteModel.LiteVault;
import it.polimi.ingsw.model.resources.ResourceType;
import it.polimi.ingsw.view.gui.buttons.ButtonImage;

import javax.swing.*;
import java.awt.*;

public class VaultPanel extends JLayeredPane {

    private final int buttonSize = 110;

    public VaultPanel(LiteVault liteVault) {
        super();

        this.setLayout(null);
        this.setOpaque(false);

        //LAYER0 -------------------------------------------------------------------------------------------------------
        JPanel layer0 = new JPanel();
        layer0.setLayout(new BoxLayout(layer0, BoxLayout.Y_AXIS));
        layer0.setOpaque(false);
        layer0.setBounds(0,0,330, 270);

        //ROW 0-1 -------------------------
        JPanel row01 = new JPanel();
        row01.setLayout(new BoxLayout(row01, BoxLayout.X_AXIS));
        row01.setOpaque(false);

        ButtonImage gold = new ButtonImage("/images/resourceImages/gold.png", new Dimension(buttonSize,buttonSize));
        gold.setMinimumSize(new Dimension(buttonSize-1, buttonSize-1));
        gold.setMaximumSize(new Dimension(buttonSize, buttonSize));
        gold.setOpaque(false);

        ButtonImage stone = new ButtonImage("/images/resourceImages/stone.png", new Dimension(buttonSize,buttonSize));
        stone.setMinimumSize(new Dimension(buttonSize-1, buttonSize-1));
        stone.setMaximumSize(new Dimension(buttonSize, buttonSize));
        stone.setOpaque(false);

        row01.add(gold);
        row01.add(Box.createRigidArea(new Dimension(25,5)));
        row01.add(stone);

        //ROW 0-2 -------------------------
        JPanel row02 = new JPanel();
        row02.setLayout(new BoxLayout(row02, BoxLayout.X_AXIS));
        row02.setOpaque(false);

        ButtonImage minion = new ButtonImage("/images/resourceImages/minion.png", new Dimension(buttonSize, buttonSize));
        minion.setMinimumSize(new Dimension(buttonSize-1, buttonSize-1));
        minion.setMaximumSize(new Dimension(buttonSize, buttonSize));
        minion.setOpaque(false);

        ButtonImage shield = new ButtonImage("/images/resourceImages/shield.png", new Dimension(buttonSize, buttonSize));
        shield.setMinimumSize(new Dimension(buttonSize-1, buttonSize-1));
        shield.setMaximumSize(new Dimension(buttonSize, buttonSize));
        shield.setOpaque(false);

        row02.add(minion);
        row02.add(Box.createRigidArea(new Dimension(25,5)));
        row02.add(shield);

        layer0.add(row01);
        layer0.add(Box.createRigidArea(new Dimension(5,25)));
        layer0.add(row02);

        this.add(layer0, JLayeredPane.DEFAULT_LAYER);


        //LAYER1 -------------------------------------------------------------------------------------------------------
        JPanel layer1 = new JPanel();
        layer1.setLayout(new BoxLayout(layer1, BoxLayout.Y_AXIS));
        layer1.setOpaque(false);
        layer1.setBounds(-10,0,330, 270);

        Font font = new Font("Rubik", Font.BOLD, 25);

        //ROW 1-1 -------------------------
        JPanel row11 = new JPanel();
        row11.setLayout(new BoxLayout(row11, BoxLayout.X_AXIS));
        row11.setOpaque(false);

        JLabel goldLabel = new JLabel(" " + liteVault.getQtyOfResource(ResourceType.GOLD) + " ");
        goldLabel.setFont(font);
        goldLabel.setForeground(Color.BLACK);

        JLabel stoneLabel = new JLabel(" " + liteVault.getQtyOfResource(ResourceType.STONE) + " ");
        stoneLabel.setFont(font);
        stoneLabel.setForeground(Color.BLACK);

        row11.add(goldLabel);
        row11.add(Box.createRigidArea(new Dimension(100,5)));
        row11.add(stoneLabel);

        //ROW 1-2 -------------------------
        JPanel row12 = new JPanel();
        row12.setLayout(new BoxLayout(row12, BoxLayout.X_AXIS));
        row12.setOpaque(false);

        JLabel minionLabel = new JLabel(" " + liteVault.getQtyOfResource(ResourceType.MINION) + " ");
        minionLabel.setFont(font);
        minionLabel.setForeground(Color.BLACK);

        JLabel shieldLabel = new JLabel(" " + liteVault.getQtyOfResource(ResourceType.SHIELD) + " ");
        shieldLabel.setFont(font);
        shieldLabel.setForeground(Color.BLACK);

        row12.add(minionLabel);
        row12.add(Box.createRigidArea(new Dimension(100,5)));
        row12.add(shieldLabel);

        layer1.add(Box.createRigidArea(new Dimension(5,30)));
        layer1.add(row11);
        layer1.add(Box.createRigidArea(new Dimension(5,110)));
        layer1.add(row12);

        this.add(layer1, JLayeredPane.POPUP_LAYER);
    }
}
