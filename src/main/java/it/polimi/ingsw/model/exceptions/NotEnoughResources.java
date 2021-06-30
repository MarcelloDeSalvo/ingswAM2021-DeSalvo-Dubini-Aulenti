package it.polimi.ingsw.model.exceptions;

public class NotEnoughResources extends Exception{

    /**
     * Thrown when the user is trying to do an action and he doesn't have the necessary resources <br>
     * E.G. The user tries to buy a  dev. card and his deposit and vault don't store enough resources to pay it.
     */
    public NotEnoughResources(String s){
        super (s);
    }
}
