package it.polimi.ingsw.controller;

import com.google.gson.Gson;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Util;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.cards.Status;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.model.player.ConversionMode;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerStatus;
import it.polimi.ingsw.model.player.deposit.DepositSlot;
import it.polimi.ingsw.model.player.production.ProductionSlot;
import it.polimi.ingsw.model.resources.ResourceContainer;
import it.polimi.ingsw.model.resources.ResourceType;
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
    private Player currPlayer;

    private boolean mainActionAvailable = true;

    //BUFFERS ----------------------------------------------------------------------------------------------------------\
    private ArrayList<ResourceContainer> marketOut;

    private DevelopmentCard newDevelopmentCard;
    private int productionSlotId;

    private ArrayList<Integer> productionSlotIDs;
    //------------------------------------------------------------------------------------------------------------------/


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

                view.notifyGameSetup(game.getCardgrid().getIDsOnTop(), game.getNicknames(),game.getMarket().getMarketSetUp());
                //view.printOrder(playersNicknames);

                for (Player player : game.getPlayerList()) {
                    view.notifyCardsInHand(player.getHandIDs(), player.getNickname());
                    view.askForLeaderCardID(player.getNickname());
                }

            }
            else {
                game = new Game(playersNicknames.get(0));   //SINGLE PLAYER
                game.addView(view);

                view.notifyGameSetup(game.getCardgrid().getIDsOnTop(), game.getNicknames(),game.getMarket().getMarketSetUp());
                view.printReply_uni("SINGLE PLAYER MODE", playersNicknames.get(0));
                view.notifyCardsInHand(game.getPlayer(0).getHandIDs(), game.getPlayer(0).getNickname());
                view.askForLeaderCardID(game.getPlayer(0).getNickname());

            }

            //currPlayer = game.getPlayer(0);

        }catch (FileNotFoundException e){
            e.printStackTrace();
            view.printReply("Cannot read the configuration file of the game");
        }
    }

    @Override
    public void update(String mex, Command command, String senderNick) {

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

        int playerNumber = game.getNicknames().indexOf(senderNick);

        switch (command){

            case DISCARD_LEADER:
                discardLeaderSetUpPhase(mex, senderNick, playerNumber);
                break;

            case SETUP_CONTAINER:
                if(!checkIfAllLeadersHaveBeenDiscarded() && !game.isSinglePlayer()){
                    view.printReply_uni("Please discard 2 the leader first or wait the other players to do so ", senderNick);
                    return;
                }

                SendContainer sendContainer1 = gson.fromJson(mex, SendContainer.class);
                addSetUpContainerToPlayer(playerNumber, sendContainer1.getContainer(), sendContainer1.getDestinationID(), senderNick);
                break;

            case MANAGE_DEPOSIT:
            case SWITCH_DEPOSIT:
                currPlayer = game.getPlayer(playerNumber);
                manageDeposit(mex, senderNick, command);
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

        if (currPlayer.getPlayerStatus() == PlayerStatus.SELECTING_DESTINATION_AFTER_MARKET){
            if (command == Command.MANAGE_DEPOSIT || command == Command.SWITCH_DEPOSIT)
                manageDeposit(mex, senderNick, command);
            else
                selectDestinationAfterMarket( mex, senderNick, command);
            return;
        }

        if (currPlayer.getPlayerStatus() == PlayerStatus.SELECTING_CONVERSION){
            mustConvert(mex, command, senderNick);
            return;
        }

        if (currPlayer.getPlayerStatus() == PlayerStatus.SELECTING_BUY_RESOURCES || currPlayer.getPlayerStatus() == PlayerStatus.SELECTING_PRODUCTION_RESOURCES){
            selectResources(mex, senderNick, command);
            return;
        }

        if (currPlayer.getPlayerStatus() == PlayerStatus.SELECTING_QM){
            selectQM(mex, senderNick, command);
            return;
        }

        switch (command){
            case CHEAT_VAULT:
                currPlayer.getVault().addToVault(new ResourceContainer(ResourceType.STONE, 999));
                currPlayer.getVault().addToVault(new ResourceContainer(ResourceType.GOLD, 999));
                currPlayer.getVault().addToVault(new ResourceContainer(ResourceType.MINION, 999));
                currPlayer.getVault().addToVault(new ResourceContainer(ResourceType.SHIELD, 999));
                game.addFaithPointsToCurrentPLayer(12);
                view.printReply_uni(currPlayer.getVault().toString(), currPlayer.getNickname());
                break;

            case BUY:
                if (!mainActionHandler(senderNick))
                    break;

                if(!checkBuy(mex, senderNick, currPlayer))
                    return;

                currPlayer.setPlayerStatus(PlayerStatus.SELECTING_BUY_RESOURCES);
                break;

            case PRODUCE:
                if (!mainActionHandler(senderNick))
                    break;

                checkQuestionMarks(mex, senderNick);
                break;

            case PICK_FROM_MARKET:
                if (!mainActionHandler(senderNick))
                    break;

                pickFromMarket(mex, senderNick);
                break;

            case ACTIVATE_LEADER:
                activateLeader(mex, senderNick);
                break;

            case DISCARD_LEADER:
                discardLeader(mex, senderNick);
                break;

            case MANAGE_DEPOSIT:
                manageDeposit(mex, senderNick, Command.MANAGE_DEPOSIT);
                break;

            case SWITCH_DEPOSIT:
                manageDeposit(mex, senderNick, Command.SWITCH_DEPOSIT);
                break;

            case END_TURN:
                if(mainActionAvailable) {
                    view.printReply_uni("You can't pass yet, you have to perform at least a main action!", senderNick);
                    break;
                }

                game.nextTurn();
                mainActionAvailable = true;

                if (game.isGameEnded()) break;

                currPlayer = game.getCurrentPlayer();
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
     * This method is used to discard a specific leader but only after checking that the player trying to do so meets all the criteria.
     * When every leader has been discarded calls 'askForResources()' method.
     * This method also notifies the view on what happens using specific messages
     * @param mex message received
     * @param senderNick current player nickname
     * @param playerNumber player number
     */
    public void discardLeaderSetUpPhase(String mex, String senderNick, int playerNumber) {
        IdMessage idMessage = gson.fromJson(mex, IdMessage.class);

        if(game.getPlayer(playerNumber).isLeadersHaveBeenDiscarded()){
            view.printReply_uni("You can't do this action because you already discarded 2 Leaders! Please wait for the other players to to so", senderNick);
            return;
        }

        if (!game.getPlayer(playerNumber).discardFromHand(idMessage.getId())) {
            view.printReply_uni("Wrong Leader ID", senderNick);
            return;
        }

        if(game.getPlayer(playerNumber).isLeadersHaveBeenDiscarded()){
            view.printReply_uni("You discarded 2 Leaders!" , senderNick);
            if (!game.isSinglePlayer())
                view.printReply_uni("Please wait the other players to do so" , senderNick);
        }


        if(checkIfAllLeadersHaveBeenDiscarded() && !game.isSinglePlayer())
            askForResources();


        else if(checkIfAllLeadersHaveBeenDiscarded())   //IF SINGLE PLAYER THE GAME CAN BE STARTED IMMEDIATELY
            startGame();
    }

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
            view.notifyCurrentPlayerIncrease( 1,game.getNicknames().get(2));

        if (game.getNumOfPlayers() > 3)
            view.notifyCurrentPlayerIncrease( 1, game.getNicknames().get(3));

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

            view.setCurrPlayer(currNickname);
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
        currPlayer = game.getPlayer(0);
        view.setCurrPlayer(game.getCurrentPlayerNick());
        game.startGame();
        view.notifyGameIsStarted();
        view.printItsYourTurn(game.getCurrentPlayerNick());
    }
    //------------------------------------------------------------------------------------------------------------------/


    //TURN HANDLING -----------------------------------------------------------------------------------------------------------------------------------#
    /**
     * Used for checking if the player can do his main action.
     * @param senderNick currPLayer nick
     * @return true if the player can do his main action, false if he already did it
     */
    public boolean mainActionHandler(String senderNick){
        if(!mainActionAvailable) {
            view.printReply_uni("You can't do that, you already used your main action this turn!", senderNick);
            return false;
        }
        return true;
    }
    //------------------------------------------------------------------------------------------------------------------/


    //BUY PHASE ---------------------------------------------------------------------------------------------------------------------BUY PHASE---------#
    /**
     * Checks if the user has enough resources in total to buy the selected DevelopmentCard
     * @param mex  message received
     * @param senderNick is the player's nickname that wants to buy the card
     * @param currPlayer current player
     * @return true if he has selected a valid number for the row and column
     */
    private boolean checkBuy(String mex, String senderNick, Player currPlayer) {
        BuyMessage buyMessage = gson.fromJson(mex, BuyMessage.class);
        int cardID = buyMessage.getCardID();
        int id = buyMessage.getProductionSlotID();

        try {
            DevelopmentCard selectedCard = game.getCardgrid().getDevelopmentCardOnTop(cardID);

            if(selectedCard == null) {
                view.notifyBuyError("The card selected is not purchasable at the moment OR does not exist! Please select another ID");
                return false;
            }

            if(!currPlayer.hasEnoughResources(selectedCard.getDiscountedPrice(currPlayer.getPlayerBoard()))) {
                view.notifyBuyError("You don't have enough resources to buy this Development Card!");
                return false;
            }

            if(!currPlayer.getProductionSlotByID(id).canInsertOnTop(selectedCard)){
                view.notifyBuyError("You can't insert the this card in the selected Production Slot!");
                return false;
            }

            newDevelopmentCard = selectedCard;
            this.productionSlotId = id;
            view.notifyBuySlotOk("The card you selected requires: " + selectedCard.priceToString() +
                    "\nPlease select resources as a payment by typing > GIVE Qty ResourceType 'FROM' ('DEPOSIT' DepositID) or ('VAULT') ");
            return true;

        } catch (IndexOutOfBoundsException exception) {
            view.notifyBuyError("The selected Production Slot does not exists!");
            return false;
        }
    }

    /**
     * When the player is in 'PlayerStatus.SELECTING_BUY_RESOURCES' or 'PlayerStatus.PlayerStatus.SELECTING_PRODUCTION_RESOURCES' the method is used to receive the resources selected and checking <br>
     * if the removing of the container goes right.
     * When the command 'DONE' is received calls 'buyDevelopmentCard()' or 'produce()' method
     * @param mex message received
     * @param senderNick current player nickname
     * @param command command received
     */
    private void selectResources(String mex, String senderNick, Command command) {

        if(command == Command.SEND_CONTAINER) {
            SendContainer sendContainer =  gson.fromJson(mex, SendContainer.class);

            if(!removeContainer(sendContainer.getContainer(), sendContainer.getDestination(), sendContainer.getDestinationID(), senderNick))
                return;
        }

        if(command == Command.DONE) {
            if(currPlayer.getPlayerStatus() == PlayerStatus.SELECTING_BUY_RESOURCES) {
                buyDevelopmentCard(senderNick);
                currPlayer.setPlayerStatus(PlayerStatus.IDLE);
                return;
            }

            if(currPlayer.getPlayerStatus() == PlayerStatus.SELECTING_PRODUCTION_RESOURCES) {
                produce(senderNick);
                currPlayer.setPlayerStatus(PlayerStatus.IDLE);
                return;
            }
        }

        view.printReply_uni("Please keep selecting the resources or type 'DONE'", senderNick);
    }

    /**
     * Checks if the curr player owns enough resources to remove 'resourceContainer'.
     * Then if the 'destination' is:
     *  - 'VAULT', vault's buffer gets filled
     *  - 'DEPOSIT', the specific deposit id's buffer gets filled
     * This method also notifies the view on what happens using specific messages
     * @param resourceContainer the container that i want to remove (use as a payment)
     * @param destination 'deposit' or 'vault'
     * @param destinationID the
     * @param senderNick current player nickname
     * @return true if the container is correctly removed, false otherwise
     */
    private boolean removeContainer(ResourceContainer resourceContainer, String destination, int destinationID, String senderNick){

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
     */
    private void buyDevelopmentCard(String senderNick) {

        if(!currPlayer.canBuy(newDevelopmentCard)) {
            view.printReply_uni("The resources you selected aren't correct!", senderNick);
            view.printTurnHelp(senderNick);
            currPlayer.emptyBuffers();
            return;
        }

        currPlayer.buy();
        currPlayer.emptyBuffers();
        currPlayer.insertBoughtCardOn(productionSlotId, newDevelopmentCard);
        game.getCardgrid().removeDevelopmentCard(newDevelopmentCard.getId());

        newDevelopmentCard = null;
        productionSlotId = -1;

        //view.printProduction(currPlayer.getProductionSite(), senderNick);
        mainActionAvailable = false;
    }
    //------------------------------------------------------------------------------------------------------------------/


    //PRODUCTION PHASE ---------------------------------------------------------------------------------------------------------------------PRODUCTION PHASE---------#
    /**
     * Checks if the selected Production Slot are valid and if at least one of the Production Slots has QM to fill, <br>
     * the currentPlayer PlayerStatus is set to 'SELECTING_QM' otherwise is set to 'SELECTING_PRODUCTION_RESOURCES'
     */
    private void checkQuestionMarks(String mex, String senderNick) {
        ProduceMessage produceMessage = gson.fromJson(mex, ProduceMessage.class);
        productionSlotIDs = produceMessage.getIDs();
        int firstID = -1;

        if(productionSlotIDs.size() > currPlayer.getProductionSite().getProductionSlots().size()) {
            view.printReply_uni("You don't own this many ProductionSlots! Please type in a valid amount!", senderNick);
            return;
        }

        try{
            for (int id : productionSlotIDs) {
                ProductionSlot currProductionSlot = currPlayer.getProductionSlotByID(id);

                if(currProductionSlot.isEmpty()){
                    currPlayer.setPlayerStatus(PlayerStatus.IDLE);
                    view.printReply_uni("The selected Production Slot is empty so you cannot use it for production!", senderNick);
                    return;
                }
                else if(currProductionSlot.hasQuestionMarks()) {
                    firstID = id;
                    currPlayer.setPlayerStatus(PlayerStatus.SELECTING_QM);
                }
            }

        }catch (IndexOutOfBoundsException exception) {
            view.printReply_uni("The selected Production Slot does not exists!", senderNick);
            return;
        }

        if(currPlayer.getPlayerStatus() == PlayerStatus.SELECTING_QM){
            view.printReply_uni("Please start filling the Production Slots N: " + firstID + " with resources of your choice by typing >FILL ResourceType1 ResourceType2  ... 'DONE'", senderNick);
        }

        if(currPlayer.getPlayerStatus() == PlayerStatus.IDLE) {
            setUpProduction(senderNick);
        }
    }

    /**
     * Used for selecting the ResourceTypes to put in the QMs
     * The first ResourceTypes Selected are put in QMI, then when QMI is full, the left over ResourceTypes are put in QMO.
     * When every QM of every ProductionSlot is selected the method setUpProduction() is called
     * @param mex message received
     * @param senderNick current player nickname
     * @param command command received
     */
    private void selectQM(String mex, String senderNick, Command command){
        if(command != Command.FILL_QM) {
            view.printReply_uni("You cannot do this action! Please keep filling the Production Slots with resources of your choice!", senderNick);
            return;
        }

        ResourceTypeSend message = gson.fromJson(mex, ResourceTypeSend.class);
        ArrayList<ResourceType> selectedResources = message.getResourceTypeArrayList();
        System.out.println(selectedResources);

        for (int id : productionSlotIDs) {
            ProductionSlot currProductionSlot = currPlayer.getProductionSlotByID(id);
            int x = 0;      //used to iterate selectedResources

            if(currProductionSlot.hasStillQuestionMarks()) {
                int QMI = currProductionSlot.getQMI();
                int QMO = currProductionSlot.getQMO();

                if(QMI + QMO != selectedResources.size()) {
                    view.printReply_uni("You didn't select the right amount of ResourceType!", senderNick);
                    return;
                }

                for(int i = 0; i < QMI; i++) {
                    currProductionSlot.fillQuestionMarkInput(selectedResources.get(x));
                    x++;
                }

                for(int i = 0; i < QMO; i++) {
                    currProductionSlot.fillQuestionMarkOutput(selectedResources.get(x));
                    x++;
                }

                view.printReply_uni("Resources of your choice for Production Slot N: " + id + " have been filled correctly!", senderNick);
            }
        }

        for (int id : productionSlotIDs) {
            if(currPlayer.getProductionSlotByID(id).hasStillQuestionMarks()) {
                view.printReply_uni("Please start filling the Production Slots N: " + id + " with resources of your choice by typing >FILL ResourceType1 ResourceType2  ... 'DONE'", senderNick);
                return;
            }
        }

        setUpProduction(senderNick);
    }

    /**
     * This method fills the buffers and checks if the currPlayer has enough resources altogether to activate the selected ProductionSlots.
     * If everything goes right currPlayer PlayerStatus is set to 'SELECTING_PRODUCTION_RESOURCES'
     * @param senderNick current player nickname
     */
    private void setUpProduction(String senderNick) {
        ArrayList<ProductionSlot> selectedProductionCards = new ArrayList<>();

        for (int id : productionSlotIDs) {
            selectedProductionCards.add(currPlayer.getProductionSlotByID(id));
        }

        currPlayer.fillProductionBuffers(selectedProductionCards);

        if(!currPlayer.hasEnoughResourcesForProduction())  {
            currPlayer.setPlayerStatus(PlayerStatus.IDLE);
            view.printReply_uni("You don't own enough resources altogether to activate the selected Production Slots!", senderNick);
            return;
        }

        currPlayer.setPlayerStatus(PlayerStatus.SELECTING_PRODUCTION_RESOURCES);

        view.printReply_uni("The ProductionSlots you selected requires: " + Util.mapToString(currPlayer.getProductionSite().getBufferInputMap()) +
        "\nPlease select resources as a payment by typing > GIVE Qty ResourceType 'FROM' ('DEPOSIT' DepositID) or ('VAULT') ", senderNick);
    }

    /**
     * Used to actually produce but only after checking with the method 'canProduce()' if you can do it.
     * This method also notifies the view on what happens using specific messages
     * @param senderNick current player nickname
     */
    private void produce(String senderNick) {
        try{
            currPlayer.canProduce();
            currPlayer.produce();
            currPlayer.emptyBuffers();

            view.notifyProductionOk(senderNick);
            mainActionAvailable = false;

        } catch (NotEnoughResources | DepositSlotMaxDimExceeded exception) {
            currPlayer.emptyBuffers();

            view.printReply_uni(exception.getMessage(), senderNick);
            view.printTurnHelp(senderNick);
        }
    }
    //------------------------------------------------------------------------------------------------------------------/


    //MARKET ACTION CONTROLLER-------------------------------------------------------------------------------------------------------PICK FROM MARKET--#
    /**
     * Manages the user action 'Pick From Market' :<br>
     * - Checks if the user selected a valid row/column number <br>
     * - Checks if the user has one or more active conversion <br>
     * - Sets the playerStatus to SELECTING_DESTINATION_AFTER_MARKET
     */
    public void pickFromMarket(String mex, String senderNick){
        MarketMessage marketMessage = gson.fromJson(mex, MarketMessage.class);

        try {
            marketOut = game.getMarket().getRowOrColumn(marketMessage.getSelection(),marketMessage.getNum());
            view.printReply_everyOneElse(senderNick+" has extracted the "+marketMessage.getSelection()+" "+marketMessage.getNum()+" from the market!", senderNick);
            view.notifyUsers(marketMessage);
            mainActionAvailable = false;

        }catch (InvalidColumnNumber | InvalidRowNumber e ){
            view.printReply_uni(e.getMessage(), senderNick);
            return;
        }

        if(currPlayer.canConvert() == ConversionMode.CHOICE_REQUIRED) {
            view.printReply_uni("You have multiple leaders with the conversion ability, please select which one do you want to use for each blank marble by typing one of the active conversion available ", senderNick);
            game.getCurrentPlayer().setPlayerStatus(PlayerStatus.SELECTING_CONVERSION);
            conversionController(senderNick);
            return;
        }

        if(currPlayer.canConvert() == ConversionMode.AUTOMATIC) {
            currPlayer.convert(marketOut);
            view.printReply_uni("All blank marbles have been converted to " + currPlayer.getConversionSite().getConversionsAvailable().get(0) + " : " + marketOut.toString(), senderNick);
        }

        beginMarketDestinationSelection(senderNick);
    }

    /**
     * Filters the marketOutput and tells the user that now he can select the destinations
     */
    public void beginMarketDestinationSelection(String senderNick){
        filterNonAddableResources();

        if (marketOut.isEmpty()){
            view.printTurnHelp(senderNick);
            return;
        }

        currPlayer.setPlayerStatus(PlayerStatus.SELECTING_DESTINATION_AFTER_MARKET);

        marketAddDepositController(senderNick);
    }

    /**
     * Manages the sending of resources to the deposits after taking them from the market
     */
    public void marketAddDepositController(String senderNick){
        if (marketOut.size()>0){
            view.askForMarketDestination(marketOut, senderNick);
            return;
        }

        view.printTurnHelp(senderNick);
        currPlayer.setPlayerStatus(PlayerStatus.IDLE);
        marketOut = null;
    }

    /**
     * Checks if the user typed the SEND_DEPOSIT_ID command that contains the infos about the deposit in which he wants to place the resource
     */
    public void selectDestinationAfterMarket(String mex, String senderNick, Command command) {

        if (command != Command.SEND_DEPOSIT_ID){
            view.printReply_uni("Please select a depositID", senderNick);
            return;
        }

        SendContainer putMessage = gson.fromJson(mex, SendContainer.class);
        ResourceContainer container = putMessage.getContainer();

        ResourceContainer selected;

        try{
            selected = marketOut.get(marketOut.indexOf(container));
        }catch (IndexOutOfBoundsException e){
            view.printReply_uni("Wrong selection", senderNick);
            return;
        }

        try {
            if(!mustDiscardCheck(container, senderNick)){
                currPlayer.getDepositSlotByID(putMessage.getDestinationID()).canAddToDepositSlot(selected);
                currPlayer.getDepositSlotByID(putMessage.getDestinationID()).addToDepositSlot(selected);
                marketOut.remove(container);
            }

        }catch (DifferentResourceType | DepositSlotMaxDimExceeded | ResourceTypeAlreadyStored  e){
            view.printReply_uni(e.getMessage(), senderNick);
            return ;

        }catch (IndexOutOfBoundsException exception){
            view.printReply_uni("The selected Deposit Slot does not exists!", senderNick);
            return;
        }

        marketAddDepositController(senderNick);

    }

    /**
     * Filters all the resources that cannot be added to a deposit and checks if they can increase the player position on the faithpath
     */
    public void filterNonAddableResources(){
        for (ResourceContainer cont: marketOut) {
            if (cont.getResourceType().canAddToFaithPath())
                incPosOfCurrentPlayer(cont.getQty());
        }

        marketOut.removeIf(res -> !res.getResourceType().canAddToDeposit());
    }

    /**
     * Checks automatically if the user has no place where to put the resource and force him to discard her if there aren't any
     * @return true if he must discard the current resource on the marketOutput array
     */
    public boolean mustDiscardCheck(ResourceContainer container, String senderNick) {

        if (currPlayer.getDeposit().mustDiscardResource(container)){
            view.printReply_uni("You cant add this resource anywhere, it will be discarded..", senderNick);
            marketOut.remove(container);
            incPosOfOthers(1);
            return true;
        }

        return false;
    }

    /**
     * Counts the convertible marbles
     */
    public void conversionController(String nick){
        view.printReply_uni("You Have "+ currPlayer.getConversionSite().countConvertible(marketOut)+ " "+
                currPlayer.getConversionSite().getDefaultConverted()+ " to convert, type >CONVERT ResourceType for each one", nick);
        view.printReply_uni("Conversion available: "+ currPlayer.getConversionSite().getConversionsAvailable().toString(), nick);
    }

    /**
     * Called when the current player has more than one conversion available
     */
    public void mustConvert(String mex, Command command, String senderNick){
        if (command != Command.CONVERSION){
            view.printReply_uni("Please select a valid conversion", senderNick);
            return;
        }

        ResourceTypeSend resourceTypeSend = gson.fromJson(mex, ResourceTypeSend.class);
        ResourceType conversionSelected = resourceTypeSend.getResourceType();

        if(currPlayer.getPlayerBoard().getConversionSite().getConversionsAvailable().contains(new ResourceContainer(conversionSelected,1))){
            currPlayer.getConversionSite().convertSingleElement(
                    marketOut.get(marketOut.indexOf(new ResourceContainer(currPlayer.getConversionSite().getDefaultConverted(), 1))),
                    new ResourceContainer(conversionSelected, 1)
            );

            if (currPlayer.getConversionSite().countConvertible(marketOut)!=0){
                view.printReply_uni("Conversion done. You must convert another marble", senderNick);
                return;
            }

            beginMarketDestinationSelection(senderNick);
            return;
        }

        view.printReply_uni("You don't have this conversion available", senderNick);

    }
    //------------------------------------------------------------------------------------------------------------------


    //DEPOSIT MANAGEMENT------------------------------------------------------------------------------------------------\
    /**
     * This method is used for both 'MANAGE_DEPOSIT' and 'SWITCH_DEPOSIT' commands.
     * Executes the selected command after checking that the currPlayer can do it.
     * @param mex message received containing all the info
     * @param senderNick currPlayer nickname
     * @param commandType 'Command.MANAGE_DEPOSIT' or 'Command.SWITCH_DEPOSIT'
     */
    public void manageDeposit(String mex, String senderNick, Command commandType){
        int qty;
        int sourceID;
        int destinationID;

        if(commandType == Command.MANAGE_DEPOSIT){
            ManageDepositMessage manageDepositMessage = gson.fromJson(mex,ManageDepositMessage.class);

            qty = manageDepositMessage.getQty();
            sourceID = manageDepositMessage.getSourceID();
            destinationID = manageDepositMessage.getDestinationID();
        }else {
            SwitchDepositMessage switchDepositMessage = gson.fromJson(mex,SwitchDepositMessage.class);

            sourceID = switchDepositMessage.getSourceID();
            destinationID = switchDepositMessage.getDestinationID();
            qty = currPlayer.getDepositSlotByID(sourceID).getResourceQty();
        }

        DepositSlot sourceDepositSlot = currPlayer.getDepositSlotByID(sourceID);
        DepositSlot destinationDepositSlot = currPlayer.getDepositSlotByID(destinationID);

        try {
            currPlayer.getPlayerBoard().getDeposit().moveTo(sourceDepositSlot, qty, destinationDepositSlot);
            view.notifyMoveOk(senderNick);
        }
        catch (DepositSlotMaxDimExceeded | DifferentResourceType | NotEnoughResources | ResourceTypeAlreadyStored e){
            view.printReply_uni(e.getMessage(), senderNick);
        }
    }
    //------------------------------------------------------------------------------------------------------------------/


    //LEADER ACTIONS----------------------------------------------------------------------------------------------------\
    /**
     * Called when the player wants to activate a Leader Card in his hand.
     * If the player meets the criteria to activate the selected card, the leader is correctly activated.
     * Otherwise the view gets notified with specific error messages
     * @param mex message received, containing info about the Leader to activate
     * @param nickname currPlayer nickname
     */
    public void activateLeader(String mex , String nickname){
        IdMessage idMessage = gson.fromJson(mex, IdMessage.class);
        int id = idMessage.getId();

        for (LeaderCard lc : currPlayer.getHand()) {
            if(lc.getId() == id){
                if(lc.checkRequirements(game.getCurrentPlayer().getPlayerBoard()))
                    currPlayer.activateLeader(lc);
                else
                    view.printReply_uni("Sorry, you do not meet the requirements to activate this leader.", nickname);

                return;
            }
        }
        view.printReply_uni("You do not own a leader with this id!", nickname);
    }

    /**
     * Called when the player wants to discard a Leader Card in his hand.
     * If the player meets the criteria to discard the selected card, the leader is correctly discarded and the player's position incremented.
     * Otherwise the view gets notified with specific error messages
     * @param mex message received, containing info about the Leader to discard
     * @param senderNick currPlayer nickname
     */
    public void discardLeader(String mex, String senderNick){
        IdMessage idMessage = gson.fromJson(mex, IdMessage.class);
        int leaderCardID = idMessage.getId();


        if(currPlayer.getHand().isEmpty()) {
            view.printReply_uni("Your Hand is empty! You cannot discard nor activate Leader Cards!", senderNick);
            return;
        }

        for (LeaderCard lc : currPlayer.getHand()) {
            if(lc.getId() == leaderCardID){
                if(lc.getStatus().equals(Status.ACTIVE)){
                    view.printReply_uni("You cannot discard a Leader that is already active!", senderNick);
                    return;
                }
            }
        }

        if (!currPlayer.discardFromHand(leaderCardID)) {
            view.printReply_uni("Wrong Leader ID", senderNick);
            return;
        }

        incPosOfCurrentPlayer(1);
    }
    //------------------------------------------------------------------------------------------------------------------/


    //FAITH PATH CONTROLLER-----------------------------------------------------------------------------------------------------------FAITHPATH CONTROLLER--#
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