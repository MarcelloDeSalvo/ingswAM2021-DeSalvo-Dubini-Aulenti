package it.polimi.ingsw.controller;

import com.google.gson.Gson;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.model.player.ConversionMode;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerStatus;
import it.polimi.ingsw.model.player.deposit.DepositSlot;
import it.polimi.ingsw.model.resources.ResourceContainer;
import it.polimi.ingsw.network.commands.*;
import it.polimi.ingsw.network.server.User;
import it.polimi.ingsw.observers.ObserverController;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.VirtualView;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

public class Controller implements ObserverController {

    private final VirtualView view; //TO CHANGE IN VIEW AFTER WE FINISH TO IMPLEMENT ALL THE PRINTINGS
    private Game game;
    private final Gson gson;

    //BUFFERS ----------------------------------------------------------------------------------------------------------
    private ArrayList<ResourceContainer> marketOut;
    private int marketOutCont = 0;

    private DevelopmentCard newDevelopmentCard;
    private int productionSlotId;
    //------------------------------------------------------------------------------------------------------------------


    public Controller (HashMap<String, User> connectedPlayers){
        this.view = new VirtualView(connectedPlayers);
        view.addObserverController(this);
        gson = new Gson();

        ArrayList<String> playersNicknames = new ArrayList<>(connectedPlayers.keySet());
        int numOfPlayers = playersNicknames.size();

        try {
            if(numOfPlayers > 1) {
                game = new Game(playersNicknames, numOfPlayers);    //MULTIPLAYER
                game.addView(view);

                view.printOrder(playersNicknames);

                for (Player player : game.getPlayerList()) {
                    view.printHand(player.getHandIDs(), player.getNickname());
                    view.printLeaderCardRequest(player.getNickname());
                }
            }
            else {
                game = new Game(playersNicknames.get(0));   //SINGLE PLAYER
                game.addView(view);
                view.printReply_uni("SINGLE PLAYER MODE", playersNicknames.get(0));
                view.printHand(game.getPlayer(0).getHandIDs(), game.getPlayer(0).getNickname());
                view.printLeaderCardRequest(game.getPlayer(0).getNickname());
            }

        }catch (FileNotFoundException e){
            e.printStackTrace();
            view.printReply("Cannot read the configuration file of the game");
        }
    }

    @Override
    public void update(String mex) {

        Message deserializedMex = gson.fromJson(mex, Message.class);
        Command command = deserializedMex.getCommand();
        String senderNick = deserializedMex.getSenderNickname();

        if (!game.isGameStarted()){
            setUp_Commands(mex, senderNick, command);
            return;
        }

        if (!isTheCurrentPlayer(senderNick))
            return;

        turnPhase_Commands(mex, senderNick, command);
    }


    /**
     * This function manages all the commands that can be sent during the set-up phase
     * @param mex is the message received
     * @param senderNick is the player that sent the message
     * @param command is the command that the user typed
     */
    private void setUp_Commands(String mex, String senderNick, Command command){

        int playerNumber = game.getPlayerListString().indexOf(senderNick);

        switch (command){

            case DISCARD_LEADER:
                IdMessage idMessage = gson.fromJson(mex, IdMessage.class);

                if(game.getPlayer(playerNumber).isLeadersHaveBeenDiscarded()){
                    view.printReply_uni("You can't do this action because you already discarded 2 Leaders! Please wait for the other players to to so", senderNick);
                    return;
                }

                if (!game.getPlayer(playerNumber).discardFromHand(idMessage.getId())) {
                    view.printReply_uni("Wrong Leader ID", senderNick);
                    return;
                }

                view.printReply_uni("Leader Discarded!", senderNick);

                if(game.getPlayer(playerNumber).isLeadersHaveBeenDiscarded()){
                    view.printReply_uni("You discarded 2 Leaders!" , senderNick);
                    if (!game.isSinglePlayer())
                        view.printReply_uni("Please wait the other players to do so" , senderNick);
                }


                if(checkIfAllLeadersHaveBeenDiscarded() && !game.isSinglePlayer())
                    askForResources();


                else if(checkIfAllLeadersHaveBeenDiscarded())   //IF SINGLE PLAYER THE GAME CAN BE STARTED IMMEDIATELY
                    startGame();

                break;


            case SETUP_CONTAINER:
                if(!checkIfAllLeadersHaveBeenDiscarded() && !game.isSinglePlayer()){
                    view.printReply_uni("Please discard 2 the leader first or wait the other players to do so ", senderNick);
                    return;
                }

                SendContainer sendContainer1 =  gson.fromJson(mex, SendContainer.class);
                addSetUpContainerToPlayer(playerNumber, sendContainer1.getContainer(), sendContainer1.getDestinationID(), senderNick);
                break;

            default:
                view.printReply_uni("Please wait for the set-up phase to end!", senderNick);
                break;
        }
    }


    /**
     * This function manages all the commands that can be sent during the turn phase
     * @param mex is the message received
     * @param senderNick is the player that sent the message
     * @param command is the command that the user typed
     */
    private void turnPhase_Commands(String mex, String senderNick, Command command){
        Player currPlayer = game.getCurrentPlayer();

        if (currPlayer.getPlayerStatus() == PlayerStatus.SELECTING_DESTINATION_AFTER_MARKET){
            selectDestinationAfterMarket( mex, senderNick, command);
            return;
        }

        if (currPlayer.getPlayerStatus() == PlayerStatus.SELECTING_BUY_RESOURCES){
            selectResources(mex, senderNick, command, currPlayer);
            return;
        }


        switch (command){

            case BUY:
                BuyMessage buyMessage = gson.fromJson(mex, BuyMessage.class);

                if(!checkBuy(buyMessage.getRow(), buyMessage.getColumn(), buyMessage.getProductionSlotID(), senderNick, currPlayer))
                    return;

                currPlayer.setPlayerStatus(PlayerStatus.SELECTING_BUY_RESOURCES);

                break;

            case PICK_FROM_MARKET:
                pickFromMarket(mex, senderNick);
                break;

            case SHOW_HAND:
                view.printHand(currPlayer.leaderListToInt(), senderNick);
                break;

            case SHOW_DEPOSIT:
                view.printDeposit(game.getCurrentPlayer().getPlayerBoard().getDeposit(), senderNick);
                break;

            case SHOW_VAULT:
                view.printVault(game.getCurrentPlayer().getPlayerBoard().getVault(), senderNick);
                break;

            case SHOW_PRODUCTION:
                view.printProduction(game.getCurrentPlayer().getPlayerBoard().getProductionSite(), senderNick);
                break;

            case SHOW_MAKET:
                view.printMarket(game.getMarket(), senderNick);
                break;

            case SHOW_CARDGRID:
                view.printCardGrid(game.getCardgrid(), senderNick);
                break;

            case SHOW_FAITHPATH:
                view.printFaithPath(game.getFaithPath(),senderNick,game.getPlayerListString());
                break;

            case END_TURN:
                //if (ha eseguitoalmeno  una azione primaria )
                game.nextTurn();
                view.printItsYourTurn(game.getCurrentPlayerNick());
                break;

            default:
                view.printReply_uni("Invalid command", senderNick);
                break;
        }
    }


    /**
     * Checks if "nick" is the current player
     */
    public boolean isTheCurrentPlayer(String nick) {
        if (!game.getCurrentPlayerNick().equals(nick)){
            view.printReply_uni("It's not your turn", nick);
            return false;
        }
        return true;
    }


    //GAME SETUP PHASE---------------------------------------------------------------------------------------------------------------GAME SETUP PHASE----#
    /**
     * Check if every player in game has already discarded 2 LeaderCards
     */
    public boolean checkIfAllLeadersHaveBeenDiscarded() {
        if(game.isSinglePlayer()) {
            return game.getPlayer(0).isLeadersHaveBeenDiscarded();
        }

        for (Player player : game.getPlayerList()) {
            if(!player.isLeadersHaveBeenDiscarded())
                return false;
        }
        return true;
    }


    /**
     * Used for sending messages about the gameSetUpPhase to every player
     */
    private void askForResources() {

        if (game.getNumOfPlayers() > 2 )
            view.notifyFaithPathProgression( 1,game.getPlayerListString().get(2));

        if (game.getNumOfPlayers() > 3)
            view.notifyFaithPathProgression( 1, game.getPlayerListString().get(3));

        for (Player player : game.getPlayerList()) {

            switch (player.getOrderID()) {
                case 0:
                    player.setReady(true);
                    view.printReply_uni("Please wait for the other players to select their bonus resources", player.getNickname());
                    break;

                case 3:
                    view.askForResources(player.getNickname(), 2);
                    break;

                default:
                    view.askForResources(player.getNickname(), 1);
                    break;
            }
        }
    }


    /**
     * Checks if the current player can add a container to his deposit and notifies the Client by sending specific messages thru the Virtual View <br>
     * CurrPlayerNum is used to do different actions following the game rules, if a player has the right to select bonus resources <br>
     * "addContainerToPlayer" method is called. <br>
     * If every player in game is ready (meaning that they already did their set up actions) the game is actually started by calling "startGame" method
     * @param currPlayerNum current player number
     * @param container container to add
     * @param depositSlotID id of the deposit slot
     * @param currNickname player's nickname
     */
    public void addSetUpContainerToPlayer(int currPlayerNum, ResourceContainer container, int depositSlotID, String currNickname) {
        Player currPlayer = game.getPlayer(currPlayerNum);

        switch (currPlayerNum) {
            case 0:
                view.printReply_uni("You are the first player so you cannot select a bonus resource!", currNickname);
                break;

            case 3:
                if(currPlayer.getSelectedResources() == 2) {
                    view.printReply_uni("You already selected your bonus resources! Please wait for the other players to do so", currNickname);
                    break;
                }

                if(addContainerToPlayer(currPlayerNum, container, depositSlotID, currNickname))
                    currPlayer.incrementSelectedResources();

                break;

            default:
                if(currPlayer.getSelectedResources() == 1) {
                    view.printReply_uni("You already selected your bonus resource! Please wait for the other players to do so", currNickname);
                    break;
                }

                if(addContainerToPlayer(currPlayerNum, container, depositSlotID, currNickname))
                    currPlayer.incrementSelectedResources();

                break;
        }

        for (Player player : game.getPlayerList()) {
            if(!player.isReady())
                return;
        }

        if(!game.isGameStarted())
            startGame();
    }


    /**
     * Adds to currPlayer a ResourceContainer in a specific depositSlotID after checking that everything goes right.
     * This method also notifies the Client by sending specific messages thru the Virtual View
     * @param currPlayerNum current player number
     * @param container container to add
     * @param depositSlotID id of the deposit slot
     * @param currNickname player's nickname
     * @return true if everything goes right, false if an exception is caught
     */
    public boolean addContainerToPlayer(int currPlayerNum, ResourceContainer container, int depositSlotID, String currNickname) {

        try {
            game.getPlayer(currPlayerNum).getDepositSlotByID(depositSlotID).canAddToDepositSlot(container);

            game.getPlayer(currPlayerNum).getDepositSlotByID(depositSlotID).addToDepositSlot(container);

            view.printReply_uni(container.getQty() + " " + container.getResourceType().toString() + " has been added to your deposit slot N: " + depositSlotID, currNickname);

            return true;

        } catch (DifferentResourceType | DepositSlotMaxDimExceeded | ResourceTypeAlreadyStored exception) {
            view.printReply_uni(exception.getMessage(), currNickname);
            return false;

        } catch (IndexOutOfBoundsException indexOutOfBoundsException){
            view.printReply_uni("The selected Deposit Slot does not exists!", currNickname);
            return false;

        }
    }


    /**
     * Starts the turn phase
     */
    private void startGame() {
        game.startGame();
        view.printReply("---THE GAME HAS BEEN STARTED---\n\t--HAVE FUN--");
        view.printItsYourTurn(game.getCurrentPlayerNick());
        //PRINT VARIE DI TUTTE LE PORCHERIE
    }
    //------------------------------------------------------------------------------------------------------------------/


    //BUY PHASE ---------------------------------------------------------------------------------------------------------------------BUY PHASE---------#
    /**
     * Checks if the user has enough resources in total before asking him to type where does he want to
     * @param row is the selected row number
     * @param column is the selected column number
     * @param id is the ID of the card on the row/column position on the grid
     * @param senderNick is the player's nickname that wants to buy the card
     * @return true if he has selected a valid number for the row and column
     */
    private boolean checkBuy(int row, int column, int id, String senderNick, Player currPlayer) {

        try {
            DevelopmentCard selectedCard = game.getCardgrid().getDevelopmentCardOnTop(row, column);

            System.out.println(selectedCard.toString());

            if(!currPlayer.hasEnoughResources(selectedCard.getPrice())) {
                view.printReply_uni("You don't have enough resources to buy this Development Card!", senderNick);
                return false;
            }

            if(!currPlayer.getProductionSlotByID(id).canInsertOnTop(selectedCard)){
                view.printReply_uni("You can't insert the this card in the selected Production Slot!", senderNick);
                return false;
            }

            newDevelopmentCard = selectedCard;
            this.productionSlotId = id;

            view.printReply_uni("Please select resources as a payment by typing > GIVE Qty ResourceType 'FROM' ('DEPOSIT' DepositID) or ('VAULT') ", senderNick);
            return true;

        } catch (InvalidColumnNumber | InvalidRowNumber exception) {
            view.printReply_uni(exception.getMessage(), senderNick);
            return false;

        }catch (IndexOutOfBoundsException exception){
            view.printReply_uni("The selected Production Slot does not exists!", senderNick);
            return false;

        }
    }

    /**
     * When the player is in PlayerStatus.SELECTING_BUY_RESOURCES the method is used to receive the resources selected and checking <br>
     * if the removing of the container goes right.
     * When the command 'DONE' is received calls buyDevelopmentCard() method
     * @param mex message received
     * @param senderNick current player nickname
     * @param command command received
     * @param currPlayer the current player
     */
    private void selectResources(String mex, String senderNick, Command command, Player currPlayer) {

        if(command == Command.SEND_CONTAINER) {
            SendContainer sendContainer =  gson.fromJson(mex, SendContainer.class);
            System.out.println("Arrived: " + sendContainer);

            if(!removeContainer(sendContainer.getContainer(), sendContainer.getDestination(), sendContainer.getDestinationID(), senderNick, currPlayer))
                return;
        }

        if(command == Command.DONE) {
            if(currPlayer.getPlayerStatus() == PlayerStatus.SELECTING_BUY_RESOURCES) {
                buyDevelopmentCard(senderNick, currPlayer);
                currPlayer.setPlayerStatus(PlayerStatus.IDLE);
                return;
            }
        }

        view.printReply_uni("Please keep selecting the resources or type 'DONE'", senderNick);
    }

    /**
     * Checks if the curr player owns enough resources to remove 'resourceContainer'.
     * Then if the 'destination' is:
     *  - 'vault', vault's buffer gets filled
     *  - 'deposit', the specific deposit id's buffer gets filled
     * This method also notifies the view on what happens using specific messages
     * @param resourceContainer the container that i want to remove (use as a payment)
     * @param destination 'deposit' or 'vault'
     * @param destinationID the
     * @param senderNick current player nickname
     * @param currPlayer the current player
     * @return true if the container is correctly removed, false otherwise
     */
    private boolean removeContainer(ResourceContainer resourceContainer, String destination, int destinationID, String senderNick, Player currPlayer){

        if(!currPlayer.hasEnoughResources(resourceContainer)) {
            view.printReply_uni("You don't own the selected resources!", senderNick);
            return false;
        }

        if(destination.equalsIgnoreCase("VAULT")) {
            try {
                currPlayer.canRemoveFromVault(resourceContainer);

                view.printReply_uni("Resources accepted!", senderNick);

                return true;

            } catch (NotEnoughResources exception) {

                view.printReply_uni(exception.getMessage(), senderNick);

                return false;
            }
        }

        if(destination.equalsIgnoreCase("DEPOSIT")) {
            try {
                currPlayer.getDepositSlotByID(destinationID).canRemoveFromDepositSlot(resourceContainer);

                System.out.println("Buffer container: " + currPlayer.getDepositSlotByID(destinationID).getBufferContainer().toString());

                view.printReply_uni("Resources accepted!", senderNick);

                return true;

            } catch (DifferentResourceType | NotEnoughResources exception) {
                view.printReply_uni(exception.getMessage(), senderNick);
                return false;

            } catch (IndexOutOfBoundsException indexOutOfBoundsException){
                view.printReply_uni("The selected Deposit Slot does not exists!", senderNick);
                return false;

            }
        }

        return false;
    }

    /**
     * Used to actually buy the Development Card but only after checking with the method 'canBuy()' if you can do it.
     * If the card is correctly bought it is inserted in a specific 'productionSlotId' and removed from the Cardgrid
     * This method also notifies the view on what happens using specific messages
     * @param senderNick current player nickname
     * @param currPlayer the current player
     * @return true if the card is correctly bought, false otherwise
     */
    private boolean buyDevelopmentCard(String senderNick, Player currPlayer) {

        if(!currPlayer.canBuy(newDevelopmentCard)) {
            view.printReply_uni("The resources you selected aren't correct!", senderNick);
            view.printTurnHelp(senderNick);
            currPlayer.emptyBuffers();
            return false;
        }

        currPlayer.buy();
        currPlayer.insertBoughtCardOn(productionSlotId, newDevelopmentCard);
        game.getCardgrid().removeDevelopmentCard(newDevelopmentCard.getId());

        newDevelopmentCard = null;
        productionSlotId = -1;

        view.printReply_uni("You bought the card correctly!", senderNick);
        view.printProduction(currPlayer.getProductionSite(), senderNick);
        return true;
    }
    //------------------------------------------------------------------------------------------------------------------/


    //MARKET ACTION CONTROLLER-------------------------------------------------------------------------------------------------------PICK FROM MARKET--#
    /**
     * Manages the user action 'Pick From Market' :<br>
     * - Checks if the user selected a valid row/column number <br>
     * - Checks if the user has one or more active conversion <br>
     * - Sets the playerStatus to SELECTING_DESTINATION_AFTER_MARKET in order to force him to sends only certain commands
     */
    public void pickFromMarket(String mex, String senderNick){
        MarketMessage marketMessage = gson.fromJson(mex, MarketMessage.class);

        try {
            marketOut = game.getMarket().getRowOrColumn(marketMessage.getSelection(),marketMessage.getNum());

        }catch (InvalidColumnNumber | InvalidRowNumber e ){
            view.printReply_uni(e.getMessage(), senderNick);
            return;
        }

        if(game.getCurrentPlayer().canConvert() == ConversionMode.CHOICE_REQUIRED) {
            view.printReply_uni("You have multiple leaders with the conversion ability, please select which one do you want to use for each blank marble by typing one of the active conversion available ", senderNick);
            game.getCurrentPlayer().setPlayerStatus(PlayerStatus.SELECTING_CONVERSION);
            return;
        }

        if(game.getCurrentPlayer().canConvert() == ConversionMode.INACTIVE) {
            view.printReply_uni("\n\nNow select where do you want to place them by typing >DEPOSIT deposit_id", senderNick);
        }

        if(game.getCurrentPlayer().canConvert() == ConversionMode.AUTOMATIC) {
            game.getCurrentPlayer().convert(marketOut);

            view.printReply_uni("All blank marbles have been converted to " + game.getCurrentPlayer().getConversionSite().getConversionsAvailable().get(0) + " : " + marketOut.toString() +
                    "\n\nNow select where do you want to place them them by typing >DEPOSIT deposit_id", senderNick);
        }

        game.getCurrentPlayer().setPlayerStatus(PlayerStatus.SELECTING_DESTINATION_AFTER_MARKET);
        marketAddDepositController(senderNick);
    }

    /**
     * Manages the sending of resources to the deposits after taking them from the market:<br>
     * -At the beginning scrolls the array until an addable resource is found (canAddToDeposit==true) <br>
     * -If there is at least one addable resource it asks the user where does he want to put her <br>
     * -When the users
     */
    public void marketAddDepositController(String senderNick){
        increaseMarketOut_NotAddableResources();

        if (marketOutCont < marketOut.size()){
            view.printReply_uni("Where do you want to put " + marketOut.get(marketOutCont).getResourceType().toString(), senderNick);
            return;
        }

        view.printTurnHelp(senderNick);
        game.getCurrentPlayer().setPlayerStatus(PlayerStatus.IDLE);
        marketOut = null;
        marketOutCont = 0;
    }


    /**
     * Checks if the user typed the SEND_DEPOSIT_ID command that contains the infos about the deposit in which he wants to place the resource
     */
    public void selectDestinationAfterMarket(String mex, String senderNick, Command command) {

        if (command != Command.SEND_DEPOSIT_ID){
            view.printReply_uni("Please select a depositID", senderNick);
            return ;
        }

        IdMessage idMessage = gson.fromJson(mex, IdMessage.class);

        try {
            if(!mustDiscardCheck(senderNick)){
                game.getCurrentPlayer().getDepositSlotByID(idMessage.getId()).canAddToDepositSlot(marketOut.get(marketOutCont));
                game.getCurrentPlayer().getDepositSlotByID(idMessage.getId()).addToDepositSlot(marketOut.get(marketOutCont));
            }

        }catch (DifferentResourceType | DepositSlotMaxDimExceeded | ResourceTypeAlreadyStored  e){
            view.printReply_uni(e.getMessage(), senderNick);
            return ;

        }catch (IndexOutOfBoundsException exception){
            view.printReply_uni("The selected Deposit Slot does not exists!", senderNick);
            return;
        }

        marketOutCont++;
        marketAddDepositController(senderNick);

    }

    /**
     * Scrolls the marketOutput array and skips the resources that cannot be added to a deposit
     */
    public void increaseMarketOut_NotAddableResources(){
        if (marketOutCont==marketOut.size())
            return;

       while (!marketOut.get(marketOutCont).getResourceType().canAddToDeposit()){
           if (marketOut.get(marketOutCont).getResourceType().canAddToFaithPath())
               incPosOfCurrentPlayer(1);
           marketOutCont++;
           if (marketOutCont==marketOut.size())
               break;
       }

    }

    /**
     * Checks automatically if the user has no place where to put the resource and force him to discard her if there aren't any
     * @return true if he must discard the current resource on the marketOutput array
     */
    public boolean mustDiscardCheck(String senderNick) {
        boolean canAdd = false;
        for (DepositSlot depositSlot: game.getCurrentPlayer().getDeposit().getDepositList()) {
            if (depositSlot.simpleCanAddToDepositSlot(marketOut.get(marketOutCont)))
                canAdd = true;
        }

        if (!canAdd){
            view.printReply_uni("You cant add this resource anywhere, it will be discarded..", senderNick);
            incPosOfOthers(1);
        }

        return !canAdd;
    }
    //------------------------------------------------------------------------------------------------------------------


    //FAITPATH CONTROLLER------------------------------------------------------------------------------------------------------------FAITPATH CONTROLLER--#
    public void incPosOfCurrentPlayer(int qty){
        game.addFaithPointsToCurrentPLayer(qty);
    }

    public void incPosOfOthers(int qty){
        game.addFaithPointsToOtherPlayers(qty);
    }
    //------------------------------------------------------------------------------------------------------------------/


    //GETTERS-----------------------------------------------------------------------------------------------------------
    public View getView() {
        return view;
    }

    public Game getGame() {
        return game;
    }
    //------------------------------------------------------------------------------------------------------------------
}