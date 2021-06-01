package it.polimi.ingsw.view.gui.panels;


import it.polimi.ingsw.model.resources.ResourceContainer;
import it.polimi.ingsw.view.gui.buttons.ButtonImage;
import it.polimi.ingsw.view.gui.buttons.DepositButton;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class DepositPanel extends JPanel {
    private HashMap<Integer, ArrayList<DepositButton>> depositButtons;
    private ArrayList<Point> selectedIds;

    private final int buttonSize = 110;

    public DepositPanel() {
        selectedIds= new ArrayList<>();
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.setOpaque(false);

        JPanel deposit = new JPanel();
        deposit.setLayout(new BoxLayout(deposit, BoxLayout.Y_AXIS));
        deposit.setOpaque(false);

        //ROW 1
        JPanel depositRow1 = new JPanel();
        depositRow1.setLayout(new BoxLayout(depositRow1, BoxLayout.X_AXIS));
        //depositRow1.setPreferredSize(new Dimension(buttonSize,buttonSize));
        depositRow1.setOpaque(false);

        DepositButton depositButton1_1 = new DepositButton(1,0);
        //depositButton1_1.setMinimumSize(new Dimension(buttonSize,buttonSize));
        depositButton1_1.setMinimumSize(new Dimension(buttonSize-1,buttonSize-1));
        depositButton1_1.setMaximumSize(new Dimension(buttonSize,buttonSize));
        depositButton1_1.setOpaque(false);


        depositRow1.add(depositButton1_1);
        //depositRow1.setAlignmentX(CENTER_ALIGNMENT);

        //ROW 2
        JPanel depositRow2 = new JPanel();
        depositRow2.setLayout(new BoxLayout(depositRow2, BoxLayout.X_AXIS));
        depositRow2.setOpaque(false);

        DepositButton depositButton2_1 = new DepositButton(2,0);
        depositButton2_1.setMinimumSize(new Dimension(buttonSize-1,buttonSize-1));
        depositButton2_1.setMaximumSize(new Dimension(buttonSize,buttonSize));
        depositButton2_1.setOpaque(false);

        DepositButton depositButton2_2 = new DepositButton(2,1);
        depositButton2_2.setMinimumSize(new Dimension(buttonSize-1,buttonSize-1));
        depositButton2_2.setMaximumSize(new Dimension(buttonSize,buttonSize));
        depositButton2_2.setOpaque(false);

        depositRow2.add(depositButton2_1);
        depositRow2.add(depositButton2_2);
        //depositRow2.setAlignmentX(Component.CENTER_ALIGNMENT);

        //ROW 3
        JPanel depositRow3 = new JPanel();
        depositRow3.setLayout(new BoxLayout(depositRow3, BoxLayout.X_AXIS));
        depositRow3.setOpaque(false);

        DepositButton depositButton3_1 = new DepositButton(3,0);
        depositButton3_1.setMinimumSize(new Dimension(buttonSize-1,buttonSize-1));
        depositButton3_1.setMaximumSize(new Dimension(buttonSize,buttonSize));
        depositButton3_1.setOpaque(false);

        DepositButton depositButton3_2 = new DepositButton(3,1);
        depositButton3_2.setMinimumSize(new Dimension(buttonSize-1,buttonSize-1));
        depositButton3_2.setMaximumSize(new Dimension(buttonSize,buttonSize));
        depositButton3_2.setOpaque(false);

        DepositButton depositButton3_3 = new DepositButton(3,2);
        depositButton3_3.setMinimumSize(new Dimension(buttonSize-1,buttonSize-1));
        depositButton3_3.setMaximumSize(new Dimension(buttonSize,buttonSize));
        depositButton3_3.setOpaque(false);

        depositRow3.add(depositButton3_1);
        depositRow3.add(depositButton3_2);
        depositRow3.add(depositButton3_3);
        //depositRow3.setAlignmentX(CENTER_ALIGNMENT);

        //ADDS THE ROWS
        deposit.add(depositRow1);
        deposit.add(Box.createRigidArea(new Dimension(2,2)));
        deposit.add(depositRow2);
        deposit.add(Box.createRigidArea(new Dimension(2,2)));
        deposit.add(depositRow3);

        depositButtons = new HashMap<>();
        ArrayList<DepositButton> row1 = new ArrayList<>();
        row1.add(depositButton1_1);
        ArrayList<DepositButton> row2 = new ArrayList<>();
        row2.add(depositButton2_1);
        row2.add(depositButton2_2);
        ArrayList<DepositButton> row3 = new ArrayList<>();
        row3.add(depositButton3_1);
        row3.add(depositButton3_2);
        row3.add(depositButton3_3);

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

    public void addImage(ResourceContainer resourceContainer, int id){
        System.out.println("FF");
        for (int i = 0; i<resourceContainer.getQty();i++){
            while (!depositButtons.get(id).get(i).isEmpty())
                i++;
            if (i>=depositButtons.get(id).size())
                break;
            depositButtons.get(id).get(i).setResourceTypeImage(resourceContainer.getResourceType());
        }


    }

    public void remove(ResourceContainer resourceContainer, int id){
        for (int i = 0; i<resourceContainer.getQty();i++)
            if(depositButtons.get(id).get(i).isEmpty())
                depositButtons.get(id).get(i).setResourceTypeImage(null);

    }


    public void copy( HashMap<Integer, ArrayList<DepositButton>> depositButtonsToClone){
        for (Integer i: depositButtons.keySet()) {
            int j=0;
            for (DepositButton depositButton : depositButtons.get(i)) {
                depositButtonsToClone.get(i).get(j).setResourceTypeImage(depositButton.getResourceType());
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
            for (DepositButton depositButton : depositButtons.get(i)) {
                if (depositButton.isSelected())
                    depositButton.setSelected(false);
            }
        }
    }

    public void fillSelectedBuffer() {
        for (Integer i: depositButtons.keySet()) {
            for (DepositButton depositButton : depositButtons.get(i)) {
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
            for (DepositButton depositButton : depositButtons.get(i)) {
                if (depositButton.isSelected())
                    iDs.add(depositButton.getId());
            }
        }

        return iDs;
    }

    public HashMap<Integer, ArrayList<DepositButton>> getDepositButtons() {
        return depositButtons;
    }
}
