package it.polimi.ingsw.model.exceptions;

public class ImageNotFound extends RuntimeException {

    /**
     * Thrown in gui when there are issues digging up the image's path.
     */
    public ImageNotFound(String s){
        super("- Missing image: " + s);
    }
}
