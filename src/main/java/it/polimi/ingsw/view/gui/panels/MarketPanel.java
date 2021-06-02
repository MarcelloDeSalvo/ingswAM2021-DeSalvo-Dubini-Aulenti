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
        super("/images/backgrounds/marketBackground.png", 650, 5, false);
        this.setLayout(new BorderLayout());
        this.setBounds(0,0,850,900);
        this.setWidth(696);
        this.setHeight(900);

        this.gui=gui;
        this.liteMarket=liteMarket;
        this.marbles=null;
        this.vacant=null;

        //JPanel marketPanel=new JPanel();

        JPanel mainPanel=new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setOpaque(false);

        JPanel marketBox=new JPanel();
        marketBox.setLayout(new BoxLayout(marketBox,BoxLayout.X_AXIS));

        showMarket();

        marketBox.setOpaque(false);
        //marketAndVacant.add(vacant);
        //marketAndVacant.add(Box.createHorizontalGlue());
        marketBox.add(marbles);


        JPanel rowButtons=new JPanel();
        rowButtons.setLayout(new BoxLayout(rowButtons,BoxLayout.Y_AXIS));
        rowButtons.setOpaque(false);
        //rowButtons.add(Box.createVerticalGlue());

        JPanel columnButtons= new JPanel();
        columnButtons.setLayout(new BoxLayout(columnButtons,BoxLayout.X_AXIS));
        columnButtons.setOpaque(false);
        //columnButtons.add(Box.createHorizontalGlue());


        rowButtons.add(Box.createRigidArea(new Dimension(0,15)));
        for(int i=0;i<3;i++){
            ButtonImage buttonImage=new ButtonImage(" + ",false);
            final int j=i;
            buttonImage.addActionListener(e->{
                System.out.println("Riga numero "+(j+1));
            });
            rowButtons.add(buttonImage);
            rowButtons.add(Box.createVerticalGlue());
        }
        rowButtons.add(Box.createRigidArea(new Dimension(0,117)));


        columnButtons.add(Box.createRigidArea(new Dimension(770,0)));
        for(int i=0;i<4;i++){
            ButtonImage buttonImage=new ButtonImage(" + ",false);
            final int j=i;
            buttonImage.addActionListener(e->{
                System.out.println("Colonna numero "+(j+1));
            });
            columnButtons.add(buttonImage);
            columnButtons.add(Box.createHorizontalGlue());
        }
        columnButtons.add(Box.createRigidArea(new Dimension(235,0)));


        marketBox.setBorder(BorderFactory.createEmptyBorder(0,81,127,28) );
        //marketAndVacant.setBounds(290,676,360,700);

        columnButtons.setBorder(BorderFactory.createEmptyBorder(30,0,0,0));
        rowButtons.setBorder(BorderFactory.createEmptyBorder(0,130,0,40));
        vacant.setBorder(BorderFactory.createEmptyBorder(0,0,0,400));

        mainPanel.setBorder(BorderFactory.createEmptyBorder(200,100,270,497) );


        mainPanel.add(marketBox,BorderLayout.CENTER);


        mainPanel.add(rowButtons,BorderLayout.EAST);
        mainPanel.add(columnButtons,BorderLayout.SOUTH);
        mainPanel.add(vacant,BorderLayout.WEST);
        this.add(mainPanel,BorderLayout.CENTER);

    }

    public void showMarket(){
        ArrayList<ResourceContainer> marbleArray=liteMarket.getMarketArray();

        marbles=new JPanel();
        vacant=new JPanel();
        marbles.setLayout(new GridLayout(3,4));
        marbles.setOpaque(false);

        for (ResourceContainer marb :marbleArray) {
            ButtonImage buttonImage=new ButtonImage("/images/marblesScuffed/marble"+marb.getResourceType().deColored().toLowerCase()+".png",new Dimension(60,60));
            buttonImage.setBorderPainted(false);
            buttonImage.setOpaque(false);
            buttonImage.setContentAreaFilled(false);
            marbles.add(buttonImage);
        }

        vacant.setLayout(new BoxLayout(vacant, BoxLayout.Y_AXIS));
        JLabel vacantText1=new JLabel("This is the ");
        vacantText1.setVerticalAlignment(SwingConstants.CENTER);
        vacantText1.setFont(new Font("Helvetica", Font.PLAIN, 40));

        JLabel vacantText2=new JLabel("current vacant: ");
        vacantText2.setVerticalAlignment(SwingConstants.CENTER);
        vacantText2.setFont(new Font("Helvetica", Font.PLAIN, 40));

        vacant.add(vacantText1);
        vacant.add(vacantText2);

        ButtonImage vacantMarble=new ButtonImage("/images/marblesScuffed/marble"+liteMarket.getVacant().getResourceType().deColored().toLowerCase()+".png");
        vacantMarble.setBorderPainted(false);
        vacant.add(vacantMarble);
        vacant.setOpaque(false);
    }


}
