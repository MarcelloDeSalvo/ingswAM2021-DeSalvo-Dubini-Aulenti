package it.polimi.ingsw.model.exceptions;

public class InvalidColumnNumber extends Exception{

    /**
     * Thrown when the user selects a column number from the market that doesn't exist
     */
    public InvalidColumnNumber(String s){
        super(s);
    }
}
