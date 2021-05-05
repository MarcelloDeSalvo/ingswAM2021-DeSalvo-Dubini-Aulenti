package it.polimi.ingsw.controller;

import com.google.gson.Gson;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.network.commands.Command;
import it.polimi.ingsw.network.commands.DiscardLeaderMessage;
import it.polimi.ingsw.network.commands.Message;
import it.polimi.ingsw.network.commands.Target;
import it.polimi.ingsw.observers.ObserverController;
import it.polimi.ingsw.view.VirtualView;

import java.io.FileNotFoundException;

public class Controller implements ObserverController {

    VirtualView virtualView;
    Game game;

    public Controller (){
        this.virtualView = new VirtualView();

        try {
            game = new Game();
        }catch (FileNotFoundException e){
            e.printStackTrace();
            virtualView.notifyUsers(
                    new Message.MessageBuilder().setCommand(Command.REPLY).setInfo("Cannot read the configuration file of the game").build());
        }
    }

    public Controller(VirtualView virtualView) {
        this.virtualView = virtualView;
        try {
            game = new Game();
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

}