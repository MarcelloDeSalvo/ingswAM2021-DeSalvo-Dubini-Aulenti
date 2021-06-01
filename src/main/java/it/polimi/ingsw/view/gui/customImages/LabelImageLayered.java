package it.polimi.ingsw.view.gui.customImages;

import it.polimi.ingsw.model.exceptions.ImageNotFound;
import it.polimi.ingsw.view.ImageUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class LabelImageLayered extends JLayeredPane {

    private BufferedImage backGroundImage;
    private Image tmp;
    private JLabel backgroundLabel;

    public LabelImageLayered(String path, Dimension resizedDim) throws ImageNotFound {
        super();
        setLayout(null);

        JLabel background = new JLabel();

        backGroundImage =  ImageUtil.loadImage(path);

        backGroundImage =  ImageUtil.resizeImage(backGroundImage, resizedDim);

        background.setIcon(new ImageIcon(backGroundImage));
        this.add(background, JLayeredPane.DEFAULT_LAYER);

        this.setBounds(0,0, backGroundImage.getWidth(), backGroundImage.getHeight());
        setOpaque(false);

    }

    public LabelImageLayered(String path) throws ImageNotFound{
        super();
        setLayout(null);

        JLabel background = new JLabel();

        backGroundImage =  ImageUtil.loadImage(path);

        background.setIcon(new ImageIcon(backGroundImage));
        this.add(background, JLayeredPane.DEFAULT_LAYER);

        this.setBounds(0,0, backGroundImage.getWidth(), backGroundImage.getHeight());
        setOpaque(false);
    }

    public void changeBackground(String path) throws ImageNotFound {

        backGroundImage =  ImageUtil.loadImage(path);

        this.remove(backgroundLabel);
        backgroundLabel.setIcon(new ImageIcon(backGroundImage));
        this.add(backgroundLabel, JLayeredPane.DEFAULT_LAYER);
        this.setBounds(0,0, backGroundImage.getWidth(), backGroundImage.getHeight());


    }

    public void changeBackgroundResized(String path, Dimension resizedDim) throws ImageNotFound{

        backGroundImage = ImageUtil.loadImage(path);

        backGroundImage =  ImageUtil.resizeImage(backGroundImage, resizedDim);

        this.remove(backgroundLabel);
        backgroundLabel.setIcon(new ImageIcon(backGroundImage));
        this.add(backgroundLabel, JLayeredPane.DEFAULT_LAYER);
        this.setBounds(0,0, backGroundImage.getWidth(), backGroundImage.getHeight());
    }

    public BufferedImage getBackGroundImage() {
        return backGroundImage;
    }

    public JLabel getBackgroundLabel() {
        return backgroundLabel;
    }



}
