package it.polimi.ingsw.view.gui.panels;


import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LayeredPanel extends JPanel {

    public LayeredPanel() {
        super();
        setLayout(null);

        JLayeredPane layeredPane =  new JLayeredPane();
        layeredPane.setBounds(0,0,1000,1000);

        JButton jButton = new JButton("CIAO");
        jButton.setBounds(700,400,200, 300);
        jButton.setOpaque(true);

        JLabel jLabel = new JLabel("CIAO");
        jLabel.setBounds(0,0,100,100);
        jLabel.setOpaque(true);

        JLabel jLabelImg = new JLabel();
        ImageIcon icon = new ImageIcon(getClass().getResource("/images/croceRossa.png"));
        jLabelImg.setIcon(new ImageIcon(getClass().getResource("/images/croceRossa.png")));
        jLabelImg.setBounds(500,500, icon.getIconWidth(), icon.getIconHeight());
        jLabelImg.setOpaque(true);

        DraggableImage draggableImage = new DraggableImage("/images/croceRossa.png");
        draggableImage.setBounds(200,200, draggableImage.getWidth(), draggableImage.getHeight());
        draggableImage.setOpaque(false);

        System.out.println(draggableImage.getHeight());


        jLabelImg.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Click");
            }
        });

        layeredPane.add(jButton, 1);
        layeredPane.add(draggableImage, 0);

        this.add(layeredPane);


    }
}
