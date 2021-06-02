package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.model.exceptions.ImageNotFound;
import it.polimi.ingsw.model.resources.ResourceContainer;
import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.gui.buttons.ButtonImage;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class AfterMarketPanel extends BackgroundImagePanel {

    //private  ArrayList<ResourceContainer> resourcesReceived;

    public AfterMarketPanel(ArrayList<ResourceContainer> resourcesReceived, Gui gui)throws ImageNotFound {
        super("/images/others/deposit.png", 650, 310, false);
        this.setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
        this.add(Box.createRigidArea(new Dimension(150,0)));


        JPanel received=new JPanel();
        received.setOpaque(false);
        received.setLayout(new BoxLayout(received,BoxLayout.Y_AXIS));


        received.add(Box.createVerticalGlue());
        for (ResourceContainer rc:resourcesReceived){
            ButtonImage bi=new ButtonImage("/images/resourceImages/"+rc.getResourceType().deColored().toLowerCase()+".png");
            received.add(bi);
            received.add(Box.createVerticalGlue());

        }

        this.add(received);
        this.add(Box.createRigidArea(new Dimension(500,0)));

        DepositPanel depositPanel=new DepositPanel(gui);
        depositPanel.setOpaque(false);
        this.add(depositPanel);
        this.add(Box.createRigidArea(new Dimension(50,0)));
    }
}
