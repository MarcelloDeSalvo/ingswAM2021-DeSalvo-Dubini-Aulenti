package it.polimi.ingsw.model.exceptions;

public class ResourceTypeAlreadyStored extends Exception{

    /**
     * Thrown when the user attempts to put a Resource in a deposit row and another row is already containing
     * said Resource's ResourceType.
     */
    public ResourceTypeAlreadyStored(String s){
        super(s);
    }
}
