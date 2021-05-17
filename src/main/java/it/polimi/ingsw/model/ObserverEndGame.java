package it.polimi.ingsw.model;

public interface ObserverEndGame {

    /**
     * Updates all the observers when the game enters his final stage (one last round)
     */
    void updateEndGame();

    /**
     * Notifies that Lorenzo has won the game
     */
    void lorenzoWon();

}
