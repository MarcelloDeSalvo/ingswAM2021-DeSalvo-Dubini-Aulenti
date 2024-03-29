package it.polimi.ingsw.model;

public interface ObservableEndGame {

    /**
     * Notifies all the observers that the game entered his final stage
     */
    void notifyEndGame();
    void addObserver(ObserverEndGame observerEndGame);
    void removeObserver(ObserverEndGame observerEndGame);
}
