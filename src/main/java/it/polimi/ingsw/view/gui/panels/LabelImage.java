package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.view.ImageUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class LabelImage extends JPanel {

    private final BufferedImage imageLabel;

    public LabelImage(String path){
        super();
        imageLabel =  ImageUtil.loadImage(path);
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(imageLabel, 0, 0, this.getWidth(), this.getHeight(), null);
    }

}
