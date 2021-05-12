package it.polimi.ingsw.controller;

import com.google.gson.Gson;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.model.player.ConversionMode;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerStatus;
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

    //BUFFERS
    private ArrayList<ResourceContainer> marketOut;
    private int marketOutCont = 0;

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

        if (!game.getCurrentPlayerNick().equals(senderNick)){
            view.printReply_uni("It's not your turn!", senderNick);
            return;
        }

        turnPhase_Commands(mex, senderNick, command);
    }

    private void setUp_Commands(String mex, String senderNick, Command command){

        int playerNumber = game.getPlayerListString().indexOf(senderNick);

        switch (command){

            case DISCARD_LEADER:
                LeaderIdMessage leaderIdMessage = gson.fromJson(mex, LeaderIdMessage.class);

                if(game.getPlayer(playerNumber).isLeadersHaveBeenDiscarded()){
                    view.printReply_uni("You can't do this action because you already discarded 2 Leaders! Please wait for the other players to to so", senderNick);
                    return;
                }

                if (!game.getPlayer(playerNumber).discardFromHand(leaderIdMessage.getLeaderID())) {
                    view.printReply_uni("Wrong Leader ID", senderNick);
                    return;
                }

                view.printReply_uni("Leader Discarded!", senderNick);

                if(game.getPlayer(playerNumber).isLeadersHaveBeenDiscarded())
                    view.printReply_uni("You discarded 2 Leaders! Please wait for the other players to to so ", senderNick);

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


    private void turnPhase_Commands(String mex, String senderNick, Command command){

        if (game.getCurrentPlayer().getPlayerStatus() == PlayerStatus.SELECTING_DESTINATION_AFTER_MARKET)
            selectDestinationAfterMarket( mex, senderNick, command);



        switch (command){

            case SEND_CONTAINER:
                SendContainer sendContainer =  gson.fromJson(mex, SendContainer.class);
                System.out.println("Arrivato: " + sendContainer);
                break;

            case PICK_FROM_MARKET:
                MarketMessage marketMessage = gson.fromJson(mex, MarketMessage.class);

                try {
                    marketOut = game.getMarket().getRowOrColumn(marketMessage.getSelection(),marketMessage.getNum());
                    System.out.println(marketOut);

                }catch (InvalidColumnNumber | InvalidRowNumber e ){
                    view.printReply_uni(e.getMessage(), senderNick);
                    return;
                }

                if(game.getCurrentPlayer().canConvert() == ConversionMode.INACTIVE) {
                    view.printReply_uni("You selected: " + marketOut.toString() + "\nNow select where do you want to place them by typing >DEPOSIT deposit_id", senderNick);
                    game.getCurrentPlayer().setPlayerStatus(PlayerStatus.SELECTING_DESTINATION_AFTER_MARKET);
                    return;
                }

                if(game.getCurrentPlayer().canConvert() == ConversionMode.AUTOMATIC) {
                    game.getCurrentPlayer().convert(marketOut);
                    view.printReply_uni("All blank marbles have been converted: " + marketOut.toString() + "\nNow select where do you want to place them them by typing >DEPOSIT deposit_id", senderNick);
                    game.getCurrentPlayer().setPlayerStatus(PlayerStatus.SELECTING_DESTINATION_AFTER_MARKET);
                    return;
                }

                if(game.getCurrentPlayer().canConvert() == ConversionMode.CHOICE_REQUIRED) {
                    view.printReply_uni("You have multiple leaders with the conversion ability, please select which one do you want to use for each blank marble by typing one of the active conversion available ", senderNick);
                    game.getCurrentPlayer().setPlayerStatus(PlayerStatus.SELECTING_CONVERSION);
                    return;
                }
                break;

            case SHOW_DEPOSIT:
                view.printReply_uni(game.getCurrentPlayer().getPlayerBoard().getDeposit().toString(), senderNick);
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
        if (!game.getPlayerList().get(game.getCurrentPlayerNumber()).getNickname().equals(nick)){
            view.printReply_uni("Not the current Player", nick);
            return false;
        }
        return true;
    }


    //GAME SETUP PHASE -------------------------------------------------------------------------------------------------
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

        } catch (DifferentResourceType | DepositSlotMaxDimExceeded | ResourceTypeAlreadyStored | IndexOutOfBoundsException exception) {

            view.printReply_uni(exception.getMessage(), currNickname);

            return false;
        }
    }


    private void startGame() {
        game.startGame();
        view.printReply("---THE GAME HAS BEEN STARTED---\n\t--HAVE FUN--");
        view.printItsYourTurn(game.getCurrentPlayerNick());
        //PRINT VARIE DI TUTTE LE PORCHERIE
    }
    //------------------------------------------------------------------------------------------------------------------


    //SELECT A DEPOSIT ID AFTER PICKING FROM MARKET---------------------------------------------------------------------
    public boolean selectDestinationAfterMarket(String mex, String senderNick, Command command) {
        view.printReply_uni("Where do you want to put" + marketOut.get(marketOutCont).getResourceType().toString(), senderNick);

        if (command != Command.SEND_DEPOSIT_ID){
            view.printReply_uni("Please select a depositID", senderNick);
            return false;
        }

        Message leaderID = gson.fromJson(mex, Message.class);

        try {
            game.getCurrentPlayer().getDepositSlotByID(Integer.parseInt(leaderID.getInfo())).canAddToDepositSlot(marketOut.get(marketOutCont));
        }catch (DifferentResourceType | DepositSlotMaxDimExceeded | ResourceTypeAlreadyStored e){
            view.printReply_uni(e.getMessage(), senderNick);
            return false;
        }

        game.getCurrentPlayer().getDepositSlotByID(Integer.parseInt(leaderID.getInfo())).addToDepositSlot(marketOut.get(marketOutCont));
        marketOutCont++;

        return true;
    }

    //------------------------------------------------------------------------------------------------------------------


    //GETTERS-----------------------------------------------------------------------------------------------------------
    public View getView() {
        return view;
    }

    public Game getGame() {
        return game;
    }
}