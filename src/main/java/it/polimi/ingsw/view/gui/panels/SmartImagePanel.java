package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.view.gui.Gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.IOException;

public abstract class SmartImagePanel extends JComponent {
    private final Gui gui;

    private BufferedImage originalImage;
    private final BufferedImage scaledImage;

    private final Rectangle imgArea;
    private final Dimension imageDimension;
    private final BackgroundImagePanel backGround;
    private Point imagePosition;
    private final Point backGroundPivot;
    private final Image tmp;

    private boolean highlight = false;

    /**
     * Renders a scaled Image
     * @param imageDimension is the wanted dimension
     * @param relativePosition is the starting position relative to the background
     * @param backGround is the background panel
     */
    public SmartImagePanel(Gui gui, String path, Dimension imageDimension, Point relativePosition, BackgroundImagePanel backGround)  {
        this.gui = gui;
        this.backGround = backGround;
        this.backGroundPivot = backGround.getPivot();
        this.imagePosition = new Point();

        try{
            originalImage = ImageIO.read(this.getClass().getResourceAsStream(path));
        }catch (IOException|IllegalArgumentException e){
            System.out.println(path+ " not found");
        }

        int width = imageDimension.width;
        int height = imageDimension.height;

        this.imageDimension = new Dimension(width,height);

        tmp = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        scaledImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        imagePosition.x = (relativePosition.x + backGroundPivot.x) ;
        imagePosition.y = (relativePosition.y + backGroundPivot.y) ;

        this.setBounds(imagePosition.x,imagePosition.y,imageDimension.width,imageDimension.height);
        imgArea = new Rectangle(imagePosition.x, imagePosition.y, scaledImage.getWidth(), scaledImage.getHeight());

        ClickListener clickListener = new ClickListener();
        //MovementListener movementListener = new MovementListener();

        this.addMouseListener(clickListener);
        //this.addMouseMotionListener(movementListener);

        setOpaque(false);
    }

    /**
     * Renders the image with its original dimensions
     * @param relativePosition is the starting position relative to the background
     * @param backGround is the background panel
     */
    public SmartImagePanel(Gui gui, String path, Point relativePosition, BackgroundImagePanel backGround)  {
        this.gui = gui;
        this.backGround = backGround;
        this.backGroundPivot = backGround.getPivot();
        this.imagePosition = new Point();

        try{
            originalImage = ImageIO.read(this.getClass().getResourceAsStream(path));
        }catch (IOException|IllegalArgumentException e){
            System.out.println(path+ " not found");
        }

        tmp = originalImage;
        scaledImage = originalImage;

        int width = scaledImage.getWidth();
        int height = scaledImage.getHeight();

        this.imageDimension = new Dimension(width,height);

        imagePosition.x = (relativePosition.x + backGroundPivot.x );
        imagePosition.y = (relativePosition.y + backGroundPivot.y);


        this.setBounds(imagePosition.x,imagePosition.y,imageDimension.width,imageDimension.height);
        imgArea = new Rectangle( imagePosition.x,  imagePosition.y, scaledImage.getWidth(), scaledImage.getHeight());

        ClickListener clickListener = new ClickListener();
        //MovementListener movementListener = new MovementListener();

        this.addMouseListener(clickListener);
        //this.addMouseMotionListener(movementListener);

        setOpaque(false);
    }

    /**
     * Called when the mouse clicks the image
     * @param e is the mouse event
     */
    public abstract void onMouseClicked(MouseEvent e);

    /**
     * Called when the mouse is over the image
     */
    public abstract void onHighlight(Graphics2D graphics2D);

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (backGround==null)
            return;

        backGround.getGraphics2D().drawImage(tmp, imagePosition.x, imagePosition.y, imageDimension.width, imageDimension.height, this);
    }


    /**
     * Listens for mouse clicks
     */
    private class ClickListener extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            onMouseClicked(e);
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            highlight = true;
            repaint();
        }

        @Override
        public void mouseExited(MouseEvent e) {
            highlight = false;
            repaint();
        }
    }

    /*
    /**
     * Waits for the mouse to enter the image area

    private class MovementListener extends MouseMotionAdapter {
        @Override
        public void mouseMoved(MouseEvent e) {

        }

    } */

    public Point getImagePosition() {
        return imagePosition;
    }

    public void setImagePosition(Point imagePosition) {
        this.imagePosition = imagePosition;
        this.setBounds(imagePosition.x,imagePosition.y,imageDimension.width,imageDimension.height);
        this.imgArea.setLocation(imagePosition.x,imagePosition.y);
        repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        return imageDimension;
    }

    public BufferedImage getBufferedImage() {
        return scaledImage;
    }

    public Rectangle getImgArea() {
        return imgArea;
    }

    public BackgroundImagePanel getBackGroundPanel() { return backGround; }

    public boolean isHighlight() {
        return highlight;
    }

    public Gui getGui() {
        return gui;
    }
}
