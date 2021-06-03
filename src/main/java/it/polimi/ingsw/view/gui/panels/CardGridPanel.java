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

        //Entrano in un ordine sbagliato :C
        for (Integer id: liteCardGrid.getCardIDs()) {
            ButtonImage lc = new ButtonImage("/images/cardFrontJpgs/DevelopmentFront_"+id+".jpg", new Dimension(173,262));
            lc.addActionListener(e -> {
                buyMenu(lc, id);
                gui.printReply("ciao sono una carta sviluppo "+"(ID: "+id+")");
            });

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
        for (Integer id: liteCardGrid.getCardIDs()) {
            if (id != -1){
                ButtonImage lc = new ButtonImage("/images/cardFrontJpgs/DevelopmentFront_"+id+".jpg", new Dimension(173,262));

                lc.addActionListener(e -> {
                    buyMenu(lc, id);
                    gui.printReply("ciao sono una carta sviluppo "+"(ID: "+id+")");
                });
                this.add(lc);
            }
            else{
                JPanel jPanel = new JPanel();
                jPanel.setOpaque(false);
                this.add(jPanel);
            }
        }
        this.repaint();
        //ELIMINO IL BOTTONE VECCHIO E NE METTO UNO NUOVO?
        //GLI CAMBIO SOLO L'IMMAGINE? ---> Ho fatto un metodo apposta in Button image nel caso, ma dovremmo cambiargli anche l'id che prende quando si preme, bho
        //Come si prende la colonna e riga precisa? --> https://stackoverflow.com/questions/2510159/can-i-add-a-component-to-a-specific-grid-cell-when-a-gridlayout-is-used/38800227
    }
}
