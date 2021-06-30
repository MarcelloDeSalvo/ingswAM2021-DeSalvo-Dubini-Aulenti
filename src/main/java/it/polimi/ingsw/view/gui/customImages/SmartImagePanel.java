package it.polimi.ingsw.view.gui.customImages;

import it.polimi.ingsw.model.exceptions.ImageNotFound;
import it.polimi.ingsw.view.ImageUtil;
import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.gui.panels.BackgroundImagePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public abstract class SmartImagePanel extends JComponent {
    private final Gui gui;

    private final BufferedImage originalImage;
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
    public SmartImagePanel(Gui gui, String path, Dimension imageDimension, Point relativePosition, BackgroundImagePanel backGround) throws ImageNotFound {
        this.gui = gui;
        this.backGround = backGround;
        this.backGroundPivot = backGround.getPivot();
        this.imageDimension = imageDimension;
        this.imagePosition = new Point();

        originalImage = ImageUtil.loadImage(path);

        tmp = originalImage.getScaledInstance(imageDimension.width, imageDimension.height, Image.SCALE_SMOOTH);
        scaledImage = ImageUtil.resizeImage(originalImage, imageDimension);

        imagePosition.x = (relativePosition.x + backGroundPivot.x) ;
        imagePosition.y = (relativePosition.y + backGroundPivot.y) ;

        this.setBounds(imagePosition.x,imagePosition.y,imageDimension.width,imageDimension.height);
        imgArea = new Rectangle(imagePosition.x, imagePosition.y, scaledImage.getWidth(), scaledImage.getHeight());

        ClickListener clickListener = new ClickListener();

        this.addMouseListener(clickListener);

        setOpaque(false);
    }

    /**
     * Renders the image with its original dimensions
     * @param relativePosition is the starting position relative to the background
     * @param backGround is the background panel
     */
    public SmartImagePanel(Gui gui, String path, Point relativePosition, BackgroundImagePanel backGround) {
        this.gui = gui;
        this.backGround = backGround;
        this.backGroundPivot = backGround.getPivot();
        this.imagePosition = new Point();

        originalImage = ImageUtil.loadImage(path);

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
        this.addMouseListener(clickListener);

        setOpaque(false);
    }

    /**
     * Called when the mouse clicks the image
     * @param e is the mouse event
     */
    public abstract void onMouseClicked(MouseEvent e, Graphics2D graphics2D);

    /**
     * Called when the mouse is over the image
     */
    public abstract void onHighlight(Graphics2D graphics2D);

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (backGround==null)
            return;

        if(isHighlight()){
            onHighlight(backGround.getGraphics2D());
            backGround.repaint();
        }

        backGround.getGraphics2D().drawImage(tmp, imagePosition.x, imagePosition.y, imageDimension.width, imageDimension.height, this);
    }


    /**
     * Listens for mouse clicks
     */
    private class ClickListener extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            onMouseClicked(e, backGround.getGraphics2D());
            backGround.repaint();
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            highlight = true;
            backGround.repaint();
        }

        @Override
        public void mouseExited(MouseEvent e) {
            highlight = false;
            backGround.repaint();
        }
    }


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

    public BackgroundImagePanel getBackGroundPanel() { return backGround; }

    public boolean isHighlight() {
        return highlight;
    }

    public Gui getGui() {
        return gui;
    }
}
