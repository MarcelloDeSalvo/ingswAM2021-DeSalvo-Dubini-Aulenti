package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.model.resources.ResourceType;
import it.polimi.ingsw.view.ImageUtil;
import it.polimi.ingsw.view.gui.Gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;


public class DepositButton extends JButton {

    private ResourceType resourceType;
    private boolean selected = false;

    public DepositButton() {
        super();
        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (!selected){
                    selected= true;
                    setBorder(BorderFactory.createLineBorder(Color.GREEN, 4));
                }
                else{
                    selected = false;
                    setBorder(BorderFactory.createEmptyBorder());
                }

            }
        });
    }

    public void setResourceTypeImage(ResourceType resourceType) {
        BufferedImage image = ImageUtil.loadImage("/images/resourceImages/" + resourceType.deColored().toLowerCase() + ".png");
        this.setIcon(new ImageIcon(image));
    }

    @Override
    public boolean isSelected() {
        return selected;
    }
}
