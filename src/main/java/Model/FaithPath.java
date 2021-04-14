package Model;

import java.util.ArrayList;
import java.util.LinkedList;

public class FaithPath implements ObserverFaithPath, ObservableEndGame {
    private int numberOfPlayers;
    private int currentPlayer;
    private ArrayList<Integer> positions;
    private int length;
    private ArrayList<Character> vaticanReports;
    private ArrayList<Integer> victoryPoints;
    private ArrayList<ObserverEndGame> observersEndGame;
    private ArrayList<Integer> papalFavours;

    public FaithPath(int length, ArrayList<Character> vaticanReports, ArrayList<Integer> victoryPoints,ArrayList<Integer> papalFavours){
        this.currentPlayer=0;
        this.length=length;
        this.vaticanReports=vaticanReports;
        this.victoryPoints=victoryPoints;
        this.papalFavours=papalFavours;
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

    public int getLength() {
        return length;
    }

    public ArrayList<Character> getVaticanReports() {
        return vaticanReports;
    }

    public ArrayList<Integer> getPositions() {
        return positions;
    }

    public ArrayList<Integer> getVictoryPoints() {
        return victoryPoints;
    }

    public ArrayList<Integer> getPapalFavours() {
        return papalFavours;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }
    //------------------------------------------------------------------------------------------------------------------
}


