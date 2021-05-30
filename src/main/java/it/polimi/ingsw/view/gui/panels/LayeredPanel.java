package it.polimi.ingsw.view.gui.panels;


import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LayeredPanel extends JPanel {

    public LayeredPanel() {
        setLayout(new BorderLayout());

        JLayeredPane layeredPane =  new JLayeredPane();
        layeredPane.setLayout(null);
        layeredPane.setBounds(0,0,1920,1080);

        JButton jButton = new JButton("CIAO");
        jButton.setBounds(700,400,200, 300);
        jButton.setOpaque(true);

        JLabel jLabel = new JLabel("CIAO");
        jLabel.setBounds(0,0,100,100);
        jLabel.setOpaque(true);

        JLabel playerBoard = new JLabel();
        ImageIcon iconPlayerBoard = new ImageIcon(getClass().getResource("/images/PlayerBoard.jpg"));
        playerBoard.setIcon(iconPlayerBoard);
        playerBoard.setBounds(0,0, iconPlayerBoard.getIconWidth(), iconPlayerBoard.getIconHeight());
        playerBoard.setOpaque(true);

        JLabel jLabelImg = new JLabel();
        ImageIcon icon = new ImageIcon(getClass().getResource("/images/croceRossa.png"));
        jLabelImg.setIcon(icon);
        jLabelImg.setBounds(200,200, icon.getIconWidth(), icon.getIconHeight());
        jLabelImg.setOpaque(false);

        DraggableImage draggableImage = new DraggableImage("/images/croceRossa.png");
        draggableImage.setOpaque(false);
        //draggableImage.setBounds(0,0,draggableImage.getWidth(),draggableImage.getHeight());



       //System.out.println(draggableImage.getHeight());

        jLabelImg.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Click");
            }
        });

        layeredPane.add(playerBoard, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(jLabelImg, JLayeredPane.PALETTE_LAYER);
        layeredPane.add(draggableImage, JLayeredPane.DRAG_LAYER);

        this.add(draggableImage, BorderLayout.CENTER);


    }
}
