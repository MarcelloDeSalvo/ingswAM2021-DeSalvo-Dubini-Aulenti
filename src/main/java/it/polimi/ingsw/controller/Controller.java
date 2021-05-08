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
import it.polimi.ingsw.view.VirtualView;
import it.polimi.ingsw.view.cli.Color;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

public class Controller implements ObserverController {

    private VirtualView virtualView;
    private Game game;

    public Controller (HashMap<String, User> connectedPlayers){
        this.virtualView = new VirtualView(connectedPlayers);
        virtualView.addObserverController(this);

        ArrayList<String> playersNicknames = new ArrayList<>(connectedPlayers.keySet());
        int numOfPlayers = playersNicknames.size();

        try {
            if(numOfPlayers > 1) {
                game = new Game(playersNicknames, numOfPlayers);    //MULTIPLAYER
                virtualView.printOrder(playersNicknames);

                for (Player player : game.getPlayerList()) {
                    virtualView.printHand(player.getHandIDs(), player.getNickname());
                    virtualView.printLeaderCardRequest(player.getNickname());
                }
            }
            else {
                game = new Game(playersNicknames.get(0));   //SINGLE PLAYER
                virtualView.printHand(game.getPlayer(0).getHandIDs(), game.getPlayer(0).getNickname());
                virtualView.printLeaderCardRequest(game.getPlayer(0).getNickname());
            }

        }catch (FileNotFoundException e){
            e.printStackTrace();
            virtualView.notifyUsers(
                    new Message.MessageBuilder().setCommand(Command.REPLY).setInfo("Cannot read the configuration file of the game").build());
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

                System.out.println("Arrivato: " + sendContainer1);
                break;

            case SEND_CONTAINER:
                SendContainer sendContainer =  gson.fromJson(mex, SendContainer.class);
                System.out.println("Arrivato: " + sendContainer);
                break;

            case DISCARD_LEADER:
                DiscardLeaderMessage discardLeaderMessage = gson.fromJson(mex, DiscardLeaderMessage.class);

                /*if(!isTheCurrentPlayer(senderNick))
                    return;     useful if we decide to setup with turns instead of in parallel*/

                int currP = game.getCurrentPlayer();

                if(game.getPlayer(playerNumber).isLeadersHaveBeenDiscarded()){
                    virtualView.notifyUsers(new Message.MessageBuilder().setCommand(Command.REPLY).setInfo("You can't do this action because you already discarded 2 Leaders! Please wait for the other players to to so")
                            .setNickname(senderNick).build());
                    return;
                }

                if (!game.getPlayer(playerNumber).discardFromHand(discardLeaderMessage.getLeaderID())) {
                    virtualView.notifyUsers(new Message.MessageBuilder().setCommand(Command.REPLY).setInfo("Wrong Leader ID").setNickname(senderNick).build());
                    return;
                }

                virtualView.notifyUsers(new Message.MessageBuilder().setCommand(Command.REPLY).setInfo("The leader has been discarded correctly!").setNickname(senderNick).build());

                if(checkIfAllLeadersHaveBeenDiscarded() && !game.isSinglePlayer())
                    askForResources();

                break;


            default:
                System.out.println("Invalid command, siamo in controller update");
                break;
        }
    }

    public boolean isTheCurrentPlayer(String nick) {
        if (!game.getPlayerList().get(game.getCurrentPlayer()).getNickname().equals(nick)){
            virtualView.notifyUsers(new Message.MessageBuilder().setCommand(Command.REPLY).setInfo("Not the current Player").build());
            return false;
        }
        return true;
    }

    public boolean checkIfAllLeadersHaveBeenDiscarded() {
        for (Player player : game.getPlayerList()) {
            if(!player.isLeadersHaveBeenDiscarded())
                return false;
        }
        return true;
    }

    private void askForResources() {
        for (Player player : game.getPlayerList()) {

            switch (player.getOrderID()) {
                case 0:
                    player.setReady(true);
                    virtualView.notifyUsers(new Message.MessageBuilder().setCommand(Command.REPLY)
                            .setInfo("Please wait for the other players to select their bonus resources").setNickname(player.getNickname()).build());
                    break;

                case 1:
                    virtualView.askForResources(player.getNickname(), 1);
                    break;

                case 2:
                    virtualView.askForResources(player.getNickname(), 1);
                    virtualView.notifyFaithPathProgression(player.getNickname(), 1);
                    //game.addFaithPoints();
                    break;

                case 3:
                    virtualView.askForResources(player.getNickname(), 2);
                    virtualView.notifyFaithPathProgression(player.getNickname(), 1);
                    break;
            }
        }
    }


    public void addSetUpContainerToPlayer(int currPlayerNum, ResourceContainer container, int depositSlotID, String currNickname) {
        Player currPlayer = game.getPlayer(currPlayerNum);

        System.out.println("Sono in addSetUpContainer: "+ container.getResourceType() + container.getQty() + " Deposit slot: " + depositSlotID);

        switch (currPlayerNum) {
            case 0:
                virtualView.notifyUsers(new Message.MessageBuilder().setCommand(Command.REPLY)
                        .setInfo("You are the first player so you cannot select a bonus resource!").setNickname(currNickname).build());
                break;

            case 3:
                if(currPlayer.getSelectedResources() == 2) {
                    virtualView.notifyUsers(new Message.MessageBuilder().setCommand(Command.REPLY)
                            .setInfo("You already selected your bonus resources! Please wait for the other players to do so").setNickname(currNickname).build());
                    break;
                }

                if(addContainerToPlayer(currPlayerNum, container, depositSlotID, currNickname))
                    currPlayer.incrementSelectedResources();
                break;

            default:
                if(currPlayer.getSelectedResources() == 1) {
                    virtualView.notifyUsers(new Message.MessageBuilder().setCommand(Command.REPLY)
                            .setInfo("You already selected your bonus resource! Please wait for the other players to do so").setNickname(currNickname).build());
                    break;
                }

                if(addContainerToPlayer(currPlayerNum, container, depositSlotID, currNickname))
                    currPlayer.incrementSelectedResources();
                break;
        }

        for (Player player : game.getPlayerList()) {

            System.out.println(player.isReady());

            if(!player.isReady())
                return;
        }

        startGame();
    }


    public boolean addContainerToPlayer(int currPlayer, ResourceContainer container, int depositSlotID, String currNickname) {

        try {
            game.getPlayer(currPlayer).getDepositSlotByID(depositSlotID).canAddToDepositSlot(container);

            game.getPlayer(currPlayer).getDepositSlotByID(depositSlotID).addToDepositSlot(container);

            virtualView.notifyUsers(new Message.MessageBuilder().setCommand(Command.REPLY)
                    .setInfo(container.getQty() + " " + container.getResourceType().toString() + " has been added to your deposit slot N: " + depositSlotID)
                        .setNickname(currNickname).build());

            return true;

        } catch (DifferentResourceType | DepositSlotMaxDimExceeded | ResourceTypeAlreadyStored | IndexOutOfBoundsException exception) {

            virtualView.notifyUsers(new Message.MessageBuilder().setCommand(Command.REPLY)
                    .setInfo(exception.getMessage()).setNickname(currNickname).build());

            return false;
        }
    }

    private void startGame() {
        game.startGame();

        System.out.println("in start game");

        virtualView.notifyUsers(new Message.MessageBuilder().setCommand(Command.REPLY)
                .setInfo("---THE GAME HAS BEEN STARTED---\n\tHAVE FUN").setTarget(Target.BROADCAST).build());

        //PRINT VARIE DI TUTTE LE PORCHERIE
    }

    //GETTERS-----------------------------------------------------------------------------------------------------------
    public VirtualView getVirtualView() {
        return virtualView;
    }

    public Game getGame() {
        return game;
    }
}