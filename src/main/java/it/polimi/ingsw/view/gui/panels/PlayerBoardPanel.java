package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.gui.customImages.LabelImage;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

public class PlayerBoardPanel extends JLayeredPane {

    private final Gui gui;
    private final DepositPanel depositPanel;
    private final ProductionPanel productionPanel;
    private final VaultPanel vaultPanel;
    private  AfterMarketPanel afterMarketPanel;
    private HandPanel handPanel;

    public PlayerBoardPanel(Gui gui) {
        super();
        this.gui = gui;

        this.setLayout(null);
        this.setBounds(0, 0, 1920, 980);


        //BACKGROUND LAYER 0 ------------------------------------------------------------------------------------------------------
        JPanel pl0 = new JPanel();
        pl0.setLayout(new BorderLayout());
        pl0.setOpaque(false);

        JPanel pl1 = new JPanel();
        pl1.setLayout(new BorderLayout());
        pl1.setBorder(new EmptyBorder(30,20,70,0));
        LabelImage boardImg = new LabelImage("/images/backgrounds/PlayerBoard.jpg");
        pl1.add(boardImg);
        pl1.setOpaque(false);

        handPanel = new HandPanel(gui);
        handPanel.setOpaque(false);

        pl0.setBorder(new EmptyBorder(0,0,0,10));
        pl0.add(handPanel, BorderLayout.EAST);
        pl0.add(pl1, BorderLayout.CENTER);
        pl0.setBounds(0, 0, 1920, 980);

        this.add(pl0, JLayeredPane.DEFAULT_LAYER);

        //VAULT-DEPOSIT LAYER ------------------------------------------------------------------------------------------------------
        JPanel storage = new JPanel();
        storage.setBounds(0,0,1920, 980);
        storage.setOpaque(false);
        storage.setLayout(null);

        //DEPOSIT ZONE
        depositPanel = new DepositPanel(gui);
        depositPanel.setBounds(70,120,320,385);
        storage.add(depositPanel);


        //VAULT
        vaultPanel = new VaultPanel(gui);
        vaultPanel.setBounds(40,550,330, 270);
        storage.add(vaultPanel);

        this.add(storage, JLayeredPane.PALETTE_LAYER);

        //PRODUCTION LAYER--------------------------------------------------------------------------------------------
        productionPanel = new ProductionPanel(gui);
        productionPanel.setBounds(0,0,1920, 980);

        this.add(productionPanel, JLayeredPane.PALETTE_LAYER);

        //MARKET LAYER
        afterMarketPanel= new AfterMarketPanel(gui);
        afterMarketPanel.setBounds(300,0,1920,980);
        this.add(afterMarketPanel, JLayeredPane.POPUP_LAYER);
        //afterMarketPanel.setVisible(false);


    }

    public DepositPanel getDepositPanel() {
        return depositPanel;
    }

    public ProductionPanel getProductionPanel() { return productionPanel; }

    public VaultPanel getVaultPanel() { return vaultPanel; }

    public AfterMarketPanel getAfterMarketPanel() { return afterMarketPanel;  }

    public void visibleMarketOut(){afterMarketPanel.setVisible(true);}

    public HandPanel getHandPanel() { return handPanel; }
}

