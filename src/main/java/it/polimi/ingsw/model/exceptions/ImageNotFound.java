package it.polimi.ingsw.model.exceptions;

public class ImageNotFound extends RuntimeException {

    public ImageNotFound(String s){
        super("- Missing image: " + s);
    }
}
