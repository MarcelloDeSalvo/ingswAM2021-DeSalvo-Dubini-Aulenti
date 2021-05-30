package it.polimi.ingsw.view.gui.panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class DraggableImage extends JPanel {

    private ImageIcon imageIcon;
    private Point corner;
    private Point prevPoint;
    private int height;
    private int width;


    public DraggableImage(String path) {
        corner = new Point(0,0);
        prevPoint =  new Point();

        try{
            imageIcon = new ImageIcon(this.getClass().getResource(path));
        }catch (IllegalArgumentException e){
            System.out.println(path+ " not found");
        }

        height = imageIcon.getIconHeight();
        width = imageIcon.getIconWidth();

        ClickListener clickListener = new ClickListener();
        MovementListener movementListener = new MovementListener();

        this.addMouseListener(clickListener);
        this.addMouseMotionListener(movementListener);


    }

    @Override
    protected void printComponent(Graphics g) {
        super.printComponent(g);
        imageIcon.paintIcon(this, g, (int)corner.getX(), (int)corner.getY());
    }

    /**
     * Listens for mouse clicks
     */
    private class ClickListener extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            prevPoint = e.getPoint();
            System.out.println(e.getPoint());
        }

    }

    private class MovementListener extends MouseMotionAdapter {

        @Override
        public void mouseMoved(MouseEvent e) {
            Point currentPt = e.getPoint();
            corner.translate((int) (currentPt.getX() - prevPoint.getX()),
                    (int) (currentPt.getY() - prevPoint.getY()));

            prevPoint = currentPt;
            repaint();
        }
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getWidth() {
        return width;
    }
}
