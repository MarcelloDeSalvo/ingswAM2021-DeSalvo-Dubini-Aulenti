package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.liteModel.LiteCardGrid;
import it.polimi.ingsw.view.gui.GuiStatus;
import it.polimi.ingsw.view.gui.buttons.ButtonImage;
import it.polimi.ingsw.view.gui.Gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CardGridPanel extends JPanel {

    private final Gui gui;
    private final LiteCardGrid liteCardGrid;

    public CardGridPanel(Gui gui, LiteCardGrid liteCardGrid) {
        this.gui = gui;
        this.liteCardGrid = liteCardGrid;
        this.setBorder(BorderFactory.createEmptyBorder(2, 280, 2, 280));
        this.setLayout(new GridLayout(3,4));

        for (Integer id: liteCardGrid.getGUIcardIDs()) {
            ButtonImage lc = new ButtonImage("/images/cardFrontJpgs/DevelopmentFront_"+id+".jpg", new Dimension(173,262));
            lc.addActionListener(e -> buyMenu(lc, id));

            this.add(lc);
        }
    }

    public void buyMenu(ButtonImage button, int id){
        JPopupMenu popupmenu = new JPopupMenu("Development Card");
        JMenuItem buy = new JMenuItem("Buy");

        popupmenu.add(buy);

        button.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                popupmenu.show(button , e.getX(), e.getY());
            }
        });


        buy.addActionListener(e -> {
            gui.setGuiStatus(GuiStatus.SELECTING_CARD_SLOT);
            gui.getGamePanel().getPlayerBoardPanel().getProductionPanel().setBuyCardIdBuffer(id);
            gui.printReply("Ok! Now select a Production Slot");
            gui.getGamePanel().getCardLayout().show( gui.getGamePanel().getMain(), "playerBoardPanel");
        });

    }

    public void updateGrid(){
        this.removeAll();
        for (Integer id: liteCardGrid.getGUIcardIDs()) {
            if (id != -1){
                ButtonImage lc = new ButtonImage("/images/cardFrontJpgs/DevelopmentFront_"+id+".jpg", new Dimension(173,262));

                lc.addActionListener(e ->buyMenu(lc, id));
                this.add(lc);
            }
            else{
                JPanel jPanel = new JPanel();
                jPanel.setOpaque(false);
                this.add(jPanel);
            }
        }
        this.repaint();
    }
}
