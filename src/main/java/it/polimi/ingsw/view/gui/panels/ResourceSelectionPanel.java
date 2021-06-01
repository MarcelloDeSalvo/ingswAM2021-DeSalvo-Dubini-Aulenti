package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.model.resources.ResourceContainer;
import it.polimi.ingsw.model.resources.ResourceType;
import it.polimi.ingsw.network.commands.Command;
import it.polimi.ingsw.network.commands.SendContainer;
import it.polimi.ingsw.view.gui.buttons.ButtonImage;
import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.gui.buttons.ButtonSelectable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ResourceSelectionPanel extends JPanel {

    private final Gui gui;
    //JPanel resourcesPanel;
    private ResourceType selectedResource;
    private DepositPanel depositPanel;

    public ResourceSelectionPanel(Gui gui) {
        super();
        this.gui = gui;
        this.setLayout(new BorderLayout(2,1));
        this.selectedResource=ResourceType.BLANK;


        BackgroundImagePanel mainBoxPanel=new BackgroundImagePanel("/images/deposit.png",938,220,false);
        mainBoxPanel.setLayout(new BoxLayout(mainBoxPanel,BoxLayout.X_AXIS));

        JPanel resourcesPanel=new JPanel();
        //BackGroundImageResizePanel depositPanel=new BackGroundImageResizePanel("/images/deposit.png");
        depositPanel= new DepositPanel();

        ButtonImage okButton=new ButtonImage("    OK     ",true);


        mainBoxPanel.add(Box.createRigidArea(new Dimension(250,0)));
        mainBoxPanel.add(resourcesPanel);
        mainBoxPanel.add(Box.createHorizontalGlue());
        mainBoxPanel.add(depositPanel);
        mainBoxPanel.add(Box.createRigidArea(new Dimension(2,0)));
        mainBoxPanel.add(okButton);
        mainBoxPanel.add(Box.createRigidArea(new Dimension(85,0)));


        resourcesPanel.setLayout(new BoxLayout(resourcesPanel,BoxLayout.Y_AXIS));
        resourcesPanel.add(Box.createVerticalGlue());
        resourcesPanel.setOpaque(false);

        for (ResourceType rt:ResourceType.values()) {
            if(rt.canAddToDeposit()) {
                ButtonSelectable resButton = new ButtonSelectable("/images/resourceImages/" + rt.deColored().toLowerCase() + ".png", Color.CYAN, 2);
                resourcesPanel.add(resButton);
                resButton.addActionListener(e -> {
                    selectedResource = rt;
                });
                resourcesPanel.add(Box.createVerticalGlue());
            }
        }

        //depositPanel.add(new ButtonImage("/images/deposito.png"), 0);
        //LabelImage depositImage= new LabelImage("/images/deposit.png");
        //depositPanel.add(depositImage,new Integer(0));

        //JPanel buttonPanel=new JPanel();
        /*JPanel depositRow1 = new JPanel();
        depositRow1.setBackground(new Color(100,200,100,100));

        JButton depositButton1_1 = new JButton("UNO");
        depositButton1_1.setBackground(new Color(100,200,100,100));
        depositButton1_1.setOpaque(true);
        depositRow1.add(depositButton1_1);

        JPanel depositRow2 = new JPanel();
        depositRow2.setBackground(new Color(100,200,100,100));
        depositRow2.setLayout(new GridLayout(1,2));

        JButton depositButton2_1 = new JButton("DUE");
        depositButton2_1.setBackground(new Color(150,250,200,100));
        depositButton2_1.setOpaque(true);
        depositRow2.add(depositButton2_1);

        JPanel depositRow3 = new JPanel();
        depositRow3.setBackground(new Color(100,200,100,100));
        depositRow3.setLayout(new  GridLayout(1,3));

        JButton depositButton3_1 = new JButton("TRE");
        depositButton3_1.setBackground(new Color(250,250,200,100));
        depositButton3_1.setOpaque(true);
        depositButton3_1.addActionListener(e -> {
            System.out.println("Mi Hai premuto!");
        } );
        depositRow3.add(depositButton3_1);

        buttonPanel.add(depositRow1);
        buttonPanel.add(depositRow2);
        buttonPanel.add(depositRow3);
        buttonPanel.setVisible(true);


         */
        //buttonPanel.setBounds(); map bounds through an action listener


        //depositPanel.add(buttonPanel);
        //depositPanel.setOpaque(false);
        depositPanel.setBorder(new EmptyBorder(265,225,285,310));

        okButton.addActionListener(e->{
            if(selectedResource==ResourceType.BLANK)
                return;

            depositPanel.fillSelectedBuffer();
            for (Integer id: depositPanel.getSelectedIds()) {
                ResourceContainer rc=new ResourceContainer(selectedResource,1);
                gui.send(new SendContainer(Command.SETUP_CONTAINER,rc,"DEPOSIT",id,gui.getNickname()));
            }
            depositPanel.clearSelected();

        });

        JLabel title= new JLabel("Select the bonus resources");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setVerticalAlignment(SwingConstants.CENTER);
        title.setFont(new Font("Helvetica", Font.PLAIN, 40));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        this.add(title,BorderLayout.NORTH);
        this.add(mainBoxPanel,BorderLayout.CENTER);

    }

    public DepositPanel getDepositPanel() {
        return depositPanel;
    }
}
