package it.polimi.ingsw.view.gui;


import com.google.gson.Gson;
import it.polimi.ingsw.liteModel.LiteCardGrid;
import it.polimi.ingsw.liteModel.LiteHand;
import it.polimi.ingsw.liteModel.LiteMarket;
import it.polimi.ingsw.model.cards.Colour;
import it.polimi.ingsw.model.cards.ProductionAbility;
import it.polimi.ingsw.model.exceptions.ImageNotFound;
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

    private JPanel mainPanel;
    private CardLayout cardLayout;      //used for changing scenes

    private LoginPanel loginPanel;
    private LobbyPanel lobbyPanel;
    private LobbyRoomPanel lobbyRoomPanel;
    private GamePanel gamePanel;

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
        infoLabel.setSize(30,40);
        infoLabel.setFont(new Font("Rubik", Font.PLAIN, 20));
        infoLabel.setBackground(new Color(219, 139, 0));
        infoLabel.setAlignment(Label.CENTER);
        infoLabel.setForeground(Color.WHITE);

        mainPanel.add(loginPanel, "loginPanel");
        cardLayout.show(mainPanel, "loginPanel");

        frame.add(mainPanel, BorderLayout.CENTER);
        frame.add(infoLabel, BorderLayout.PAGE_END);

        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void printLobby(ArrayList<LobbyListMessage.LobbyInfo> lobbyInfos) {

        lobbyPanel = new LobbyPanel(this, lobbyInfos);

        mainPanel.add(lobbyPanel, "lobbyPanel");
        cardLayout.show(mainPanel, "lobbyPanel");
    }

    public void printWaitingRoom(StringsMessage stringsMessage){

        lobbyRoomPanel = new LobbyRoomPanel(this);
        printPlayerList(stringsMessage.getInfo(), stringsMessage.getData());

        mainPanel.add(lobbyRoomPanel, "lobbyRoomPanel");
        cardLayout.show(mainPanel, "lobbyRoomPanel");
    }

    @Override
    public void onLogin(String info) {
        loginPanel.setVisible(false);
    }

    @Override
    public void printPlayerList(String info, ArrayList<String> names) {
        lobbyRoomPanel.printPlayerList(info, names);
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
        System.out.println("Ho ricevuto il res set up");
        ResourceSelectionPanel resourceSelectionPanel=new ResourceSelectionPanel(this);
        mainPanel.add(resourceSelectionPanel, "5");
        cardLayout.show(mainPanel, "5");
    }

    @Override
    public void askForLeaderCardID(String nickname) {

    }

    @Override
    public void askForMarketDestination(ArrayList<ResourceContainer> containers, String nickname) {

    }

    @Override
    public void notifyCardsInHand(ArrayList<Integer> leaderIDs, String nickname) {
        setMyHand(new LiteHand(leaderIDs, getLeaderCards()));
        frame.setSize(1920,1080);
        frame.setLocationRelativeTo(null);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        DiscardHandPanel discardLeaders = new DiscardHandPanel(getMyHand(), this);

        mainPanel.add(discardLeaders, "discardLeaders");
        cardLayout.show(mainPanel, "discardLeaders");
    }

    @Override
    public void notifyGameIsStarted() {
        try {
            gamePanel = new GamePanel(this, getLiteFaithPath(), this.getLiteCardGrid());
        } catch (ImageNotFound e) {
            System.out.println("A critical error has been occurred File not Found");
        }

        mainPanel.add(gamePanel, "gamePanel");
        cardLayout.show(mainPanel, "gamePanel");

        gamePanel.getNotifyLabel().setText("THE GAME HAS BEEN STARTED!");
    }

    @Override
    public void notifyBuyOk(String nickname, int slotID, int cardID) {

    }

    @Override
    public void notifyGameSetup(ArrayList<Integer> cardGridIDs, ArrayList<String> nicknames, ArrayList<ResourceContainer> marketSetUp) {
        setLiteCardGrid(new LiteCardGrid(cardGridIDs,getDevelopmentCards()));
        litePlayerBoardsSetUp(nicknames);
        setLiteMarket(new LiteMarket(marketSetUp));
        getLiteFaithPath().reset(nicknames); // Should i be creating a new one each time through parsing?
        infoLabel.setText("");
        this.setInGame(true);
    }

    @Override
    public void notifyLeaderDiscarded(int id, String nickname) {
        if(gamePanel != null)
            gamePanel.getNotifyLabel().setText("Leader discarded!");
        else
            printReply("Leader discarded!");

        getMyHand().discardFromHand(id);
    }

    @Override
    public void notifyLeaderActivated(int id, String nickname){
        if(nickname.equals(getNickname()))
            gamePanel.getNotifyLabel().setText("Leader activated!");
        else {
            gamePanel.getNotifyLabel().setText(nickname + " has activated the " + id + " ID leader!\n");
            getSomeonesHand(nickname).addLeader(id);
        }
        getSomeonesHand(nickname).activateLeader(id);
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
        gamePanel.getNotifyLabel().setText("LORENZO has removed "+ amount+ " "+color+ " development cards with level = " + level);
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
    public void notifyCurrentPlayerIncrease(int faithPoints, String nickname) {
        getLiteFaithPath().incrementPosition(faithPoints, nickname);
        gamePanel.getFaithPathPanel().incRedCrossImages(nickname, faithPoints);

        if (!nickname.equals(getNickname()))
            gamePanel.getNotifyLabel().setText(nickname + "'s position has been incremented by " + faithPoints + " FAITH POINT");
        else
            gamePanel.getNotifyLabel().setText("Your current position has been incremented by " + faithPoints + " FAITH POINT");
    }

    @Override
    public void notifyOthersIncrease(int faithPoints, String nickname) {
        getLiteFaithPath().incrementOthersPositions(faithPoints, nickname);
        gamePanel.getFaithPathPanel().incOtherRedCrossImages(nickname, faithPoints);

        if (!nickname.equals(getNickname()))
            gamePanel.getNotifyLabel().setText(nickname+ " has discarded " + faithPoints+ " resources.\n Your current position has been incremented by " + faithPoints + " FAITH POINT");

        gamePanel.getNotifyLabel().setText("Everybody's position (except "+ nickname+") has been incremented by " + faithPoints + " FAITH POINT");
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

    public void onReconnected(){
        loginPanel.setVisible(false);
        cardLayout.show(mainPanel, "gamePanel");
    }

    @Override
    public void onExitLobby() {

    }

    @Override
    public void onJoinLobby() {

    }

    @Override
    public void printChatMessage(String senderNick, String info, boolean all) {

    }

    @Override
    public void printOrder() {

    }

    @Override
    public boolean readInput() {
        int exit = 0;
        while (exit ==0){
            //CASE
        }
        return false;
    }

}
