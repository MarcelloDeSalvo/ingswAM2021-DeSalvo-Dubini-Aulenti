package it.polimi.ingsw.view;

import com.google.gson.Gson;
import it.polimi.ingsw.liteModel.*;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.parser.DevelopmentCardParser;
import it.polimi.ingsw.model.parser.FaithPathSetUpParser;
import it.polimi.ingsw.model.parser.LeaderCardParser;
import it.polimi.ingsw.network.commands.*;
import it.polimi.ingsw.observers.ObserverController;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class ClientView implements View, UserInput {

    private boolean isInGame = false;
    private boolean singlePlayer = false;

    private String nickname;
    private final List<ObserverController> observerControllers;

    private final List<LeaderCard> leaderCards;
    private final List<DevelopmentCard> developmentCards;

    private final LiteFaithPath liteFaithPath;
    private LiteCardGrid liteCardGrid;
    private LiteMarket liteMarket;
    private HashMap<String,LitePlayerBoard> liteBoards;

    private int counter = 0;

    private final Gson gson;

    private final Message pong = new Message.MessageBuilder().setCommand(Command.PONG).
            setNickname(this.getNickname()).build();

    public ClientView() throws FileNotFoundException {
        leaderCards = LeaderCardParser.deserializeLeaderList();
        developmentCards = DevelopmentCardParser.deserializeDevelopmentList();
        liteFaithPath = FaithPathSetUpParser.deserializeLiteFaithPathSetUp();
        observerControllers = new ArrayList<>();
        this.gson = new Gson();
    }

    @Override
    public void send(Message mex){
        String stringToSend = mex.serialize();
        notifyController(stringToSend, mex.getCommand(), nickname);
    }

    public void litePlayerBoardsSetUp(List<String> nicknames){
        this.liteBoards=new HashMap<>();
        for (String nic:nicknames) {
            liteBoards.put(nic, new LitePlayerBoard(leaderCards, developmentCards));
        }
    }


    //OBSERVERS --------------------------------------------------------------------------------------------------------
    @Override
    public void addObserverController(ObserverController observerController) {
        if(observerController!=null)
            observerControllers.add(observerController);
    }

    @Override
    public void notifyController(String mex, Command command, String senderNick) {
        for (ObserverController obs: observerControllers) {
            obs.update(mex,command, senderNick);
        }
    }

    //READ UPDATES------------------------------------------------------------------------------------------------------
    @Override
    public void readUpdates(String mex){
        Message deserializedMex = gson.fromJson(mex, Message.class);
        Command command = deserializedMex.getCommand();
        String senderNick = deserializedMex.getSenderNickname();

        switch (command){

            case PING:
                System.out.println("PONG "+counter+ " to be sent: " + pong.toString());
                send(pong);
                System.out.println("PONG "+counter+ " sent: " + pong.toString());
                counter++;
                break;

            case JOIN_LOBBY:
            case EXIT_LOBBY:
            case USER_JOINED_LOBBY:
            case USER_LEFT_LOBBY:
            case REPLY:
                printReply(deserializedMex.getInfo());
                break;

            case HELLO:
                printHello();
                break;

            case LOGIN:
                onLogin(deserializedMex.getInfo());
                System.out.println(nickname);
                break;

            case RECONNECTED:
                onReconnected();
                break;

            case CHAT_ALL:
                printChatMessage(senderNick, deserializedMex.getInfo(), true);
                break;

            case CHAT:
                printChatMessage(senderNick, deserializedMex.getInfo(), false);
                break;

            case LOBBY_LIST:
                LobbyListMessage lobbyListMessage = gson.fromJson(mex, LobbyListMessage.class);
                printLobby(lobbyListMessage.getLobbiesInfos());
                break;

            case PLAYER_LIST:
                StringsMessage stringsMessage = gson.fromJson(mex, StringsMessage.class);
                printWaitingRoom(stringsMessage);
                break;

            case GAME_CREATION_ERROR:
                notifyGameCreationError(deserializedMex.getInfo());
                break;

            case GAME_SETUP:
                GameSetUp gameSetUp=gson.fromJson(mex,GameSetUp.class);
                notifyGameSetup(gameSetUp.getCardGridIDs(), gameSetUp.getNicknames(),gameSetUp.getMarketSetUp());
                break;

            case NOTIFY_GAME_STARTED:
                notifyGameIsStarted();
                break;

            case SHOW_TURN_HELP:
                printTurnHelp(senderNick);
                break;

            case NOTIFY_TURN_CHANGE:
                printItsYourTurn(senderNick);
                break;

            case NOTIFY_CARDGRID:
                NotifyCardGrid notifyCardGrid = gson.fromJson(mex, NotifyCardGrid.class);
                notifyCardGridChanges(notifyCardGrid.getOldID(), notifyCardGrid.getNewID());
                break;

            case NOTIFY_HAND:
                ArrayListIntegerMessage showHandMessage = gson.fromJson(mex, ArrayListIntegerMessage.class);
                notifyCardsInHand(showHandMessage.getCardsID(), senderNick);
                break;

            case NOTIFY_FAITHPATH_CURRENT:
                FaithPathUpdateMessage faithPathUpdateMessage = gson.fromJson(mex,FaithPathUpdateMessage.class);
                notifyCurrentPlayerIncrease(faithPathUpdateMessage.getId(), senderNick);
                break;

            case NOTIFY_FAITHPATH_OTHERS:
                FaithPathUpdateMessage faithPathUpdateOthersMessage = gson.fromJson(mex,FaithPathUpdateMessage.class);
                notifyOthersIncrease(faithPathUpdateOthersMessage.getId(), senderNick);
                break;

            case NOTIFY_FAITHPATH_FAVOURS:
                PapalFavourUpdateMessage papalFavourUpdateMessage = gson.fromJson(mex, PapalFavourUpdateMessage.class);
                notifyPapalFavour(papalFavourUpdateMessage.getPlayerFavours(), senderNick);
                break;

            case NOTIFY_VAULT_UPDATE:
                SendContainer vaultChanges = gson.fromJson(mex, SendContainer.class);
                notifyVaultChanges(vaultChanges.getContainer(), vaultChanges.isAdded(), senderNick);
                break;

            case NOTIFY_NEW_DEPOSIT:
                NewDepositMessage newDepositMessage = gson.fromJson(mex, NewDepositMessage.class);
                notifyNewDepositSlot(newDepositMessage.getMaxDim(), newDepositMessage.getResourceType(), senderNick);
                break;

            case NOTIFY_DEPOSIT_UPDATE:
                SendContainer depositUpdate = gson.fromJson(mex, SendContainer.class);
                notifyDepositChanges(depositUpdate.getDestinationID(), depositUpdate.getContainer(), depositUpdate.isAdded(), senderNick);
                break;

            case NOTIFY_NEW_PROD_SLOT:
                NewProductionSlotMessage newProductionSlotMessage = gson.fromJson(mex, NewProductionSlotMessage.class);
                notifyNewProductionSlot(newProductionSlotMessage.getProductionAbility(), senderNick);
                break;

            case NOTIFY_SCORES:
                ScoreMessage scoreMessage = gson.fromJson(mex, ScoreMessage.class);
                notifyScores(scoreMessage.getPlayersTotalVictoryPoints(), scoreMessage.getNicknames(), scoreMessage.getWinners());
                break;

            case USER_LEFT_GAME:
                onUserLeftGame();
                break;

            case REMOVE_CONTAINER_OK:
                notifyRemoveContainerOk(deserializedMex.getInfo());
                break;

            case REMOVE_CONTAINER_ERROR:
                notifyRemoveContainerError(deserializedMex.getInfo());
                break;

            case BUY_OK:
                BuyMessage buyMessage = gson.fromJson(mex, BuyMessage.class);
                notifyBuyOk(senderNick, buyMessage.getProductionSlotID(), buyMessage.getCardID());
                break;

            case BUY_ERROR:
                notifyBuyError(deserializedMex.getInfo());
                break;

            case PRODUCE_OK:
                notifyProductionOk(senderNick);
                break;

            case PRODUCTION_PRICE:
                ContainerArrayListMessage priceMessage = gson.fromJson(mex, ContainerArrayListMessage.class);
                notifyProductionPrice(priceMessage.getContainers(), senderNick);
                break;

            case PRODUCE_ERROR:
                notifyProductionError(deserializedMex.getInfo(), senderNick);
                break;

            case DISCARD_OK:
                IdMessage idMessage = gson.fromJson(mex, IdMessage.class);
                notifyLeaderDiscarded(idMessage.getId(),"");
                break;

            case ACTIVATE_OK:
                IdMessage activatedLeader = gson.fromJson(mex, IdMessage.class);
                notifyLeaderActivated(activatedLeader.getId(), activatedLeader.getSenderNickname());
                break;

            case PICK_FROM_MARKET:
                MarketMessage marketMessage = gson.fromJson(mex, MarketMessage.class);
                notifyMarketUpdate(marketMessage.getSelection(), marketMessage.getNum());
                break;

            case MARKET_OK:
                notifyMarketOk(senderNick);
                break;

            case BUY_SLOT_OK:
                ContainerArrayListMessage priceContainerMessage = gson.fromJson(mex, ContainerArrayListMessage.class);
                notifyBuySlotOk(priceContainerMessage.getContainers());
                break;

            case MANAGE_DEPOSIT_OK:
                notifyMoveOk(senderNick);
                break;

            case START_FILL:
                FillMessage idFill = gson.fromJson(mex, FillMessage.class);
                notifyStartFilling(idFill.getSlotID(), idFill.getQmi(), idFill.getQmo(), senderNick);
                break;

            case FILL_OK:
                IdMessage idFillOk = gson.fromJson(mex, IdMessage.class);
                notifyFillOk(idFillOk.getId(), senderNick);
                break;

            case CONVERSION_ERROR:
                notifyConversionError(deserializedMex.getInfo());
                break;

            case ASK_LEADER_ID:
                askForLeaderCardID(senderNick);
                break;

            case ASK_MARKET_DEST:
                ContainerArrayListMessage containerArrayListMessage = gson.fromJson(mex,ContainerArrayListMessage.class);
                askForMarketDestination(containerArrayListMessage.getContainers(), this.getNickname());
                break;

            case ASK_SETUP_RESOURCES:
                IdMessage idMessage1 = gson.fromJson(mex, IdMessage.class);
                askForResources(senderNick, idMessage1.getId());
                break;

            case ASK_MULTIPLE_CONVERSION:
                AskConversionMessage askConversionMessage = gson.fromJson(mex, AskConversionMessage.class);
                askMultipleConversion(askConversionMessage.getNumToConvert(), askConversionMessage.getConvertedType(), askConversionMessage.getAvailableConversions());
                break;

            case NOTIFY_LORENZO_ACTION:
                LorenzoActionMessage lorenzoActionMessage = gson.fromJson(mex, LorenzoActionMessage.class);
                notifyLorenzoAction(lorenzoActionMessage.getActionID(), lorenzoActionMessage.getColour());
                break;

            case END_GAME:
                notifyGameEnded();
                break;
        }
    }
    //------------------------------------------------------------------------------------------------------------------


    //CLI AND GUI ------------------------------------------------------------------------------------------------------
    /**
     * Called when the user receives the login reply
     * @param info the info contained in the reply message
     */
    public abstract void onLogin(String info);

    /**
     * Called after a disconnection from the server
     */
    public abstract void onDisconnected();

    /**
     * Called after a reconnection to the server
     */
    public abstract void onReconnected();

    /**
     * Returns to the lobby after someone left the game
     */
    public abstract void onUserLeftGame();

    /**
     * Prints the turn order of the game
     */
    public abstract void printOrder();

    /**
     * Prints all connected players' nicknames in the current lobby
     */
    public abstract void printPlayerList(String info, List<String> names);

    /**
     * Prints the list of all available lobbies
     * @param lobbyInfos the mex that contains all the information regarding a lobby
     */
    public abstract void printLobby(List<LobbyListMessage.LobbyInfo> lobbyInfos);

    /**
     * Prints all connected players' nicknames in the current lobby right after the player joins
     */
    public abstract void printWaitingRoom(StringsMessage stringsMessage);

    /**
     * Prints the chat message
     */
    public abstract void printChatMessage(String senderNick, String info, boolean all);
    //------------------------------------------------------------------------------------------------------------------


    //GETTER AND SETTER METHODS FOR LITE MODEL--------------------------------------------------------------------------
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public List<LeaderCard> getLeaderCards() {
        return leaderCards;
    }

    public List<DevelopmentCard> getDevelopmentCards() { return developmentCards; }

    public void setMyHand(LiteHand hand) { liteBoards.get(nickname).setLiteHand(hand); }

    public LiteHand getMyHand() { return liteBoards.get(nickname).getLiteHand();}

    public LiteVault getMyLiteVault() {
        return liteBoards.get(nickname).getLiteVault();
    }

    public LiteDeposit getMyLiteDeposit() {
        return  liteBoards.get(nickname).getLiteDeposit();
    }

    public LiteHand getSomeonesHand(String nickname){ return liteBoards.get(nickname).getLiteHand();}

    public LiteVault getSomeonesLiteVault(String nickname) { return liteBoards.get(nickname).getLiteVault();  }

    public LiteDeposit getSomeonesLiteDeposit(String nickname) {return  liteBoards.get(nickname).getLiteDeposit(); }

    public LiteProduction getSomeonesLiteProduction(String nickname) { return liteBoards.get(nickname).getLiteProduction(); }

    public LiteProduction getMyLiteProduction() {  return liteBoards.get(nickname).getLiteProduction(); }

    public LitePlayerBoard getLitePlayerBoard(String nickname){   return liteBoards.get(nickname); }

    public LiteMarket getLiteMarket(){return liteMarket;}

    public void setLiteMarket(LiteMarket liteMarket) { this.liteMarket = liteMarket; }

    public LiteCardGrid getLiteCardGrid() { return liteCardGrid; }

    public void setLiteCardGrid(LiteCardGrid liteCardGrid) { this.liteCardGrid = liteCardGrid; }

    public LiteFaithPath getLiteFaithPath() { return liteFaithPath;  }

    public boolean isInGame() { return isInGame; }

    public void setInGame(boolean inGame) {  isInGame = inGame; }

    public boolean isSinglePlayer() { return singlePlayer; }

    public void setSinglePlayer(boolean singlePlayer) { this.singlePlayer = singlePlayer; }

    //------------------------------------------------------------------------------------------------------------------

}
