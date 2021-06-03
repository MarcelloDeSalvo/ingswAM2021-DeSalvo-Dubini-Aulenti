package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.model.resources.ResourceContainer;
import it.polimi.ingsw.network.commands.Command;
import it.polimi.ingsw.network.commands.Message;
import it.polimi.ingsw.network.commands.SendContainer;
import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.gui.GuiStatus;
import it.polimi.ingsw.view.gui.customJObject.ResourceTypeLabel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;

public class DepositPanel extends JPanel {
    private final HashMap<Integer, ArrayList<ResourceTypeLabel>> depositButtons;
    private final ArrayList<Point> selectedIds;

    private final int labelSize = 110;
    private final Gui gui;

    public DepositPanel(Gui gui) {
        this.gui = gui;
        selectedIds = new ArrayList<>();
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

        //UTILITY BUTTONS
        JPanel utilityButtons = new JPanel();
        utilityButtons.setLayout(new BoxLayout(utilityButtons, BoxLayout.Y_AXIS));
        utilityButtons.setOpaque(false);

        utilityButtons.add(new JButton("SWITCH"));
        utilityButtons.add(new JButton("SEND"));
        utilityButtons.add(new JButton("ADD"));

        this.add(deposit);
        //this.add(utilityButtons);

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

    private void resourceMenu(Gui gui, ResourceTypeLabel resourceTypeLabel){
        JPopupMenu popupmenu = new JPopupMenu("Deposit Slot");
        JMenuItem give = new JMenuItem("Pay with this");
        //JMenuItem send_here = new JMenuItem("Send Here");
        JMenuItem done = new JMenuItem("Done");

        popupmenu.add(give);
        popupmenu.add(done);

        //if (gui.getGuiStatus() == GuiStatus.SELECTING_DESTINATION_AFTER_MARKET)
        //    popupmenu.add(send_here);

        resourceTypeLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if(gui.getGuiStatus()== GuiStatus.SELECTING_PAY_RESOURCES)
                    popupmenu.show(resourceTypeLabel , e.getX(), e.getY());
            }
        });

        give.addActionListener(e -> {
            if (gui.getGuiStatus() == GuiStatus.SELECTING_PAY_RESOURCES){
                gui.send(new SendContainer(Command.SEND_CONTAINER, resourceTypeLabel.getResourceContainer(),
                        "DEPOSIT", resourceTypeLabel.getId(), gui.getNickname()));

                gui.printReply("Ok! Keep selecting or click DONE");

                //resourceTypeLabel.setBorder(BorderFactory.createLineBorder(Color.CYAN, 5));
            }
        });

        done.addActionListener(e -> {
            if (gui.getGuiStatus() == GuiStatus.SELECTING_DEST_AFTER_MARKET || gui.getGuiStatus() == GuiStatus.SELECTING_PAY_RESOURCES){
                gui.send(new Message.MessageBuilder().setCommand(Command.DONE).setNickname(gui.getNickname()).build());
            }
        });
    }

    public void addImage(ResourceContainer resourceContainer, int id){
        for (int i = 0; i<resourceContainer.getQty();i++) {
            while (!depositButtons.get(id).get(i).isEmpty())
                i++;
            if (i >= depositButtons.get(id).size())
                break;
            depositButtons.get(id).get(i).setResourceTypeImage(resourceContainer.getResourceType());
        }
    }

    public void remove(ResourceContainer resourceContainer, int id){
        for (int i = 0; i<resourceContainer.getQty();i++){
            if(!depositButtons.get(id).get(i).isEmpty())
                depositButtons.get(id).get(i).setResourceTypeImage(null);
        }
    }


    public void copy( HashMap<Integer, ArrayList<ResourceTypeLabel>> depositButtonsToClone){
        for (Integer i: depositButtons.keySet()) {
            int j=0;
            for (ResourceTypeLabel resourceTypeLabel : depositButtons.get(i)) {
                depositButtonsToClone.get(i).get(j).setResourceTypeImage(resourceTypeLabel.getResourceType());
                j++;
            }
        }
    }
 /*
    public void fill(ResourceContainer resourceContainer, int id){
        for (Point point: selectedIds) {
            if (point.x == id)
                for (int i = 0; i<resourceContainer.getQty();i++)
                    depositButtons.get(id).get(i).setResourceTypeImage(resourceContainer.getResourceType());
        }
        clearBuffer();
    }

    public void remove(ResourceContainer resourceContainer, int id){
        for (Point point: selectedIds) {
            if (point.x == id)
                for (int i = 0; i<resourceContainer.getQty();i++)
                    depositButtons.get(id).get(i).setResourceTypeImage(null);
        }
        clearBuffer();
    }

    public void clearSelected(){
        for (Integer i: depositButtons.keySet()) {
            for (ResourceTypeLabel depositButton : depositButtons.get(i)) {
                if (depositButton.isSelected())
                    depositButton.setSelected(false);
            }
        }
    }

    public void fillSelectedBuffer() {
        for (Integer i: depositButtons.keySet()) {
            for (ResourceTypeLabel depositButton : depositButtons.get(i)) {
                if (depositButton.isSelected())
                    selectedIds.add(new Point(depositButton.getId(), depositButton.getPos()));
            }
        }
    }

    public void clearBuffer(){
        selectedIds.clear();
    }
     */

    public ArrayList<Integer> getSelectedIds() {
        ArrayList<Integer> iDs =  new ArrayList<>();

        for (Integer i: depositButtons.keySet()) {
            for (ResourceTypeLabel resourceTypeLabel : depositButtons.get(i)) {
                if (resourceTypeLabel.isSelected())
                    iDs.add(resourceTypeLabel.getId());
            }
        }

        return iDs;
    }

    public HashMap<Integer, ArrayList<ResourceTypeLabel>> getDepositButtons() {
        return depositButtons;
    }
}
