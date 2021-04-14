package Model;


import Model.Cards.LeaderCard;
import Model.Parser.FaithPathSetUpParser;
import Model.Parser.LeaderCardParser;
import Model.Parser.MarketSetUpParser;
import Model.Player.Player;
import Model.Resources.ResourceContainer;
import Model.Resources.ResourceType;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class Game implements ObserverEndGame{

    public static int currentPlayer;
    public int turnNumber;

    private int numOfPlayers;
    private ArrayList<Player> playerList;

    private Market market;
    private Cardgrid cardgrid;
    private FaithPath faithPath;
    private ArrayList<LeaderCard> leaderList;

    private boolean finalTurn;
    private boolean gameEnded;


    /**
     * Test Constructor
     * @throws FileNotFoundException
     */
    public void Game() throws  FileNotFoundException {
        playerList = new ArrayList<>();
        for (int i= 0; i<4; i++){
            playerList.add(new Player("default",3,3, i ));
        }

        ArrayList<ResourceContainer> marketMarbles = MarketSetUpParser.deserializeMarketElements();
        market = new Market(marketMarbles);
        cardgrid = new Cardgrid();

        currentPlayer = 0;

        finalTurn = false;
        gameEnded = false;
    }

    /**
     * Standard rules multiplayer constructor
     * @param playersNicknames is the list of the connected player's nicknames
     * @throws FileNotFoundException
     */
    public void Game(ArrayList<String> playersNicknames, int numOfPlayers) throws FileNotFoundException{
        newPlayerOrder(playersNicknames);
        this.numOfPlayers = numOfPlayers;

        standard_rules_start();
    }

    /**
     * Custom rule multiplayer constructor
     * @param playersNicknames is the list of the connected player's nicknames
     * @param pyramidHeight is the initial DefaultDeposit number
     * @param prodSlotNum is the initial DevelopmentProduction slot number
     * @throws FileNotFoundException
     */
    public void Game(ArrayList<String> playersNicknames, int pyramidHeight, int prodSlotNum, int numOfPlayers) throws FileNotFoundException{
        newPlayerOrder(playersNicknames,pyramidHeight,prodSlotNum);
        this.numOfPlayers = numOfPlayers;

        standard_rules_start(); //to change when custom rules methods are ready
    }

    /**
     * Standard rules single player constructor
     * @param playerNickname is the list of the connected player's nicknames
     * @throws FileNotFoundException
     */
    public void Game(String playerNickname) throws FileNotFoundException{
        newSinglePlayer(playerNickname);
        this.numOfPlayers  = 2;

        standard_rules_start();
    }


    //GAME CREATION----------------------------------------------------------------------------------------------------
    public void standard_rules_start() throws FileNotFoundException{
        newFaithPath();
        newMarket();
        newCardGrid();
        newLeaderCardList();
        setUpObserves();

        finalTurn = false;
        gameEnded = false;

        turnNumber = 0;
    }

    public void newSinglePlayer(String nickname){
        Lorenzo lorenzo = new Lorenzo();
        playerList = new ArrayList<>();
        playerList.add(new Player(nickname,0));
        setUpObserves();

        finalTurn = false;
        gameEnded = false;

        turnNumber = 0;
        currentPlayer = 0;
    }

    public void newMarket() throws FileNotFoundException{
        try {
            ArrayList<ResourceContainer> marketMarbles = MarketSetUpParser.deserializeMarketElements();
            market = new Market(marketMarbles);
        }catch (FileNotFoundException e){
            throw new FileNotFoundException("MarketSetUp.json not found");
        }

    }

    public void newFaithPath() throws FileNotFoundException{
        try {
            faithPath = FaithPathSetUpParser.deserializeFaithPathSetUp(numOfPlayers);
        }catch (FileNotFoundException e){
            throw new FileNotFoundException("FaithPath.json not found");
        }

    }

    public void newCardGrid() throws FileNotFoundException{
        try {
            cardgrid = new Cardgrid();
        }catch (FileNotFoundException e){
            throw new FileNotFoundException("DevelopmentCards.json not found");
        }

    }

    public void newLeaderCardList() throws FileNotFoundException{
        try {
            leaderList = LeaderCardParser.deserializeLeaderList();
        }catch (FileNotFoundException e){
            throw new FileNotFoundException("LeaderCards.json not found");
        }
    }

    public void newPlayerOrder(ArrayList<String> playersNicknames){
        Collections.shuffle(playersNicknames);
        playerList = new ArrayList<>();

        int i = 0;
        Iterator<String> iter = playersNicknames.iterator();
        String current;
        while(iter.hasNext()){
            current=iter.next();
            playerList.add(new Player(current,i));
            i++;
        }

        currentPlayer = 0;
    }

    public void newPlayerOrder(ArrayList<String> playersNicknames, int pyramidHeight, int prodSlotNum){
        Collections.shuffle(playersNicknames);
        playerList = new ArrayList<>();
        int i = 0;
        Iterator<String> iter = playersNicknames.iterator();
        String current;
        while(iter.hasNext()){
            current=iter.next();
            playerList.add(new Player(current,pyramidHeight,prodSlotNum,i));
            i++;
        }

        currentPlayer = 0;
    }

    public void setUpObserves(){
        faithPath.addObserver(this);

        for (Player p: playerList) {
            p.getPlayerBoard().addObserver(this);
        }

        ResourceContainer resourceContainer = new ResourceContainer(ResourceType.BLANK,1);
        resourceContainer.addObserver(faithPath);
    }
    //-----------------------------------------------------------------------------------------------------------------


    //GAME METHODS-----------------------------------------------------------------------------------------------------
    public void nextTurn(){
        if (currentPlayer++==1 && finalTurn)
               gameEnded = true;

        if(!gameEnded) {
            currentPlayer = (currentPlayer++) % numOfPlayers;
            turnNumber++;
        }
    }
    //-----------------------------------------------------------------------------------------------------------------


    //OBSERVER METHODS---(end game notify)-----------------------------------------------------------------------------
    @Override
    public void update() {
        finalTurn = true;
    }
    //-----------------------------------------------------------------------------------------------------------------


    //GETTER AND SETTER------------------------------------------------------------------------------------------------
    public boolean isThisTheFinalTurn() {
        return finalTurn;
    }

    public boolean isTheGameEnded() {
        return finalTurn;
    }

    public int getOrderId(Player player){
        return player.getOrderID();
    }

    public static int getCurrentPlayer() {
        return currentPlayer;
    }

    public int getTurnNumber() {
        return turnNumber;
    }
    //-----------------------------------------------------------------------------------------------------------------

}
