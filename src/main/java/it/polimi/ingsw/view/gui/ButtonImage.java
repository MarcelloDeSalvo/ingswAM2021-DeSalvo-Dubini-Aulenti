package it.polimi.ingsw.view.gui;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class ButtonImage extends JButton {

    public ButtonImage(String text, boolean mode) {
        super();
        try {
            this.setText(text);
            this.setFont(new Font("Rubik", Font.BOLD, 44));
            this.setBorder(new LineBorder(Color.orange));
            this.setForeground(new Color(255, 255, 255));
            this.setBackground(new Color(241, 153, 0));
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }

    }

    public ButtonImage(String path) {
        super();
        try {
            ImageIcon ico = new ImageIcon(this.getClass().getResource(path));
            this.setIcon(ico);
            this.setBorderPainted(false);
            this.setContentAreaFilled(false);
            this.setPreferredSize(new Dimension(ico.getIconWidth(), ico.getIconHeight()));
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }

    }


}
