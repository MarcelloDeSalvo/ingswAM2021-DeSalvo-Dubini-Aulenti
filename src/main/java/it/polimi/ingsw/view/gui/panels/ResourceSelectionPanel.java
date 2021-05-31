package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.model.resources.ResourceType;
import it.polimi.ingsw.view.gui.ButtonImage;
import it.polimi.ingsw.view.gui.Gui;

import javax.swing.*;
import java.awt.*;


public class ResourceSelectionPanel extends JPanel {

    private final Gui gui;
    //JPanel resourcesPanel;
    ResourceType selectedResource;

    public ResourceSelectionPanel(Gui gui) {
        super();
        this.gui = gui;
        this.setLayout(new BorderLayout(2,1));
        this.selectedResource=ResourceType.BLANK;

        JPanel resourcesPanel=new JPanel();
        JPanel mainBoxPanel=new JPanel();
        JLayeredPane depositPanel=new JLayeredPane();


        mainBoxPanel.setLayout(new BoxLayout(mainBoxPanel,BoxLayout.X_AXIS));
        mainBoxPanel.add(Box.createRigidArea(new Dimension(200,0)));
        mainBoxPanel.add(resourcesPanel);
        mainBoxPanel.add(Box.createHorizontalGlue());
        mainBoxPanel.add(depositPanel);
        mainBoxPanel.add(Box.createRigidArea(new Dimension(500,0)));

        resourcesPanel.setLayout(new BoxLayout(resourcesPanel,BoxLayout.Y_AXIS));
        resourcesPanel.add(Box.createVerticalGlue());


        for (ResourceType rt:ResourceType.values()) {
            if(rt.canAddToDeposit()) {
                ButtonImage resButton = new ButtonImage("/images/resourceImages/" + rt.deColored().toLowerCase() + ".png");
                resourcesPanel.add(resButton);
                resButton.addActionListener(e -> {
                    selectedResource = rt;
                });
                resourcesPanel.add(Box.createVerticalGlue());
            }
        }


        depositPanel.add(new ButtonImage("/images/deposito.png"), 0);
        JPanel buttonPanel=new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel,BoxLayout.Y_AXIS));


        JPanel depositRow1 = new JPanel();
        depositRow1.setBackground(new Color(100,200,100,100));
        JButton depositButton1_1 = new JButton("UNO");
        depositRow1.add(depositButton1_1);

        JPanel depositRow2 = new JPanel();
        depositRow2.setBackground(new Color(100,200,100,100));
        depositRow2.setLayout(new GridLayout(1,2));

        JButton depositButton2_1 = new JButton("DUE");
        depositButton2_1.setBackground(new Color(150,250,200,100));
        depositRow2.add(depositButton2_1);

        JPanel depositRow3 = new JPanel();
        depositRow3.setBackground(new Color(100,200,100,100));
        depositRow3.setLayout(new  GridLayout(1,3));

        JButton depositButton3_1 = new JButton("TRE");
        depositButton3_1.setBackground(new Color(250,250,200,100));
        depositRow3.add(depositButton3_1);

        buttonPanel.add(depositRow1);
        buttonPanel.add(depositRow2);
        buttonPanel.add(depositRow3);


        //buttonPanel.setBounds(); map bounds through an action listener


        depositPanel.setLayer(buttonPanel,1);

        JLabel title= new JLabel("Select the bonus resources");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setVerticalAlignment(SwingConstants.CENTER);
        title.setFont(new Font("Helvetica", Font.PLAIN, 40));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        this.add(title,BorderLayout.NORTH);
        this.add(mainBoxPanel,BorderLayout.CENTER);

    }
}
