package it.polimi.ingsw.view;


import com.google.gson.Gson;
import it.polimi.ingsw.network.UserManager;
import it.polimi.ingsw.network.commands.Command;
import it.polimi.ingsw.network.commands.Message;
import it.polimi.ingsw.network.server.User;
import it.polimi.ingsw.observers.*;


import java.util.ArrayList;
import java.util.HashMap;

public class VirtualView implements View, ObserverViewIO, ObservableController, ObserverModel {

    HashMap<String, User> connectedPlayers;
    ArrayList<ObserverController> observerControllers;

    public VirtualView(HashMap<String, User> connectedPlayers) {
        this.connectedPlayers = new HashMap<>(connectedPlayers);
        observerControllers = new ArrayList<>();
    }

    public void notifyUsers(Message message){
        UserManager.notifyUsers(connectedPlayers, message);
    }

    //OBSERVER VIEW IO-------(RECEIVED DATA)-----------------------------------------------------------------------------------
    @Override
    public void update(String mex){
        Gson gson = new Gson();
        Message deserializedMex = gson.fromJson(mex, Message.class);

        Command command = deserializedMex.getCommand();

        switch (command){
            case QUIT:
                printQuit();
                break;
            case HELLO:
                printHello();
                break;

            default:
                notifyController(deserializedMex);
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
        UserManager.notifyUsers(connectedPlayers, new Message.MessageBuilder().setCommand(Command.REPLY).setInfo("Hello!").build());
    }

    @Override
    public void printQuit() {
        UserManager.notifyUsers(connectedPlayers, new Message.MessageBuilder().setCommand(Command.REPLY).setInfo("Bye!").build());
    }

    @Override
    public void printReply(String payload) {

    }

    @Override
    public void printHand(ArrayList<Integer> leaderIDs) {

    }

    @Override
    public void printLobby(ArrayList<String> lobbiesInfos) {

    }

    //------------------------------------------------------------------------------------------------------------------


}