package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.model.cards.Status;
import it.polimi.ingsw.network.commands.Command;
import it.polimi.ingsw.network.commands.IdMessage;
import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.gui.buttons.ButtonCard;
import it.polimi.ingsw.view.gui.buttons.ButtonImage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;

public class HandPanel extends JPanel {

    private final Gui gui;
    private final HashMap<Integer, ButtonCard> leaders;

    public HandPanel(Gui gui) {
        super();
        this.gui = gui;
        leaders = new HashMap<>();
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        ArrayList<Integer> IDs = gui.getMyHand().getHand();

        this.add(Box.createVerticalGlue());
        for (Integer id : IDs) {

            ButtonCard button = new ButtonCard(gui,"/images/cardFrontJpgs/LeaderFront_" + id + ".jpg", new Dimension(243, 367), id);  //new dimension is 70% of the original size
            this.add(button, BorderLayout.CENTER);
            leaders.put(id, button);

            button.addActionListener(e-> leaderActionWindow(id, button));

            this.add(Box.createRigidArea(new Dimension(30, 50)));
        }
        this.add(Box.createVerticalGlue());
    }

    /**
     * Different constructor used for showPlayer
     */
    public HandPanel(Gui gui,String nick) {
        super();
        this.gui = gui;
        leaders = new HashMap<>();
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        ArrayList<Integer> IDs = gui.getSomeonesHand(nick).getHand();
        int j=2;
        this.add(Box.createVerticalGlue());

        for (Integer id : IDs) {
            ButtonCard button = new ButtonCard(gui,"/images/cardFrontJpgs/LeaderFront_" + id + ".jpg", new Dimension(243, 367), id);  //new dimension is 70% of the original size
            this.add(button, BorderLayout.CENTER);
            leaders.put(id, button);
            this.add(Box.createRigidArea(new Dimension(30, 50)));
            j--;
        }

        while(j>0){
            ButtonImage button = new ButtonImage("/images/cardBackJpgs/leaderCardBack.jpg", new Dimension(243, 367));  //new dimension is 70% of the original size
            this.add(button, BorderLayout.CENTER);
            this.add(Box.createRigidArea(new Dimension(30, 50)));
            j--;
        }

        this.add(Box.createVerticalGlue());
    }

    private void leaderActionWindow (int selectedID, ButtonCard button){
        JPopupMenu popupmenu = new JPopupMenu("Leader Action");
        JMenuItem activate = new JMenuItem("Activate");
        JMenuItem discard = new JMenuItem("Discard");

        popupmenu.add(activate);
        popupmenu.add(discard);

        button.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if(gui.getMyHand().getStatusFromSpecificLeaderCard(selectedID) == Status.ACTIVE || !gui.getMyHand().getHand().contains(selectedID))
                    return;

                popupmenu.show(button , e.getX(), e.getY());
            }
        });

        activate.addActionListener(e -> gui.send(new IdMessage(Command.ACTIVATE_LEADER, selectedID, gui.getNickname())));

        discard.addActionListener(e -> gui.send(new IdMessage(Command.DISCARD_LEADER, selectedID, gui.getNickname())));
    }

    public void addLeader(Integer id){
        ButtonCard button = new ButtonCard(gui,"/images/cardFrontJpgs/LeaderFront_" + id + ".jpg", new Dimension(243, 367), id);  //new dimension is 70% of the original size
        this.add(button, BorderLayout.CENTER);
        this.add(Box.createRigidArea(new Dimension(30, 50)));

        System.out.println("Ho aggiunto alla mano di rick una bella "+id);

        leaders.put(id, button);
    };

    public HashMap<Integer, ButtonCard> getLeaders() {
        return leaders;
    }
}
