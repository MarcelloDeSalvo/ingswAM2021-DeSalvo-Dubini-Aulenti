package it.polimi.ingsw.view;

import it.polimi.ingsw.model.cards.Colour;
import it.polimi.ingsw.model.cards.ProductionAbility;
import it.polimi.ingsw.model.resources.ResourceContainer;
import it.polimi.ingsw.model.resources.ResourceType;
import it.polimi.ingsw.network.ServerArea;
import it.polimi.ingsw.network.UserManager;
import it.polimi.ingsw.network.commands.*;
import it.polimi.ingsw.network.server.Status;
import it.polimi.ingsw.network.server.User;
import it.polimi.ingsw.observers.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VirtualView implements View, ServerArea {

    private final HashMap<String, User> connectedPlayers;
    private final ArrayList<ObserverController> observerControllers;
    private String currPlayer;

    /**
     * Test Constructor
     */
    public VirtualView() {
        this.connectedPlayers = new HashMap<>();
        observerControllers = new ArrayList<>();
    }

    public VirtualView(HashMap<String, User> connectedPlayers) {
        this.connectedPlayers = new HashMap<>(connectedPlayers);
        observerControllers = new ArrayList<>();
        for (String userNick: connectedPlayers.keySet()) {
            connectedPlayers.get(userNick).addServerArea(this);
        }
    }

    /**
     * Calls the user manager that sends a message to the connected players
     */
    public void notifyUsers(Message message){
        UserManager.notifyUsers(connectedPlayers, message);
    }

    /**
     * Remove all the users from the game and tells the lobby that the game is finished
     */
    private void removeAllUsers(){
        boolean first = true;
        Message endGameMex = new Message.MessageBuilder().setCommand(Command.END_GAME).build();

        for (String nick: connectedPlayers.keySet()) {
            connectedPlayers.get(nick).setStatus(Status.IN_LOBBY);
            if(first)
                connectedPlayers.get(nick).notifyServerAreas(endGameMex.getCommand(), endGameMex.serialize());
            first = false;
            connectedPlayers.get(nick).removeServerArea(this);
        }
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


    @Override
    public boolean hasPermission (User user, Command command) {
        if(!Command.canUseCommand(user, command)){

            if(user.getStatus() == Status.IN_GAME) {
                UserManager.notifyUsers(connectedPlayers,
                        new Message.MessageBuilder().setCommand(Command.REPLY).
                                setInfo("You can't use this command in game!").setNickname(user.getNickname()).build());
            }

            return false;
        }

        return true;
    }

    @Override
    public synchronized void onDisconnect(User user) {
        notifyUsers(new Message.MessageBuilder().setCommand(Command.USER_LEFT_GAME).setTarget(Target.EVERYONE_ELSE).setNickname(user.getNickname()).build());
        removeAllUsers();
    }

    @Override
    public void notifyGameCreationError(String error) {
        notifyUsers(new Message.MessageBuilder().setCommand(Command.GAME_CREATION_ERROR).setTarget(Target.BROADCAST).build());
        removeAllUsers();
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
                new Message.MessageBuilder().setCommand(Command.ASK_LEADER_ID)
                        .setNickname(nickname).build());
    }

    @Override
    public void askForResources(String nickname, int qty) {
        notifyUsers(new IdMessage(Command.ASK_SETUP_RESOURCES, qty, nickname));

    }

    @Override
    public void askForMarketDestination(List<ResourceContainer> containers, String nickname){
        UserManager.notifyUsers(connectedPlayers, new ContainerArrayListMessage(Command.ASK_MARKET_DEST,containers,nickname));
    }

    @Override
    public void askMultipleConversion(int numToConvert, ResourceType typeToConvert, List<ResourceType> availableConversion) {
       notifyUsers( new AskConversionMessage (new Message.MessageBuilder().setCommand(Command.ASK_MULTIPLE_CONVERSION).setNickname(currPlayer),
               typeToConvert, availableConversion, numToConvert));
    }
    //------------------------------------------------------------------------------------------------------------------


    //NOTIFIES----------------------------------------------------------------------------------------------------------
    @Override
    public void notifyGameSetup(List<Integer> cardGridIDs, List<String> nicknames, List<ResourceContainer> marketSetUp){
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
    public void notifyPapalFavour(List<Integer> playerFavours, String nickname) {
        notifyUsers(new PapalFavourUpdateMessage(playerFavours,nickname));
    }

    @Override
    public void notifyCardGridChanges(int oldID, int newID){
        notifyUsers(new NotifyCardGrid(oldID, newID));
    }

    @Override
    public void notifyCardsInHand(List<Integer> leaderIDs, String nickname) {
        notifyUsers(new ArrayListIntegerMessage(Command.NOTIFY_HAND,leaderIDs, nickname));
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
    public void notifyLorenzoAction(int actionID, Colour colour) {
        notifyUsers(new LorenzoActionMessage(actionID, colour, Target.BROADCAST));
    }

    @Override
    public void notifyBuyOk(String nickname, int slotID, int cardID) {
        notifyUsers(new BuyMessage(Command.BUY_OK, cardID, slotID, nickname));
    }

    @Override
    public void notifyBuySlotOk(List<ResourceContainer> price) {
        notifyUsers(new ContainerArrayListMessage(Command.BUY_SLOT_OK, price, currPlayer));
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
    public void notifyStartFilling(int productionID, int qmi, int qmo, String senderNick) {
        notifyUsers(new FillMessage(productionID, qmi, qmo, senderNick));
    }

    @Override
    public void notifyFillOk(int productionID, String senderNick) {
        notifyUsers(new IdMessage(Command.FILL_OK, productionID, senderNick));
    }

    @Override
    public void notifyProductionPrice(List<ResourceContainer> resourcesPrice, String senderNick) {
        notifyUsers(new ContainerArrayListMessage(Command.PRODUCTION_PRICE, resourcesPrice, senderNick));
    }

    @Override
    public void notifyMoveOk(String senderNick) {
        notifyUsers(new Message.MessageBuilder().setCommand(Command.MANAGE_DEPOSIT_OK).setNickname(senderNick).build());
    }

    @Override
    public void notifyRemoveContainerError(String error) {
        notifyUsers(new Message.MessageBuilder().setCommand(Command.REMOVE_CONTAINER_ERROR).setNickname(currPlayer).setInfo(error).build());
    }

    @Override
    public void notifyRemoveContainerOk(String ok) {
        notifyUsers(new Message.MessageBuilder().setCommand(Command.REMOVE_CONTAINER_OK).setNickname(currPlayer).setInfo(ok).build());
    }

    @Override
    public void notifyMarketUpdate(String selection, int selected) {
        MarketMessage marketMessage = new MarketMessage(selection, selected, currPlayer);
        printReply_everyOneElse(currPlayer + " has extracted the "+ marketMessage.getSelection() + " "
                + marketMessage.getNum() + " from the market!", currPlayer);
        notifyUsers(marketMessage);
    }

    @Override
    public void notifyConversionError(String error) {
        notifyUsers(new Message.MessageBuilder().setCommand(Command.CONVERSION_ERROR).setInfo(error).build());
    }

    @Override
    public void notifyMarketOk(String senderNick) {
        notifyUsers(new Message.MessageBuilder().setCommand(Command.MARKET_OK).setNickname(senderNick).build());
    }

    @Override
    public void notifyNewProductionSlot(ProductionAbility productionAbility, String senderNick) {
        notifyUsers(
                new NewProductionSlotMessage(
                new Message.MessageBuilder().setCommand(Command.NOTIFY_NEW_PROD_SLOT).setTarget(Target.BROADCAST).setNickname(currPlayer),
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
    public void notifyScores(List<Integer> playersTotalVictoryPoints, List<String> nicknames, List<String> winners){
        notifyUsers(new ScoreMessage(playersTotalVictoryPoints, nicknames, winners));
    }

    @Override
    public void notifyGameEnded(){
        notifyUsers(new Message.MessageBuilder().setTarget(Target.BROADCAST).setCommand(Command.END_GAME).build());
        removeAllUsers();
    }
    //------------------------------------------------------------------------------------------------------------------

    @Override
    public Status getAreaStatus() {
        return Status.IN_GAME;
    }

    public void setCurrPlayer(String currPlayer) {
        this.currPlayer = currPlayer;
    }
}