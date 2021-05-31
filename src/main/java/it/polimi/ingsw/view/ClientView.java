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
import it.polimi.ingsw.view.gui.panels.ResourceSelectionPanel;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class ClientView implements View, UserInput {
    private boolean isInGame = false;
    private String nickname;
    private final ArrayList<ObserverController> observerControllers;

    private final ArrayList<LeaderCard> leaderCards;
    private final ArrayList<DevelopmentCard> developmentCards;

    private final LiteFaithPath liteFaithPath;
    private LiteCardGrid liteCardGrid;
    private LiteMarket liteMarket;

    private Gson gson;

    private HashMap<String,LitePlayerBoard> liteBoards;

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
        notifyController(stringToSend, null, null);
    }

    public void litePlayerBoardsSetUp(ArrayList<String> nicknames){
        this.liteBoards=new HashMap<>();
        for (String nic:nicknames) {
            liteBoards.put(nic,new LitePlayerBoard(leaderCards, developmentCards));
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

    @Override
    public void update(String mex, Command command, String senderNick) {
        //OFFLINE NOT YET IMPLEMENTED
        readUpdates(mex);
    }

    //READ UPDATES------------------------------------------------------------------------------------------------------
    @Override
    public void readUpdates(String mex){
        Message deserializedMex = gson.fromJson(mex, Message.class);

        Command command = deserializedMex.getCommand();
        String senderNick = deserializedMex.getSenderNickname();

        switch (command){

            case CHAT_ALL:
                printChatMessage(senderNick, deserializedMex.getInfo(), true);
                break;

            case CHAT:
                printChatMessage(senderNick, deserializedMex.getInfo(), false);
                break;

            case HELLO:
                printHello();
                break;

            case PING:
                send(new Message.MessageBuilder().setCommand(Command.PONG).
                        setNickname(this.getNickname()).build());
                break;

            case REPLY:
                printReply(deserializedMex.getInfo());
                break;

            case LOGIN:
                onLogin(deserializedMex.getInfo());
                break;

            case RECONNECTED:
                onReconnected();
                break;

            case GAME_SETUP:
                GameSetUp gameSetUp=gson.fromJson(mex,GameSetUp.class);
                notifyGameSetup(gameSetUp.getCardGridIDs(), gameSetUp.getNicknames(),gameSetUp.getMarketSetUp());
                break;

            case LOBBY_LIST:
                LobbyListMessage lobbyListMessage = gson.fromJson(mex, LobbyListMessage.class);
                printLobby(lobbyListMessage.getLobbiesInfos());
                break;

            case PLAYER_LIST:
                StringsMessage stringsMessage = gson.fromJson(mex, StringsMessage.class);
                printWaitingRoom(stringsMessage);
                break;

            case JOIN_LOBBY:
                onJoinLobby();
                printReply(deserializedMex.getInfo());
                break;

            case USER_JOINED_LOBBY:
                printReply(deserializedMex.getInfo());
                break;

            case USER_LEFT_LOBBY:
                printReply(deserializedMex.getInfo());
                break;

            case EXIT_LOBBY:
                onExitLobby();
                printReply(deserializedMex.getInfo());
                break;

            case SHOW_TURN_HELP:
                printItsYourTurn(senderNick);
                break;

            case NOTIFY_GAME_STARTED:
                notifyGameIsStarted();
                break;

            case NOTIFY_CARDGRID:
                NotifyCardGrid notifyCardGrid = gson.fromJson(mex, NotifyCardGrid.class);
                notifyCardGridChanges(notifyCardGrid.getOldID(), notifyCardGrid.getNewID());
                break;

            case NOTIFY_HAND:
                ShowHandMessage showHandMessage = gson.fromJson(mex, ShowHandMessage.class);
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

            case PICK_FROM_MARKET:
                MarketMessage marketMessage=gson.fromJson(mex,MarketMessage.class);
                notifyMarketUpdate(marketMessage.getSelection(),marketMessage.getNum());
                break;

            case NOTIFY_NEW_DEPOSIT:
                NewDepositMessage newDepositMessage = gson.fromJson(mex, NewDepositMessage.class);
                notifyNewDepositSlot(newDepositMessage.getMaxDim(), newDepositMessage.getResourceType(), senderNick);
                break;

            case NOTIFY_DEPOSIT_UPDATE:
                SendContainer depositUpdate = gson.fromJson(mex, SendContainer.class);
                notifyDepositChanges(depositUpdate.getDestinationID(), depositUpdate.getContainer(), depositUpdate.isAdded(), senderNick);
                break;

            case NOTIFY_NEW_PRODSLOT:
                NewProductionSlotMessage newProductionSlotMessage = gson.fromJson(mex, NewProductionSlotMessage.class);
                notifyNewProductionSlot(newProductionSlotMessage.getProductionAbility(), senderNick);
                break;

            case ASK_MARKET_DEST:
                printReply(deserializedMex.getInfo());
                break;

            case ASK_SETUP_RESOURCES:
                askForResources(senderNick, 0);
                printReply(deserializedMex.getInfo());
                break;

            case DISCARD_OK:
                IdMessage idMessage = gson.fromJson(mex, IdMessage.class);
                notifyLeaderDiscarded(idMessage.getId(),"");
                break;

            case ACTIVATE_OK:
                IdMessage activatedLeader = gson.fromJson(mex, IdMessage.class);
                notifyLeaderActivated(activatedLeader.getId(), activatedLeader.getSenderNickname());
                break;

            case BUY_OK:
                BuyMessage buyMessage = gson.fromJson(mex, BuyMessage.class);
                notifyBuyOk(senderNick, buyMessage.getProductionSlotID(), buyMessage.getCardID());
                break;

            case PRODUCE_OK:
                notifyProductionOk(senderNick);
                break;

            case MANAGE_DEPOSIT_OK:
                notifyMoveOk(senderNick);
                break;

            case END_GAME:
                notifyGameEnded();
                break;

        }
    }
    //------------------------------------------------------------------------------------------------------------------


    //CLI AND GUI ------------------------------------------------------------------------------------------------------
    public abstract void onLogin(String info);
    public abstract void onReconnected();
    public abstract void onExitLobby();
    public abstract void onJoinLobby();

    public abstract void printOrder();
    public abstract void printPlayerList(String info, ArrayList<String> names);
    public abstract void printLobby(ArrayList<LobbyListMessage.LobbyInfo> lobbyInfos);
    public abstract void printWaitingRoom(StringsMessage stringsMessage);
    public abstract void printChatMessage(String senderNick, String info, boolean all);
    //------------------------------------------------------------------------------------------------------------------




    //GETTER AND SETTER METHODS FOR LITE MODEL--------------------------------------------------------------------------
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public ArrayList<LeaderCard> getLeaderCards() {
        return leaderCards;
    }

    public ArrayList<DevelopmentCard> getDevelopmentCards() { return developmentCards; }

    public void setMyHand(LiteHand hand) {
        liteBoards.get(nickname).setLiteHand(hand);
    }

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
    //------------------------------------------------------------------------------------------------------------------

}
