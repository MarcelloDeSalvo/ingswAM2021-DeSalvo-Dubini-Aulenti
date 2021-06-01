package it.polimi.ingsw.view.gui.buttons;

import it.polimi.ingsw.model.exceptions.ImageNotFound;
import it.polimi.ingsw.model.resources.ResourceType;
import it.polimi.ingsw.view.ImageUtil;
import it.polimi.ingsw.view.gui.customImages.LabelImage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;


public class DepositButton extends LabelImage {

    private boolean selected = false;
    private ResourceType resourceType;
    private boolean empty = true;
    private int id;
    private int pos;

    public DepositButton(int id, int pos) {
        super();
        this.id = id;
        this.pos = pos;
        this.addMouseListener(
                new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (!selected){
                            selected= true;
                            setBorder(BorderFactory.createLineBorder(Color.GREEN, 1));
                        }
                        else{
                            selected = false;
                            setBorder(BorderFactory.createEmptyBorder());
                        }
                        repaint();
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        DepositButton.super.repaint();
                    }
                }
        );
    }

    public void setResourceTypeImage(ResourceType resourceType) throws ImageNotFound {
        if (resourceType==null){
            this.setImageLabel(null);
            empty = true;
            return;
        }

        BufferedImage image = ImageUtil.loadImage("/images/resourceImages/" + resourceType.deColored().toLowerCase() + ".png");
        this.setImageLabel(image);
        this.resourceType = resourceType;
        empty = false;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

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
