package it.polimi.ingsw.view.gui;


import com.google.gson.Gson;
import it.polimi.ingsw.model.cards.Colour;
import it.polimi.ingsw.model.cards.ProductionAbility;
import it.polimi.ingsw.model.resources.ResourceContainer;
import it.polimi.ingsw.model.resources.ResourceType;
import it.polimi.ingsw.network.commands.*;
import it.polimi.ingsw.network.server.User;
import it.polimi.ingsw.view.ClientView;
import it.polimi.ingsw.view.gui.panels.*;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class Gui extends ClientView {

    private final Gson gson ;
    private final String nicknameTemp = null;

    private JPanel mainPanel;
    private CardLayout cardLayout;      //used for changing scenes

    private LoginPanel loginPanel;
    private LobbyPanel lobbyPanel;
    private LobbyRoomPanel lobbyRoomPanel;

    private JFrame frame;
    private Label infoLabel;

    public Gui() throws FileNotFoundException{
        super();
        gson = new Gson();

        SwingUtilities.invokeLater(this::createAndShowGUI);
    }

    public void createAndShowGUI(){
        frame = new JFrame("MASTER OF RENAISSANCE");
        frame.setSize(1200,800);
        //frame.pack();
        frame.setResizable(false);

        mainPanel = new JPanel();
        cardLayout = new CardLayout();
        mainPanel.setLayout(cardLayout);

        loginPanel = new LoginPanel(this);

        infoLabel = new Label("");
        infoLabel.setSize(30,30);
        infoLabel.setBackground(Color.GRAY);
        infoLabel.setForeground(Color.BLACK);

        mainPanel.add(loginPanel, "1");
        cardLayout.show(mainPanel, "1");

        frame.add(mainPanel, BorderLayout.CENTER);
        frame.add(infoLabel, BorderLayout.PAGE_END);

        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void printLobby(ArrayList<LobbyListMessage.LobbyInfo> lobbyInfos) {
        //mainPanel.removeAll();

        lobbyPanel = new LobbyPanel(this, lobbyInfos);

        mainPanel.add(lobbyPanel, "2");
        cardLayout.show(mainPanel, "2");

        //mainPanel.validate();
        //mainPanel.repaint();
        //mainPanel.setVisible(true);
    }

    public void printWaitingRoom(StringsMessage stringsMessage){
        //mainPanel.removeAll();

        lobbyRoomPanel = new LobbyRoomPanel(this);
        printPlayerList(stringsMessage.getInfo(), stringsMessage.getData());

        mainPanel.add(lobbyRoomPanel, "3");
        cardLayout.show(mainPanel, "3");

        //mainPanel.validate();
        //mainPanel.repaint();
        //mainPanel.setVisible(true);
    }

    @Override
    public void printPlayerList(String info, ArrayList<String> names) {
        lobbyRoomPanel.printPlayerList(info, names);
    }

    @Override
    public void printHello() {
        /*frame.setSize(1920,980);
        frame.setLocationRelativeTo(null);

        System.out.println("hello");
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        PlayerBoardPanel pl1 = new PlayerBoardPanel(this);
        PlayerBoardPanel pl2 = new PlayerBoardPanel(this);
        PlayerBoardPanel pl3 = new PlayerBoardPanel(this);
        PlayerBoardPanel pl4 = new PlayerBoardPanel(this);

        panel.add(pl1, BorderLayout.PAGE_START);
        panel.add(pl2, BorderLayout.PAGE_END);
        panel.add(pl3, BorderLayout.LINE_START);
        panel.add(pl4, BorderLayout.LINE_END);

        mainPanel.add(panel, "5");
        cardLayout.show(mainPanel, "5");*/
    }

    @Override
    public void printQuit(String nickname) {

    }

    @Override
    public void printReply(String payload) {
        if (frame!=null)
            infoLabel.setText(payload);
    }

    @Override
    public void printReply_uni(String payload, String nickname) {
        printReply(payload);
    }

    @Override
    public void printReply_everyOneElse(String payload, String nickname) {
        printReply(payload);
    }

    @Override
    public void printItsYourTurn(String nickname) {

    }

    @Override
    public void askForResources(String nickname, int qty) {

    }

    @Override
    public void askForLeaderCardID(String nickname) {

    }

    @Override
    public void askForMarketDestination(ArrayList<ResourceContainer> containers, String nickname) {

    }

    @Override
    public void notifyCardsInHand(ArrayList<Integer> leaderIDs, String nickname) {
        frame.setSize(1920,980);
        frame.setLocationRelativeTo(null);

        //mainPanel.removeAll();
        DiscardHandPanel discardLeaders = new DiscardHandPanel(leaderIDs);

        //discardLeaders.setVisible(true);

        //mainPanel.add(discardLeaders);
        //mainPanel.revalidate();
        //mainPanel.repaint();

        mainPanel.add(discardLeaders, "4");
        cardLayout.show(mainPanel, "4");
    }

    @Override
    public void notifyBuyOk(String nickname, int slotID, int cardID) {

    }

    @Override
    public void notifyGameSetup(ArrayList<Integer> cardGridIDs, ArrayList<String> nicknames, ArrayList<ResourceContainer> marketSetUp) {

    }

    @Override
    public void notifyLeaderDiscarded(int id, String nickname) {

    }

    @Override
    public void notifyLeaderActivated(int id, String nickname) {

    }

    @Override
    public void notifyProductionOk(String senderNick) {

    }

    @Override
    public void notifyMoveOk(String senderNick) {

    }

    @Override
    public void notifyMarketUpdate(String selection, int selected) {

    }

    @Override
    public void notifyLastTurn() {

    }

    @Override
    public void notifyWinner(ArrayList<String> winners) {

    }

    @Override
    public void notifyScores(List<Integer> playersTotalVictoryPoints, ArrayList<String> nicknames) {

    }

    @Override
    public void notifyGameEnded() {

    }

    @Override
    public void onDisconnect(User user) {

    }

    @Override
    public void notifyCardRemoved(int amount, Colour color, int level) {

    }

    @Override
    public void notifyCardGridChanges(int oldID, int newID) {

    }

    @Override
    public void notifyNewDepositSlot(int maxDim, ResourceType resourceType, String senderNick) {

    }

    @Override
    public void notifyDepositChanges(int id, ResourceContainer resourceContainer, boolean added, String senderNick) {

    }

    @Override
    public void notifyCurrentPlayerIncrease(int faithpoints, String nickname) {

    }

    @Override
    public void notifyOthersIncrease(int faithpoints, String nickname) {

    }

    @Override
    public void notifyPapalFavour(ArrayList<Integer> playerFavours, String senderNick) {

    }

    @Override
    public void notifyNewProductionSlot(ProductionAbility productionAbility, String senderNick) {

    }

    @Override
    public void notifyVaultChanges(ResourceContainer container, boolean added, String senderNick) {

    }

    @Override
    public boolean readInput() {
        int exit = 0;
        while (exit ==0){
            //CASE
        }
        return false;
    }

    @Override
    public void readUpdates(String mex){
        Message deserializedMex = gson.fromJson(mex, Message.class);

        Command command = deserializedMex.getCommand();
        String senderNick = deserializedMex.getSenderNickname();

        switch (command){

            case CHAT_ALL:
                //System.out.print(it.polimi.ingsw.view.cli.Color.ANSI_PURPLE.escape() + senderNick + " in ALL chat:" + Color.ANSI_RESET);
                printReply(deserializedMex.getInfo());
                break;

            case CHAT:
                ChatMessage chatMessage = gson.fromJson(mex, ChatMessage.class);
                //System.out.print(Color.ANSI_BLUE.escape() + chatMessage.getReceiver() + Color.ANSI_RESET + " whispers you: ");
                printReply(deserializedMex.getInfo());
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
                loginPanel.setVisible(false);
                this.setNickname(nicknameTemp);
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

            /*case JOIN_LOBBY:
                //printWaitingRoom();
                break;*/

            case EXIT_LOBBY:
                //printLobby();
                break;

            case SHOW_TURN_HELP:
                printItsYourTurn(senderNick);
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
                FaithPathUpdateMessage faithPathUpdateMessage= gson.fromJson(mex,FaithPathUpdateMessage.class);
                notifyCurrentPlayerIncrease(faithPathUpdateMessage.getId(), senderNick);
                break;

            case NOTIFY_FAITHPATH_OTHERS:
                FaithPathUpdateMessage faithPathUpdateOthersMessage= gson.fromJson(mex,FaithPathUpdateMessage.class);
                notifyOthersIncrease(faithPathUpdateOthersMessage.getId(), senderNick);
                break;

            case NOTIFY_FAITHPATH_FAVOURS:
                PapalFavourUpdateMessage papalFavourUpdateMessage=gson.fromJson(mex, PapalFavourUpdateMessage.class);
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

            case ASK_FOR_RESOURCES:
                askForResources(senderNick, 0);
                printReply(deserializedMex.getInfo());
                break;

            case DISCARD_OK:
                IdMessage idMessage =gson.fromJson(mex, IdMessage.class);
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
}
