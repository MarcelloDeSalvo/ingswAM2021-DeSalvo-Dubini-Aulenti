package it.polimi.ingsw.view;

import it.polimi.ingsw.model.cards.Colour;
import it.polimi.ingsw.model.cards.ProductionAbility;
import it.polimi.ingsw.model.resources.ResourceContainer;
import it.polimi.ingsw.model.resources.ResourceType;
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
    private String currPlayer;

    public VirtualView() {
        this.connectedPlayers = new HashMap<>();
        observerControllers = new ArrayList<>();
    }

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
    public void printItsYourTurn(String nickname){
        currPlayer = nickname;
        notifyUsers(new Message.MessageBuilder().setCommand(Command.NOTIFY_TURN_CHANGE).setNickname(nickname).setTarget(Target.UNICAST).build());
        printReply_everyOneElse("@ It is " + nickname + "'s turn", nickname);
    }

    @Override
    public void printTurnHelp(String nickname){
        notifyUsers(new Message.MessageBuilder().setCommand(Command.SHOW_TURN_HELP).setNickname(nickname).setTarget(Target.UNICAST).build());
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
        notifyUsers(new Message.MessageBuilder().setCommand(Command.ASK_SETUP_RESOURCES)
                    .setInfo("Please select " + qty + " type of resources of your choice by typing 'SELECT ResourceType Deposit DepositID'")
                        .setNickname(nickname).build());

    }

    @Override
    public void askForMarketDestination(ArrayList<ResourceContainer> containers, String nickname){
        StringBuilder marketOutChoice = new StringBuilder("Now select where do you want to place them by typing >PUT ResourceType 'IN deposit' deposit_id").append("\n");
        marketOutChoice.append("Where do you want to put: ");
        for (ResourceContainer res: containers) {
            marketOutChoice.append(res.getResourceType()).append("  ");
        }

        UserManager.notifyUsers(connectedPlayers,
                new Message.MessageBuilder().setCommand(Command.ASK_MARKET_DEST)
                        .setInfo(marketOutChoice.toString()).setNickname(nickname).build());
    }
    //------------------------------------------------------------------------------------------------------------------


    //NOTIFIES----------------------------------------------------------------------------------------------------------
    @Override
    public void notifyGameSetup(ArrayList<Integer> cardGridIDs, ArrayList<String> nicknames,ArrayList<ResourceContainer> marketSetUp){
        notifyUsers(new GameSetUp(cardGridIDs,nicknames,marketSetUp));
    }

    @Override
    public void notifyGameIsStarted(){
        notifyUsers(new Message.MessageBuilder().setCommand(Command.NOTIFY_GAME_STARTED).setTarget(Target.BROADCAST).build());
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
        notifyUsers(new IdMessage(new Message.MessageBuilder().setNickname(nickname).setCommand(Command.ACTIVATE_OK).setTarget(Target.BROADCAST), id));
    }

    @Override
    public void notifyCardRemoved(int amount, Colour color, int level) {
        printReply("LORENZO has removed " + amount + " " + color + " development cards with level = " + level);
    }

    @Override
    public void notifyBuyOk(String nickname, int slotID, int cardID) {
        notifyUsers(new BuyMessage(Command.BUY_OK, cardID, slotID, nickname));
    }

    @Override
    public void notifyBuySlotOk(String mex) {
        notifyUsers(new Message.MessageBuilder().setCommand(Command.BUY_SLOT_OK).setInfo(mex).setNickname(currPlayer).build());
    }

    @Override
    public void notifyBuyError(String error) {
        notifyUsers(new Message.MessageBuilder().setCommand(Command.BUY_ERROR).setInfo(error).setNickname(currPlayer).build());
    }

    @Override
    public void notifyProductionOk(String senderNick) {
        notifyUsers(new Message.MessageBuilder().setCommand(Command.PRODUCE_OK).setTarget(Target.BROADCAST).setNickname(senderNick).build());
    }

    @Override
    public void notifyProductionError(String error, String senderNick) {
        notifyUsers(new Message.MessageBuilder().setCommand(Command.PRODUCE_ERROR).setInfo(error).setNickname(senderNick).build());
    }

    @Override
    public void notifyStartFilling(int productionID, String senderNick) {
        notifyUsers(new IdMessage(Command.START_FILL, productionID, senderNick));
    }

    @Override
    public void notifyFillOk(int productionID, String senderNick) {
        notifyUsers(new IdMessage(Command.FILL_OK, productionID, senderNick));
    }

    @Override
    public void notifyProductionPrice(HashMap<ResourceType, ResourceContainer> resourcesPrice, String senderNick) {
        notifyUsers(new PriceMessage(resourcesPrice, senderNick));
    }

    @Override
    public void notifyMoveOk(String senderNick) {
        notifyUsers(new Message.MessageBuilder().setCommand(Command.MANAGE_DEPOSIT_OK).setNickname(senderNick).build());
    }

    @Override
    public void notifyMarketUpdate(String selection, int selected) {

    }

    @Override
    public void notifyNewProductionSlot(ProductionAbility productionAbility, String senderNick) {
        notifyUsers(
                new NewProductionSlotMessage(
                new Message.MessageBuilder().setCommand(Command.NOTIFY_NEW_PRODSLOT).setTarget(Target.BROADCAST).setNickname(currPlayer),
                productionAbility)
        );
    }

    @Override
    public void notifyVaultChanges(ResourceContainer container, boolean added, String nick) {
        notifyUsers( new SendContainer(Command.NOTIFY_VAULT_UPDATE, container, currPlayer, added));
    }

    @Override
    public void notifyNewDepositSlot(int maxDim, ResourceType resourceType, String senderNick) {
        notifyUsers( new NewDepositMessage(
                new Message.MessageBuilder().setCommand(Command.NOTIFY_NEW_DEPOSIT).setTarget(Target.BROADCAST).
                setNickname(currPlayer), maxDim, resourceType));
    }

    @Override
    public void notifyDepositChanges(int id, ResourceContainer resourceContainer, boolean added, String senderNick) {
        notifyUsers( new SendContainer(Command.NOTIFY_DEPOSIT_UPDATE, resourceContainer, id, currPlayer, added));
    }

    @Override
    public void notifyLastTurn(){
        printReply("# THIS IS THE LAST ROUND");
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
    public void notifyScores(List<Integer> playersTotalVictoryPoints, ArrayList<String> nicknames){
        StringBuilder scoreboard = new StringBuilder("\n#_SCORE BOARD_#\n");
        int i = 0;
        for (String nick: nicknames) {
            scoreboard.append(" °-> ").append(nick);
            scoreboard.append(" scored { ").append(playersTotalVictoryPoints.get(i)).append(" } points").append("\n");
            i++;
        }
        printReply(scoreboard.toString());
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

        if (nickname!=null)
            connectedPlayers.get(nickname).notifyEndGame(new Message.MessageBuilder().setCommand(Command.END_GAME).build());
    }
    //------------------------------------------------------------------------------------------------------------------

    public String getCurrPlayer() {
        return currPlayer;
    }

    public void setCurrPlayer(String currPlayer) {
        this.currPlayer = currPlayer;
    }
}