package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.liteModel.LiteHand;
import it.polimi.ingsw.network.commands.Command;
import it.polimi.ingsw.network.commands.IdMessage;
import it.polimi.ingsw.view.gui.ButtonImage;
import it.polimi.ingsw.view.gui.Gui;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.util.ArrayList;

public class DiscardHandPanel extends JPanel {

    private LiteHand liteHand;
    JPanel bottomPanel;
    Gui gui;

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


    private void drawCards() {
        ArrayList<Integer> IDs=liteHand.getHand();

        for (Integer id : IDs) {

            InputStream url = getClass().getResourceAsStream("/images/cardFrontJpgs/LeaderFront_"+id+".jpg");
            bottomPanel.add(Box.createHorizontalGlue());
            ButtonImage lc=new  ButtonImage("/images/cardFrontJpgs/LeaderFront_"+id+".jpg");

            bottomPanel.add(lc);
            lc.addActionListener(e->{
                gui.send(new IdMessage(Command.DISCARD_LEADER, id, gui.getNickname()));
                bottomPanel.remove(lc);
                bottomPanel.repaint();
            });
            bottomPanel.add(Box.createHorizontalGlue());

        }
    }

}
