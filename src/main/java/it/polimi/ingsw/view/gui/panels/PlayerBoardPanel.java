package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.network.commands.BuyMessage;
import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.gui.GuiStatus;
import it.polimi.ingsw.view.gui.buttons.ButtonImage;
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

    private ArrayList<DevProdSlot> prodSlot;

    public PlayerBoardPanel(Gui gui) {
        super();
        this.gui = gui;

        prodSlot = new ArrayList<>();
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
        depositPanel = new DepositPanel(gui);
        depositPanel.setBounds(70,120,320,385);
        layer1.add(depositPanel);


        //VAULT
        VaultPanel vaultPanel = new VaultPanel(gui);
        vaultPanel.setBounds(40,550,330, 270);
        layer1.add(vaultPanel);
        //--------------------------------------------------------------------------------------------------------------

        this.add(layer1, JLayeredPane.PALETTE_LAYER);
    }

    private void developmentSlotButtons(JPanel layer1){
        for (int i=0; i<3; i++){
            DevProdSlot clickableSlot = new DevProdSlot();
            clickableSlot.setBackground(new Color(100,100,200,200));
            clickableSlot.setBounds(680 + 305*i + i*2,200,250,350);
            clickableSlot.addMouseListener(new ProdSlotClickListener(clickableSlot, i+1));
            layer1.add(clickableSlot);
            prodSlot.add(clickableSlot);
        }
    }

    private class ProdSlotClickListener extends MouseAdapter{
        private final JPanel prodSlot;
        private final int id;

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

    public void printBoughtCard(int slotId, int cardId){
        ButtonImage devCard = new ButtonImage("/images/cardFrontJpgs/DevelopmentFront_"+cardId+".jpg", new Dimension(173,262));
        getProdSlot().get(slotId - 1).addCard(devCard);
        this.repaint();
    }

    public ArrayList<DevProdSlot> getProdSlot() {
        return prodSlot;
    }

    public DepositPanel getDepositPanel() {
        return depositPanel;
    }

    public void setBuyCardIdBuffer(int buyCardIdBuffer) {
        this.buyCardIdBuffer = buyCardIdBuffer;
    }

    private class DevProdSlot extends JPanel{
        private int x_offset;
        private int y_offset;
        private int layer_offset;

        public DevProdSlot() {
            super();
            this.setLayout(null);
            this.x_offset = 0;
            this.y_offset = 0;
            this.layer_offset = 500;
        }

        public void addCard(ButtonImage devCard){
            devCard.setBounds(this.getX()+x_offset ,this.getY()+y_offset, 173,262);
            x_offset += 50;
            y_offset += 50;

            PlayerBoardPanel.this.setLayer(devCard, layer_offset);
            PlayerBoardPanel.this.add(devCard,  layer_offset);
            layer_offset++;
            this.repaint();
        }

    }
}

