package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.view.ImageUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class BackGroundImagePanelResize extends JPanel {

    private BufferedImage imageLabel;

    public BackGroundImagePanelResize(String path){
        super();

        imageLabel =  ImageUtil.loadImage(path);

    }


    @Override
    public void paint(Graphics g){
        g.drawImage(imageLabel, 0, 0, this.getWidth(), this.getHeight(), null);
    }
}
