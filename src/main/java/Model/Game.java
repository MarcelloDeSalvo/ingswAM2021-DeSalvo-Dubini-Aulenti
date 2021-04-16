package Model;


import Model.Cards.DevelopmentCard;
import Model.Cards.LeaderCard;
import Model.Parser.*;
import Model.Player.Player;
import Model.Resources.ResourceContainer;
import Model.Resources.ResourceType;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class Game implements ObserverEndGame, Game_TokensAccess{

    private int currentPlayer;
    private int numOfPlayers;

    public int turnNumber;

    private ArrayList<Player> playerList;

    private Market market;
    private Cardgrid cardgrid;
    private FaithPath faithPath;
    private Lorenzo lorenzo;

    private ArrayList<LeaderCard> leaderCards;
    private ArrayList<DevelopmentCard> developmentCards;
    private ArrayList<ResourceContainer> marbles;

    private boolean finalTurn;

    private boolean gameStarted;
    private boolean gameEnded;


    /**
     * Test Constructor
     */
    public Game() throws  FileNotFoundException {
        ArrayList<String> strings = new ArrayList<>();
        strings.add("Player_1");
        strings.add("Player_2");
        strings.add("Player_3");
        strings.add("Player_4");

        newPlayerOrder(strings);
        standard_deck_start(4);

    }


    //MULTIPLAYER CONSTRUCTORS------------------------------------------------------------------------------------------
    /**
     * Standard rules multiplayer constructor
     * @param playersNicknames is the list of the connected player's nicknames
     */
    public Game(ArrayList<String> playersNicknames, int numOfPlayers) throws FileNotFoundException, JsonIOException, JsonSyntaxException{
        newPlayerOrder(playersNicknames);
        standard_deck_start(numOfPlayers);

    }

    /**
     * Custom rules multiplayer constructor: standard rules but different player parameters
     * @param playersNicknames is the list of the connected player's nicknames
     * @param pyramidHeight is the initial DefaultDeposit number
     * @param prodSlotNum is the initial DevelopmentProduction slot number
     * @throws FileNotFoundException if one of the configuration files is missing or it cannot be opened
     */
    public Game(ArrayList<String> playersNicknames, int pyramidHeight, int prodSlotNum, int numOfPlayers) throws FileNotFoundException, JsonIOException, JsonSyntaxException{
        newPlayerOrder(playersNicknames,pyramidHeight,prodSlotNum);
        standard_deck_start(numOfPlayers); //to change when custom rules methods are ready

    }

    /**
     * Custom rules multiplayer constructor: custom decks + different player parameters
     * @param playersNicknames is the list of the connected player's nicknames
     * @param pyramidHeight is the initial DefaultDeposit number
     * @param prodSlotNum is the initial DevelopmentProduction slot number
     * @throws FileNotFoundException if one of the configuration files is missing or it cannot be opened
     */
    public Game(ArrayList<String> playersNicknames, int pyramidHeight, int prodSlotNum, int numOfPlayers, ArrayList<String> customDecks) throws FileNotFoundException, JsonIOException, JsonSyntaxException{
        newPlayerOrder(playersNicknames,pyramidHeight,prodSlotNum);
        custom_deck_default_param_start(numOfPlayers, customDecks);

    }
    //------------------------------------------------------------------------------------------------------------------


    //SINGLE PLAYER CONSTRUCTORS------------------------------------------------------------------------------------------
    /**
     * Standard rules single player constructor
     * @param playerNickname is the list of the connected player's nicknames
     * @throws FileNotFoundException if one of the configuration files is missing or it cannot be opened
     */
    public Game(String playerNickname) throws FileNotFoundException, JsonIOException, JsonSyntaxException{
        standard_single_player_start(playerNickname);
        standard_deck_start(2);

    }
    //------------------------------------------------------------------------------------------------------------------


    //GAME CREATION----------------------------------------------------------------------------------------------------
    /**
     * Starts a new Game with the default decks and parameters
     * @throws FileNotFoundException if the file cannot be opened or it's missing
     * @throws JsonSyntaxException if the file contains some syntax errors;
     * @throws JsonIOException if the file cannot be read by Json
     */
    public void standard_deck_start(int numOfPlayers) throws FileNotFoundException, JsonIOException, JsonSyntaxException{
        this.numOfPlayers = numOfPlayers;

        leaderCards = LeaderCardParser.deserializeLeaderList();
        developmentCards = DevelopmentCardParser.deserializeDevelopmentList();
        marbles = MarketSetUpParser.deserializeMarketElements();

        faithPath = FaithPathSetUpParser.deserializeFaithPathSetUp();
        faithPath.setUpPositions(numOfPlayers);

        cardgrid = new Cardgrid(developmentCards);
        market = new Market(marbles);

        distributeRandomLeadersToHands();
        setUpObserves();

        finalTurn = false;
        gameEnded = false;

        turnNumber = 0;
    }

    /**
     * Starts a new Game with custom decks and default parameters
     * @throws FileNotFoundException if the file cannot be opened or it's missing
     * @throws JsonSyntaxException if the file contains some syntax errors;
     * @throws JsonIOException if the file cannot be read by Json
     */
    public void custom_deck_default_param_start(int numOfPlayers, ArrayList<String> customFiles) throws FileNotFoundException, JsonIOException, JsonSyntaxException{
        this.numOfPlayers = numOfPlayers;

        leaderCards = LeaderCardParser.deserializeLeaderList(customFiles.get(0));
        developmentCards = DevelopmentCardParser.deserializeDevelopmentList(customFiles.get(1));
        marbles = MarketSetUpParser.deserializeMarketElements(customFiles.get(2));

        faithPath = FaithPathSetUpParser.deserializeFaithPathSetUp(customFiles.get(3));
        faithPath.setUpPositions(numOfPlayers);

        market = new Market(marbles);
        cardgrid = new Cardgrid(developmentCards);

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
    public void standard_single_player_start(String nickname) throws FileNotFoundException,JsonIOException, JsonSyntaxException {
        ArrayList<ActionToken> tokens = ActionTokensParser.deserializeActionTokens();
        lorenzo = new Lorenzo(tokens);
        lorenzo.shuffleActionTokens();

        playerList = new ArrayList<>();
        playerList.add(new Player(nickname,0));

        setUpObservers_singlePlayer();

        finalTurn = false;
        gameEnded = false;

        turnNumber = 0;
        currentPlayer = 0;
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
        Collections.shuffle(leaderCards);
        int j=0;
        for (Player p: playerList) {
            for (int i = 0; i<4; i++){
                p.addToHand(leaderCards.get(j));
                j++;
            }
        }
    }

    /**
     * Sets up all the observers for the single player
     */
    public void setUpObservers_singlePlayer(){
        lorenzo.addObserver(this);
        setUpObserves();
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
        if (currentPlayer++==1 && finalTurn){
            gameEnded = true;
            gameStarted = false;
        }


        if(!gameEnded && gameStarted) {
            currentPlayer = (currentPlayer++) % numOfPlayers;
            faithPath.setCurrentPlayer(currentPlayer);
            turnNumber++;
        }
    }

    public void startGame(){
        gameStarted = true;
    }
    //-----------------------------------------------------------------------------------------------------------------


    //OBSERVER METHODS---(end game notify)-----------------------------------------------------------------------------
    @Override
    public void update() {
        finalTurn = true;
    }
    //-----------------------------------------------------------------------------------------------------------------


    //GETTER AND SETTER------------------------------------------------------------------------------------------------
    public boolean isFinalTurn() {
        return finalTurn;
    }

    public boolean isGameEnded() {
        return gameEnded;
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    public void setGameStarted(boolean gameStarted) {
        this.gameStarted = gameStarted;
    }

    public ArrayList<Player> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(ArrayList<Player> playerList) {
        this.playerList = playerList;
    }

    public Market getMarket() {
        return market;
    }

    public void setMarket(Market market) {
        this.market = market;
    }

    @Override
    public Cardgrid getCardgrid() {
        return cardgrid;
    }

    public void setCardgrid(Cardgrid cardgrid) {
        this.cardgrid = cardgrid;
    }

    @Override
    public FaithPath getFaithPath() {
        return faithPath;
    }

    public void setFaithPath(FaithPath faithPath) {
        this.faithPath = faithPath;
    }

    @Override
    public Lorenzo getLorenzo() {
        return lorenzo;
    }

    public void setLorenzo(Lorenzo lorenzo) {
        this.lorenzo = lorenzo;
    }

    public ArrayList<LeaderCard> getLeaderCards() {
        return leaderCards;
    }

    public void setLeaderCards(ArrayList<LeaderCard> leaderCards) {
        this.leaderCards = leaderCards;
    }

    public ArrayList<DevelopmentCard> getDevelopmentCards() {
        return developmentCards;
    }

    public void setDevelopmentCards(ArrayList<DevelopmentCard> developmentCards) {
        this.developmentCards = developmentCards;
    }

    public ArrayList<ResourceContainer> getMarbles() {
        return marbles;
    }

    public void setMarbles(ArrayList<ResourceContainer> marbles) {
        this.marbles = marbles;
    }

    public int getOrderId(Player player){
        return player.getOrderID();
    }

    public int getNumOfPlayers() {
        return numOfPlayers;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public  Player getPlayer(int i){
        if(i<0 && i>getPlayerList().size())
            return null;
        return getPlayerList().get(i);
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    //-----------------------------------------------------------------------------------------------------------------

}
