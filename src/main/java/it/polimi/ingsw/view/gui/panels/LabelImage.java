package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.model.exceptions.ImageNotFound;
import it.polimi.ingsw.view.ImageUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class LabelImage extends JLabel {

    private BufferedImage imageLabel;

    public LabelImage(String path, Dimension resizedDim) throws ImageNotFound {
        super();

        imageLabel =  ImageUtil.loadImage(path);
        imageLabel =  ImageUtil.resizeImage(imageLabel, resizedDim);

        this.setIcon(new ImageIcon(imageLabel));
        this.setOpaque(false);

    }

    public LabelImage(String path){
        super();

        imageLabel =  ImageUtil.loadImage(path);

        this.setIcon(new ImageIcon(imageLabel));
        this.setOpaque(false);
    }


    @Override
    public void paint(Graphics g){
        g.drawImage(imageLabel, 0, 0, this.getWidth(), this.getHeight(), null);
    }
}
