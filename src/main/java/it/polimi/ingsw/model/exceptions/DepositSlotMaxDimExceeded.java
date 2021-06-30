package it.polimi.ingsw.model.exceptions;

public class DepositSlotMaxDimExceeded extends Exception {
    /**
     * Thrown inside deposit when a player places a number of resources that exceeds the maximum amount of that row. <br>
     * Used to increment the other players' positions by 1 on the FaithPath
     */
    public DepositSlotMaxDimExceeded(String s){
        super(s);
    }
}
