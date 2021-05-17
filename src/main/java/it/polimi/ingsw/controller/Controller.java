package it.polimi.ingsw.controller;

import com.google.gson.Gson;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.LeaderCard;
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

            currPlayer = game.getPlayer(0);

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

        int playerNumber = game.getNicknames().indexOf(senderNick);

        switch (command){

            case DISCARD_LEADER:
                discardLeader(mex, senderNick, playerNumber);
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

        if (currPlayer.getPlayerStatus() == PlayerStatus.SELECTING_DESTINATION_AFTER_MARKET){
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
                break;

            case BUY:
                if(!checkBuy(mex, senderNick, currPlayer))
                    return;

                currPlayer.setPlayerStatus(PlayerStatus.SELECTING_BUY_RESOURCES);

                break;

            case PRODUCE:
                checkQuestionMarks(mex, senderNick);
                break;

            case PICK_FROM_MARKET:
                pickFromMarket(mex, senderNick);
                break;

            case ACTIVATE_LEADER:
                activateLeader(mex,senderNick);
                break;

            case MANAGE_DEPOSIT:
                manageDeposit(mex,senderNick,Command.MANAGE_DEPOSIT);
                break;

            case SWITCH_DEPOSIT:
                manageDeposit(mex,senderNick,Command.SWITCH_DEPOSIT);
                break;

            case SHOW_DEPOSIT:
                view.printDeposit(currPlayer.getPlayerBoard().getDeposit(), senderNick);
                break;

            case SHOW_VAULT:
                view.printVault(currPlayer.getPlayerBoard().getVault(), senderNick);
                break;

            case SHOW_PRODUCTION:
                view.printProduction(currPlayer.getPlayerBoard().getProductionSite(), senderNick);
                break;

            case SHOW_MAKET:
                view.printMarket(game.getMarket(), senderNick);
                break;

            case SHOW_CARDGRID:
                view.printCardGrid(game.getCardgrid(), senderNick);
                break;

            case SHOW_FAITHPATH:
                view.printFaithPath(game.getFaithPath(),senderNick,game.getNicknames());
                break;

            case END_TURN:
                //if (ha eseguitoalmeno  una azione primaria )
                game.nextTurn();
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
    public void discardLeader (String mex, String senderNick, int playerNumber) {
        IdMessage idMessage = gson.fromJson(mex, IdMessage.class);

        if(game.getPlayer(playerNumber).isLeadersHaveBeenDiscarded()){
            view.printReply_uni("You can't do this action because you already discarded 2 Leaders! Please wait for the other players to to so", senderNick);
            return;
        }

        if (!game.getPlayer(playerNumber).discardFromHand(idMessage.getId())) {
            view.printReply_uni("Wrong Leader ID", senderNick);
            return;
        }

        view.printDiscardLeader(idMessage.getId(), senderNick);

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
            view.notifyFaithPathProgression( 1,game.getNicknames().get(2));

        if (game.getNumOfPlayers() > 3)
            view.notifyFaithPathProgression( 1, game.getNicknames().get(3));

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
     * @param mex  message received
     * @param senderNick is the player's nickname that wants to buy the card
     * @param currPlayer current player
     * @return true if he has selected a valid number for the row and column
     */
    private boolean checkBuy(String mex, String senderNick, Player currPlayer) {
        BuyMessage buyMessage = gson.fromJson(mex, BuyMessage.class);
        int row = buyMessage.getRow();
        int column = buyMessage.getColumn();
        int id = buyMessage.getProductionSlotID();

        try {
            DevelopmentCard selectedCard = game.getCardgrid().getDevelopmentCardOnTop(row, column);

            if(!currPlayer.hasEnoughResources(selectedCard.getDiscountedPrice(currPlayer.getPlayerBoard()))) {
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
     * When the player is in 'PlayerStatus.SELECTING_BUY_RESOURCES' or 'PlayerStatus.PlayerStatus.SELECTING_PRODUCTION_RESOURCES' the method is used to receive the resources selected and checking <br>
     * if the removing of the container goes right.
     * When the command 'DONE' is received calls buyDevelopmentCard() or produce() method
     * @param mex message received
     * @param senderNick current player nickname
     * @param command command received
     */
    private void selectResources(String mex, String senderNick, Command command) {

        if(command == Command.SEND_CONTAINER) {
            SendContainer sendContainer =  gson.fromJson(mex, SendContainer.class);
            //System.out.println("Arrived: " + sendContainer);

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
                //produce(senderNick);
                System.out.println("PRODUZIONE");
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

        view.printReply_uni("You bought the card correctly!", senderNick);
        view.printProduction(currPlayer.getProductionSite(), senderNick);
    }
    //------------------------------------------------------------------------------------------------------------------/


    //PRODUCTION PHASE ---------------------------------------------------------------------------------------------------------------------PRODUCTION PHASE---------#
    /**
     * Checks if the selected Production Slot are valid and if at least one of the Production Slots has QM to fill, <br>
     * the current PlayerStatus is set to 'SELECTING_QM' otherwise is set to 'SELECTING_PRODUCTION_RESOURCES'
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
            view.printReply_uni("Please start filling the Production Slots N: " + firstID + " with resources of your choice", senderNick);
        }

        if(currPlayer.getPlayerStatus() == PlayerStatus.IDLE) {
            currPlayer.setPlayerStatus(PlayerStatus.SELECTING_PRODUCTION_RESOURCES);
            view.printReply_uni("Please select resources as a payment by typing > GIVE Qty ResourceType 'FROM' ('DEPOSIT' DepositID) or ('VAULT') ", senderNick);
        }

    }

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
            }

            view.printReply_uni("Resources of your choice for Production Slot N: " + id + " have been filled correctly!", senderNick);
        }

        for (int id : productionSlotIDs) {
            if(currPlayer.getProductionSlotByID(id).hasStillQuestionMarks()) {
                view.printReply_uni("Please start filling the Production Slots N: " + id + " with resources of your choice", senderNick);
                return;
            }
        }

        currPlayer.setPlayerStatus(PlayerStatus.SELECTING_PRODUCTION_RESOURCES);
        view.printReply_uni("Please select resources as a payment by typing > GIVE Qty ResourceType 'FROM' ('DEPOSIT' DepositID) or ('VAULT') ", senderNick);
    }
    //------------------------------------------------------------------------------------------------------------------/

    //DEPOSIT MANAGEMENT------------------------------------------------------------------------------------------------

    public void manageDeposit(String mex, String senderNick, Command commandType){
        int qty;
        int sourceID;
        int destinationID;
        if(commandType==Command.MANAGE_DEPOSIT){
            ManageDepositMessage manageDepositMessage=gson.fromJson(mex,ManageDepositMessage.class);
            qty=manageDepositMessage.getQty();
            sourceID=manageDepositMessage.getSourceID()-1;
            destinationID=manageDepositMessage.getDestinationID()-1;
        }else {
            SwitchDepositMessage switchDepositMessage=gson.fromJson(mex,SwitchDepositMessage.class);
            sourceID=switchDepositMessage.getSourceID()-1;
            destinationID=switchDepositMessage.getDestinationID()-1;
            qty=currPlayer.getPlayerBoard().getDeposit().getDepositList().get(sourceID).getResourceQty();
        }
        DepositSlot sourceDepositSlot=currPlayer.getPlayerBoard().getDeposit().getDepositList().get(sourceID);
        DepositSlot destinationDepositSlot=currPlayer.getPlayerBoard().getDeposit().getDepositList().get(destinationID);

        try {
            currPlayer.getPlayerBoard().getDeposit().moveTo(sourceDepositSlot,qty,destinationDepositSlot);
        }
        catch (DepositSlotMaxDimExceeded | DifferentResourceType | NotEnoughResources | ResourceTypeAlreadyStored e){
            view.printReply_uni(e.getMessage(), senderNick);
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

        view.printReply_uni("\n\nNow select where do you want to place them by typing >PUT ResourceType 'to deposit' deposit_id", senderNick);
        currPlayer.setPlayerStatus(PlayerStatus.SELECTING_DESTINATION_AFTER_MARKET);

        marketAddDepositController(senderNick);
    }

    /**
     * Manages the sending of resources to the deposits after taking them from the market
     */
    public void marketAddDepositController(String senderNick){
        if (marketOut.size()>0){
            view.printDeposit(currPlayer.getDeposit(), senderNick);
            view.printMarketOut(marketOut, senderNick);
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
            return ;
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

        if(currPlayer.getPlayerBoard().getConversionSite().getConversionsAvailable().contains(conversionSelected)){
            currPlayer.getConversionSite().convertSingleElement(
                    marketOut.get(marketOut.indexOf(new ResourceContainer(currPlayer.getConversionSite().getDefaultConverted(), 1))),
                    new ResourceContainer(conversionSelected, 1)
            );

            view.printReply_uni("Conversion done", senderNick);
            if (currPlayer.canConvert() != ConversionMode.CHOICE_REQUIRED)
                beginMarketDestinationSelection(senderNick);
            return;
        }

        view.printReply_uni("You don't have this conversion available", senderNick);

    }
    //------------------------------------------------------------------------------------------------------------------

    //ACTIVATE LEADER---------------------------------------------------------------------------------------------------
    public boolean activateLeader(String mex , String nickname){
        IdMessage idMessage = gson.fromJson(mex, IdMessage.class);
        int id=idMessage.getId();

        for (LeaderCard lc : currPlayer.getHand()) {
            if(lc.getId()==id){
                if(lc.checkRequirements(game.getCurrentPlayer().getPlayerBoard())){
                    currPlayer.activateLeader(lc);
                    view.printActivateLeader(id,nickname);
                    return true;
                }else {
                    view.printReply_uni("Sorry, you do not meet the requirements to activate this leader.", nickname);
                    return false;
                }
            }
        }
        view.printReply_uni("You do not own a leader with this id!", nickname);
        return false;
    }
    //------------------------------------------------------------------------------------------------------------------

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