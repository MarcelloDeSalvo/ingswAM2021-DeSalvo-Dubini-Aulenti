package it.polimi.ingsw.view;


import com.google.gson.Gson;
import it.polimi.ingsw.model.Market;
import it.polimi.ingsw.model.cards.Colour;
import it.polimi.ingsw.model.player.deposit.Deposit;
import it.polimi.ingsw.model.player.production.ProductionSite;
import it.polimi.ingsw.model.resources.ResourceContainer;
import it.polimi.ingsw.network.UserManager;
import it.polimi.ingsw.network.commands.*;
import it.polimi.ingsw.network.server.Status;
import it.polimi.ingsw.network.server.User;
import it.polimi.ingsw.observers.*;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VirtualView implements View {

    private final HashMap<String, User> connectedPlayers;
    private final ArrayList<ObserverController> observerControllers;
    private final Gson gson;
    private String currPlayer;

    public VirtualView() {
        this.connectedPlayers = new HashMap<>();
        observerControllers = new ArrayList<>();
        gson = new Gson();
    }

    public VirtualView(HashMap<String, User> connectedPlayers) {
        this.connectedPlayers = new HashMap<>(connectedPlayers);
        observerControllers = new ArrayList<>();
        gson = new Gson();
    }

    /**
     * Calls the user manager that sends a message to the connected players
     */
    public void notifyUsers(Message message){
        UserManager.notifyUsers(connectedPlayers, message);
    }

    //OBSERVER VIEW IO-------(RECEIVED DATA)----------------------------------------------------------------------------
    @Override
    public synchronized void update(String mex, Command command, String senderNick){

        if(!UserManager.isNamePresent(connectedPlayers, senderNick))
            return;

        User currentUser = connectedPlayers.get(senderNick);

        if(!hasPermission(currentUser, command))
            return;

        notifyController(mex, command, senderNick);
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

    @Override
    public synchronized void onDisconnect(User user) {

    }
    //------------------------------------------------------------------------------------------------------------------



    //CONTROLLER OBSERVER-----------------------------------------------------------------------------------------------
    @Override
    public void addObserverController(ObserverController obs) {
        if(obs!=null)
            observerControllers.add(obs);
    }

    @Override
    public void notifyController(String mex, Command command, String senderNick) {
        for (ObserverController obs: observerControllers) {
            obs.update(mex, command, senderNick);
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
        currPlayer = nickname;
        notifyUsers(new Message.MessageBuilder().setCommand(Command.SHOW_TURN_HELP).setNickname(nickname).setTarget(Target.UNICAST).build());
        printReply_everyOneElse("@ It is "+ nickname +"'s turn", nickname);
    }

    public void printTurnHelp(String nickname){
        notifyUsers(new Message.MessageBuilder().setCommand(Command.SHOW_TURN_HELP).setNickname(nickname).setTarget(Target.UNICAST).build());
    }

    @Override
    public void printDeposit(Deposit deposit, String nickname) {
        printReply_uni(deposit.toString(), nickname);
    }

    public void printProduction(ProductionSite productionSite, String nickname) {
        printReply_uni(productionSite.toString(), nickname);
    }

    public void printMarket(Market market, String nickname){
        printReply_uni(market.toString(), nickname);
    }

    public void printMarketOut(ArrayList<ResourceContainer> containers, String nickname){
        StringBuilder marketOutChoice = new StringBuilder("Where do you want to put: ");
        for (ResourceContainer res: containers) {
            marketOutChoice.append(res.getResourceType()).append("  ");
        }
        printReply_uni(marketOutChoice.toString(), nickname);
    }

    //------------------------------------------------------------------------------------------------------------------


    //SERVER REQUESTS---------------------------------------------------------------------------------------------------
    @Override
    public void askForLeaderCardID(String nickname) {
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
    //------------------------------------------------------------------------------------------------------------------


    //NOTIFIES----------------------------------------------------------------------------------------------------------
    @Override
    public void notifyGameSetup(ArrayList<Integer> cardGridIDs, ArrayList<String> nicknames,ArrayList<ResourceContainer> marketSetUp){
        notifyUsers(new GameSetUp(cardGridIDs,nicknames,marketSetUp));
    }

    @Override
    public void notifyCurrentPlayerIncrease(int qty, String nickname) {
        notifyUsers(new FaithPathUpdateMessage(Command.NOTIFY_FAITHPATH_CURRENT, qty, nickname));
    }

    @Override
    public void notifyOthersIncrease(int faithPoints, String nickname) {
        notifyUsers(new FaithPathUpdateMessage(Command.NOTIFY_FAITHPATH_OTHERS, faithPoints, nickname));
    }

    @Override
    public void notifyPapalFavour(ArrayList<Integer> playerFavours, String nickname) {
        notifyUsers(new PapalFavourUpdateMessage(playerFavours,nickname));

    }

    @Override
    public void notifyBoughtCard(String nickname) {
        notifyUsers(new Message.MessageBuilder().setCommand(Command.BUY_OK).setTarget(Target.BROADCAST)
                .setNickname(nickname).build());
    }

    @Override
    public void notifyCardGridChanges(int oldID, int newID){
        notifyUsers(new NotifyCardGrid(oldID, newID));
    }

    @Override
    public void notifyCardsInHand(ArrayList<Integer> leaderIDs, String nickname) {
        notifyUsers(new ShowHandMessage(leaderIDs, nickname));
    }

    @Override
    public void notifyLeaderDiscarded(int id, String nickname){
        notifyUsers(new IdMessage(Command.DISCARD_OK, id, nickname));
    }

    @Override
    public void notifyLeaderActivated(int id, String nickname){
        notifyUsers(new IdMessage(Command.ACTIVATE_OK, id, nickname));
        printReply_everyOneElse(nickname + " has activated the " + id + " ID leader!", nickname);
    }

    @Override
    public void notifyCardRemoved(int amount, Colour color, int level) {
        printReply("LORENZO has removed "+ amount+ " "+color+ " development cards with level = " + level);
    }

    @Override
    public void notifyProductionOk(String senderNick) {
        notifyUsers(new Message.MessageBuilder().setCommand(Command.PRODUCE_OK).setNickname(senderNick).build());
    }

    @Override
    public void notifyMarketUpdate(String selection, int selected) {

    }

    @Override
    public void notifyVaultChanges(ResourceContainer container, boolean added, String nick) {
        notifyUsers(new SendContainer(Command.NOTIFY_VAULT_UPDATE, container, currPlayer, added));
    }

    @Override
    public void notifyLastTurn(){
        printReply("# THIS IS THE LAST TURN");
    }

    @Override
    public void notifyWinner(ArrayList<String> winners){
        String winnerz="";
        for (String winner : winners) {
            winnerz+=winner;
            winnerz+=" ";
        }
        printReply("[#-_- Winners: "+ winnerz +" -_-#]");
    }

    @Override
    public void notifyScores(List<Integer> playersTotalVictoryPoints){
        printReply("Scoreboard\n"+playersTotalVictoryPoints.toString());
    }

    @Override
    public void notifyGameEnded(){
        //CICLO I PLAYER E VEDO SE SONO DISCONNESSI, SE LO SONO GLI CHIAMO LA ON DISCONNECT CON STATUS IN LOBBY MANAGER
        String nickname = null;

        notifyUsers(new Message.MessageBuilder().setTarget(Target.BROADCAST).setCommand(Command.END_GAME).build());

        for (String nick: connectedPlayers.keySet()) {
            connectedPlayers.get(nick).setStatus(Status.IN_LOBBY);
            connectedPlayers.get(nick).removeServerArea(this);
            nickname = nick;
        }

        System.out.println(nickname);

        connectedPlayers.get(nickname).notifyEndGame(new Message.MessageBuilder().setCommand(Command.END_GAME).build());
    }
    //------------------------------------------------------------------------------------------------------------------


}