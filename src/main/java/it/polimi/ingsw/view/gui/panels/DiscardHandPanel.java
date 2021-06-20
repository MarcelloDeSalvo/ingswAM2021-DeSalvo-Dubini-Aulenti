package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.liteModel.LiteHand;
import it.polimi.ingsw.network.commands.Command;
import it.polimi.ingsw.network.commands.IdMessage;
import it.polimi.ingsw.view.gui.buttons.ButtonImage;
import it.polimi.ingsw.view.gui.Gui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class DiscardHandPanel extends JPanel {

    private final LiteHand liteHand;
    private final JPanel bottomPanel;
    private final Gui gui;
    private int discardCounter = 0;

    /**
     * Builds the panel used to determine which of the 4 leader cards the user wants to keep
     */
    public DiscardHandPanel(LiteHand hand, Gui gui){
        super();
        this.gui=gui;
        this.liteHand=hand;
        this.setLayout(new BorderLayout(2,1));
        this.bottomPanel=new JPanel();
        JPanel topPanel=new JPanel();

        bottomPanel.setLayout(new BoxLayout(bottomPanel,BoxLayout.X_AXIS));
        topPanel.setLayout(new BoxLayout(topPanel,BoxLayout.Y_AXIS));

        JLabel title= new JLabel("Click on the leaders you want to discard");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setVerticalAlignment(SwingConstants.CENTER);
        title.setFont(new Font("Helvetica", Font.PLAIN, 40));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        topPanel.add(Box.createRigidArea(new Dimension(100,70)));
        topPanel.add(title,BoxLayout.Y_AXIS);

        this.add(topPanel,BorderLayout.NORTH);
        this.add(bottomPanel, BorderLayout.CENTER);

        drawCards();
    }

    /**
     * Creates and the displays the leaderCards as ButtonImages
     */
    private void drawCards() {
        ArrayList<Integer> IDs=liteHand.getHand();

        for (Integer id : IDs) {

            bottomPanel.add(Box.createHorizontalGlue());
            ButtonImage lc=new  ButtonImage("/images/cardFrontJpgs/LeaderFront_"+id+".jpg");

            bottomPanel.add(lc);
            lc.addActionListener(e->{
                if(discardCounter >= 2)
                    return;

                bottomPanel.remove(lc);
                bottomPanel.repaint();
                discardCounter++;
                gui.send(new IdMessage(Command.DISCARD_LEADER, id, gui.getNickname()));

            });
            bottomPanel.add(Box.createHorizontalGlue());

        }
    }

}
