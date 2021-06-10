package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.model.resources.ResourceContainer;
import it.polimi.ingsw.model.resources.ResourceType;
import it.polimi.ingsw.network.commands.Command;
import it.polimi.ingsw.network.commands.SendContainer;
import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.gui.buttons.ButtonImage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ResourceSelectionPanel extends JPanel {

    private final Gui gui;
    private ResourceType selectedResource;
    private final DepositPanel depositPanel;

    public ResourceSelectionPanel(Gui gui) {
        super();
        this.gui = gui;
        this.setLayout(new BorderLayout(2,1));
        this.selectedResource=ResourceType.BLANK;


        BackgroundImagePanel mainBoxPanel=new BackgroundImagePanel("/images/others/deposit.png",988,220,false);
        mainBoxPanel.setLayout(new BoxLayout(mainBoxPanel,BoxLayout.X_AXIS));

        JPanel resourcesPanel=new JPanel();

        depositPanel = new DepositPanel(gui, false);

        mainBoxPanel.add(Box.createRigidArea(new Dimension(300,0)));
        mainBoxPanel.add(resourcesPanel);
        mainBoxPanel.add(Box.createRigidArea(new Dimension(550,0)));
        mainBoxPanel.add(depositPanel);
        mainBoxPanel.add(Box.createRigidArea(new Dimension(85,0)));


        resourcesPanel.setLayout(new BoxLayout(resourcesPanel,BoxLayout.Y_AXIS));
        resourcesPanel.add(Box.createVerticalGlue());
        resourcesPanel.setOpaque(false);

        for (ResourceType rt:ResourceType.values()) {
            if(rt.canAddToDeposit()) {
                ButtonImage resButton = new ButtonImage("/images/resourceImages/" + rt.deColored().toLowerCase() + ".png");
                resButton.setBorderPainted(false);
                resourcesPanel.add(resButton);
                resButton.addActionListener(e -> {
                    selectedResource = rt;
                    resourceActionWindow(resButton);
                });
                resourcesPanel.add(Box.createVerticalGlue());
            }
        }


        JLabel title = new JLabel("Select the bonus resources");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setVerticalAlignment(SwingConstants.CENTER);
        title.setFont(new Font("Helvetica", Font.PLAIN, 40));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        this.add(title,BorderLayout.NORTH);
        this.add(mainBoxPanel,BorderLayout.CENTER);

    }

    private void resourceActionWindow (ButtonImage button){
        JPopupMenu popupmenu = new JPopupMenu("Resource Action");
        JMenuItem deposit1 = new JMenuItem("Deposit 1");
        JMenuItem deposit2 = new JMenuItem("Deposit 2");
        JMenuItem deposit3 = new JMenuItem("Deposit 3");

        popupmenu.add(deposit1);
        popupmenu.add(deposit2);
        popupmenu.add(deposit3);

        button.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                popupmenu.show(button , e.getX(), e.getY());
            }
        });

        ResourceContainer container = new ResourceContainer(selectedResource,1);

        deposit1.addActionListener(e -> {
            if(selectedResource == ResourceType.BLANK)
                return;
            gui.send(new SendContainer(Command.SETUP_CONTAINER, container, "DEPOSIT", 1, gui.getNickname()));
        });

        deposit2.addActionListener(e -> {
            if(selectedResource == ResourceType.BLANK)
                return;
            gui.send(new SendContainer(Command.SETUP_CONTAINER, container, "DEPOSIT", 2, gui.getNickname()));
        });

        deposit3.addActionListener(e -> {
            if(selectedResource == ResourceType.BLANK)
                return;
            gui.send(new SendContainer(Command.SETUP_CONTAINER, container, "DEPOSIT", 3, gui.getNickname()));
        });
    }

    public DepositPanel getDepositPanel() {
        return depositPanel;
    }
}
