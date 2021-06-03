package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.liteModel.LiteDeposit;
import it.polimi.ingsw.liteModel.LiteMarket;
import it.polimi.ingsw.model.exceptions.ImageNotFound;
import it.polimi.ingsw.model.resources.ResourceContainer;
import it.polimi.ingsw.model.resources.ResourceType;
import it.polimi.ingsw.network.commands.Command;
import it.polimi.ingsw.network.commands.SendContainer;
import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.gui.buttons.ButtonImage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.jar.Attributes;

public class AfterMarketPanel extends JPanel {

    private ResourceType selectedResource;
    private JPanel received;
    private final Gui gui;

    //private  ArrayList<ResourceContainer> resourcesReceived;

    public AfterMarketPanel(Gui gui)throws ImageNotFound {
        super();
        this.setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
        this.setOpaque(false);
        this.add(Box.createRigidArea(new Dimension(150,0)));
        this.gui=gui;

        received=new JPanel();
        received.setOpaque(true);
        received.setLayout(new BoxLayout(received,BoxLayout.Y_AXIS));
        //received.setBounds(0,0,500,1200);

        this.add(received);
        this.add(Box.createRigidArea(new Dimension(500,0)));

        //DepositPanel depositPanel=new DepositPanel(gui);
        //depositPanel.setOpaque(false);
        //this.add(depositPanel);
        this.add(Box.createRigidArea(new Dimension(50,0)));
    }

    public void setResources(ArrayList<ResourceContainer> marketOut){
        received.removeAll();
        received.add(Box.createVerticalGlue());
        for (ResourceContainer rc:marketOut){
            ButtonImage bi=new ButtonImage("/images/resourceImages/"+rc.getResourceType().deColored().toLowerCase()+".png");
            bi.setBorderPainted(false);
            bi.addActionListener(e ->{
                selectedResource=rc.getResourceType();
                resourceActionWindow(bi);
            });

            received.add(bi);
            received.add(Box.createVerticalGlue());
        }
        received.revalidate();
        received.repaint();

    }


    private void resourceActionWindow (ButtonImage button){
        JPopupMenu popupmenu = new JPopupMenu("Resource Action");
        JMenuItem deposit1 = new JMenuItem("Deposit 1");
        JMenuItem deposit2 = new JMenuItem("Deposit 2");
        JMenuItem deposit3 = new JMenuItem("Deposit 3");

        popupmenu.add(deposit1);
        popupmenu.add(deposit2);
        popupmenu.add(deposit3);

        button.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                popupmenu.show(button , e.getX(), e.getY());
            }
        });

        ResourceContainer container = new ResourceContainer(selectedResource,1);

        deposit1.addActionListener(e -> {
            if(selectedResource == ResourceType.BLANK)
                return;
            gui.send(new SendContainer(Command.SEND_DEPOSIT_ID, container, "DEPOSIT", 1, gui.getNickname()));
        });

        deposit2.addActionListener(e -> {
            if(selectedResource == ResourceType.BLANK)
                return;
            gui.send(new SendContainer(Command.SEND_DEPOSIT_ID, container, "DEPOSIT", 2, gui.getNickname()));
        });

        deposit3.addActionListener(e -> {
            if(selectedResource == ResourceType.BLANK)
                return;
            gui.send(new SendContainer(Command.SEND_DEPOSIT_ID, container, "DEPOSIT", 3, gui.getNickname()));

        });


    }
}
