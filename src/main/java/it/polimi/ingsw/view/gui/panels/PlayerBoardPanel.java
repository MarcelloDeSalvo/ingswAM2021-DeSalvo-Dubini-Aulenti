package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.model.cards.Status;
import it.polimi.ingsw.network.commands.BuyMessage;
import it.polimi.ingsw.network.commands.Command;
import it.polimi.ingsw.network.commands.IdMessage;
import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.gui.GuiStatus;
import it.polimi.ingsw.view.gui.buttons.ButtonCard;
import it.polimi.ingsw.view.gui.customImages.LabelImage;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class PlayerBoardPanel extends JLayeredPane {

    private final Gui gui;
    private final DepositPanel depositPanel;

    private int buyCardIdBuffer;

    public PlayerBoardPanel(Gui gui) {
        super();
        this.gui = gui;

        this.setLayout(null);
        this.setBounds(0, 0, 1920, 980);


        //LAYER 0 ------------------------------------------------------------------------------------------------------
        JPanel pl0 = new JPanel();
        pl0.setLayout(new BorderLayout());
        pl0.setOpaque(false);

        JPanel pl1 = new JPanel();
        pl1.setLayout(new BorderLayout());
        pl1.setBorder(new EmptyBorder(30,20,70,0));
        LabelImage boardImg = new LabelImage("/images/backgrounds/PlayerBoard.jpg");
        pl1.add(boardImg);
        pl1.setOpaque(false);

        JPanel handPanel = new HandPanel(gui);

        handPanel.setOpaque(false);


        pl0.setBorder(new EmptyBorder(0,0,0,10));
        pl0.add(handPanel, BorderLayout.EAST);
        pl0.add(pl1, BorderLayout.CENTER);
        pl0.setBounds(0, 0, 1920, 980);

        this.add(pl0, JLayeredPane.DEFAULT_LAYER);

        //LAYER 1 ------------------------------------------------------------------------------------------------------
        JPanel layer1 = new JPanel();
        layer1.setBounds(0,0,1920, 980);
        layer1.setOpaque(false);
        layer1.setLayout(null);

        developmentSlotButtons(layer1);

        //DEPOSIT ZONE
        depositPanel = new DepositPanel();
        depositPanel.setBounds(70,120,320,385);
        layer1.add(depositPanel);


        //VAULT
        VaultPanel vaultPanel = new VaultPanel(gui.getMyLiteVault());
        vaultPanel.setBounds(40,550,330, 270);
        layer1.add(vaultPanel);
        //--------------------------------------------------------------------------------------------------------------

        this.add(layer1, JLayeredPane.POPUP_LAYER);
    }

    private void developmentSlotButtons(JPanel layer1){

        for (int i=0; i<3; i++){
            JPanel clickableSlot = new JPanel();
            clickableSlot.setBackground(new Color(100,100,200,200));
            clickableSlot.setBounds(680 + 305*i + i*2,200,250,350);
            clickableSlot.addMouseListener(new ProdSlotClickListener(clickableSlot, i+1));
            layer1.add(clickableSlot);
        }


    }

    private class ProdSlotClickListener extends MouseAdapter{
        private JPanel prodSlot;
        private int id;

        public ProdSlotClickListener(JPanel prodSlot, int id) {
            this.prodSlot = prodSlot;
            this.id = id;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (gui.getGuiStatus() == GuiStatus.SELECTING_THE_SLOT){
                JPopupMenu popupmenu = new JPopupMenu("Production Slot");
                JMenuItem select = new JMenuItem("Select");
                popupmenu.add(select);
                popupmenu.show(prodSlot , e.getX(), e.getY());

                select.addActionListener(f -> {
                    gui.send( new BuyMessage(buyCardIdBuffer, id, gui.getNickname()));
                });

            }
        }
    }

    public DepositPanel getDepositPanel() {
        return depositPanel;
    }

    public void setBuyCardIdBuffer(int buyCardIdBuffer) {
        this.buyCardIdBuffer = buyCardIdBuffer;
    }
}
