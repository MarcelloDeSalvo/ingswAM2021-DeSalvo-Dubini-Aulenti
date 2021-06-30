package it.polimi.ingsw.model.exceptions;

public class DifferentResourceType extends Exception{

    /**
     * Thrown when the user is trying to put a Resource in a deposit row that is already storing a different ResourceType. <br>
     * E.G. The user tries to put a STONE inside row 2 when row 2 already has a GOLD inside
     */
    public DifferentResourceType(String s){
        super(s);
    }
}
