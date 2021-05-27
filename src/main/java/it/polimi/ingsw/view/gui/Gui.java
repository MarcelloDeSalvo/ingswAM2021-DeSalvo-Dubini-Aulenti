package it.polimi.ingsw.view.gui;


import com.google.gson.Gson;
import it.polimi.ingsw.model.cards.Colour;
import it.polimi.ingsw.model.cards.ProductionAbility;
import it.polimi.ingsw.model.resources.ResourceContainer;
import it.polimi.ingsw.model.resources.ResourceType;
import it.polimi.ingsw.network.commands.*;
import it.polimi.ingsw.network.server.User;
import it.polimi.ingsw.view.ClientView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class Gui extends ClientView {

    private final Gson gson ;
    private final String nicknameTemp = null;

    private JPanel jPanel_login;
    private JPanel jPanel_lobbies;
    private JFrame frame;

    public Gui() throws FileNotFoundException{
        super();
        gson = new Gson();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }



    public void createAndShowGUI(){
        frame = new JFrame("MASTER OF RENAISSANCE");
        frame.setResizable(false);

        //LOGIN PANEL------------------
        jPanel_login = new JPanel();

        JLabel label = new JLabel("WELCOME TO MASTER OF RENAISSANCE", JLabel.CENTER);
        jPanel_login.setBorder(BorderFactory.createEmptyBorder(300,300,300,300));
        jPanel_login.setLayout(new GridLayout(0,1));
        label.setOpaque(true);

        JTextField jTextField = new JTextField("Username..");
        jTextField.setBounds(50,100, 200,30);
        JButton loginButton = new JButton("Login");

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                send(new Message.MessageBuilder().setCommand(Command.LOGIN).setInfo(jTextField.getText()).build());
            }
        });

        jPanel_login.add(label);
        jPanel_login.add(jTextField);
        jPanel_login.add(loginButton);
        //------------------------------

        frame.add(jPanel_login, BorderLayout.CENTER);

        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public void printLobby(ArrayList<String> lobbiesInfos) {
        jPanel_lobbies = new JPanel();
        jPanel_lobbies.setBorder(BorderFactory.createEmptyBorder(200,200,200,200));
        jPanel_lobbies.setLayout(new GridLayout(0,1));

        JButton loginButton = new JButton("REFRESH");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                send(new Message.MessageBuilder().setCommand(Command.LOBBY_LIST).build());
            }
        });

        if (lobbiesInfos.isEmpty()){
            JLabel label = new JLabel("NO LOBBIES AVAILABLE", JLabel.CENTER);
            label.setOpaque(true);

            jPanel_lobbies.add(label);
        }

        for (String info : lobbiesInfos) {
            System.out.println(info);
        }

        jPanel_lobbies.add(loginButton);
        frame.add(jPanel_lobbies, BorderLayout.CENTER);
        frame.setVisible(true);
    }


    @Override
    public void printHello() {

    }

    @Override
    public void printQuit(String nickname) {

    }

    @Override
    public void printReply(String payload) {

    }

    @Override
    public void printReply_uni(String payload, String nickname) {

    }

    @Override
    public void printReply_everyOneElse(String payload, String nickname) {

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

        if(command!= Command.PING)
            System.out.println();

        switch (command){

            case LOGIN:
                jPanel_login.setVisible(false);
                this.setNickname(nicknameTemp);
                break;

            case HELLO:
                printHello();
                break;

            case PING:
                send(new Message.MessageBuilder().setCommand(Command.PONG).
                        setNickname(this.getNickname()).build());
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

            case GAME_SETUP:
                GameSetUp gameSetUp=gson.fromJson(mex,GameSetUp.class);
                notifyGameSetup(gameSetUp.getCardGridIDs(), gameSetUp.getNicknames(),gameSetUp.getMarketSetUp());
                break;

            case END_GAME:
                notifyGameEnded();
                break;

            case LOBBY_LIST:
                LobbyListMessage lobbyListMessage = gson.fromJson(mex, LobbyListMessage.class);
                printLobby(lobbyListMessage.getLobbiesInfos());
                break;

            case SHOW_TURN_HELP:
                printItsYourTurn(senderNick);
                break;

            case REPLY:
                printReply(deserializedMex.getInfo());
                break;

            case CHAT_ALL:
                //System.out.print(it.polimi.ingsw.view.cli.Color.ANSI_PURPLE.escape() + senderNick + " in ALL chat:" + Color.ANSI_RESET);
                printReply(deserializedMex.getInfo());
                break;

            case CHAT:
                ChatMessage chatMessage = gson.fromJson(mex, ChatMessage.class);
                //System.out.print(Color.ANSI_BLUE.escape() + chatMessage.getReceiver() + Color.ANSI_RESET + " whispers you: ");
                printReply(deserializedMex.getInfo());
                break;

        }
    }
}
