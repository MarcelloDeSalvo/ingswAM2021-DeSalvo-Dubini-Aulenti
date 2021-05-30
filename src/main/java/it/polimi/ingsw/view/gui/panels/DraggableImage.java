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
        //this.setBounds(0,0,width,height);

        ClickListener clickListener = new ClickListener();
        MovementListener movementListener = new MovementListener();

        this.addMouseListener(clickListener);
        this.addMouseMotionListener(movementListener);


    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        imageIcon.paintIcon(this, g, (int)corner.getX(), (int)corner.getY());

    }

    /*protected void printComponent(Graphics g) {
        super.printComponent(g);
        System.out.println("Corner x"+ corner.getX());
        System.out.println("Corner y"+corner.getY());
        imageIcon.paintIcon(this, g, (int)corner.getX(), (int)corner.getY());
    }*/

    /**
     * Listens for mouse clicks
     */
    private class ClickListener extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            prevPoint = e.getPoint();
            System.out.println(e.getPoint());
        }

       /* @Override
        public void mouseReleased(MouseEvent e) {
            super.mouseReleased(e);
            Point currentPt = e.getPoint();
            setImagePosition(currentPt);
        }*/
    }

    private class MovementListener extends MouseMotionAdapter {

        @Override
        public void mouseDragged(MouseEvent e) {
            Point currentPt = e.getPoint();
            corner.translate((int) (currentPt.getX() - prevPoint.getX()),
                    (int) (currentPt.getY() - prevPoint.getY()));
            prevPoint = currentPt;
            repaint();
        }
    }

    public void setImagePosition(Point currentPt) {

        this.setLocation(currentPt.x,currentPt.y);
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
