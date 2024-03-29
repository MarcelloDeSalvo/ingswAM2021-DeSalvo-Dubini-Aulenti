package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.cards.Status;
import it.polimi.ingsw.model.parser.*;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.resources.ResourceContainer;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.observers.ObservableModel;
import it.polimi.ingsw.observers.ObserverModel;
import it.polimi.ingsw.view.VirtualView;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Game implements ObserverEndGame, Game_TokensAccess, ObservableModel {

    private int numOfPlayers;

    private int currentPlayer = 0;
    private int turnNumber = 0;

    private ArrayList<Player> playerList;

    private Market market;
    private Cardgrid cardgrid;
    private FaithPath faithPath;
    private Lorenzo lorenzo;

    private ArrayList<LeaderCard> leaderCards;
    private ArrayList<DevelopmentCard> developmentCards;
    private ArrayList<ResourceContainer> marbles;

    private boolean finalTurn = false;

    private boolean gameStarted = false;
    private boolean gameEnded = false;

    private boolean singlePlayer = false;
    private boolean lorenzoWon = false;

    private ArrayList<String> winner;

    private ObserverModel view;

    /**
     * Test Constructor
     */
    public Game() throws  FileNotFoundException {
        ArrayList<String> strings = new ArrayList<>();
        strings.add("Player_1");
        strings.add("Player_2");
        strings.add("Player_3");
        strings.add("Player_4");

        standard_deck_start(4);
        newPlayerOrder(strings);

        view = new VirtualView();
        addView(view);
    }


    //MULTIPLAYER CONSTRUCTORS------------------------------------------------------------------------------------------
    /**
     * Standard rules multiplayer constructor
     * @param playersNicknames is the list of the connected player's nicknames
     */
    public Game(ArrayList<String> playersNicknames, int numOfPlayers) throws FileNotFoundException, JsonIOException, JsonSyntaxException{
        standard_deck_start(numOfPlayers);
        newPlayerOrder(playersNicknames);
    }

    /**
     * Custom rules multiplayer constructor: standard rules but different player parameters
     * @param playersNicknames is the list of the connected player's nicknames
     * @param pyramidHeight is the initial DefaultDeposit number
     * @param prodSlotNum is the initial DevelopmentProduction slot number
     * @throws FileNotFoundException if one of the configuration files is missing or it cannot be opened
     */
    public Game(ArrayList<String> playersNicknames, int pyramidHeight, int prodSlotNum, int numOfPlayers) throws FileNotFoundException, JsonIOException, JsonSyntaxException{
        standard_deck_start(numOfPlayers); //to change when custom rules methods are ready
        newPlayerOrder(playersNicknames,pyramidHeight,prodSlotNum);
    }

    /**
     * Custom rules multiplayer constructor: custom decks + different player parameters
     * @param playersNicknames is the list of the connected player's nicknames
     * @param pyramidHeight is the initial DefaultDeposit number
     * @param prodSlotNum is the initial DevelopmentProduction slot number
     * @throws FileNotFoundException if one of the configuration files is missing or it cannot be opened
     */
    public Game(ArrayList<String> playersNicknames, int pyramidHeight, int prodSlotNum, int numOfPlayers, ArrayList<String> customDecks) throws FileNotFoundException, JsonIOException, JsonSyntaxException{
        custom_deck_default_param_start(numOfPlayers, customDecks);
        newPlayerOrder(playersNicknames,pyramidHeight,prodSlotNum);
    }
    //------------------------------------------------------------------------------------------------------------------



    //SINGLE PLAYER CONSTRUCTORS----------------------------------------------------------------------------------------
    /**
     * Standard rules single player constructor
     * @param playerNickname is the list of the connected player's nicknames
     * @throws FileNotFoundException if one of the configuration files is missing or it cannot be opened
     */
    public Game(String playerNickname) throws FileNotFoundException, JsonIOException, JsonSyntaxException{
        standard_deck_start(2);
        standard_single_player_start(playerNickname);
        singlePlayer = true;
    }

    /**
     * Standard rules single player constructor used for TESTS ONLY
     * @param playerNickname is the list of the connected player's nicknames
     * @param test indicates that i want to create a single player game for a TEST
     * @throws FileNotFoundException if one of the configuration files is missing or it cannot be opened
     */
    public Game(String playerNickname, boolean test) throws FileNotFoundException, JsonIOException, JsonSyntaxException{
        standard_deck_start(2);
        test_single_player_start(playerNickname);
        singlePlayer = true;

        view = new VirtualView();
        addView(view);
        faithPath.setNicknames(getNicknames());
    }
    //------------------------------------------------------------------------------------------------------------------


    //GAME METHODS------------------------------------------------------------------------------------------------------
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
    }

    /**
     * Starts the game and the turn's counter
     */
    public void startGame(){
        gameStarted = true;
    }
    //------------------------------------------------------------------------------------------------------------------


    //MULTIPLAYER GAME CREATION-----------------------------------------------------------------------------------------
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
            playerList.add(new Player(current, i, faithPath));

            if(i > 1) {
                faithPath.getPositions().set(i, 1);
            }
            i++;
        }

        faithPath.setNicknames(playersNicknames);
        distributeRandomLeadersToHands();
        setUpObservers();
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
            playerList.add(new Player(current, pyramidHeight, prodSlotNum, i, faithPath));

            if(i > 1) {
                faithPath.getPositions().set(i, 1);
            }
            i++;
        }

        faithPath.setNicknames(playersNicknames);
        distributeRandomLeadersToHands();
        setUpObservers();
    }

    /**
     * Gives 4 random LeaderCards to each player
     */
    public void distributeRandomLeadersToHands(){
        Collections.shuffle(leaderCards);
        int j=0;
        for (Player p: playerList) {
            for (int i = 0; i<4; i++){
                leaderCards.get(j).setStatus(Status.HAND);
                p.addToHand(leaderCards.get(j));
                j++;
            }
        }
    }
    //------------------------------------------------------------------------------------------------------------------


    //SINGLE PLAYER GAME CREATION---------------------------------------------------------------------------------------
    /**
     * Starts a single player Game
     * @param nickname is the player's nickname
     */
    public void standard_single_player_start(String nickname) throws FileNotFoundException,JsonIOException, JsonSyntaxException {
        ArrayList<ActionToken> tokens = ActionTokensParser.deserializeActionTokens();
        lorenzo = new Lorenzo(tokens);
        lorenzo.shuffleActionTokens();

        playerList = new ArrayList<>();
        playerList.add(new Player(nickname, 0, faithPath));
        playerList.add(new Player("LORENZO", 1, faithPath));

        faithPath.setNicknames(getNicknames());

        currentPlayer = 0;
        setUpObservers_singlePlayer();
        distributeRandomLeadersToPlayer();
    }

    /**
     * Starts a single player Game. Used for TEST ONLY
     * @param nickname is the player's nickname
     */
    public void test_single_player_start(String nickname) throws FileNotFoundException,JsonIOException, JsonSyntaxException {
        ArrayList<ActionToken> tokens = ActionTokensParser.deserializeActionTokens();
        lorenzo = new Lorenzo(tokens, true);
        //lorenzo.shuffleActionTokens();

        playerList = new ArrayList<>();
        playerList.add(new Player(nickname, 0, faithPath));
        playerList.add(new Player("LORENZO", 1, faithPath));

        setUpObservers_singlePlayer();
        distributeRandomLeadersToPlayer();
    }

    /**
     * Gives 4 random LeaderCards to the first (and only) player
     */
    public void distributeRandomLeadersToPlayer(){
        Collections.shuffle(leaderCards);
        for (int i = 0; i<4; i++){
            leaderCards.get(i).setStatus(Status.HAND);
            playerList.get(0).addToHand(leaderCards.get(i));
        }
    }
    //------------------------------------------------------------------------------------------------------------------


    //FAITHPATH OBSERVERS-----------------------------------------------------------------------------------------------
    public void addFaithPointsToCurrentPLayer(int qty) {
        faithPath.incrementPosition(qty);
    }

    public void addFaithPointsToOtherPlayers(int qty) {
        faithPath.incrementOthersPositions(qty);
    }
    //------------------------------------------------------------------------------------------------------------------


    //TURN MANAGER------------------------------------------------------------------------------------------------------
    /**
     * Ends the round and changes the current player
     */
    public void nextTurn(){

        if (((currentPlayer+1)%numOfPlayers == 0) && isFinalTurn() && !singlePlayer){
            winnerCalculator();
            return;
        }

        if (finalTurn)
            view.notifyLastTurn();

        if(!gameEnded && gameStarted) {
            currentPlayer = (currentPlayer+1) % numOfPlayers;
            faithPath.setCurrentPlayer(currentPlayer);
            turnNumber++;
            lorenzoPickAction();
        }
    }

    /**
     * Lorenzo's turn
     */
    public void lorenzoPickAction(){
        if (singlePlayer && currentPlayer == 1){
            view.printItsYourTurn(getCurrentPlayerNick());
            lorenzo.pickAction(this);
            nextTurn();
        }
    }
    //------------------------------------------------------------------------------------------------------------------


    //PICKING THE WINNERS-----------------------------------------------------------------------------------------------
    /**
     * Ends the game, calculates the player's points and notifies who's the winner
     */
    public void winnerCalculatorSinglePlayer(){
        gameEnded = true;
        gameStarted = false;

        winner = new ArrayList<>();
        List<Integer> playersTotalVictoryPoints= new ArrayList<>();

        if (currentPlayer==1)
            lorenzoWon = true;
        else if(faithPath.getPositions(1) == faithPath.getLength()-1)
            lorenzoWon = true;

        winner.add(lorenzoWon ? playerList.get(1).getNickname() : playerList.get(0).getNickname());
        playersTotalVictoryPoints.add(calculatePlayerVictoryPoints(playerList.get(0)));

        ArrayList<String> nick = new ArrayList<>();
        nick.add(playerList.get(0).getNickname());

        view.notifyScores(playersTotalVictoryPoints, nick, winner);
        view.notifyGameEnded();
    }

    /**
     * Ends the game, calculates all the players' points and notifies who's the winner
     */
    public void winnerCalculator(){
        gameEnded = true;
        gameStarted = false;

        winner = new ArrayList<>();
        ArrayList<Player> maxPointPlayer = new ArrayList<>();
        List<Integer> playersTotalVictoryPoints = new ArrayList<>();

        int max;

        calcAllPlayerPoints(playersTotalVictoryPoints);
        max = playersTotalVictoryPoints.stream().max(Integer::compare).get();
        setWinners(maxPointPlayer, max);

        view.notifyScores(playersTotalVictoryPoints, getNicknames(), winner);
        view.notifyGameEnded();
    }

    /**
     * Calculates all the Players' points
     */
    private void calcAllPlayerPoints(List<Integer> playersTotalVictoryPoints){
        for(int i=0;i<numOfPlayers;i++){
            playersTotalVictoryPoints.add(calculatePlayerVictoryPoints(playerList.get(i)));
        }
    }

    /**
     *@return the total amount of victory points earned by a player throughout the game
     */
    private int calculatePlayerVictoryPoints(Player player){
        int total=0;
        total+=faithPath.victoryPointCountFaithPath(player.getOrderID());
        total+=player.getPlayerBoard().resourceVictoryPointsTotal();
        total+=player.activeLeadersVictoryPoints();
        total+=player.getPlayerBoard().developmentCardsVictoryPoints();
        return total;
    }

    /**
     * In case of equal points determines which player has more resources. In case of equal points and resources <br>
     * it calls the game a draw and picks the winners.
     */
    private void drawDecider(ArrayList<Player> maxPointPlayers){
        List<Integer> resourceTotal= new ArrayList<>();
        winner.clear();
        for (Player p:maxPointPlayers) {
            resourceTotal.add(p.getPlayerBoard().resourceQuantityTotal());
        }

        int max=resourceTotal.stream().max(Integer::compare).get();
        for (Player p:maxPointPlayers) {
            if(p.getPlayerBoard().resourceQuantityTotal()==max)
                winner.add(p.getNickname());
        }
    }

    /**
     * Sets the nicknames of the winners
     */
    private void setWinners(ArrayList<Player> maxPointPlayer, int max){
        int evenCounter = 0;
        for (Player p:playerList) {
            if(calculatePlayerVictoryPoints(p)==max) {
                winner.add(p.getNickname());
                evenCounter++;
                maxPointPlayer.add(p);
            }
        }

        if(evenCounter>1)
            drawDecider(maxPointPlayer);
    }
    //------------------------------------------------------------------------------------------------------------------


    //OBSERVER METHODS---(end game notify)------------------------------------------------------------------------------
    @Override
    public void updateEndGame() {
        if (singlePlayer){
            winnerCalculatorSinglePlayer();
            return;
        }

        finalTurn = true;
    }

    /**
     * Sets up all the observers for the single player
     */
    public void setUpObservers_singlePlayer(){
        lorenzo.addObserver(this);
        faithPath.addObserver(this);

        playerList.get(0).getPlayerBoard().addObserver(this);

    }

    /**
     * Sets up all the observers
     */
    public void setUpObservers(){
        faithPath.addObserver(this);

        for (Player p: playerList) {
            p.getPlayerBoard().addObserver(this);
        }

    }

    @Override
    public void addView(ObserverModel view) {
        this.view = view;

        for (Player player: playerList) {
            player.addView(view);
        }

        if(isSinglePlayer())
            lorenzo.addView(view);

        faithPath.addListeners(view);
        cardgrid.addCardGridListener(view);
    }
    //------------------------------------------------------------------------------------------------------------------


    //GETTER AND SETTER-------------------------------------------------------------------------------------------------
    public ArrayList<String> getNicknames(){
        ArrayList<String> nicknames = new ArrayList<>();

        for (Player player : playerList) {
            nicknames.add(player.getNickname());
        }

        return nicknames;
    }

    public boolean isFinalTurn() {
        return finalTurn;
    }

    public boolean isGameEnded() {
        return gameEnded;
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    public boolean isSinglePlayer() {
        return singlePlayer;
    }

    public ArrayList<Player> getPlayerList() {
        return playerList;
    }

    public Player getPlayer(int i) throws IndexOutOfBoundsException{
        if(i<0 || i>getPlayerList().size())
            return null;
        return getPlayerList().get(i);
    }

    @Override
    public Cardgrid getCardgrid() {
        return cardgrid;
    }

    @Override
    public FaithPath getFaithPath() {
        return faithPath;
    }

    @Override
    public Lorenzo getLorenzo() {
        return lorenzo;
    }

    public Market getMarket() {
        return market;
    }


    public ArrayList<LeaderCard> getLeaderCards() {
        return leaderCards;
    }

    public ArrayList<DevelopmentCard> getDevelopmentCards() {
        return developmentCards;
    }

    public ArrayList<ResourceContainer> getMarbles() {
        return marbles;
    }


    public int getOrderId(Player player){
        return player.getOrderID();
    }

    public int getNumOfPlayers() {
        return numOfPlayers;
    }

    public int getCurrentPlayerNumber() {
        return currentPlayer;
    }

    public Player getCurrentPlayer() {
        return playerList.get(currentPlayer);
    }

    public String getCurrentPlayerNick() {
        return playerList.get(currentPlayer).getNickname();
    }

    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    public ArrayList<String> getWinner() {
        return winner;
    }

    public void setWinner(ArrayList<String> winner) {
        this.winner = winner;
    }
    //------------------------------------------------------------------------------------------------------------------

}

/*
*            ________________________________________________
            /                                                \
           |    _________________________________________     |
           |   |                                         |    |
           |   |  C:\> https://youtu.be/dQw4w9WgXcQ      |    |
           |   |                                         |    |
           |   |  Open the link for free CFU :)          |    |
           |   |                                         |    |
           |   |                                         |    |
           |   |                                         |    |
           |   |                                         |    |
           |   |                                         |    |
           |   |                                         |    |
           |   |                                         |    |
           |   |                                         |    |
           |   |                                         |    |
           |   |_________________________________________|    |
           |                                                  |
            \_________________________________________________/
                   \___________________________________/
                ___________________________________________
             _-'    .-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.  --- `-_
          _-'.-.-. .---.-.-.-.-.-.-.-.-.-.-.-.-.-.-.--.  .-.-.`-_
       _-'.-.-.-. .---.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-`__`. .-.-.-.`-_
    _-'.-.-.-.-. .-----.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-----. .-.-.-.-.`-_
 _-'.-.-.-.-.-. .---.-. .-------------------------. .-.---. .---.-.-.-.`-_
:-------------------------------------------------------------------------:
`---._.-------------------------------------------------------------._.---'

* */
