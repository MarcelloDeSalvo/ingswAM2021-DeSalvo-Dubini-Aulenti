package it.polimi.ingsw.view;


import it.polimi.ingsw.network.UserManager;
import it.polimi.ingsw.network.commands.Command;
import it.polimi.ingsw.network.commands.Message;
import it.polimi.ingsw.network.commands.Target;
import it.polimi.ingsw.network.server.User;
import it.polimi.ingsw.observers.*;


import java.util.ArrayList;
import java.util.HashMap;

public class VirtualView implements View, ObserverViewIO, ObservableController, ObserverModel {

    HashMap<String, User> connectedPlayers;
    ArrayList<ObserverController> observerControllers;

    public VirtualView() {
        connectedPlayers = new HashMap<>();
        observerControllers = new ArrayList<>();
    }

    public void notifyUsers(Message message){
        UserManager.notifyUsers(connectedPlayers, message);
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
        UserManager.notifyUsers(connectedPlayers, new Message(Command.REPLY, "Bye!", Target.UNICAST));
    }

    @Override
    public void printQuit() {
        UserManager.notifyUsers(connectedPlayers, new Message(Command.REPLY, "Bye!", Target.UNICAST));
    }

    @Override
    public void printReply(String payload) {

    }

    @Override
    public void printHand(ArrayList<Integer> leaderIDs) {

    }

    //------------------------------------------------------------------------------------------------------------------


}