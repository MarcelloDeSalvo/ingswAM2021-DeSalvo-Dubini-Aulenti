package it.polimi.ingsw.view;


import com.google.gson.Gson;
import it.polimi.ingsw.network.UserManager;
import it.polimi.ingsw.network.commands.Command;
import it.polimi.ingsw.network.commands.Message;
import it.polimi.ingsw.network.commands.ShowHandMessage;
import it.polimi.ingsw.network.commands.Target;
import it.polimi.ingsw.network.server.Status;
import it.polimi.ingsw.network.server.User;
import it.polimi.ingsw.observers.*;
import it.polimi.ingsw.view.cli.Color;


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

    //OBSERVER VIEW IO-------(RECEIVED DATA)----------------------------------------------------------------------------
    @Override
    public void update(String mex){
        Gson gson = new Gson();
        Message deserializedMex = gson.fromJson(mex, Message.class);
        Command command=deserializedMex.getCommand();

        //Command command = deserializedMex.getCommand();
        String senderNick = deserializedMex.getSenderNickname();

        if(!UserManager.isNamePresent(connectedPlayers, senderNick))
            return;

        User currentUser = connectedPlayers.get(senderNick);


        if(!Command.canUseCommand(currentUser,command)){
            if(currentUser.getStatus()==Status.IN_GAME) {
                UserManager.notifyUsers(connectedPlayers,
                        new Message.MessageBuilder().setCommand(Command.REPLY).
                                setInfo("You can't use this command in game!").setNickname(senderNick).build());
            }
            return;
        }

        if(command.getWhereToProcess()!=Status.IN_GAME)
            return;


        notifyController(mex);
    }

    public boolean hasPermission (User user) {
        return user.getStatus() == Status.IN_GAME;
    }
    //------------------------------------------------------------------------------------------------------------------


    //CONTROLLER OBSERVER-----------------------------------------------------------------------------------------------
    @Override
    public void addObserverController(ObserverController obs) {
        if(obs!=null)
            observerControllers.add(obs);
    }

    @Override
    public void notifyController(String message) {
        for (ObserverController obs: observerControllers) {
            obs.update(message);
        }
    }
    //------------------------------------------------------------------------------------------------------------------


    //OBSERVER MODEL-------(VV OBSERVES THE MODEL)----------------------------------------------------------------------
    @Override
    public void printHello() {
        UserManager.notifyUsers(connectedPlayers,
                new Message.MessageBuilder().setCommand(Command.REPLY).setInfo("Hello!").build());
    }

    @Override
    public void printQuit(String nickname) {
        UserManager.notifyUsers(connectedPlayers,
                new Message.MessageBuilder().setCommand(Command.REPLY).setInfo("Bye!").setNickname(nickname).build());
    }

    @Override
    public void printReply(String payload) {

    }

    @Override
    public void printHand(ArrayList<Integer> leaderIDs, String nickname) {
        notifyUsers(new ShowHandMessage(leaderIDs, nickname));
    }

    @Override
    public void printLobby(ArrayList<String> lobbiesInfos) {

    }

    public void printOrder(ArrayList<String> randomOrder) {
        StringBuilder orderBuild = new StringBuilder();
        orderBuild.append("This is the Turn Order \n");
        for (int i = 0; i<randomOrder.size(); i++){
            orderBuild.append(i+1).append(": ").append(randomOrder.get(i)).append(" \n");
        }

        Message orderMex = new Message.MessageBuilder().setCommand(Command.REPLY).setInfo(orderBuild.toString()).setTarget(Target.BROADCAST).build();
        notifyUsers(orderMex);
    }

    public void printLeaderCardRequest(String nickname) {
        UserManager.notifyUsers(connectedPlayers,
                new Message.MessageBuilder().setCommand(Command.REPLY)
                        .setInfo("Please select 2 Leader Cards to discard by typing 'DISCARD_LEADER id'").setNickname(nickname).build());
    }

    public void askForResources(String nickname, int qty) {
            notifyUsers(new Message.MessageBuilder().setCommand(Command.REPLY)
                    .setInfo("Please select " + qty + " type of resources of your choice by typing 'SELECT ResourceType Deposit DepositID'")
                        .setNickname(nickname).build());
    }

    public void notifyFaithPathProgression(String nickname, int qty) {
        notifyUsers(new Message.MessageBuilder().setCommand(Command.REPLY) //AL POSTO DI Command.REPLY DOVREMMO METTERE QUELLO CHE MOSTRA IL TRACCIATO FEDE IN CLI
                .setInfo("Your current position has been incremented of " + qty + Color.ANSI_RED.escape() + " FAITH POINT" + Color.ANSI_RESET.escape())
                    .setNickname(nickname).build());

        notifyUsers(new Message.MessageBuilder().setCommand(Command.REPLY) //AL POSTO DI Command.REPLY DOVREMMO METTERE QUELLO CHE MOSTRA IL TRACCIATO FEDE IN CLI
                .setInfo(nickname + "'s position has been incremented of " + qty + Color.ANSI_RED.escape() + " FAITH POINT" + Color.ANSI_RESET.escape())
                    .setTarget(Target.EVERYONE_ELSE).setNickname(nickname).build());
    }

    //------------------------------------------------------------------------------------------------------------------


}