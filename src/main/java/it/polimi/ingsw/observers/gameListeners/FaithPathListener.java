package it.polimi.ingsw.observers.gameListeners;

public interface FaithPathListener {

    void notifyCurrentPlayerIncrease(int faithpoints, String nickname);
    void notifyOthersIncrease(int faithpoints, String nickname);
    void notifyPapalFavour();

}
