package it.polimi.ingsw.view.gui.panels;


import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class DepositPanel extends JPanel {
    private HashMap<Integer, ArrayList<DepositButton>> depositButtons;
    private ArrayList<Integer> selectedIds;

    public DepositPanel() {
        super();
        selectedIds= new ArrayList<>();
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.setOpaque(false);

        JPanel deposit = new JPanel();
        JPanel utilityButtons = new JPanel();

        deposit.setLayout(new BoxLayout(deposit, BoxLayout.Y_AXIS));
        deposit.setOpaque(false);

        //ROW 1
        JPanel depositRow1 = new JPanel();
        depositRow1.setLayout(new GridLayout(1,1));
        depositRow1.setOpaque(false);

        DepositButton depositButton1_1 = new DepositButton();
        depositButton1_1.setContentAreaFilled(false);
        depositButton1_1.setOpaque(false);
        depositButton1_1.addActionListener(e->{
            if(!depositButton1_1.isSelected())
                selectedIds.add(1);
            else if(selectedIds.contains(1))
                selectedIds.remove((Integer) 1);
        });

        depositRow1.add(depositButton1_1);

        //ROW 2
        JPanel depositRow2 = new JPanel();
        depositRow2.setLayout(new GridLayout(1,2));
        depositRow2.setOpaque(false);

        DepositButton depositButton2_1 = new DepositButton();
        depositButton2_1.setContentAreaFilled(false);
        depositButton2_1.setOpaque(false);
        depositButton2_1.addActionListener(e->{
            if(!depositButton2_1.isSelected())
                selectedIds.add(2);
            else if(selectedIds.contains(2))
                selectedIds.remove((Integer) 2);
        });

        DepositButton depositButton2_2 = new DepositButton();
        depositButton2_2.setContentAreaFilled(false);
        depositButton2_2.setOpaque(false);
        depositButton2_2.addActionListener(e->{
            if(!depositButton2_2.isSelected())
                selectedIds.add(2);
            else if(selectedIds.contains(2))
                selectedIds.remove((Integer) 2);
        });

        depositRow2.add(depositButton2_1);
        depositRow2.add(depositButton2_2);

        //ROW 3
        JPanel depositRow3 = new JPanel();
        depositRow3.setLayout(new  GridLayout(1,3));
        depositRow3.setOpaque(false);

        DepositButton depositButton3_1 = new DepositButton();
        depositButton3_1.setContentAreaFilled(false);
        depositButton3_1.setOpaque(false);
        depositButton3_1.addActionListener(e->{
            if(!depositButton3_1.isSelected())
                selectedIds.add(3);
            else if(selectedIds.contains(3))
                selectedIds.remove((Integer) 3);
        });

        DepositButton depositButton3_2 = new DepositButton();
        depositButton3_2.setContentAreaFilled(false);
        depositButton3_2.setOpaque(false);
        depositButton3_2.addActionListener(e->{
            if(!depositButton3_2.isSelected())
                selectedIds.add(3);
            else if(selectedIds.contains(3))
                selectedIds.remove((Integer) 3);
        });

        DepositButton depositButton3_3 = new DepositButton();
        depositButton3_3.setContentAreaFilled(false);
        depositButton3_3.setOpaque(false);
        depositButton3_3.addActionListener(e->{
            if(!depositButton3_3.isSelected())
                selectedIds.add(3);
            else if(selectedIds.contains(3))
                selectedIds.remove((Integer) 3);
        });

        depositRow3.add(depositButton3_1);
        depositRow3.add(depositButton3_2);
        depositRow3.add(depositButton3_3);

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
        utilityButtons.setLayout(new BoxLayout(utilityButtons, BoxLayout.Y_AXIS));
        utilityButtons.setOpaque(false);

        utilityButtons.add(new JButton("SWITCH"));
        utilityButtons.add(new JButton("SEND"));
        utilityButtons.add(new JButton("ADD"));

        this.add(deposit);
        //this.add(Box.createRigidArea(new Dimension(200,10)));
        this.add(utilityButtons);

    }

    public void clearSelected(){
        selectedIds.clear();
    }

    public ArrayList<Integer> getSelectedIds() {
        return selectedIds;
    }

    public HashMap<Integer, ArrayList<DepositButton>> getDepositButtons() {
        return depositButtons;
    }
}
