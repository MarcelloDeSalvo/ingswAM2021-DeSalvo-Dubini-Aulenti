package model;

public interface ObserverEndGame {

    /**
     * Updates all the observers when the game enters his final stage (one last round)
     */
    void update();

    /**
     * Notifies that Lorenzo has won the game
     */
    void lorenzoWon();

}
