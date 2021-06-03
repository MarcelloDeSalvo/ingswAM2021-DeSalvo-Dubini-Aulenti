package it.polimi.ingsw.view.gui.panels;

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
        int itemSize = gui.getMyLiteDeposit().getDeposits().size();
        JPopupMenu popupmenu = new JPopupMenu("Resource Action");
        JMenuDeposit item = null;

        for (int i = 0; i<itemSize; i++){
            if (!gui.getMyLiteDeposit().isLeaderType(i)){
                item = new JMenuDeposit("Deposit " +(i+1), i+1);
            }else {
                item = new JMenuDeposit("Leader Storage " + gui.getMyLiteDeposit().getType(i).deColored(), i+1);
            }

            popupmenu.add(item);
        }

        button.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                popupmenu.show(button , e.getX(), e.getY());
            }
        });
    }

    private class JMenuDeposit extends JMenuItem{
        private final int id;

        public JMenuDeposit(String text, int id) {
            super(text);
            this.id = id;
            this.addActionListener( e->{
                if(selectedResource == ResourceType.BLANK)
                    return;
                gui.send(new SendContainer(Command.SEND_DEPOSIT_ID, new ResourceContainer(selectedResource,1), "DEPOSIT", id, gui.getNickname()));
            });
        }

        public int getId() {
            return id;
        }
    }
}
