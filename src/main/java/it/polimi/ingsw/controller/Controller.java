package it.polimi.ingsw.controller;

import com.google.gson.Gson;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.exceptions.DepositSlotMaxDimExceeded;
import it.polimi.ingsw.model.exceptions.DifferentResourceType;
import it.polimi.ingsw.model.exceptions.ResourceTypeAlreadyStored;
import it.polimi.ingsw.model.player.Player;
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

    public Controller (HashMap<String, User> connectedPlayers){
        this.view = new VirtualView(connectedPlayers);
        view.addObserverController(this);

        ArrayList<String> playersNicknames = new ArrayList<>(connectedPlayers.keySet());
        int numOfPlayers = playersNicknames.size();

        try {
            if(numOfPlayers > 1) {
                game = new Game(playersNicknames, numOfPlayers);    //MULTIPLAYER
                view.printOrder(playersNicknames);

                for (Player player : game.getPlayerList()) {
                    view.printHand(player.getHandIDs(), player.getNickname());
                    view.printLeaderCardRequest(player.getNickname());
                }
            }
            else {
                game = new Game(playersNicknames.get(0));   //SINGLE PLAYER
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
        Gson gson = new Gson();

        Message deserializedMex = gson.fromJson(mex, Message.class);
        Command command = deserializedMex.getCommand();
        String senderNick = deserializedMex.getSenderNickname();

        int playerNumber = game.getPlayerListString().indexOf(senderNick);

        System.out.println("Player number: " + playerNumber);

        switch (command){

            case SETUP_CONTAINER:
                SendContainer sendContainer1 =  gson.fromJson(mex, SendContainer.class);

                addSetUpContainerToPlayer(playerNumber, sendContainer1.getContainer(), sendContainer1.getDestinationID(), senderNick);

                break;

            case SEND_CONTAINER:
                SendContainer sendContainer =  gson.fromJson(mex, SendContainer.class);
                System.out.println("Arrivato: " + sendContainer);
                break;

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


                if(checkIfAllLeadersHaveBeenDiscarded() && !game.isSinglePlayer())
                    askForResources();

                break;

            case SHOW_DEPOSIT:
                view.printReply_uni(game.getPlayer(playerNumber).getPlayerBoard().getDeposit().toString(), senderNick);
                break;

            default:
                System.out.println("Invalid command, we are in controller update");
                break;
        }
    }

    /**
     * Checks if nick is the current player
     */
    public boolean isTheCurrentPlayer(String nick) {
        if (!game.getPlayerList().get(game.getCurrentPlayer()).getNickname().equals(nick)){
            view.printReply_uni("Not the current Player", nick);
            return false;
        }
        return true;
    }

    /**
     * Check if every player in game has already discarded 2 LeaderCards
     */
    public boolean checkIfAllLeadersHaveBeenDiscarded() {
        for (Player player : game.getPlayerList()) {
            if(!player.isLeadersHaveBeenDiscarded())
                return false;
        }
        return true;
    }

    /**
     * Method used for sending messages about the gameSetUpPhase to every player
     */
    private void askForResources() {

        for (Player player : game.getPlayerList()) {

            switch (player.getOrderID()) {
                case 0:
                    player.setReady(true);
                    view.printReply_uni("Please wait for the other players to select their bonus resources",player.getNickname());
                    break;

                case 1:
                    view.askForResources(player.getNickname(), 1);
                    break;

                case 2:
                    view.askForResources(player.getNickname(), 1);
                    view.notifyFaithPathProgression( 1, player.getNickname());
                    break;

                case 3:
                    view.askForResources(player.getNickname(), 2);
                    view.notifyFaithPathProgression( 1, player.getNickname());
                    break;
            }
        }
    }


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


    public boolean addContainerToPlayer(int currPlayer, ResourceContainer container, int depositSlotID, String currNickname) {

        try {
            game.getPlayer(currPlayer).getDepositSlotByID(depositSlotID).canAddToDepositSlot(container);

            game.getPlayer(currPlayer).getDepositSlotByID(depositSlotID).addToDepositSlot(container);
            view.printReply_uni(container.getQty() + " " + container.getResourceType().toString() + " has been added to your deposit slot N: " + depositSlotID, currNickname);
            return true;

        } catch (DifferentResourceType | DepositSlotMaxDimExceeded | ResourceTypeAlreadyStored | IndexOutOfBoundsException exception) {

            view.printReply_uni(exception.getMessage(), currNickname);
            return false;
        }
    }

    private void startGame() {
        game.startGame();

        view.printReply("---THE GAME HAS BEEN STARTED---\n\tHAVE FUN");
        //PRINT VARIE DI TUTTE LE PORCHERIE
    }

    //GETTERS-----------------------------------------------------------------------------------------------------------
    public View getView() {
        return view;
    }

    public Game getGame() {
        return game;
    }
}