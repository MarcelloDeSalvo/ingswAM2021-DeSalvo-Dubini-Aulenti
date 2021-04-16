package Model;

import java.util.ArrayList;

public class FaithPath implements ObserverFaithPath, ObservableEndGame {
    private ArrayList<Integer> positions;
    private int numOfPlayers;
    private int currentPlayer;
    private int length;
    private int lastPActivated;
    private ArrayList<Character> vaticanReports;
    private ArrayList<Integer> victoryPoints;
    private ArrayList<ObserverEndGame> observersEndGame;
    private ArrayList<Integer> papalFavours;
    private ArrayList<playerFavour> playersFavourList;


    /**
     * Main constructor
     */
    public FaithPath(int length, ArrayList<Character> vaticanReports, ArrayList<Integer> victoryPoints,ArrayList<Integer> papalFavours){
        this.length = length;
        this.vaticanReports = vaticanReports;
        this.victoryPoints = victoryPoints;
        this.papalFavours = papalFavours;
        this.lastPActivated=0;
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
        this.lastPActivated=0;
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
    public boolean isFirstOnPopeSpace(){
        if(vaticanReports.get(positions.get(currentPlayer)) == 'P' && positions.get(currentPlayer)>lastPActivated )
            return true;
        return false;

    }

    /**
     * Adds the top of the papalFavour stack to every eligible player. The papalFavour is now considered used, and <br>
     * is thus  removed from the stack. To check if a player is on a Pope Space that has not yet been used, we update <br>
     * the latest used P char by updating lastPActivated.
     * @return true if there are no errors.
     */
    public boolean activatePapalFavour(){
        if(papalFavours.get(0)==null)
            return false;
        for(int i=0;i<numOfPlayers;i++){
            if((vaticanReports.get(positions.get(i)) == 'P' || vaticanReports.get(positions.get(i)) == 'X') && positions.get(i)>lastPActivated)
                playersFavourList.get(i).addFavour(papalFavours.get(0));
        }
        lastPActivated=positions.get(currentPlayer);
        papalFavours.remove(0);

        return true;
    }
    //------------------------------------------------------------------------------------------------------------------


    //OBSERVER METHODS--------------------------------------------------------------------------------------------------
    @Override
    public void update(int faithPoints) {
        for(int i=0;i<faithPoints;i++){
            positions.set(currentPlayer, positions.get(currentPlayer) + 1);
            if(isFirstOnPopeSpace()){
                activatePapalFavour();
            }
            victoryConditions();
        }

    }

    /**
     * Calls the notifyEndGame() when someone reaches the end of the FaithPath
     */
    private void victoryConditions() {
        for(int position : this.positions) {
            if (position == length-1)
                notifyEndGame();
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

    public ArrayList<Integer> getFavours() {
        return Favours;
    }

}
