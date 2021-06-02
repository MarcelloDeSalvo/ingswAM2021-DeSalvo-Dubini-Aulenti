package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.liteModel.LiteMarket;
import it.polimi.ingsw.model.exceptions.ImageNotFound;
import it.polimi.ingsw.model.resources.ResourceContainer;
import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.gui.buttons.ButtonImage;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MarketPanel extends BackgroundImagePanel{

    private JPanel marbles;
    private JPanel vacant;
    private LiteMarket liteMarket;
    private Gui gui;

    public MarketPanel(Gui gui, LiteMarket liteMarket) throws ImageNotFound{
        super("/images/backgrounds/marketBackground.png", 600, 5, false);
        this.setLayout(new BorderLayout());
        this.setBounds(0,0,900,950);
        this.setWidth(900);
        this.setHeight(950);

        this.gui=gui;
        this.liteMarket=liteMarket;
        this.marbles=null;
        this.vacant=null;

        //JPanel marketPanel=new JPanel();

        JPanel mainPanel=new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setOpaque(false);

        JPanel marketAndVacant=new JPanel();
        marketAndVacant.setLayout(new BoxLayout(marketAndVacant,BoxLayout.X_AXIS));

        showMarket();

        marketAndVacant.setOpaque(false);
        marketAndVacant.add(vacant);
        marketAndVacant.add(Box.createHorizontalGlue());
        marketAndVacant.add(marbles);


        marketAndVacant.setBorder(BorderFactory.createEmptyBorder(250,676,450,750) );
        marketAndVacant.setBounds(250,676,400,700);
        mainPanel.add(marketAndVacant,BorderLayout.CENTER);

        this.add(mainPanel,BorderLayout.CENTER);

    }

    public void showMarket(){
        ArrayList<ResourceContainer> marbleArray=liteMarket.getMarketArray();

        marbles=new JPanel();
        vacant=new JPanel();
        marbles.setLayout(new GridLayout(3,4));
        marbles.setOpaque(false);

        for (ResourceContainer marb :marbleArray) {
            ButtonImage buttonImage=new ButtonImage("/images/marblesScuffed/marble"+marb.getResourceType().deColored().toLowerCase()+".png");
            buttonImage.setOpaque(false);
            buttonImage.setContentAreaFilled(false);
            marbles.add(buttonImage);
        }

        vacant.add(new ButtonImage("/images/marblesScuffed/marble"+liteMarket.getVacant().getResourceType().deColored().toLowerCase()+".png"));
        vacant.setOpaque(false);
    }


}
