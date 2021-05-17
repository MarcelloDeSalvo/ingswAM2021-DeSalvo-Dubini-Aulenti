package it.polimi.ingsw.model;


import it.polimi.ingsw.observers.gameListeners.FaithPathListener;
import it.polimi.ingsw.observers.gameListeners.FaithPathSubject;
import it.polimi.ingsw.view.cli.Color;

import java.util.ArrayList;

public class FaithPath implements ObservableEndGame, FaithPathSubject {
    private final ArrayList<Integer> positions;
    private int numOfPlayers;
    private int currentPlayer;
    private int length;
    private int lastPActivated;
    private ArrayList<Character> vaticanReports;
    private ArrayList<Integer> victoryPoints;
    private final ArrayList<ObserverEndGame> observersEndGame;
    private FaithPathListener faithPathListener;
    private ArrayList<Integer> papalFavours;
    private final ArrayList<playerFavour> playersFavourList;
    private ArrayList<String> nicknames;


    /**
     * Main constructor
     */
    public FaithPath(int length, ArrayList<Character> vaticanReports, ArrayList<Integer> victoryPoints,ArrayList<Integer> papalFavours){
        this.length = length;
        this.vaticanReports = vaticanReports;
        this.victoryPoints = victoryPoints;
        this.papalFavours = papalFavours;
        this.lastPActivated = 0;
        this.positions = new ArrayList<>();
        this.observersEndGame = new ArrayList<>();
        this.playersFavourList = new ArrayList<>();
    }

    /**
     * Json constructor <br>
     * It should read: <br>
     * - int length <br>
     * - ArrayList<Character> vaticanReports <br>
     * - ArrayList<Integer> victoryPoints <br>
     * - ArrayList<Integer> papalFavours
     */
    public FaithPath(){
        this.lastPActivated = 0;
        this.positions = new ArrayList<>();
        this.observersEndGame = new ArrayList<>();
        this.playersFavourList = new ArrayList<>();
    }


    //FAITHPATH METHODS-------------------------------------------------------------------------------------------------
    public void setUpPositions(int numOfPlayers) {
        this.numOfPlayers = numOfPlayers;
        this.currentPlayer = 0;

        for(int i = 0; i < numOfPlayers; i++){
            playersFavourList.add(new playerFavour());
            positions.add(0);
        }

        this.nicknames = nicknames;
    }

    /**
     * Checks if current player is the first to land on a Pope Space
     */
    public boolean isFirstOnPopeSpace(int thisPlayer){
        return vaticanReports.get(positions.get(thisPlayer)) == 'P' && positions.get(thisPlayer) > lastPActivated;
    }


    /**
     * Adds the top of the papalFavour stack to every eligible player. The papalFavour is now considered used, and <br>
     * is thus  removed from the stack. To check if a player is on a Pope Space that has not yet been used, we update <br>
     * the latest used P char by updating lastPActivated.
     * @return true if there are no errors.
     */
    public boolean activatePapalFavour(int thisPlayer){
        if(papalFavours.isEmpty())
            return false;
        for(int i=0;i<numOfPlayers;i++){
            if((vaticanReports.get(positions.get(i)) == 'P' || vaticanReports.get(positions.get(i)) == 'X') && positions.get(i)>lastPActivated)
                playersFavourList.get(i).addFavour(papalFavours.get(0));
        }
        lastPActivated=positions.get(thisPlayer);
        papalFavours.remove(0);

        faithPathListener.notifyPapalFavour();

        return true;
    }

    /**
     * Returns the total amount of Victory Points the selected player has collected on the FaithPath
     * @param playerNumber is the player's ID
     */
    public int victoryPointCountFaithPath(int playerNumber){
        return(victoryPoints.get(positions.get(playerNumber))+playersFavourList.get(playerNumber).favourVictoryTotal());
    }

    //------------------------------------------------------------------------------------------------------------------


    //OBSERVER METHODS--------------------------------------------------------------------------------------------------
    /**
     * Update FaithPath so that the right amount of FAITHPOINTs is added to the current player
     * @param faithPoints qty of FAITHPOINTs
     */
    public void incrementPosition(int faithPoints) {
        for(int i = 0; i < faithPoints; i++){
            if(positions.get(currentPlayer) != length-1) {
                positions.set(currentPlayer, positions.get(currentPlayer) + 1);
                if (isFirstOnPopeSpace(currentPlayer)) {
                    activatePapalFavour(currentPlayer);
                }
                victoryConditions();
            }
        }

        faithPathListener.notifyCurrentPlayerIncrease(faithPoints, nicknames.get(currentPlayer));
    }

    /**
     * Method called when a player overflows Resources in Deposit. It updates the position of every other player by <br>
     * the param
     * @param faithPoints qty of FAITHPOINTs
     */
    public void incrementOthersPositions(int faithPoints) {
        for(int j = 0; j < faithPoints; j++) {
            for (int i = 0; i < numOfPlayers; i++) {
                if (i != currentPlayer && positions.get(i) != length - 1) {
                    positions.set(i, positions.get(i) + 1);
                }
            }
            for (int i = 0; i < numOfPlayers; i++){
                if (isFirstOnPopeSpace(i)) {
                   activatePapalFavour(i);
                }
            }
        }
        faithPathListener.notifyOthersIncrease(faithPoints, nicknames.get(currentPlayer));
        victoryConditions();
    }

    /**
     * Calls the notifyEndGame() when someone reaches the end of the FaithPath
     */
    private void victoryConditions() {
        for(int position : this.positions) {
            if (position == length-1)
                notifyEndGame();
        }

        if(positions.get(1) == length-1){
            for (ObserverEndGame observer : this.observersEndGame) {
                observer.lorenzoWon();
            }
        }

    }

    @Override
    public void notifyEndGame() {
        for (ObserverEndGame observer : this.observersEndGame) {
            observer.updateEndGame();
        }
    }

    @Override
    public void addObserver(ObserverEndGame observerEndGame) {
        observersEndGame.add(observerEndGame);
    }

    @Override
    public void addListeners(FaithPathListener faithPathListener) {
        System.out.println(faithPathListener);
        this.faithPathListener = faithPathListener;
    }

    @Override
    public void removeObserver(ObserverEndGame observerEndGame) {
        observersEndGame.remove(observerEndGame);
    }


    public String toString(ArrayList<String> nicks){
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(Color.ANSI_RED.escape()).append("FAITHPATH: \n\n").append(Color.ANSI_RESET.escape());

        int c = 0;

        for(int i=0;i<length;i++){

            if(victoryPoints.get(i) != c){
                c = victoryPoints.get(i);
                stringBuilder.append(c).append(" \t");
            }else{
                stringBuilder.append("  \t");
            }

            switch (vaticanReports.get(i)) {
                case ('E'):
                    stringBuilder.append(Color.ANSI_WHITE_FRAMED_BACKGROUND.escape());
                    break;

                case('X'):
                    stringBuilder.append(Color.ANSI_YELLOW_FRAMED_BACKGROUND.escape());
                    break;

                case('P'):
                    stringBuilder.append(Color.ANSI_RED_FRAMED_BACKGROUND.escape());
                    break;

            }

            stringBuilder.append("   ").append(Color.ANSI_RESET.escape());

            int var=0;
            for (Integer in: positions){
                if(i == in)
                    stringBuilder.append(Color.ANSI_RED.escape()).append(" ✞ ").append(Color.ANSI_RESET.escape()).append(nicks.get(var)).append("\t");
                var++;
            }
            stringBuilder.append("\n");

        }
        return stringBuilder.toString();
    }
    //------------------------------------------------------------------------------------------------------------------



    //GETTERS AND SETTERS-----------------------------------------------------------------------------------------------
    public int getLength() {
        return length;
    }

    public ArrayList<Character> getVaticanReports() {
        return vaticanReports;
    }

    public int getPositions(int i) {
        return positions.get(i);
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

    public ArrayList<playerFavour> getPlayersFavourList() {
        return playersFavourList;
    }

    public int getNumOfPlayers() {
        return numOfPlayers;
    }

    public void setNumOfPlayers(int numOfPlayers) {
        this.numOfPlayers = numOfPlayers;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public ArrayList<ObserverEndGame> getObserversEndGame() { return observersEndGame; }

    public void setNicknames(ArrayList<String> nicknames) {
        this.nicknames = nicknames;
    }
    //------------------------------------------------------------------------------------------------------------------
}



//PlayerFavour (Support class)------------------------------------------------------------------------------------------
class playerFavour{
    private ArrayList<Integer> Favours;

    public playerFavour() {
        Favours = new ArrayList<>();
    }

    public boolean addFavour(int favourAmount){
        Favours.add(favourAmount);
        return true;
    }
    public boolean isEmpty(){
        return(Favours.isEmpty());
    }

    /**
     * Sums all the integers that represent all the Favours a player has collected over the game.
     */
    public int favourVictoryTotal(){
        int c=0;
        for (int i :Favours ) {
            c=c+i;
        }
        return c;

    }

    public ArrayList<Integer> getFavours() {
            return Favours;
    }

}
