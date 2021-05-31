package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.cards.Status;
import it.polimi.ingsw.network.commands.Command;
import it.polimi.ingsw.network.commands.IdMessage;
import it.polimi.ingsw.view.gui.ButtonImage;
import it.polimi.ingsw.view.gui.Gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class PlayerBoardPanel extends JLayeredPane {

    private final Gui gui;

    public PlayerBoardPanel(Gui gui) {
        super();
        this.gui = gui;

        this.setLayout(null);
        this.setBounds(0, 0, 1920, 980);


        //LAYER 0 -----------------------------------------------------------------------------------------------------------------------
        JPanel pl0 = new JPanel();
        pl0.setLayout(new BorderLayout());
        pl0.setOpaque(false);

        JPanel pl1 = new JPanel();
        LabelImage boardImg = new LabelImage("/images/PlayerBoard.jpg");
        pl1.add(boardImg);
        pl1.setOpaque(false);
        pl1.setBorder(new EmptyBorder(20,120,400,40));
        //BackgroundImagePanel pl1 = new BackgroundImagePanel("/images/PlayerBoard.jpg", 0, 65, false);
        //pl1.setBounds(0, 0, 1802, 877);
        //pl1.setWidth(1622);
        //pl1.setHeight(789);

        JPanel leadersPanel = new JPanel();
        leadersPanel.setLayout(new BoxLayout(leadersPanel, BoxLayout.Y_AXIS));
        leadersPanel.setOpaque(false);
        ArrayList<Integer> IDs = gui.getMyHand().getHand();

        leadersPanel.add(Box.createVerticalGlue());
        for (Integer id : IDs) {

            ButtonCard button = new ButtonCard(gui,"/images/cardFrontJpgs/LeaderFront_" + id + ".jpg", new Dimension(243, 367), id);  //new dimension is 70% of the original size
            leadersPanel.add(button, BorderLayout.CENTER);

            button.addActionListener(e-> leaderActionWindow(leadersPanel, id, button));

            leadersPanel.add(Box.createRigidArea(new Dimension(30, 50)));
        }
        leadersPanel.add(Box.createVerticalGlue());

        pl0.setBorder(new EmptyBorder(0,0,0,10));
        pl0.add(leadersPanel, BorderLayout.EAST);
        pl0.add(pl1, BorderLayout.CENTER);
        pl0.setBounds(0, 0, 1920, 980);

        this.add(pl0, 0);

        //LAYER 1 ------------------------------------------------------------------------------------------------------------------------
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

        //---------------------------------------------------------------------------------------------------------------------------

        this.add(layer1, JLayeredPane.POPUP_LAYER);
    }

    private void leaderActionWindow (JPanel currPanel, int selectedID, ButtonCard button){
        JPopupMenu popupmenu = new JPopupMenu("Leader Action");
        JMenuItem activate = new JMenuItem("Activate");
        JMenuItem discard = new JMenuItem("Discard");

        popupmenu.add(activate);
        popupmenu.add(discard);

        button.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if(gui.getMyHand().getStatusFromSpecificLeaderCard(selectedID) == Status.ACTIVE)
                    return;

                popupmenu.show(button , e.getX(), e.getY());
            }
        });

        activate.addActionListener(e -> {
            gui.send(new IdMessage(Command.ACTIVATE_LEADER, selectedID, gui.getNickname()));

            currPanel.repaint();
        });

        discard.addActionListener(e -> {
            gui.send(new IdMessage(Command.DISCARD_LEADER, selectedID, gui.getNickname()));

            currPanel.remove(button);
            currPanel.repaint();
        });
    }
}
