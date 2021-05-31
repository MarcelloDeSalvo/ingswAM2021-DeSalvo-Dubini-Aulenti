package it.polimi.ingsw.view;

import it.polimi.ingsw.model.exceptions.ImageNotFound;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ImageUtil {

    public static BufferedImage resizeImage(BufferedImage original, Dimension resizedDim){

        BufferedImage scaled;

        int width = resizedDim.width;
        int height = resizedDim.height;

        Image tmp = original.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        scaled =  new BufferedImage(tmp.getWidth(null), tmp.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        return scaled;
    }

    public static BufferedImage loadImage(String path) throws ImageNotFound{
        BufferedImage image;
        try{
            image = ImageIO.read(ImageUtil.class.getResourceAsStream(path));
        }catch (IOException |IllegalArgumentException e){
            throw new ImageNotFound(path);
        }

        return image;
    }
}
