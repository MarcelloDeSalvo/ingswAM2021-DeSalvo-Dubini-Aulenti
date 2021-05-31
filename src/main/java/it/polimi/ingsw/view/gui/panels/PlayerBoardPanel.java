package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.view.gui.Gui;

import javax.swing.*;
import java.awt.*;

public class PlayerBoardPanel extends JLayeredPane {

    private final Gui gui;

    public PlayerBoardPanel(Gui gui) {
        super();
        this.gui = gui;

        this.setLayout(null);
        this.setBounds(0, 0, 1920, 980);
        this.setBackground(Color.BLACK);

        BackgroundImagePanel pl1 = new BackgroundImagePanel("/images/PlayerBoard.jpg", 0, 0, false);
        pl1.setBounds(50, 0, 1802, 887);

        this.add(pl1, DEFAULT_LAYER);

        //LAYER 1 ------------------------------------------------------------------------------------------------------
        JPanel layer1 = new JPanel();
        layer1.setBounds(0,0,1920, 980);
        layer1.setBackground(new Color(220,100,100,100));
        layer1.setOpaque(false);
        layer1.setLayout(null);

        JPanel clickableSlot1 = new JPanel();
        clickableSlot1.setBackground(new Color(100,100,200,200));
        clickableSlot1.setBounds(800,400,173,262);

        JPanel clickableSlot2 = new JPanel();
        clickableSlot2.setBackground(new Color(100,100,200,200));
        clickableSlot2.setBounds(1000,400,173,262);

        JPanel clickableSlot3 = new JPanel();
        clickableSlot3.setBackground(new Color(100,100,200,200));
        clickableSlot3.setBounds(1200,400,173,262);

        layer1.add(clickableSlot1);
        layer1.add(clickableSlot2);
        layer1.add(clickableSlot3);


        //DEPOSIT ZONE
        JPanel deposit = new JPanel();
        deposit.setBackground(new Color(250,150,200,100));
        deposit.setLayout(new BoxLayout(deposit, BoxLayout.Y_AXIS));
        deposit.setOpaque(false);

        //ROW 1
        JPanel depositRow1 = new JPanel();
        depositRow1.setBackground(new Color(100,200,100,100));
        depositRow1.setLayout(new GridLayout(1,1));

        JButton depositButton1_1 = new JButton("UNO");
        depositButton1_1.setBackground(new Color(150,250,100,100));

        depositRow1.add(depositButton1_1);

        //ROW 2
        JPanel depositRow2 = new JPanel();
        depositRow2.setBackground(new Color(100,200,100,100));
        depositRow2.setLayout(new GridLayout(1,2));

        JButton depositButton2_1 = new JButton("DUE");
        depositButton2_1.setBackground(new Color(150,250,200,100));

        JButton depositButton2_2 = new JButton("DUE");
        depositButton2_2.setBackground(new Color(150,250,200,100));

        depositRow2.add(depositButton2_1);
        depositRow2.add(depositButton2_2);

        //ROW 3
        JPanel depositRow3 = new JPanel();
        depositRow3.setBackground(new Color(100,200,100,100));
        depositRow3.setLayout(new  GridLayout(1,3));

        JButton depositButton3_1 = new JButton("TRE");
        depositButton3_1.setBackground(new Color(250,250,200,100));

        JButton depositButton3_2 = new JButton("TRE");
        depositButton3_2.setBackground(new Color(250,250,200,100));

        JButton depositButton3_3 = new JButton("TRE");
        depositButton3_3.setBackground(new Color(250,250,200,100));

        depositRow3.add(depositButton3_1);
        depositRow3.add(depositButton3_2);
        depositRow3.add(depositButton3_3);


        //ADDS THE ROWS
        deposit.add(depositRow1);
        deposit.add(depositRow2);
        deposit.add(depositRow3);


        deposit.setBounds(200,200,400,400);
        layer1.add(deposit);

        //--------------------------------------------------------------------------------------------------------------

        this.add(layer1, JLayeredPane.MODAL_LAYER);

    }
}
