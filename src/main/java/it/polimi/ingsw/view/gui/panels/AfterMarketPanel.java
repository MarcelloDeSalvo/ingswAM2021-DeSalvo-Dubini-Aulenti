package it.polimi.ingsw.view.gui.panels;


import it.polimi.ingsw.model.exceptions.ImageNotFound;
import it.polimi.ingsw.model.resources.ResourceContainer;
import it.polimi.ingsw.model.resources.ResourceType;
import it.polimi.ingsw.network.commands.Command;
import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.gui.buttons.ButtonImage;
import it.polimi.ingsw.view.gui.customJObject.DepositMenu;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;


public class AfterMarketPanel extends JPanel {

    private ResourceType selectedResource;
    private final JPanel received;
    private final Gui gui;

    /**
     * Creates the panel that lets you sort out the resources you received from the market
     */
    public AfterMarketPanel(Gui gui)throws ImageNotFound {
        super();
        this.setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
        this.setOpaque(false);
        this.add(Box.createRigidArea(new Dimension(150,0)));
        this.gui=gui;

        received=new JPanel();
        received.setOpaque(true);
        received.setBackground(new Color(240, 198, 125));
        received.setBorder(new LineBorder(new Color(241, 153, 0), 1));
        received.setLayout(new BoxLayout(received,BoxLayout.Y_AXIS));

        this.add(received);
        this.add(Box.createRigidArea(new Dimension(500,0)));

        this.add(Box.createRigidArea(new Dimension(50,0)));
    }

    /**
     * Removes the current resources and lays out the ones contained in marketOut
     */
    public void setResources(List<ResourceContainer> marketOut){
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

    /**
     * Sets up the actionListeners for the input ButtonImage
     */
    private void resourceActionWindow (ButtonImage button){
        JPopupMenu depositList = new JPopupMenu("Deposits");
        DepositMenu subMenu = new DepositMenu(gui, "Send to", selectedResource, Command.SEND_DEPOSIT_ID, 0);
        depositList.add(subMenu);
        button.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                depositList.show(button , e.getX(), e.getY());
            }
        });
    }


}
