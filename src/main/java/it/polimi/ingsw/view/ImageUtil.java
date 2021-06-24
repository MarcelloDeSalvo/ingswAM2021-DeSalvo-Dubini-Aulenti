package it.polimi.ingsw.view;

import it.polimi.ingsw.model.exceptions.ImageNotFound;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

public class ImageUtil {

    private final static HashMap<String, BufferedImage> loadedImages = new HashMap<>();

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
        if (loadedImages.containsKey(path))
            return loadedImages.get(path);
        else {
            try{
                image = ImageIO.read(ImageUtil.class.getResourceAsStream(path));
                loadedImages.put(path, image);
                return image;
            }catch (IOException |IllegalArgumentException e){
                throw new ImageNotFound(path);
            }
        }
    }
}
