package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.exceptions.ImageNotFound;
import it.polimi.ingsw.view.ImageUtil;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ButtonImage extends JButton {

    /**
     * ButtonImage with an automatic image resize
     */
    public ButtonImage(String path, Dimension scaledDimension) throws ImageNotFound {
        super();
        BufferedImage originalImage = ImageUtil.loadImage(path);
        Image dimg = originalImage.getScaledInstance(scaledDimension.width, scaledDimension.height, Image.SCALE_SMOOTH);
        ImageIcon imageIcon = new ImageIcon(dimg);
        this.setIcon(imageIcon);
        this.setBorderPainted(false);
        this.setContentAreaFilled(false);
        this.setPreferredSize(new Dimension(scaledDimension.width, scaledDimension.height));
    }

    public ButtonImage(String text, int fontSize, boolean mode) {
        super();
        this.setText(text);
        this.setFont(new Font("Rubik", Font.BOLD, fontSize));
        this.setBorder(new LineBorder(new Color(219, 139, 0)));
        this.setForeground(new Color(255, 255, 255));
        this.setBackground(new Color(241, 153, 0));
        this.setMargin(new Insets(10,30,10,30));
    }

    public ButtonImage(String text, boolean mode) {
        super();
        this.setText(text);
        this.setFont(new Font("Rubik", Font.BOLD, 44));
        this.setBorder(new LineBorder(new Color(219, 139, 0)));
        this.setForeground(new Color(255, 255, 255));
        this.setBackground(new Color(241, 153, 0));
    }

    public ButtonImage(String path) {
        super();
        try {
            ImageIcon ico = new ImageIcon(this.getClass().getResource(path));
            this.setIcon(ico);
            this.setBorderPainted(false);
            this.setContentAreaFilled(false);
            this.setPreferredSize(new Dimension(ico.getIconWidth(), ico.getIconHeight()));
        } catch (NullPointerException ex) {
            System.out.println("Button image not found: " +path);
        }

    }

    /**
     * Change the image of a ButtonImage
     * @param buttonImage is the already existing button image
     * @param path is the path of the image
     * @param scaledDimension is the dimension that i want to set to the image
     */
    public void setScaledImage(ButtonImage buttonImage, String path, Dimension scaledDimension){
        try {
            BufferedImage originalImage = ImageIO.read(this.getClass().getResourceAsStream(path));
            Image dimg = originalImage.getScaledInstance(scaledDimension.width, scaledDimension.height, Image.SCALE_SMOOTH);
            ImageIcon imageIcon = new ImageIcon(dimg);
            buttonImage.setIcon(imageIcon);
            this.setPreferredSize(new Dimension(scaledDimension.width, scaledDimension.height));
        } catch (NullPointerException | IllegalArgumentException | IOException ex) {
            System.out.println("Button image not found: " +path);
        }
    }
}
