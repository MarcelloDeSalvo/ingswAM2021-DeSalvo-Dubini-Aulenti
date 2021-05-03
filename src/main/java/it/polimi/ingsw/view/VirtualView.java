package it.polimi.ingsw.view;


import it.polimi.ingsw.network.commands.Command;
import it.polimi.ingsw.network.commands.Message;
import it.polimi.ingsw.network.commands.Target;
import it.polimi.ingsw.observers.*;


import java.util.ArrayList;
import java.util.HashMap;

public class VirtualView implements View, ObservableViewIO, ObserverViewIO, ObservableController, ObserverModel {

    HashMap<String,ObserverViewIO> players;
    ArrayList<ObserverController> observerControllers;

    public VirtualView() {
        players = new HashMap<>();
        observerControllers = new ArrayList<>();
    }

    //OBSERVER VIEW IO-------(RECEIVED DATA)-----------------------------------------------------------------------------------
    @Override
    public void update(Message mex){
        Command command = mex.getCommand();

        switch (command){
            case QUIT:
                printQuit();
                break;
            case HELLO:
                printHello();
                break;

            default:
                notifyController(mex);
                break;
        }
    }
    //------------------------------------------------------------------------------------------------------------------


    //OBSERVABLE VIEW IO---------(SEND DATA)---------------------------------------------------------------------------
    @Override
    public void notifyIO(Message message) {
        switch (message.getTarget()){
            case UNICAST:
                if (isNamePresent(message.getSenderNickname()))
                    players.get(message.getSenderNickname()).update(message);
                break;

            case BROADCAST:
                for (String nick: players.keySet()) {
                    players.get(nick).update(message);
                }
                break;

            case EVERYONE_ELSE:
                for (String nick: players.keySet()) {
                    if (!nick.equals(message.getSenderNickname()))
                        players.get(nick).update(message);
                }
                break;
        }
    }
    //------------------------------------------------------------------------------------------------------------------

    //PLAYERSLIST MANAGEMENT -------------------------------------------------------------------------------------------
    @Override
    public boolean addPlayer(String nick, ObserverViewIO threadSender){
        if(!players.containsKey(nick)) {
            players.put(nick, threadSender);
            return true;
        }
        else
            return false;
    }

    @Override
    public boolean isNamePresent(String nick) {
        return players.containsKey(nick);
    }

    @Override
    public boolean removePlayer(String nick) {
        if (players.containsKey(nick)) {
            players.remove(nick);
            return true;
        }
        return false;
    }
    //------------------------------------------------------------------------------------------------------------------

    //CONTROLLER OBSERVER-----------------------------------------------------------------------------------------------
    @Override
    public void addObserverController(ObserverController obs) {
        if(obs!=null)
            observerControllers.add(obs);
    }

    @Override
    public void notifyController(Message message) {
        for (ObserverController obs: observerControllers) {
            obs.update(message);
        }
    }
    //------------------------------------------------------------------------------------------------------------------


    //OBSERVER MODEL-------(VV OBSERVES THE MODEL)----------------------------------------------------------------------
    @Override
    public void printHello() {
        notifyIO(new Message(Command.REPLY, "Bye!", Target.UNICAST));
    }

    @Override
    public void printQuit() {
        notifyIO(new Message(Command.REPLY, "Bye!", Target.UNICAST));
    }

    @Override
    public void printReply(String payload) {

    }

    @Override
    public void printHand(ArrayList<Integer> leaderIDs) {

    }

    //------------------------------------------------------------------------------------------------------------------


}