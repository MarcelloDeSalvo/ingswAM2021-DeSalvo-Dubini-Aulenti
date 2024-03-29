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

    /**
     * Switches the main sections
     */
    private CardLayout cardLayout;

    private LoginPanel loginPanel;
    private LobbyPanel lobbyPanel;
    private LobbyRoomPanel lobbyRoomPanel;
    private GamePanel gamePanel;
    private ResourceSelectionPanel resourceSelectionPanel;

    private GuiStatus guiStatus = GuiStatus.IDLE;

    private JFrame frame;
    private Label infoLabel;

    private boolean ready = false;

    private int activatedLeaderId;

    public Gui() throws FileNotFoundException{
        super();
        SwingUtilities.invokeLater(this::createAndShowGUI);
    }

    /**
     * Creates the main JFrame and starts the login phase
     */
    public void createAndShowGUI(){
        frame = new JFrame("MASTER OF RENAISSANCE");
        setDefaultFrameSize();

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

        if(!isSolo()){
            mainPanel.add(loginPanel, "loginPanel");
            cardLayout.show(mainPanel, "loginPanel");
        }else{
            mainPanel.add(new JPanel(), "offline");
            cardLayout.show(mainPanel, "offline");
        }

        frame.add(mainPanel, BorderLayout.CENTER);
        frame.add(infoLabel, BorderLayout.PAGE_END);

        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        ready = true;
    }

    private void setDefaultFrameSize(){
        frame.setSize(1200,800);
        frame.setLocationRelativeTo(null);
    }

    private void setMaxFrameSize(){
        frame.setSize(1920,1080);
        frame.setLocationRelativeTo(null);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    //USER INPUT--------------------------------------------------------------------------------------------------------
    @Override
    public void readInput() { }
    //------------------------------------------------------------------------------------------------------------------

    //ON EVENT----------------------------------------------------------------------------------------------------------
    @Override
    public void onDisconnected() {
        ImageIcon icon = new ImageIcon();
        icon.setImage(ImageUtil.loadImage("/images/others/lorenzoCircle.png"));

        int option = JOptionPane.showConfirmDialog(frame,
                "           I'm sorry, something went really wrong!         " +
                        "\nIt looks like you have connection issues.",
                "OOPS!",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.ERROR_MESSAGE, icon);

        if (option == JOptionPane.OK_OPTION)
            System.exit(-1);

        setInGame(false);
    }

    @Override
    public void onServerKick() {
        ImageIcon icon = new ImageIcon();
        icon.setImage(ImageUtil.loadImage("/images/others/lorenzoCircle.png"));

        int option = JOptionPane.showConfirmDialog(frame,
                "You have been kicked for inactivity",
                "Inactivity",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.ERROR_MESSAGE, icon);

        if (option == JOptionPane.OK_OPTION)
            System.exit(-1);
    }

    @Override
    public void onReconnected(){
        loginPanel.setVisible(false);
        cardLayout.show(mainPanel, "gamePanel");
        guiStatus = GuiStatus.IDLE;
        //WORK IN PROGRESS
    }

    @Override
    public void onLogin(String info) {
        loginPanel.setVisible(false);
    }

    @Override
    public void onUserLeftGame() {
        JOptionPane.showMessageDialog(frame, "Someone disconnected from the game, you are now in the lobby",
                "Disconnected from the game", JOptionPane.INFORMATION_MESSAGE);

        cardLayout.show(mainPanel, "lobbyRoomPanel");
        setDefaultFrameSize();
        guiStatus = GuiStatus.IDLE;
    }

    @Override
    public boolean isViewReady() {
        return ready;
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
        if (infoLabel!=null)
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
        if (!all)
            printReply(senderNick + ": " + info);
        else
            printReply(senderNick + " in ALL chat: " + info);

    }

    @Override
    public void printLobby(List<LobbyListMessage.LobbyInfo> lobbyInfos) {
        lobbyPanel = new LobbyPanel(this, lobbyInfos);

        mainPanel.add(lobbyPanel, "lobbyPanel");
        cardLayout.show(mainPanel, "lobbyPanel");
    }
    //------------------------------------------------------------------------------------------------------------------


    //GAME PRINTS-------------------------------------------------------------------------------------------------------
    @Override
    public void printItsYourTurn(String nickname) {
        if (!nickname.equals(this.getNickname()))
            return;

        String mex = "IT'S YOUR TURN, CHOSE AN ACTION: \n" +
                "                        1) BUY A CARD (>CARD GRID then select a card)\n" +
                "                        2) SELECT FROM MARKET (>MARKET then select a row or column)\n" +
                "                        3) PRODUCE (>MY BOARD then select the Development Card to produce)\n" +
                "                        4) ACTIVATE LEADER (>MY BOARD then select the Leader Card to activate)\n" +
                "                        5) MANAGE DEPOSIT (>MOVE then select two deposit to switch)\n" +
                "                        6) END TURN (>END_TURN)\n" +
                "                        7) WATCH OTHER PLAYERS (>PLAYER NAME)\n" +
                "                        #) >HELP to see the full command list\n";

        ImageIcon icon = new ImageIcon();
        icon.setImage(ImageUtil.loadImage("/images/others/lorenzoCircle.png"));

        JOptionPane.showMessageDialog(frame, mex, "HELP", JOptionPane.INFORMATION_MESSAGE, icon);

        infoLabel.setText("Your Turn!");
    }

    @Override
    public void printTurnHelp(String nickname) {
        cardLayout.show(mainPanel,"gamePanel");
    }

    @Override
    public void printPlayerList(String info, List<String> names) {
        lobbyRoomPanel.printPlayerList(info, names);
    }

    @Override
    public void printOrder() {
        if (!this.isInGame()) return;

        ImageIcon icon = new ImageIcon();
        icon.setImage(ImageUtil.loadImage("/images/others/lorenzoCircle.png"));

        String mex;
        String offlineMex = "You're about to challenge me!\n" +
                " Please, go ahead and start your turn";

        List<String> randomOrder = getLiteFaithPath().getNicknames();

        StringBuilder orderBuild = new StringBuilder();
        orderBuild.append("This is the Turn Order:                \n");

        for (int i = 0; i<randomOrder.size(); i++){
            orderBuild.append(i+1).append(": ").append(randomOrder.get(i)).append(" \n");
        }

        mex = isSolo() ? offlineMex : orderBuild.toString();

        JOptionPane.showConfirmDialog(frame, mex, "The game is starting!",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, icon);
    }

    @Override
    public void setCurrPlayer(String currPlayer){ }
    //------------------------------------------------------------------------------------------------------------------


    //ASK---------------------------------------------------------------------------------------------------------------
    @Override
    public void askForResources(String nickname, int qty) {
        resourceSelectionPanel = new ResourceSelectionPanel(this);
        getMyLiteDeposit().setDepositPanel(resourceSelectionPanel.getDepositPanel());
        mainPanel.add(resourceSelectionPanel, "5");
        cardLayout.show(mainPanel, "5");
        printReply("Please select " + qty + " resources of your choice by clicking on them");
    }

    @Override
    public void askForLeaderCardID(String nickname) {
        printReply("Please discard 2 Leader Cards by clicking on them");
    }

    @Override
    public void askForMarketDestination(List<ResourceContainer> containers, String nickname) {
        gamePanel.getPlayerBoardPanel().getAfterMarketPanel().setResources(containers);
        gamePanel.getPlayerBoardPanel().getAfterMarketPanel().setVisible(true);
        gamePanel.getCardLayout().show(gamePanel.getMain(),"playerBoardPanel");
        guiStatus = GuiStatus.SELECTING_DEST_AFTER_MARKET;
    }

    @Override
    public void askMultipleConversion(int numToConvert, ResourceType typeToConvert, List<ResourceType> availableConversion) {
        guiStatus = GuiStatus.SELECTING_CONVERSION;

        cardLayout.show(mainPanel, "gamePanel");
        gamePanel.getCardLayout().show(gamePanel.getMain(), "playerBoardPanel");
        String nick = getNickname();

        int response = JOptionPane.showOptionDialog(null, "You have multiple conversion available, please choose one for each blank marble", "Conversion Request",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, decolourType(availableConversion).toArray(), decolourType(availableConversion).toArray()[0]);

        if (response == -1){
            askMultipleConversion(numToConvert, typeToConvert, availableConversion);
            return;
        }

        ResourceTypeSend convTypeSend = new ResourceTypeSend(Command.CONVERSION, availableConversion.get(response), nick);
        send(convTypeSend);
    }
    //------------------------------------------------------------------------------------------------------------------


    //UPDATES FROM THE SERVER-------------------------------------------------------------------------------------------
    @Override
    public void notifyGameIsStarted() {
        try {
            gamePanel = new GamePanel(this, getLiteFaithPath(), this.getLiteCardGrid(),this.getLiteMarket());
            getMyLiteDeposit().setDepositPanel(gamePanel.getPlayerBoardPanel().getDepositPanel());

        } catch (ImageNotFound e) {
            System.out.println("A critical error has occurred! File not Found");
            System.exit(-1);
        }

        mainPanel.add(gamePanel, "gamePanel");
        cardLayout.show(mainPanel, "gamePanel");

        gamePanel.getNotifyLabel().setText("The game has started, have fun!");
        infoLabel.setText("");
    }

    @Override
    public void notifyGameSetup(List<Integer> cardGridIDs, List<String> nicknames, List<ResourceContainer> marketSetUp) {
        setLiteCardGrid(new LiteCardGrid(cardGridIDs,getDevelopmentCards()));
        litePlayerBoardsSetUp(nicknames);
        setLiteMarket(new LiteMarket(marketSetUp));
        getLiteFaithPath().reset(nicknames);

        for (String nick: nicknames) {
            if(!nick.equals(this.getNickname())){
                getSomeonesLiteDeposit(nick).setDepositPanel(new DepositPanel(this, true));
            }
        }

        this.setInGame(true);
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
    public void notifyPapalFavour(List<Integer> playerFavours, String senderNick) {
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
        if (!nickname.equals(getNickname())) {
            gamePanel.getNotifyLabel().setText(nickname + " bought a new card (ID: " + cardID + " ) !");
            gamePanel.getOtherProductionPanel(nickname).printBoughtCard(slotID, cardID, gamePanel.getPlayerPanelsMap().get(nickname));

        } else {
            gamePanel.getNotifyLabel().setText("You bought the card correctly!");
            gamePanel.getPlayerBoardPanel().getProductionPanel().printBoughtCard(slotID, cardID, gamePanel.getPlayerBoardPanel());
            guiStatus = GuiStatus.IDLE;
            gamePanel.getPlayerBoardPanel().deselectAllResources();
        }

        infoLabel.setText("");

    }

    @Override
    public void notifyBuySlotOk(List<ResourceContainer> price) {
        StringBuilder stringBuilder = new StringBuilder();
        int i=0;
        for (ResourceContainer container : price) {
            if(i == 0)
                stringBuilder.append(container.getQty()).append(" ").append(container.getResourceType().deColored());
            else
                stringBuilder.append(" + ").append(container.getQty()).append(" ").append(container.getResourceType().deColored());
            i++;
        }

        gamePanel.getNotifyLabel().setText("Now you can select the resources in order to pay");
        infoLabel.setText(stringBuilder.toString());
        guiStatus = GuiStatus.SELECTING_PAY_RESOURCES;
    }

    @Override
    public void notifyBuyError(String error) {
        infoLabel.setText(error);
        guiStatus = GuiStatus.IDLE;
        gamePanel.getPlayerBoardPanel().deselectAllResources();
    }

    @Override
    public void notifyProductionError(String error, String senderNick) {
        infoLabel.setText(error);
        guiStatus = GuiStatus.IDLE;
        gamePanel.getPlayerBoardPanel().deselectAllResources();
    }

    @Override
    public void notifyStartFilling(int productionID, int qmi, int qmo, String senderNick) {

        guiStatus = GuiStatus.SELECTING_QM;

        ArrayList<String> addableTypes = new ArrayList<>();
        ArrayList<ResourceType> resources = new ArrayList<>();

        ArrayList<ResourceType> toSend = new ArrayList<>();

        for (ResourceType res: ResourceType.values()) {
            if(res.canAddToVault()){
                addableTypes.add(res.deColored());
                resources.add(res);
            }
        }

        fillWindow(qmi, qmo, addableTypes, toSend, resources, productionID);

    }

    /**
     * creates JOptionPanes to let the user select which resources the question marks should be
     */
    private void fillWindow (int qmi, int qmo, ArrayList<String> addableTypes, ArrayList<ResourceType> toSend, ArrayList<ResourceType> resources, int productionID) {

        for(int i = qmi; i>0; i--){
            int response = JOptionPane.showOptionDialog(null, "Please Fill the "+qmi+" INPUT question marks of the Production Slot N: " + productionID, "Fill Request",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                    null, addableTypes.toArray(), addableTypes.toArray()[0]);
            if(response == -1){
                resetProductionFill();
                return;
            } else {
                toSend.add(resources.get(response));
            }
        }
        for(int i = qmo; i>0; i--){
            int response = JOptionPane.showOptionDialog(null, "Please Fill the "+qmo+" OUTPUT question marks of the Production Slot N: " + productionID, "Fill Request",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                    null, addableTypes.toArray(), addableTypes.toArray()[0]);
            if(response == -1){
                resetProductionFill();
                return;
            } else {
                toSend.add(resources.get(response));
            }
        }

        send(new ResourceTypeSend(Command.FILL_QM, toSend, getNickname()));
    }

    /**
     * Sends an empty fill massage in case you want to cancel your action
     */
    private void resetProductionFill(){
        guiStatus = GuiStatus.IDLE;
        send(new ResourceTypeSend(Command.FILL_QM, new ArrayList<>(), getNickname()));
    }

    @Override
    public void notifyFillOk(int productionID, String senderNick) {
        infoLabel.setText("Resources of your choice for Production Slot N: " + productionID + " have been filled correctly!");
    }

    @Override
    public void notifyProductionPrice(List<ResourceContainer> resourcesPrice, String senderNick) {
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
            gamePanel.getPlayerBoardPanel().deselectAllResources();
        }
    }

    @Override
    public void notifyMoveOk(String senderNick) {
        if(gamePanel!=null)
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
    public void notifyMarketOk(String senderNick) {
        gamePanel.getPlayerBoardPanel().getAfterMarketPanel().setVisible(false);
        guiStatus = GuiStatus.IDLE;
    }

    @Override
    public void notifyConversionError(String error) {
        infoLabel.setText(error);
    }

    @Override
    public void notifyCardGridChanges(int oldID, int newID) {
        getLiteCardGrid().gridUpdated(oldID, newID);

        gamePanel.getCardGridPanel().updateGrid();
        gamePanel.revalidate();
        gamePanel.repaint();
    }

    @Override
    public void notifyCardsInHand(List<Integer> leaderIDs, String nickname) {
        setMyHand(new LiteHand(leaderIDs, getLeaderCards()));
        setMaxFrameSize();

        DiscardHandPanel discardLeaders = new DiscardHandPanel(getMyHand(), this);

        mainPanel.add(discardLeaders, "discardLeaders");
        cardLayout.show(mainPanel, "discardLeaders");

        printOrder();
    }

    @Override
    public void notifyLeaderDiscarded(int id, String nickname) {
        if(gamePanel != null)
            gamePanel.getNotifyLabel().setText("Leader discarded!");
        else
            printReply("Leader discarded!");

        getMyHand().discardFromHand(id);

        if(gamePanel != null){
            gamePanel.getPlayerBoardPanel().revalidate();
            gamePanel.getPlayerBoardPanel().repaint();
        }
    }

    @Override
    public void notifyLeaderActivated(int id, String nickname){

        getSomeonesHand(nickname).activateLeader(id);

        if(nickname.equals(getNickname())) {
            gamePanel.getNotifyLabel().setText("Leader activated!");
        }
        else {
            gamePanel.getNotifyLabel().setText(nickname + " has activated the " + id + " ID leader!\n");
            getSomeonesHand(nickname).addLeader(id);
            gamePanel.getOtherHandPanels(nickname).remake(nickname);
        }

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

        JOptionPane.showMessageDialog(frame, text, "LORENZO ACTION", JOptionPane.INFORMATION_MESSAGE, icon);
    }

    @Override
    public void notifyVaultChanges(ResourceContainer container, boolean added, String senderNick) {
        if (added)
            getSomeonesLiteVault(senderNick).addToVault(container);
        else
            getSomeonesLiteVault(senderNick).removeFromVault(container);

        gamePanel.getPlayerBoardPanel().revalidate();
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
        Point p;

        if(senderNick.equals(getNickname())){
             p = getGamePanel().getPlayerBoardPanel().getHandPanel().getButtonLocation(activatedLeaderId);

            getMyLiteDeposit().addSlot(maxDim,resourceType, p, getGamePanel().getPlayerBoardPanel(), false);

        } else{
            p = getGamePanel().getOtherHandPanels(senderNick).getButtonLocation(activatedLeaderId);

            getSomeonesLiteDeposit(senderNick).addSlot(maxDim, resourceType, p,
                    getGamePanel().getPlayerPanelsMap().get(senderNick), true);
        }
    }

    @Override
    public void notifyRemoveContainerError(String error) {
        infoLabel.setText(error);
    }

    @Override
    public void notifyRemoveContainerOk(String ok) {
        //print nothing
    }

    @Override
    public void notifyNewProductionSlot(ProductionAbility productionAbility, String senderNick) {
        getSomeonesLiteProduction(senderNick).addProductionSlot(productionAbility);

        if(senderNick.equals(getNickname())){
            gamePanel.getPlayerBoardPanel().getProductionPanel().addExtraSlot(
                    getGamePanel().getPlayerBoardPanel().getHandPanel().getLeaders().get(activatedLeaderId).getLocation());
            gamePanel.getNotifyLabel().setText("You activated a new production!");
        }
    }

    @Override
    public void notifyLastTurn() {
        gamePanel.getNotifyLabel().setText("-THIS IS THE LAST TURN-");
    }

    @Override
    public void notifyScores(List<Integer> playersTotalVictoryPoints, List<String> nicknames, List<String> winners){
        ImageIcon icon = new ImageIcon();
        String path = "/images/others/Chalice.png";

        try{
            icon.setImage(ImageUtil.loadImage(path));
        }catch (ImageNotFound e){
            System.out.println(path + " image not found");
        }

        StringBuilder scoreboard = new StringBuilder();
        int i = 0;

        for (String nick: winners) {
            scoreboard.append(nick);
            scoreboard.append(" Won the Game! ");
        }

        scoreboard.append("\n");
        for (String nick: nicknames) {
            scoreboard.append(nick);
            scoreboard.append(" scored: ").append(playersTotalVictoryPoints.get(i)).append("  points").append("\n");
            i++;
        }

        JOptionPane.showMessageDialog(frame, scoreboard,"SCOREBOARD", JOptionPane.INFORMATION_MESSAGE, icon);

        setDefaultFrameSize();

        if(!isSolo())
            cardLayout.show(mainPanel, "lobbyRoomPanel");
        else
            System.exit(-1);

    }

    @Override
    public void notifyGameEnded() {
        printReply("# The game is ended, you are now in the lobby");
        this.setInGame(false);
    }

    @Override
    public void notifyGameCreationError(String error) {
        printReply(error);
        this.setInGame(false);
        cardLayout.show(mainPanel, "lobbyRoomPanel");
        guiStatus = GuiStatus.IDLE;
    }
    //------------------------------------------------------------------------------------------------------------------


    //GETTER SETTER-----------------------------------------------------------------------------------------------------
    public GuiStatus getGuiStatus() { return guiStatus; }

    public void setGuiStatus(GuiStatus guiStatus) { this.guiStatus = guiStatus; }

    public GamePanel getGamePanel() { return gamePanel; }

    public JFrame getFrame() { return frame; }

    //------------------------------------------------------------------------------------------------------------------


    //UTIL METHODS------------------------------------------------------------------------------------------------------
    /**
     * Method used to remove the ansi color from the ResourceType toString
     */
    private List<String> decolourType(List<ResourceType> resourceTypes){
        ArrayList<String> addableTypes = new ArrayList<>();

        for (ResourceType res: resourceTypes) {
            if(res.canAddToVault()){
                addableTypes.add(res.deColored());
            }
        }

        return addableTypes;
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
