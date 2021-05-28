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
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class Gui extends ClientView {

    private final Gson gson ;
    private final String nicknameTemp = null;

    private JPanel mainPanel;

    private JPanel jPanel_login;
    private JPanel jPanel_lobbies;

    private JPanel lobbyRoom; //= new JPanel();
    private JPanel playerList;
    private JPanel lobbyOptions;

    private JScrollPane jScrollable_lobbies;
    private JFrame frame;
    private Label infoLabel;

    public Gui() throws FileNotFoundException{
        super();
        gson = new Gson();
        //lobbyRoom = new JPanel();
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    public void createAndShowGUI(){
        frame = new JFrame("MASTER OF RENAISSANCE");
        frame.pack();
        frame.setSize(1200,800);
        frame.setResizable(false);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        //LOGIN PANEL------------------
        jPanel_login = new JPanel();

        JLabel title = new JLabel("WELCOME TO MASTER OF RENAISSANCE", JLabel.CENTER);
        title.setFont(new Font("Helvetica", Font.PLAIN, 44));
        title.setForeground(new Color(240,150,100));
        title.setBorder((BorderFactory.createEmptyBorder(150,0,0,0)));
        title.setOpaque(true);

        jPanel_login.setBorder(BorderFactory.createEmptyBorder(150,300,300,300));
        jPanel_login.setLayout(new GridLayout(0,1));

        JTextField jTextField = new JTextField("Username...");
        jTextField.setForeground(Color.GRAY);
        jTextField.setBounds(50,100, 200,30);

        JButton loginButton = new JButton("Login");
        loginButton.setBorderPainted(false);
        loginButton.setBackground(new Color(255,100,133));// inside the brackets your rgb color value like 255,255,255
        loginButton.setFocusPainted(false);

        loginButton.addActionListener(e -> send(new Message.MessageBuilder().setCommand(Command.LOGIN).setInfo(jTextField.getText()).build()));

        mainPanel.add(title, BorderLayout.NORTH);
        jPanel_login.add(jTextField);
        jPanel_login.add(loginButton);
        //------------------------------

        infoLabel = new Label("");
        infoLabel.setSize(30,30);
        infoLabel.setBackground(Color.GRAY);
        infoLabel.setForeground(Color.BLACK);

        mainPanel.add(jPanel_login);

        frame.add(mainPanel, BorderLayout.CENTER);
        frame.add(infoLabel, BorderLayout.PAGE_END);

        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void printLobby(ArrayList<LobbyListMessage.LobbyInfo> lobbyInfos) {
        mainPanel.removeAll();

        jPanel_lobbies = new JPanel();
        jPanel_lobbies.setBorder(BorderFactory.createEmptyBorder(200,50,200,100));
        jPanel_lobbies.setLayout(new GridLayout(0 , 5));

        if (lobbyInfos.isEmpty()){
            JLabel label = new JLabel("NO LOBBIES AVAILABLE", JLabel.CENTER);
            label.setOpaque(true);

            jPanel_lobbies.add(label);
        }
        else {
            jPanel_lobbies.add(new JLabel("Lobby Name", JLabel.CENTER));
            jPanel_lobbies.add(new JLabel("Owner", JLabel.CENTER));
            jPanel_lobbies.add(new JLabel("Num of Players", JLabel.CENTER));
            jPanel_lobbies.add(new JLabel("Full", JLabel.CENTER));
            jPanel_lobbies.add(new JLabel("Closed", JLabel.CENTER));
        }

        for (LobbyListMessage.LobbyInfo lobby : lobbyInfos) {
            JButton lobby_button = new JButton(lobby.getLobbyName());
            lobby_button.addActionListener(e -> send(new JoinLobbyMessage(lobby.getLobbyName(), getNickname())));
            jPanel_lobbies.add(lobby_button);

            jPanel_lobbies.add(new JLabel(lobby.getOwner(), JLabel.CENTER));
            jPanel_lobbies.add(new JLabel(lobby.getNumOfPlayersConnected() + " / " + lobby.getMaxPlayers(), JLabel.CENTER));
            jPanel_lobbies.add(new JLabel(String.valueOf(lobby.isFull()), JLabel.CENTER));
            jPanel_lobbies.add(new JLabel(String.valueOf(lobby.isClosed()), JLabel.CENTER));
        }

        jScrollable_lobbies = new JScrollPane(jPanel_lobbies, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        mainPanel.add(jScrollable_lobbies, BorderLayout.CENTER);

        // BOTTOM PANEL -----------------------------------------------------------------------
        JPanel final_panel = new JPanel();
        final_panel.setBorder(BorderFactory.createEmptyBorder(50,100,50,100));
        final_panel.setLayout(new GridLayout(0 , 2));

        JButton refreshButton = new JButton("REFRESH");
        refreshButton.addActionListener(e -> send(new Message.MessageBuilder().setCommand(Command.LOBBY_LIST).build()));

        JButton createButton = new JButton("CREATE LOBBY");
        createButton.addActionListener(e -> createLobbyWindow());

        final_panel.add(refreshButton);
        final_panel.add(createButton);
        //--------------------------------------------------------------------------------------

        mainPanel.add(final_panel, BorderLayout.PAGE_END);

        mainPanel.validate();
        mainPanel.repaint();
        mainPanel.setVisible(true);
    }

    private void createLobbyWindow() {
        JPanel pane = new JPanel();
        pane.setLayout(new GridLayout(0, 2, 2, 2));

        pane.add(new JLabel("Lobby name"));
        JTextField lobbyName = new JTextField(5);
        pane.add(lobbyName);

        pane.add(new JLabel("Number of players"));
        JComboBox<String> menu = new JComboBox<>(new String[] {"1", "2", "3", "4"});
        pane.add(menu);

        int option = JOptionPane.showConfirmDialog(mainPanel, pane, "CREATE LOBBY", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null);

        if(option == JOptionPane.OK_OPTION){
            if (lobbyName.getText().length()==0)
                printReply("Please select a non empty name");
            else
                send(new CreateLobbyMessage(lobbyName.getText(), Integer.parseInt(menu.getSelectedItem().toString()), getNickname()));
        }

    }

    public void printWaitingRoom(){

        mainPanel.removeAll();

        lobbyRoom = new JPanel();
        lobbyRoom.setLayout(new BorderLayout());

        playerList = new JPanel();
        playerList.setBorder(BorderFactory.createEmptyBorder(200,50,200,100));
        playerList.setLayout(new GridLayout(4,1));
        playerList.setFont(new Font("Helvetica", Font.PLAIN, 34));

        lobbyOptions = new JPanel();
        lobbyOptions.setBorder(BorderFactory.createEmptyBorder(50,100,50,100));
        lobbyOptions.setLayout(new GridLayout(0 , 2));

        JButton startButton = new JButton("START");
        lobbyOptions.add(startButton);
        startButton.addActionListener(e ->
                send(new Message.MessageBuilder().setCommand(Command.START_GAME).setNickname(getNickname()).build()));

        JButton exitButton = new JButton("EXIT");
        lobbyOptions.add(exitButton);
        exitButton.addActionListener(e ->
                send(new Message.MessageBuilder().setCommand(Command.EXIT_LOBBY).setNickname(getNickname()).build()));

        lobbyRoom.add(playerList, BorderLayout.CENTER);
        lobbyRoom.add(lobbyOptions, BorderLayout.PAGE_END);

        mainPanel.add(lobbyRoom, BorderLayout.CENTER);

        //mainPanel.add(playerList, BorderLayout.CENTER);
        //mainPanel.add(lobbyOptions, BorderLayout.PAGE_END);

        mainPanel.validate();
        mainPanel.repaint();
        mainPanel.setVisible(true);

    }

    @Override
    public void printHello() {

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
                jPanel_login.setVisible(false);
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
                if (playerList != null)
                    playerList.removeAll();

                printWaitingRoom();
                playerList.add(new Label(deserializedMex.getInfo(), JLabel.CENTER));
                playerList.validate();
                playerList.repaint();

                //mainPanel.validate();
                //mainPanel.repaint();
                break;

           /* case JOIN_LOBBY:
                printWaitingRoom();
                playerList.add(new Label(deserializedMex.getSenderNickname()));
                playerList.validate();
                playerList.repaint();
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
