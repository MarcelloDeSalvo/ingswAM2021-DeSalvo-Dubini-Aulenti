package Model;

public interface ObserverEndGame {

    /**
     * Updates all the observers when the game enters his final stage (one last round)
     */
    void update();

    /**
     * Sends to the observer who won the game
     */
    void lorenzoWon();

}
