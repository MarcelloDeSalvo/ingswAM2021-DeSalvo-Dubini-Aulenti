package it.polimi.ingsw.view.gui;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.util.ArrayList;

public class DiscardHandPanel extends JPanel {

    private ArrayList<Integer> IDs;
    JPanel bottomPanel;

    public DiscardHandPanel(ArrayList<Integer> IDs){
        super();
        this.IDs=IDs;
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

        for (Integer id : IDs) {
            System.out.println(id);
        }


        for (Integer id : IDs) {

            InputStream url = getClass().getResourceAsStream("/images/cardFrontJpgs/LeaderFront_"+id+".jpg");
            bottomPanel.add(Box.createHorizontalGlue());

            bottomPanel.add(new ButtonImage("/images/cardFrontJpgs/LeaderFront_"+id+".jpg"));
            bottomPanel.add(Box.createHorizontalGlue());

        }
    }

}
