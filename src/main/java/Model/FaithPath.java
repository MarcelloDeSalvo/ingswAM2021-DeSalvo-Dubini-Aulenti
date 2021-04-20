package Model;


import Model.Player.Player;

import java.util.ArrayList;

public class FaithPath implements ObserverFaithPath, ObservableEndGame {
    private final ArrayList<Integer> positions;
    private int numOfPlayers;
    private int currentPlayer;
    private int length;
    private int lastPActivated;
    private ArrayList<Character> vaticanReports;
    private ArrayList<Integer> victoryPoints;
    private final ArrayList<ObserverEndGame> observersEndGame;
    private ArrayList<Integer> papalFavours;
    private final ArrayList<playerFavour> playersFavourList;


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

        for(int i=0;i <numOfPlayers;i++){
            playersFavourList.add(new playerFavour());
            positions.add(0);
        }
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
    @Override
    public void update(int faithPoints) {
        for(int i=0;i<faithPoints;i++){
            if(positions.get(currentPlayer) != length-1) {
                positions.set(currentPlayer, positions.get(currentPlayer) + 1);
                if (isFirstOnPopeSpace(currentPlayer)) {
                    activatePapalFavour(currentPlayer);
                }
                victoryConditions();
            }
        }

    }

    @Override
    public void updateEveryOneElse(int faithPoints) {
        for(int j=0;j<faithPoints;j++) {
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
            observer.update();
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

    //------------------------------------------------------------------------------------------------------------------
}



//PlayerFavour (Support class)--------------------------------------------------------------------------------------
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
     * Sums all the integers that represent all the Favoure a player has collected over the game.
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
