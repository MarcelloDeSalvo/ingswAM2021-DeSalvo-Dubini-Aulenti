package it.polimi.ingsw.view.gui;

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
import it.polimi.ingsw.view.ImageUtil;
import it.polimi.ingsw.view.gui.panels.*;
import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class Gui extends ClientView {

    private JPanel mainPanel;
    private CardLayout cardLayout;    //used for changing scenes

    private LoginPanel loginPanel;
    private LobbyPanel lobbyPanel;
    private LobbyRoomPanel lobbyRoomPanel;
    private GamePanel gamePanel;
    private ResourceSelectionPanel resourceSelectionPanel;

    private GuiStatus guiStatus = GuiStatus.IDLE;

    private JFrame frame;
    private Label infoLabel;

    private int activatedLeaderId;

    public Gui() throws FileNotFoundException{
        super();
        SwingUtilities.invokeLater(this::createAndShowGUI);
    }

    //USER INPUT AND UPDATES--------------------------------------------------------------------------------------------
    @Override
    public boolean readInput() {
        int exit = 0;
        while (exit == 0){
            //CASE
        }
        return false;
    }
    //------------------------------------------------------------------------------------------------------------------


    public void createAndShowGUI(){
        frame = new JFrame("MASTER OF RENAISSANCE");
        frame.setSize(1200,800);
        //frame.pack();
        ImageIcon barbagialla=new ImageIcon(getClass().getResource("/images/others/barbagiallaenave.png"));
        frame.setIconImage(barbagialla.getImage());
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


    //ON EVENT----------------------------------------------------------------------------------------------------------
    @Override
    public void onDisconnect(User user) {
        ImageIcon icon = new ImageIcon();
        icon.setImage(ImageUtil.loadImage("/images/others/retrocerchi.png"));

        int option = JOptionPane.showConfirmDialog(frame,
                "           I'm sorry, something went really wrong!         " +
                        "\nIt looks like you have connection issue.",
                "OOPS!",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.ERROR_MESSAGE, icon);

        if (option == JOptionPane.OK_OPTION)
            System.exit(-1);
    }

    @Override
    public void onReconnected(){
        loginPanel.setVisible(false);
        cardLayout.show(mainPanel, "gamePanel");
    }

    @Override
    public void onLogin(String info) {
        loginPanel.setVisible(false);
    }

    @Override
    public void onExitLobby() {
    }

    @Override
    public void onJoinLobby() {
    }
    //------------------------------------------------------------------------------------------------------------------


    //GENERIC PRINTS----------------------------------------------------------------------------------------------------
    @Override
    public void printHello() {
    }

    @Override
    public void printQuit(String nickname) {
        printReply("Disconnected");
    }

    @Override
    public void printReply(String payload) {
        if (frame!=null)
            infoLabel.setText(payload);
    }

    @Override
    public void printWaitingRoom(StringsMessage stringsMessage){

        lobbyRoomPanel = new LobbyRoomPanel(this);
        printPlayerList(stringsMessage.getInfo(), stringsMessage.getData());

        mainPanel.add(lobbyRoomPanel, "lobbyRoomPanel");
        cardLayout.show(mainPanel, "lobbyRoomPanel");
    }

    @Override
    public void printChatMessage(String senderNick, String info, boolean all) {

    }

    @Override
    public void printLobby(ArrayList<LobbyListMessage.LobbyInfo> lobbyInfos) {

        lobbyPanel = new LobbyPanel(this, lobbyInfos);

        mainPanel.add(lobbyPanel, "lobbyPanel");
        cardLayout.show(mainPanel, "lobbyPanel");
    }
    //------------------------------------------------------------------------------------------------------------------


    //GAME PRINTS-------------------------------------------------------------------------------------------------------
    @Override
    public void printItsYourTurn(String nickname) {
        ImageIcon icon = new ImageIcon();
        icon.setImage(ImageUtil.loadImage("/images/others/retrocerchi.png"));

        JOptionPane.showMessageDialog(frame,
                "IT'S YOUR TURN, CHOSE AN ACTION: " +
                        "\n1) BUY A CARD (>CARDGRID then select a card) " +
                        "\n2) SELECT FROM MARKET (>MARKET then select a row or column)" +
                        "\n3) PRODUCE (>MY BOARD then select the Development Card to produce)"+
                        "\n4) ACTIVATE LEADER (>MY BOARD then select the Leader Card to activate)"+
                        "\n5) MANAGE DEPOSIT (>MOVE then select two deposit to switch)"+
                        "\n6) END TURN (>END_TURN)" +
                        "\n7) WATCH OTHER PLAYERS (>PLAYER NAME)" +
                        "\n#) >HELP to see the full command list ",
                "Mini HELP",
                JOptionPane.INFORMATION_MESSAGE,
                icon);

        infoLabel.setText("Your Turn!");
    }

    @Override
    public void printTurnHelp(String nickname) {
        cardLayout.show(mainPanel,"gamePanel");

    }

    @Override
    public void printPlayerList(String info, ArrayList<String> names) {
        lobbyRoomPanel.printPlayerList(info, names);
    }

    @Override
    public void printOrder() {
        if (!this.isInGame()) return;

        ArrayList<String> randomOrder = getLiteFaithPath().getNicknames();

        StringBuilder orderBuild = new StringBuilder();
        orderBuild.append("This is the Turn Order:                \n");

        for (int i = 0; i<randomOrder.size(); i++){
            orderBuild.append(i+1).append(": ").append(randomOrder.get(i)).append(" \n");
        }

        ImageIcon icon = new ImageIcon();
        icon.setImage(ImageUtil.loadImage("/images/others/retrocerchi.png"));

        JOptionPane.showMessageDialog(frame,
                orderBuild.toString(),
                "Turn Order",
                JOptionPane.INFORMATION_MESSAGE,
                icon);
    }
    //------------------------------------------------------------------------------------------------------------------


    //ASK---------------------------------------------------------------------------------------------------------------
    @Override
    public void askForResources(String nickname, int qty) {
        resourceSelectionPanel = new ResourceSelectionPanel(this);
        getMyLiteDeposit().setDepositPanel(resourceSelectionPanel.getDepositPanel());
        mainPanel.add(resourceSelectionPanel, "5");
        cardLayout.show(mainPanel, "5");
    }

    @Override
    public void askForLeaderCardID(String nickname) { }

    @Override
    public void askForMarketDestination(ArrayList<ResourceContainer> containers, String nickname) { }
    //------------------------------------------------------------------------------------------------------------------


    //UPDATES FROM THE SERVER-------------------------------------------------------------------------------------------
    @Override
    public void notifyGameIsStarted() {
        try {
            gamePanel = new GamePanel(this, getLiteFaithPath(), this.getLiteCardGrid(),this.getLiteMarket());
            getMyLiteDeposit().setDepositPanel(gamePanel.getPlayerBoardPanel().getDepositPanel());
        } catch (ImageNotFound e) {
            System.out.println("A critical error has been occurred File not Found");
            System.exit(-1);
        }

        mainPanel.add(gamePanel, "gamePanel");
        cardLayout.show(mainPanel, "gamePanel");

        gamePanel.getNotifyLabel().setText("THE GAME HAS BEEN STARTED!");
        infoLabel.setText("");
    }

    @Override
    public void notifyGameSetup(ArrayList<Integer> cardGridIDs, ArrayList<String> nicknames, ArrayList<ResourceContainer> marketSetUp) {
        setLiteCardGrid(new LiteCardGrid(cardGridIDs,getDevelopmentCards()));
        litePlayerBoardsSetUp(nicknames);
        setLiteMarket(new LiteMarket(marketSetUp));
        getLiteFaithPath().reset(nicknames); // Should i be creating a new one each time through parsing?

        this.setInGame(true);
        printOrder();
    }

    @Override
    public void notifyCurrentPlayerIncrease(int faithPoints, String nickname) {
        getLiteFaithPath().incrementPosition(faithPoints, nickname);

        if (gamePanel==null)
            return;

        gamePanel.getFaithPathPanel().incRedCrossImages(nickname, faithPoints);

        if (!nickname.equals(getNickname()))
            gamePanel.getNotifyLabel().setText(nickname + "'s position has been incremented by " + faithPoints + " FAITH POINT");
        else
            gamePanel.getNotifyLabel().setText("Your current position has been incremented by " + faithPoints + " FAITH POINT");
    }

    @Override
    public void notifyOthersIncrease(int faithPoints, String nickname) {
        getLiteFaithPath().incrementOthersPositions(faithPoints, nickname);

        if (gamePanel==null)
            return;

        gamePanel.getFaithPathPanel().incOtherRedCrossImages(nickname, faithPoints);

        if (!nickname.equals(getNickname()))
            gamePanel.getNotifyLabel().setText(nickname+ " has discarded " + faithPoints + " resources.\n Your current position has been incremented by " + faithPoints + " FAITH POINT");

        gamePanel.getNotifyLabel().setText("Everybody's position (except " + nickname + ") has been incremented by " + faithPoints + " FAITH POINT");
    }

    @Override
    public void notifyPapalFavour(ArrayList<Integer> playerFavours, String senderNick) {
        getLiteFaithPath().incrementPlayerFavours(playerFavours);
        StringBuilder playersThatGotThePoints = new StringBuilder();
        playersThatGotThePoints.append("A papal favour has been activated: ");
        int i=0;
        int eligiblePlayer=0;
        int point = 0 ;
        ArrayList<String> nicksThatGotThePoints = new ArrayList<>();
        for (String nick: getLiteFaithPath().getNicknames()) {
            if(playerFavours.get(i)!=0){
                playersThatGotThePoints.append(nick).append(" ");
                nicksThatGotThePoints.add(nick);
                point = playerFavours.get(i);
                eligiblePlayer++;
            }
            i++;
        }

        gamePanel.getFaithPathPanel().printTicket(point,nicksThatGotThePoints);

        if (eligiblePlayer>1)
            playersThatGotThePoints.append("were ");
        else
            playersThatGotThePoints.append("was ");

        playersThatGotThePoints.append("eligible and received ").append(point).append(" VICTORY POINTS");
        gamePanel.getNotifyLabel().setText(playersThatGotThePoints.toString());
    }

    @Override
    public void notifyBuyOk(String nickname, int slotID, int cardID) {
        getSomeonesLiteProduction(nickname).addCardToSlot(slotID, cardID);
        if (!nickname.equals(getNickname()))
            gamePanel.getNotifyLabel().setText(nickname + " bought a new card (ID: "+ cardID +" ) !");
        else {
            gamePanel.getNotifyLabel().setText("You bought the card correctly!");
        }

        infoLabel.setText("");
        gamePanel.getPlayerBoardPanel().getProductionPanel().printBoughtCard(slotID, cardID, gamePanel.getPlayerBoardPanel());
        guiStatus = GuiStatus.IDLE;
    }

    @Override
    public void notifyBuySlotOk(String mex) {
        gamePanel.getNotifyLabel().setText("Now you can select the resources in order to pay");
        infoLabel.setText(getDevelopmentCards().get(gamePanel.getPlayerBoardPanel().getProductionPanel().
                getBuyCardIdBuffer()-1).priceToStringDecoloured());
        guiStatus = GuiStatus.SELECTING_PAY_RESOURCES;
    }

    @Override
    public void notifyBuyError(String error) {
        infoLabel.setText(error);
        guiStatus = GuiStatus.IDLE;
    }


    @Override
    public void notifyProductionError(String error, String senderNick) {
        infoLabel.setText(error);
        guiStatus = GuiStatus.IDLE;
    }

    @Override
    public void notifyStartFilling(int productionID, String senderNick) {

        guiStatus = GuiStatus.SELECTING_QM;

        ArrayList<String> addableTypes = new ArrayList<>();
        ArrayList<ResourceType> send = new ArrayList<>();

        addableTypes.add("DONE");

        for (ResourceType res: ResourceType.values()) {
            if(res.canAddToVault()){
                addableTypes.add(res.deColored());
            }
        }

        int response = JOptionPane.showOptionDialog(null, "Please Fill the question mark in order", "Fill Request",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, addableTypes.toArray(), addableTypes.toArray()[0]);



        //System.out.println("Please start filling the Production Slots N: " + productionID +
               // " with resources of your choice by typing >FILL ResourceType1 ResourceType2  ... 'DONE'");
    }

    @Override
    public void notifyFillOk(int productionID, String senderNick) {
        //System.out.println("Resources of your choice for Production Slot N: " + productionID + " have been filled correctly!");
    }

    @Override
    public void notifyProductionPrice(ArrayList<ResourceContainer> resourcesPrice, String senderNick) {
        StringBuilder price = new StringBuilder();
        for (ResourceContainer resC: resourcesPrice) {
            price.append(resC.getQty());
            price.append(" ");
            price.append(resC.getResourceType().deColored());
            price.append(" ");
        }

        infoLabel.setText(price.toString());
        guiStatus = GuiStatus.SELECTING_PAY_RESOURCES;
    }

    @Override
    public void notifyProductionOk(String senderNick) {
        guiStatus = GuiStatus.IDLE;

        if (!senderNick.equals(getNickname()))
            gamePanel.getNotifyLabel().setText(senderNick+" has used the production this turn!");
        else{
            gamePanel.getNotifyLabel().setText("Production executed correctly!");
        }
    }

    @Override
    public void notifyMoveOk(String senderNick) {
        gamePanel.getNotifyLabel().setText("The action on deposit has been executed correctly!\n");
    }

    @Override
    public void notifyMarketUpdate(String selection, int selected) {
        getLiteMarket().liteMarketUpdate(selection, selected);
        gamePanel.getMarketPanel().showMarket();
        gamePanel.revalidate();
        gamePanel.repaint();
    }

    @Override
    public void notifyResourcesArrived(ArrayList<ResourceContainer> resourceContainers) {
       gamePanel.getPlayerBoardPanel().getAfterMarketPanel().setResources(resourceContainers);
       gamePanel.getPlayerBoardPanel().getAfterMarketPanel().setVisible(true);
       gamePanel.getCardLayout().show(gamePanel.getMain(),"playerBoardPanel");
       guiStatus = GuiStatus.SELECTING_DEST_AFTER_MARKET;

    }

    @Override
    public void notifyCardGridChanges(int oldID, int newID) {
        getLiteCardGrid().gridUpdated(oldID, newID);
        gamePanel.getCardGridPanel().updateGrid();
        gamePanel.repaint();
        gamePanel.getCardGridPanel().repaint();
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
    public void notifyLeaderDiscarded(int id, String nickname) {
        if(gamePanel != null)
            gamePanel.getNotifyLabel().setText("Leader discarded!");
        else
            printReply("Leader discarded!");

        getMyHand().discardFromHand(id);

        if(gamePanel != null){
            gamePanel.getPlayerBoardPanel().validate();
            gamePanel.getPlayerBoardPanel().repaint();
        }
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
        activatedLeaderId = id;
    }

    @Override
    public void notifyLorenzoAction(int actionID, Colour colour) {
        ImageIcon icon = new ImageIcon();
        icon.setImage(ImageUtil.loadImage("/images/lorenzoActions/cerchio" + actionID + ".png"));

        String text = "ERROR";

        if(actionID < 5)
            text = "LORENZO has removed 2 " + colour.noColorToString() + " development cards   ";
        else if(actionID == 5)
            text = "LORENZO's position has been incremented of 2 FAITH POINTS   ";
        else if(actionID == 6)
            text = "LORENZO's position has been incremented of 2 FAITH POINTS. \nThe Action Tokens have been shuffled!   ";

        JOptionPane.showMessageDialog(frame,
                text,
                "LORENZO ACTION",
                JOptionPane.INFORMATION_MESSAGE,
                icon);
    }

    @Override
    public void notifyVaultChanges(ResourceContainer container, boolean added, String senderNick) {
        if (added)
            getSomeonesLiteVault(senderNick).addToVault(container);
        else
            getSomeonesLiteVault(senderNick).removeFromVault(container);

        gamePanel.getPlayerBoardPanel().validate();
        gamePanel.getPlayerBoardPanel().repaint();
    }

    @Override
    public void notifyDepositChanges(int id, ResourceContainer resourceContainer, boolean added, String senderNick) {
        if (added)
            getSomeonesLiteDeposit(senderNick).addRes(resourceContainer, id);
        else
            getSomeonesLiteDeposit(senderNick).removeRes(resourceContainer, id);
    }

    @Override
    public void notifyNewDepositSlot(int maxDim, ResourceType resourceType, String senderNick) {
        getSomeonesLiteDeposit(senderNick).addSlot(maxDim, resourceType,
                getGamePanel().getPlayerBoardPanel().getHandPanel().getLeaders().get(activatedLeaderId).getLocation());
    }

    @Override
    public void notifyNewProductionSlot(ProductionAbility productionAbility, String senderNick) {
        getSomeonesLiteProduction(senderNick).addProductionSlot(productionAbility);
        if(senderNick.equals(getNickname())){
            gamePanel.getNotifyLabel().setText("You activated a new production!");
        }
    }

    @Override
    public void notifyLastTurn() {
        gamePanel.getNotifyLabel().setText("-THIS IS THE LAST TURN-");
    }

    @Override
    public void notifyWinner(ArrayList<String> winner) {
        gamePanel.getNotifyLabel().setText("The winner is: "+ winner);
    }

    @Override
    public void notifyScores(List<Integer> playersTotalVictoryPoints, ArrayList<String> nicknames) {
        StringBuilder scoreboard = new StringBuilder("");
        int i = 0;
        for (String nick: nicknames) {
            scoreboard.append(nick);
            scoreboard.append(" scored: ").append(playersTotalVictoryPoints.get(i)).append("  points").append("\n");
            i++;
        }
        int option = JOptionPane.showConfirmDialog(frame,
                scoreboard,
                "SCORE BOARD",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE);

        if (option == JOptionPane.OK_OPTION){
            frame.setSize(1200,800);
            frame.setLocationRelativeTo(null);
            cardLayout.show(mainPanel, "lobbyRoomPanel");
        }

    }

    @Override
    public void notifyMarketOk(String senderNick) {
        gamePanel.getPlayerBoardPanel().getAfterMarketPanel().setVisible(false);
        guiStatus = GuiStatus.IDLE;
    }

    @Override
    public void notifyGameEnded() {
        printReply("# The game is ended, you are now in the lobby");

        this.setInGame(false);
    }
    //------------------------------------------------------------------------------------------------------------------


    //GETTER SETTER-----------------------------------------------------------------------------------------------------
    public GuiStatus getGuiStatus() {
        return guiStatus;
    }

    public void setGuiStatus(GuiStatus guiStatus) {
        this.guiStatus = guiStatus;
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }
    //------------------------------------------------------------------------------------------------------------------


    //------------------------------------------------------------------------------------------------------------------
    @Override
    public void printReply_uni(String payload, String nickname) {
        printReply(payload);
    }

    @Override
    public void printReply_everyOneElse(String payload, String nickname) {
        printReply(payload);
    }
    //------------------------------------------------------------------------------------------------------------------
}
