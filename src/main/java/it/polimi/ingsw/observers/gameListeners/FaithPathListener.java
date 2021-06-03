package it.polimi.ingsw.observers.gameListeners;

import java.util.ArrayList;

public interface FaithPathListener {
    /**
     * Notifies that the current player's position has been incremented
     * @param faithPoints amount of faith points added
     * @param nickname nickname that needs to be incremented
     */
    void notifyCurrentPlayerIncrease(int faithPoints, String nickname);

    /**
     * Notifies that everybody's positions except the current player's one have been incremented.
     * Method called when a player overflows Resources in Deposit.
     * @param faithPoints amount of faith points added
     * @param nickname nickname that needs to be incremented
     */
    void notifyOthersIncrease(int faithPoints, String nickname);

    /**
     * Notifies that a papal favour has been activated
     * @param playerFavours every players' papal favours
     */
    void notifyPapalFavour(ArrayList<Integer> playerFavours, String senderNick);

}
