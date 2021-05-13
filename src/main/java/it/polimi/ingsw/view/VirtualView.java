package it.polimi.ingsw.view;


import com.google.gson.Gson;
import it.polimi.ingsw.model.Cardgrid;
import it.polimi.ingsw.model.Market;
import it.polimi.ingsw.model.player.Vault;
import it.polimi.ingsw.model.player.deposit.Deposit;
import it.polimi.ingsw.model.player.production.ProductionSite;
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

public class VirtualView implements View {

    private final HashMap<String, User> connectedPlayers;
    private final ArrayList<ObserverController> observerControllers;

    public VirtualView(HashMap<String, User> connectedPlayers) {
        this.connectedPlayers = new HashMap<>(connectedPlayers);
        observerControllers = new ArrayList<>();
    }

    /**
     * Calls the user manager that sends a message to the connected players
     */
    public void notifyUsers(Message message){
        UserManager.notifyUsers(connectedPlayers, message);
    }

    //OBSERVER VIEW IO-------(RECEIVED DATA)----------------------------------------------------------------------------
    @Override
    public void update(String mex){
        Gson gson = new Gson();
        Message deserializedMex = gson.fromJson(mex, Message.class);
        Command command = deserializedMex.getCommand();

        String senderNick = deserializedMex.getSenderNickname();

        if(!UserManager.isNamePresent(connectedPlayers, senderNick))
            return;

        User currentUser = connectedPlayers.get(senderNick);


        if(!hasPermission(currentUser, command))
            return;


        notifyController(mex);
    }

    /**
     * Checks if the user has a specific level of permission
     * @param user user to check
     * @return true if this is the case, false otherwise
     */
    public boolean hasPermission (User user, Command command) {
        if(!Command.canUseCommand(user, command)){

            if(user.getStatus() == Status.IN_GAME) {
                UserManager.notifyUsers(connectedPlayers,
                        new Message.MessageBuilder().setCommand(Command.REPLY).
                                setInfo("You can't use this command in game!").setNickname(user.getNickname()).build());
            }

            return false;
        }

        return command.getWhereToProcess() == Status.IN_GAME;
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
    public void printReply_uni(String payload, String nickname) {
        UserManager.notifyUsers(connectedPlayers,
                new Message.MessageBuilder().setCommand(Command.REPLY).setInfo(payload).setNickname(nickname).build());
    }

    @Override
    public void printReply_everyOneElse(String payload, String nickname) {
        UserManager.notifyUsers(connectedPlayers,
                new Message.MessageBuilder().setCommand(Command.REPLY).setInfo(payload).setNickname(nickname).setTarget(Target.EVERYONE_ELSE).build());
    }

    @Override
    public void printReply(String payload) {
        UserManager.notifyUsers(connectedPlayers,
                new Message.MessageBuilder().setCommand(Command.REPLY).setInfo(payload).setTarget(Target.BROADCAST).build());
    }

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
    public void printHand(ArrayList<Integer> leaderIDs, String nickname) {
        notifyUsers(new ShowHandMessage(leaderIDs, nickname));
    }

    @Override
    public void printLobby(ArrayList<String> lobbiesInfos) {

    }

    @Override
    public void printOrder(ArrayList<String> randomOrder) {
        StringBuilder orderBuild = new StringBuilder();
        orderBuild.append("This is the Turn Order \n");
        for (int i = 0; i<randomOrder.size(); i++){
            orderBuild.append(i+1).append(": ").append(randomOrder.get(i)).append(" \n");
        }
        Message orderMex = new Message.MessageBuilder().setCommand(Command.REPLY).setInfo(orderBuild.toString()).setTarget(Target.BROADCAST).build();
        notifyUsers(orderMex);
    }

    @Override
    public void printItsYourTurn(String nickname){
        notifyUsers(new Message.MessageBuilder().setCommand(Command.SHOW_TURN_HELP).setNickname(nickname).setTarget(Target.UNICAST).build());
        printReply_everyOneElse("@ It is "+ nickname +"'s turn", nickname);
    }

    @Override
    public void printDeposit(Deposit deposit, String nickname) {
        printReply_uni(deposit.toString(), nickname);
    }

    public void printVault(Vault vault, String nickname) {
        printReply_uni(vault.toString(), nickname);
    }

    public void printProduction(ProductionSite productionSite, String nickname) {
        printReply_uni(productionSite.toString(), nickname);
    }

    public void printMarket(Market market, String nickname){
        printReply_uni(market.toString(), nickname);
    }

    public void printCardGrid(Cardgrid cardgrid, String nickname){
        printReply_uni(cardgrid.toString(), nickname);
    }

    @Override
    public void printLeaderCardRequest(String nickname) {
        UserManager.notifyUsers(connectedPlayers,
                new Message.MessageBuilder().setCommand(Command.REPLY)
                        .setInfo("Please select 2 Leader Cards to discard by typing 'DISCARD id'").setNickname(nickname).build());
    }

    @Override
    public void askForResources(String nickname, int qty) {
        notifyUsers(new Message.MessageBuilder().setCommand(Command.REPLY)
                    .setInfo("Please select " + qty + " type of resources of your choice by typing 'SELECT ResourceType Deposit DepositID'")
                        .setNickname(nickname).build());
    }

    @Override
    public void notifyFaithPathProgression(int qty, String nickname) {

        notifyUsers(new Message.MessageBuilder().setCommand(Command.REPLY) //AL POSTO DI Command.REPLY DOVREMMO METTERE QUELLO CHE MOSTRA IL TRACCIATO FEDE IN CLI
                .setInfo("Your current position has been incremented by " + qty + Color.ANSI_RED.escape() + " FAITH POINT" + Color.ANSI_RESET.escape())
                    .setNickname(nickname).build());

        notifyUsers(new Message.MessageBuilder().setCommand(Command.REPLY) //AL POSTO DI Command.REPLY DOVREMMO METTERE QUELLO CHE MOSTRA IL TRACCIATO FEDE IN CLI
                .setInfo(nickname + "'s position has been incremented by " + qty + Color.ANSI_RED.escape() + " FAITH POINT" + Color.ANSI_RESET.escape())
                    .setTarget(Target.EVERYONE_ELSE).setNickname(nickname).build());
    }

    //------------------------------------------------------------------------------------------------------------------


}