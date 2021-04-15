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


    //MULTIPLAYER CONSTRUCTORS------------------------------------------------------------------------------------------
    /**
     * Standard rules multiplayer constructor
     * @param playersNicknames is the list of the connected player's nicknames
     */
    public void Game(ArrayList<String> playersNicknames, int numOfPlayers) throws FileNotFoundException{
        newPlayerOrder(playersNicknames);
        this.numOfPlayers = numOfPlayers;

        standard_rules_start();
    }

    /**
     * Custom rules multiplayer constructor
     * @param playersNicknames is the list of the connected player's nicknames
     * @param pyramidHeight is the initial DefaultDeposit number
     * @param prodSlotNum is the initial DevelopmentProduction slot number
     * @throws FileNotFoundException if one of the configuration files is missing or it cannot be opened
     */
    public void Game(ArrayList<String> playersNicknames, int pyramidHeight, int prodSlotNum, int numOfPlayers) throws FileNotFoundException{
        newPlayerOrder(playersNicknames,pyramidHeight,prodSlotNum);
        this.numOfPlayers = numOfPlayers;

        standard_rules_start(); //to change when custom rules methods are ready
    }
    //------------------------------------------------------------------------------------------------------------------


    //SINGLE PLAYER CONSTRUCTORS------------------------------------------------------------------------------------------
    /**
     * Standard rules single player constructor
     * @param playerNickname is the list of the connected player's nicknames
     * @throws FileNotFoundException if one of the configuration files is missing or it cannot be opened
     */
    public void Game(String playerNickname) throws FileNotFoundException{
        newSinglePlayer(playerNickname);
        this.numOfPlayers  = 2;

        standard_rules_start();
    }
    //------------------------------------------------------------------------------------------------------------------


    //GAME CREATION----------------------------------------------------------------------------------------------------

    /**
     * Starts a new Game with the default rules
     * @throws FileNotFoundException if one of the configuration files is missing or it cannot be opened
     */
    public void standard_rules_start() throws FileNotFoundException{
        newFaithPath();
        newMarket();
        newCardGrid();
        newLeaderCardList();
        distributeRandomLeadersToHands();
        setUpObserves();

        finalTurn = false;
        gameEnded = false;

        turnNumber = 0;
    }

    /**
     * starts a single player Game
     * @param nickname is the player's nickname
     */
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

    /**
     * Creates the market with the default parameters
     */
    public void newMarket() throws FileNotFoundException{
        try {
            ArrayList<ResourceContainer> marketMarbles = MarketSetUpParser.deserializeMarketElements();
            market = new Market(marketMarbles);
        }catch (FileNotFoundException e){
            throw new FileNotFoundException("MarketSetUp.json not found");
        }

    }

    /**
     * Creates the faithPath with the default parameters
     */
    public void newFaithPath() throws FileNotFoundException{
        try {
            faithPath = FaithPathSetUpParser.deserializeFaithPathSetUp(numOfPlayers);
        }catch (FileNotFoundException e){
            throw new FileNotFoundException("FaithPath.json not found");
        }

    }

    /**
     * Creates the cardGrid with the default parameters
     */
    public void newCardGrid() throws FileNotFoundException{
        try {
            cardgrid = new Cardgrid();
        }catch (FileNotFoundException e){
            throw new FileNotFoundException("DevelopmentCards.json not found");
        }

    }

    /**
     * Reads the default leaderCards' deck
     */
    public void newLeaderCardList() throws FileNotFoundException{
        try {
            leaderList = LeaderCardParser.deserializeLeaderList();
        }catch (FileNotFoundException e){
            throw new FileNotFoundException("LeaderCards.json not found");
        }
    }

    /**
     * Creates the Players and orders them randomly <br>
     * Sets the standard parameters for the players
     * @param playersNicknames are the connected players' nicknames
     */
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

    /**
     * Creates the Players and orders them randomly <br>
     * Sets the custom parameters for the players
     * @param playersNicknames are the connected players' nicknames
     * @param pyramidHeight is the custom dimension of the Player's deposits
     * @param prodSlotNum is the custom dimension of the Player's production slots number
     */
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

    /**
     * Gives 4 random leader to each player
     */
    public void distributeRandomLeadersToHands(){
        Collections.shuffle(leaderList);
        int j=0;
        for (Player p: playerList) {
            for (int i = 0; i<4; i++){
                p.addToHand(leaderList.get(j));
            }
            j++;
        }
    }

    /**
     * Sets up all the observers
     */
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
    /**
     * Ends the round and changes the current player
     */
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
