package it.polimi.ingsw.controller;

import com.google.gson.Gson;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.network.commands.Command;
import it.polimi.ingsw.network.commands.DiscardLeaderMessage;
import it.polimi.ingsw.network.commands.Message;
import it.polimi.ingsw.network.server.User;
import it.polimi.ingsw.observers.ObserverController;
import it.polimi.ingsw.view.VirtualView;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

public class Controller implements ObserverController {

    VirtualView virtualView;
    Game game;

    public Controller (HashMap<String, User> connectedPlayers){
        this.virtualView = new VirtualView(connectedPlayers);
        virtualView.addObserverController(this);

        ArrayList<String> playersNicknames = new ArrayList<>(connectedPlayers.keySet());
        int numOfPlayers = playersNicknames.size();

        try {
            if(numOfPlayers > 1){
                game = new Game(playersNicknames, numOfPlayers);    //MULTIPLAYER
                virtualView.printOrder(playersNicknames);
            }

            else
                game = new Game(playersNicknames.get(0));   //SINGLE PLAYER

        }catch (FileNotFoundException e){
            e.printStackTrace();
            virtualView.notifyUsers(
                    new Message.MessageBuilder().setCommand(Command.REPLY).setInfo("Cannot read the configuration file of the game").build());
        }
    }

    @Override
    public void update(Message mex) {
        Command command = mex.getCommand();
        Gson gson = new Gson();
        String original = mex.serialize();

        switch (command){

            case DISCARD_LEADER:
                DiscardLeaderMessage discardLeaderMessage = gson.fromJson(original, DiscardLeaderMessage.class);

                isTheCurrentPlayer(mex.getSenderNickname());

                int currP = game.getCurrentPlayer();

                if (!game.getPlayer(currP).discardFromHand(discardLeaderMessage.getLeaderID()))
                    virtualView.notifyUsers(new Message.MessageBuilder().setCommand(Command.REPLY).setInfo("Wrong Leader ID").build());

                break;

            default:
                System.out.println("Invalid command");
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

    public VirtualView getVirtualView() {
        return virtualView;
    }

    public Game getGame() {
        return game;
    }
}