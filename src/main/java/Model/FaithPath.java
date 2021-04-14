package Model;

import java.util.ArrayList;

public class FaithPath implements ObserverFaithPath, ObservableEndGame {
    private int currentPlayer;
    private ArrayList<Integer> positions;
    private int length;
    private ArrayList<Integer> victoryPoints;
    private ArrayList<ObserverEndGame> observersEndGame;
    private int papalFavourCounter;

    public FaithPath() {
        this.positions = new ArrayList<>();
        this.observersEndGame = new ArrayList<>();
    }


    //OBSERVER METHODS--------------------------------------------------------------------------------------------------
    @Override
    public void update(int faithPoints) {
        positions.set(currentPlayer, positions.get(currentPlayer) + faithPoints);
    }

    /**
     * Calls the notifyEndGame() when someone reaches the end of the FaithPath
     */
    private void victoryConditions() {
        for(int position : this.positions) {
            if (position >= length-1)
                notifyEndGame();
        }
    }

    @Override
    public void notifyEndGame() {
        for (ObserverEndGame observer : this.observersEndGame) {
            observer.update(true);
        }
    }

    @Override
    public void addObserver(ObserverEndGame observerEndGame) {
        observersEndGame.add(observerEndGame);
    }

    @Override
    public void removeObserver(ObserverEndGame observerEndGame) {
        observersEndGame.remove(observerEndGame);
    }
    //------------------------------------------------------------------------------------------------------------------



    //GETTERS AND SETTERS-----------------------------------------------------------------------------------------------
    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
    //------------------------------------------------------------------------------------------------------------------
}
