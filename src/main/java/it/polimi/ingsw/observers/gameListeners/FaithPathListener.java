package it.polimi.ingsw.observers.gameListeners;

import java.util.ArrayList;

public interface FaithPathListener {

    void notifyCurrentPlayerIncrease(int faithpoints, String nickname);
    void notifyOthersIncrease(int faithpoints, String nickname);
    void notifyPapalFavour(ArrayList<Integer> playerFavours, String senderNick);

}
