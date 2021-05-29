package it.polimi.ingsw.view.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class DiscardHandPanel extends JPanel {
    private ArrayList<Integer> IDs;

    DiscardHandPanel(ArrayList<Integer> IDs){
        this.IDs=IDs;
    }



    public void paint(Graphics g) {
        System.out.println("Quante volte chiamo paint?");
        drawCards(g);
    }



    private void drawCards(Graphics g) {
        ClassLoader cl = this.getClass().getClassLoader();
        for (Integer id : IDs) {
            System.out.println(id);
        }

        int x = 30;
        int y = 100;
        for (Integer id : IDs) {

            InputStream url = getClass().getResourceAsStream("/images/cardFrontJpgs/LeaderFront_"+id+".jpg");

            BufferedImage img = null;
            JLabel imageLabel;

            try {
                img = ImageIO.read(url);
                //Icon icon = new ImageIcon("/images/cardFrontJpgs/LeaderFront_"+id+".jpg");
                //imageLabel = new JLabel(icon);
                int W = img.getWidth();
                int H = img.getHeight();

                /*imageLabel.addMouseListener(new MouseAdapter()
                {
                    @Override
                    public void mouseClicked(MouseEvent e)
                    {
                        System.out.println("CLICK "+ e.getClickCount());
                    }
                });*/

                g.drawImage(img, x, y, W/2, H/2, null);
                x+=W/2+50;
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

        }
    }

}
