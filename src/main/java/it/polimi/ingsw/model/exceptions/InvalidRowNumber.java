package it.polimi.ingsw.model.exceptions;

public class InvalidRowNumber extends Exception{

    /**
     * Thrown when the user selects a row number from the market that doesn't exist
     */
    public InvalidRowNumber(String s){
        super(s);
    }
}
