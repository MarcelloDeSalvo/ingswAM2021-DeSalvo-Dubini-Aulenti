package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.liteModel.LiteCardGrid;
import it.polimi.ingsw.view.gui.buttons.ButtonImage;
import it.polimi.ingsw.view.gui.Gui;

import javax.swing.*;
import java.awt.*;

public class CardGridPanel extends JPanel {

    private Gui gui;
    private LiteCardGrid liteCardGrid;

    public CardGridPanel(Gui gui, LiteCardGrid liteCardGrid) {
        this.gui = gui;
        this.liteCardGrid = liteCardGrid;
        this.setBorder(BorderFactory.createEmptyBorder(2, 280, 2, 280));
        this.setLayout(new GridLayout(3,4));

        //Entrano in un ordine sbagliato :C
        for (Integer id: liteCardGrid.getCardIDs()) {
            ButtonImage lc= new  ButtonImage("/images/cardFrontJpgs/DevelopmentFront_"+id+".jpg", new Dimension(173,262));

            this.add(lc);
            lc.addActionListener(e->{
                gui.printReply("ciao sono una carta sviluppo "+"(ID: "+id+")");
                //MI SA CHE SERVE UN BUFFER PER COMPRARE
            });
        }
    }

    public void updateGrid(){
        //ELIMINO IL BOTTONE VECCHIO E NE METTO UNO NUOVO?
        //GLI CAMBIO SOLO L'IMMAGINE? ---> Ho fatto un metodo apposta in Button image nel caso, ma dovremmo cambiargli anche l'id che prende quando si preme, bho
        //Come si prende la colonna e riga precisa? --> https://stackoverflow.com/questions/2510159/can-i-add-a-component-to-a-specific-grid-cell-when-a-gridlayout-is-used/38800227
    }
}
