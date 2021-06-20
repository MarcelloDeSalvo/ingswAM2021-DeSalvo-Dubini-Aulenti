package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.model.resources.ResourceContainer;
import it.polimi.ingsw.network.commands.Command;
import it.polimi.ingsw.network.commands.Message;
import it.polimi.ingsw.network.commands.SendContainer;
import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.gui.GuiStatus;
import it.polimi.ingsw.view.gui.customJObject.DepositMenu;
import it.polimi.ingsw.view.gui.customJObject.ResourceTypeLabel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;

public class DepositPanel extends JPanel {
    private final HashMap<Integer, ArrayList<ResourceTypeLabel>> depositButtons;

    private final int labelSize = 110;
    private final Gui gui;

    /**
     * creates the structure for the deposits
     * @param printOnly doesn't add the listeners if true
     */
    public DepositPanel(Gui gui, boolean printOnly) {
        this.gui = gui;
        depositButtons = new HashMap<>();

        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.setOpaque(false);

        JPanel deposit = new JPanel();
        deposit.setLayout(new BoxLayout(deposit, BoxLayout.Y_AXIS));
        deposit.setOpaque(false);

        //ROW 1
        ArrayList<ResourceTypeLabel> row1 = new ArrayList<>();

        JPanel depositRow1 = new JPanel();
        depositRow1.setLayout(new BoxLayout(depositRow1, BoxLayout.X_AXIS));
        depositRow1.setOpaque(false);

        ResourceTypeLabel resourceTypeLabel1_1 = new ResourceTypeLabel(1,0, labelSize);

        if(!printOnly)
            resourceTypeLabel1_1.addMouseListener(new DepositListener(resourceTypeLabel1_1));

        row1.add(resourceTypeLabel1_1);
        depositRow1.add(resourceTypeLabel1_1);

        //ROW 2
        ArrayList<ResourceTypeLabel> row2 = new ArrayList<>();

        JPanel depositRow2 = new JPanel();
        depositRow2.setLayout(new BoxLayout(depositRow2, BoxLayout.X_AXIS));
        depositRow2.setOpaque(false);

        for(int i = 0; i<2; i++){
            ResourceTypeLabel resourceTypeLabel2_x = new ResourceTypeLabel(2,i, labelSize);
            if(!printOnly)
                resourceTypeLabel2_x.addMouseListener(new DepositListener(resourceTypeLabel2_x));
            row2.add(resourceTypeLabel2_x);
            depositRow2.add(resourceTypeLabel2_x);
        }


        //ROW 3
        ArrayList<ResourceTypeLabel> row3 = new ArrayList<>();
        JPanel depositRow3 = new JPanel();
        depositRow3.setLayout(new BoxLayout(depositRow3, BoxLayout.X_AXIS));
        depositRow3.setOpaque(false);

        for(int i = 0; i<3; i++){
            ResourceTypeLabel resourceTypeLabel3_x = new ResourceTypeLabel(3,i, labelSize);
            if(!printOnly)
                resourceTypeLabel3_x.addMouseListener(new DepositListener(resourceTypeLabel3_x));
            depositRow3.add(resourceTypeLabel3_x);
            row3.add(resourceTypeLabel3_x);
        }

        //ADDS THE ROWS
        deposit.add(depositRow1);
        deposit.add(Box.createRigidArea(new Dimension(2,2)));
        deposit.add(depositRow2);
        deposit.add(Box.createRigidArea(new Dimension(2,2)));
        deposit.add(depositRow3);

        depositButtons.put(1, row1);
        depositButtons.put(2, row2);
        depositButtons.put(3, row3);

        //EXTRA DEPOSIT SLOTS
        this.add(deposit);

    }

    private class DepositListener extends MouseAdapter{
        private final ResourceTypeLabel resourceTypeLabel;

        public DepositListener(ResourceTypeLabel resourceTypeLabel) {
            this.resourceTypeLabel = resourceTypeLabel;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            resourceMenu(gui, resourceTypeLabel);
        }
    }

    /**
     * Lists the operations that you can apply to a selected deposit
     */
    private void resourceMenu(Gui gui, ResourceTypeLabel resourceTypeLabel){
        JPopupMenu popupmenu = new JPopupMenu("Deposit Slot");

        JMenuItem give = new JMenuItem("Pay with this");
        DepositMenu submenuTransfer = new DepositMenu(gui, "Transfer to", resourceTypeLabel.getResourceType(),Command.MANAGE_DEPOSIT, resourceTypeLabel.getId());
        DepositMenu submenuSwitch = new DepositMenu(gui, "Switch with", resourceTypeLabel.getResourceType(),Command.SWITCH_DEPOSIT, resourceTypeLabel.getId());
        JMenuItem done = new JMenuItem("Done");

        resourceTypeLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if(gui.getGuiStatus()== GuiStatus.SELECTING_PAY_RESOURCES) {
                    popupmenu.add(give);
                    popupmenu.add(done);
                }

                popupmenu.show(resourceTypeLabel , e.getX(), e.getY());
            }
        });

        give.addActionListener(e -> {
            if (gui.getGuiStatus() == GuiStatus.SELECTING_PAY_RESOURCES){
                gui.send(new SendContainer(Command.SEND_CONTAINER, resourceTypeLabel.getResourceContainer(),
                        "DEPOSIT", resourceTypeLabel.getId(), gui.getNickname()));
            }
            resourceTypeLabel.setSelected(true);
        });

        done.addActionListener(e -> {
            if (gui.getGuiStatus() == GuiStatus.SELECTING_DEST_AFTER_MARKET || gui.getGuiStatus() == GuiStatus.SELECTING_PAY_RESOURCES){
                gui.send(new Message.MessageBuilder().setCommand(Command.DONE).setNickname(gui.getNickname()).build());
            }
        });

        popupmenu.add(submenuTransfer);
        popupmenu.add(submenuSwitch);
    }

    /**
     * Deselects all the button (removes the selection border)
     */
    public void deselectAll(){
        for (Integer i: depositButtons.keySet()) {
            for (ResourceTypeLabel resourceTypeLabel1 : depositButtons.get(i)) {
                resourceTypeLabel1.setSelected(false);
            }
        }
    }

    /**
     * Adds an extra deposit
     */
    public void addExtraSlot(Point position, JLayeredPane playerPanel, boolean printOnly){
        ArrayList<ResourceTypeLabel> leaderRow = new ArrayList<>();

        JPanel leaderStorage = new JPanel();
        leaderStorage.setLayout(new BoxLayout(leaderStorage, BoxLayout.X_AXIS));
        leaderStorage.setBounds(position.x+1670, position.y+250,210,110);
        leaderStorage.setOpaque(false);

        for(int i = 0; i<2; i++){
            ResourceTypeLabel resourceTypeLeader = new ResourceTypeLabel(depositButtons.size()+1, i, labelSize);
            if(!printOnly)
                resourceTypeLeader.addMouseListener(new DepositListener(resourceTypeLeader));
            leaderRow.add(resourceTypeLeader);
            leaderStorage.add(resourceTypeLeader);
        }

        depositButtons.put(depositButtons.size()+1, leaderRow);
        playerPanel.add(leaderStorage, JLayeredPane.POPUP_LAYER);

        this.revalidate();
        this.repaint();
    }

    /**
     * Sets the resource type image for a deposit
     */
    public void setImage(ResourceContainer resourceContainer, int id){
        int i;
        for (i = 0; i<resourceContainer.getQty(); i++) {
            depositButtons.get(id).get(i).setResourceTypeImage(resourceContainer.getResourceType());
        }

        for (int j=i; j<depositButtons.get(id).size(); j++){
            depositButtons.get(id).get(j).setResourceTypeImage(null);
        }
    }

    /**
     * Copies the images of the deposit buttons from this DepositPanel to another
     */
    public void copy( HashMap<Integer, ArrayList<ResourceTypeLabel>> depositButtonsToClone){
        for (Integer i: depositButtons.keySet()) {
            int j=0;
            for (ResourceTypeLabel resourceTypeLabel : depositButtons.get(i)) {
                depositButtonsToClone.get(i).get(j).setResourceTypeImage(resourceTypeLabel.getResourceType());
                j++;
            }
        }
    }


    public HashMap<Integer, ArrayList<ResourceTypeLabel>> getDepositButtons() {
        return depositButtons;
    }




}
