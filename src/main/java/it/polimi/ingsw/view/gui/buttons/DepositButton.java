package it.polimi.ingsw.view.gui.buttons;

import it.polimi.ingsw.model.exceptions.ImageNotFound;
import it.polimi.ingsw.model.resources.ResourceType;
import it.polimi.ingsw.view.ImageUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;


public class DepositButton extends JButton {

    private boolean selected = false;
    private ResourceType resourceType;
    private int id;
    private int pos;

    public DepositButton(int id, int pos) {
        super();
        this.id = id;
        this.pos = pos;

        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (!selected){
                    selected= true;
                    setBorder(BorderFactory.createLineBorder(Color.GREEN, 2));
                }
                else{
                    selected = false;
                    setBorder(BorderFactory.createEmptyBorder());
                }

            }
        });
    }

    public void setResourceTypeImage(ResourceType resourceType) throws ImageNotFound {
        if (resourceType==null){
            this.setIcon(null);
            return;
        }

        BufferedImage image = ImageUtil.loadImage("/images/resourceImages/" + resourceType.deColored().toLowerCase() + ".png");
        this.setIcon(new ImageIcon(image));
        this.resourceType = resourceType;
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public int getId() {
        return id;
    }

    public int getPos() {
        return pos;
    }
}
