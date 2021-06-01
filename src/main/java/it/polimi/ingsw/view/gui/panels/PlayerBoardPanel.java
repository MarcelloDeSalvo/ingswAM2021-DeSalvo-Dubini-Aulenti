package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.model.cards.Status;
import it.polimi.ingsw.network.commands.Command;
import it.polimi.ingsw.network.commands.IdMessage;
import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.gui.buttons.ButtonCard;
import it.polimi.ingsw.view.gui.customImages.LabelImage;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class PlayerBoardPanel extends JLayeredPane {

    private final Gui gui;
    private DepositPanel depositPanel;

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
        pl1.setLayout(new BorderLayout());
        pl1.setBorder(new EmptyBorder(30,20,70,0));
        LabelImage boardImg = new LabelImage("/images/PlayerBoard.jpg");
        pl1.add(boardImg);
        pl1.setOpaque(false);

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
        layer1.setOpaque(false);
        layer1.setLayout(null);

        developmentSlotButtons(layer1);

        //DEPOSIT ZONE
        depositPanel = new DepositPanel();
        depositPanel.setBounds(70,120,320,385);
        layer1.add(depositPanel);

        //---------------------------------------------------------------------------------------------------------------------------

        this.add(layer1, JLayeredPane.POPUP_LAYER);
    }

    private void developmentSlotButtons(JPanel layer1){
        JPanel clickableSlot1 = new JPanel();
        clickableSlot1.setBackground(new Color(100,100,200,200));
        clickableSlot1.setBounds(680,200,250,350);

        JPanel clickableSlot2 = new JPanel();
        clickableSlot2.setBackground(new Color(100,100,200,200));
        clickableSlot2.setBounds(980,200,250,350);

        JPanel clickableSlot3 = new JPanel();
        clickableSlot3.setBackground(new Color(100,100,200,200));
        clickableSlot3.setBounds(1310,200,250,350);

        layer1.add(clickableSlot1);
        layer1.add(clickableSlot2);
        layer1.add(clickableSlot3);
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

    public DepositPanel getDepositPanel() {
        return depositPanel;
    }
}
