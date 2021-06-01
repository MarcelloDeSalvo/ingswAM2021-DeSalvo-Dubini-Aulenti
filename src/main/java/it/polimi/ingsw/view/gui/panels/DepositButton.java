package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.model.resources.ResourceType;
import it.polimi.ingsw.view.ImageUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;


public class DepositButton extends JButton {

    private boolean selected = false;
    private boolean empty = true;
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
        if (resourceType==null)
            this.setIcon(null);

        BufferedImage image = ImageUtil.loadImage("/images/resourceImages/" + resourceType.deColored().toLowerCase() + ".png");
        this.setIcon(new ImageIcon(image));
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    public int getId() {
        return id;
    }

    public int getPos() {
        return pos;
    }
}
