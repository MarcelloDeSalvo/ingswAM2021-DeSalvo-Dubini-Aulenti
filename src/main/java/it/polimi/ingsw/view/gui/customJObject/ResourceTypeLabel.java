package it.polimi.ingsw.view.gui.customJObject;

import it.polimi.ingsw.model.exceptions.ImageNotFound;
import it.polimi.ingsw.model.resources.ResourceContainer;
import it.polimi.ingsw.model.resources.ResourceType;
import it.polimi.ingsw.view.ImageUtil;
import it.polimi.ingsw.view.gui.customImages.LabelImage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;


public class ResourceTypeLabel extends LabelImage {

    private boolean selected = false;
    private ResourceType resourceType;
    private boolean empty = true;
    private int id;
    private int pos;

    public ResourceTypeLabel(int id, int pos, int size) {
        super();
        this.id = id;
        this.pos = pos;
        this.setOpaque(false);
        this.setMinimumSize(new Dimension(size-1,size-1));
        this.setMaximumSize(new Dimension(size, size));

        this.addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseExited(MouseEvent e) {
                        setBorder(BorderFactory.createEmptyBorder());
                        ResourceTypeLabel.super.repaint();
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        setBorder(BorderFactory.createLineBorder(Color.GREEN, 1));
                        ResourceTypeLabel.super.repaint();
                    }
                }
        );
    }

    /**
     * Sets the label image to a specific resourceType image
     */
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

    public ResourceContainer getResourceContainer() {
        return new ResourceContainer(resourceType, 1);
    }

    public int getId() {
        return id;
    }

    public int getPos() {
        return pos;
    }
}
