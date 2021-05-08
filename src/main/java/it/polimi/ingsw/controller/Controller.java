package it.polimi.ingsw.controller;

import com.google.gson.Gson;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.commands.Command;
import it.polimi.ingsw.network.commands.DiscardLeaderMessage;
import it.polimi.ingsw.network.commands.Message;
import it.polimi.ingsw.network.commands.SendContainer;
import it.polimi.ingsw.network.server.User;
import it.polimi.ingsw.observers.ObserverController;
import it.polimi.ingsw.view.VirtualView;

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

        switch (command){

            case SETUP_CONTAINER:
                SendContainer sendContainer1 =  gson.fromJson(mex, SendContainer.class);
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
                int playerNumber = game.getPlayerListString().indexOf(senderNick);

                if(game.getPlayer(playerNumber).isLeadersHaveBeenDiscarded()){
                    virtualView.notifyUsers(new Message.MessageBuilder().setCommand(Command.REPLY).setInfo("You can't do this action because you already discarded 2 Leaders!").setNickname(senderNick).build());
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


    //GETTERS-----------------------------------------------------------------------------------------------------------
    public VirtualView getVirtualView() {
        return virtualView;
    }

    public Game getGame() {
        return game;
    }
}