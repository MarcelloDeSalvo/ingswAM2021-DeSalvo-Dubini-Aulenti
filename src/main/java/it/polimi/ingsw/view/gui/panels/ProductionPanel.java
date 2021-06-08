package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.network.commands.BuyMessage;
import it.polimi.ingsw.network.commands.ProduceMessage;
import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.gui.GuiStatus;
import it.polimi.ingsw.view.gui.buttons.ButtonImage;
import it.polimi.ingsw.view.gui.customJObject.ResourceTypeLabel;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class ProductionPanel extends JPanel {

    private final ArrayList<DevProdSlot> prodSlot;
    private final ArrayList<Integer> selectedProdSlotBuffer;
    private final Gui gui;
    private int buyCardIdBuffer;
    private ButtonImage done;

    public ProductionPanel(Gui gui) {
        this.gui = gui;
        prodSlot = new ArrayList<>();
        selectedProdSlotBuffer = new ArrayList<>();

        this.setOpaque(false);
        this.setLayout(null);

        developmentSlotButtons();
    }

    public ProductionPanel(Gui gui,String nickname){
        this.gui = gui;
        prodSlot = new ArrayList<>();
        selectedProdSlotBuffer = new ArrayList<>();

        this.setOpaque(false);
        this.setLayout(null);

        DevProdSlot basicProdSlot = new DevProdSlot();
        basicProdSlot.setBounds(380 ,480,250,250);
        this.add(basicProdSlot);
        prodSlot.add(basicProdSlot);

        for (int i=0; i<3; i++){
            DevProdSlot clickableSlot = new DevProdSlot();
            clickableSlot.setBounds(680 + 305*i + i*2,200,250,350);
            this.add(clickableSlot);
            prodSlot.add(clickableSlot);
        }
        //you still have to print the actual cards, best way of doing this is using the litemodelUpdate


    }

    public void addExtraSlot(Point position){

        DevProdSlot clickableSlot = new DevProdSlot();
        clickableSlot.setBounds(position.x+1650, position.y,243, 367);
        clickableSlot.addMouseListener(new ProdSlotClickListener(clickableSlot, prodSlot.size()));
        this.add(clickableSlot);
        prodSlot.add(clickableSlot);

        this.revalidate();
        this.repaint();
    }

    private class DevProdSlot extends JPanel{
        private int x_offset;
        private int y_offset;
        private int layer_offset;
        private final LineBorder defaultBorder = new LineBorder(Color.magenta);
        private boolean selected = false;

        public DevProdSlot() {
            super();
            this.setLayout(null);
            this.setOpaque(false);
            this.setBorder(defaultBorder);
            this.x_offset = 0;
            this.y_offset = 0;
            this.layer_offset = 96;
        }

        public void addCard(ButtonImage devCard, JLayeredPane board){
            devCard.setBounds(this.getX()+x_offset ,this.getY()+y_offset, 173,262);
            x_offset += 50;
            y_offset += 50;

            board.setLayer(devCard, layer_offset);
            board.add(devCard,  layer_offset);
            layer_offset++;
            this.repaint();
        }

        public void resetBorder(){
            this.setBorder(defaultBorder);
            selected = false;
        }

        public void setBorder(){
            this.setBorder(new LineBorder(Color.CYAN, 3));
            selected = true;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }
    }

    private void developmentSlotButtons(){
        done = new ButtonImage("DONE",22, true);
        done.setBounds(680 ,580,850,100);
        done.setPreferredSize(new Dimension(850,100));
        done.setVisible(false);
        done.addActionListener(e ->{
            gui.send(new ProduceMessage(selectedProdSlotBuffer, gui.getNickname()));
            selectedProdSlotBuffer.clear();
            done.setVisible(false);
            for (DevProdSlot slot: prodSlot) {
                    slot.resetBorder();
            }
        });
        this.add(done);

        DevProdSlot basicProdSlot = new DevProdSlot();
        basicProdSlot.setBounds(380 ,480,250,250);
        basicProdSlot.addMouseListener(new ProdSlotClickListener(basicProdSlot, 0));
        this.add(basicProdSlot);
        prodSlot.add(basicProdSlot);

        for (int i=0; i<3; i++){
            DevProdSlot clickableSlot = new DevProdSlot();
            clickableSlot.setBounds(680 + 305*i + i*2,200,250,350);
            clickableSlot.addMouseListener(new ProdSlotClickListener(clickableSlot, i+1));
            this.add(clickableSlot);
            prodSlot.add(clickableSlot);
        }

    }
    private class ProdSlotClickListener extends MouseAdapter {
        private final DevProdSlot prodSlot;
        private final int id;

        public ProdSlotClickListener(DevProdSlot prodSlot, int id) {
            this.prodSlot = prodSlot;
            this.id = id;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            JPopupMenu popupmenu = new JPopupMenu("Production Slot");
            JMenuItem select = new JMenuItem("Select");
            JMenuItem produce = new JMenuItem("Activate production");

            if (gui.getGuiStatus() == GuiStatus.SELECTING_CARD_SLOT){
                popupmenu.add(select);
                popupmenu.show(prodSlot , e.getX(), e.getY());

                select.addActionListener(f ->
                        gui.send( new BuyMessage(buyCardIdBuffer, id, gui.getNickname())));
            }

            if( gui.getGuiStatus()==GuiStatus.IDLE || gui.getGuiStatus() == GuiStatus.SELECTING_PROD_SLOT ){
                popupmenu.add(produce);

                popupmenu.show(prodSlot , e.getX(), e.getY());

                produce.addActionListener(g -> {
                    if (!done.isVisible())
                        done.setVisible(true);
                    gui.setGuiStatus(GuiStatus.SELECTING_PROD_SLOT);

                    if (!prodSlot.isSelected()){
                        prodSlot.setBorder();
                        selectedProdSlotBuffer.add(id);
                    }else{
                        prodSlot.resetBorder();
                        selectedProdSlotBuffer.remove((Integer)id);
                    }

                });

            }
        }
    }

    public void printBoughtCard(int slotId, int cardId, JLayeredPane board){
        ButtonImage devCard = new ButtonImage("/images/cardFrontJpgs/DevelopmentFront_"+cardId+".jpg", new Dimension(173,262));
        getProdSlot().get(slotId).addCard(devCard, board);
        this.repaint();
    }

    public ArrayList<DevProdSlot> getProdSlot() {
        return prodSlot;
    }

    public int getBuyCardIdBuffer() {
        return buyCardIdBuffer;
    }

    public void setBuyCardIdBuffer(int buyCardIdBuffer) {
        this.buyCardIdBuffer = buyCardIdBuffer;
    }




}
