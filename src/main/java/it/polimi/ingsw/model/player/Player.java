package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.FaithPath;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.cards.Status;
import it.polimi.ingsw.model.exceptions.DepositSlotMaxDimExceeded;
import it.polimi.ingsw.model.exceptions.NotEnoughResources;
import it.polimi.ingsw.model.player.deposit.Deposit;
import it.polimi.ingsw.model.player.deposit.DepositSlot;
import it.polimi.ingsw.model.player.production.ProductionSite;
import it.polimi.ingsw.model.player.production.ProductionSlot;
import it.polimi.ingsw.model.resources.ResourceContainer;
import it.polimi.ingsw.observers.ObservableModel;
import it.polimi.ingsw.observers.ObserverModel;

import java.util.ArrayList;

public class Player implements ObservableModel {

    private final ArrayList<ObserverModel> observers;

    private String nickname;
    private ArrayList<LeaderCard> hand;
    private PlayerBoard playerBoard;
    private int orderID;

    private boolean leadersHaveBeenDiscarded = false;
    private int selectedResources = 0;
    private boolean ready = false;

    public Player(String nickname) {
        this.nickname = nickname;
        this.playerBoard = new PlayerBoard(3,3);
        hand = new ArrayList<>();

        observers = new ArrayList<>();
    }

    public Player(String nickname, int orderID, FaithPath faithPath) {
        this.nickname = nickname;
        this.playerBoard = new PlayerBoard(3,3, faithPath);
        this.orderID = orderID;
        hand = new ArrayList<>();

        observers = new ArrayList<>();
    }

    public Player(String nickname, int pyramidHeight, int prodSlotNum) {
        this.nickname = nickname;
        this.playerBoard = new PlayerBoard(pyramidHeight,prodSlotNum);
        hand = new ArrayList<>();

        observers = new ArrayList<>();
    }

    public Player(String nickname, int pyramidHeight, int prodSlotNum, int orderID, FaithPath faithPath) {
        this.nickname = nickname;
        this.playerBoard = new PlayerBoard(pyramidHeight, prodSlotNum, faithPath);
        this.orderID = orderID;
        hand = new ArrayList<>();

        observers = new ArrayList<>();
    }

    //PLAYER METHODS----------------------------------------------------------------------------------------------------
    /**
     * Adds a leaderCard to a Player's hand
     * @param leaderCard is the drawn card
     * @return true if it can be added
     */
    public boolean addToHand(LeaderCard leaderCard) throws NullPointerException, IllegalArgumentException{
        return leaderCard != null && hand.add(leaderCard);
    }

    /**
     * Discards a leaderCard from a Player's hand
     * @param leaderCard is the discarded leaderCard
     * @return true if it can be discarded
     * @throws NullPointerException
     * @throws IndexOutOfBoundsException
     */
    public boolean discardFromHand(LeaderCard leaderCard) throws NullPointerException, IllegalArgumentException{
        return leaderCard != null && hand.remove(leaderCard);
    }

    /**
     * Discards a leaderCard from a Player's hand <br>
     * @param id is the LeaderCard id
     * @return true if it can be removed
     * @throws NullPointerException
     * @throws IndexOutOfBoundsException
     */
    public boolean discardFromHand(int id) throws NullPointerException, IndexOutOfBoundsException{
        if (id > 0) {
            LeaderCard leaderCardToRemove = null;

            for (LeaderCard lCard: hand) {
                if(lCard.getId() == id)
                    leaderCardToRemove = lCard;
            }

            if (hand.remove(leaderCardToRemove)) {
                for (ObserverModel obs: observers) {
                    obs.printHand(leaderListToInt(), nickname);
                }

                if(hand.size() == 2)
                    setLeadersHaveBeenDiscarded(true);

                return true;
            }
        }

        return false;
    }


    private ArrayList<Integer>  leaderListToInt () {
        ArrayList<Integer> leaderListInt = new ArrayList<>();

        for (LeaderCard leader : hand)
            leaderListInt.add(leader.getId());

        return leaderListInt;
    }


    public boolean activateLeader(LeaderCard leaderCard) throws NullPointerException {

        if (leaderCard == null)
            return false;

        if(leaderCard.checkRequirements(playerBoard)){
            leaderCard.changeStatus(Status.ACTIVE);
            return leaderCard.executeAbility(playerBoard);
        }
        return false;
    }

    /**
     * Determines the amount of points a player earns through his active leader cards
     */
    public int activeLeadersVictoryPoints(){
        int c=0;
        for (LeaderCard card:hand) {
            if(card.getStatus()==Status.ACTIVE)
                c=c+ card.getVictoryPoints();
        }
        return c;
    }
    //------------------------------------------------------------------------------------------------------------------

    //BUY METHODS---(Calls the methods of the lowest levels)------------------------------------------------------------
    /**
     * Tells the controller if the user has selected the right quantity of resources in order to buy one development card
     * @param developmentCard it's the selected card (it's a list of resourceContainer)
     * @return true if the selected resources are equals to the card's price
     */
    public boolean canBuy(DevelopmentCard developmentCard) {
        return playerBoard.canBuy(developmentCard.getPrice());
    }

    /**
     * Called when canBuy() returns true <br>
     * Subtracts all buffers from all the deposits and the vault in order to buy one development card
     * @return true if the subtraction is successful
     */
    public boolean buy(){
        return playerBoard.buy();
    }

    /**
     * Inserts the just bought card into the selected production slot
     * @param id is the index of the selected ProductionSlot
     * @param developmentCard is the card bought by the user
     * @return true if the insert is successful
     */
    public boolean insertBoughtCardOn(int id, DevelopmentCard developmentCard){
        return playerBoard.insertBoughtCard(getProductionSlotByID(id),developmentCard);
    }
    //------------------------------------------------------------------------------------------------------------------


    //PRODUCTION PIPELINE---(Calls the methods of the lowest levels)----------------------------------------------------
    /**
     * Puts all the resources needed to activate the selected production cards and all the resources produced by those cards into a buffer
     * @param selectedProductionCard is the list of the cards selected by the user
     */
    public boolean fillProductionBuffers(ArrayList<ProductionSlot> selectedProductionCard){
        return playerBoard.fillProductionBuffers(selectedProductionCard);
    }

    /**
     * Checks if the selected resources are equal to the ones needed to produce
     * @param selectedResources is the list of resources selected by the user
     * @return true if he can produce the previously selected cards
     * @throws NotEnoughResources if the user has not enough resources
     * @throws DepositSlotMaxDimExceeded if the user selected too many resources
     */
    public boolean canProduce(ArrayList<ResourceContainer> selectedResources) throws NotEnoughResources, DepositSlotMaxDimExceeded {
        return playerBoard.canProduce(selectedResources);
    }

    /**
     * Called after canProduce() <br>
     * Execute the production
     */
    public boolean produce(){
       return playerBoard.produce();
    }
    //------------------------------------------------------------------------------------------------------------------


    //GETTER AND SETTER-------------------------------------------------------------------------------------------------
    public ArrayList<Integer> getHandIDs () {
        ArrayList<Integer> handIDs = new ArrayList<>();

        for (LeaderCard leaderCard : hand) {
            handIDs.add(leaderCard.getId());
        }

        return handIDs;
    }

    public ProductionSlot getProductionSlotByID(int n){
        return playerBoard.getProductionSlotByID(n);
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public PlayerBoard getPlayerBoard() {
        return playerBoard;
    }

    public void setPlayerBoard(PlayerBoard playerBoard) {
        this.playerBoard = playerBoard;
    }

    public int getOrderID() {
        return orderID;
    }

    public Deposit getDeposit() {
        return playerBoard.getDeposit();
    }

    public Vault getVault() {
        return playerBoard.getVault();
    }

    public ProductionSite getProductionSite() {
        return playerBoard.getProductionSite();
    }

    public ConversionSite getConversionSite() {
        return playerBoard.getConversionSite();
    }

    public DiscountSite getDiscountSite() {
        return playerBoard.getDiscountSite();
    }

    public DepositSlot getDepositSlotWithDim(int dim){
        return playerBoard.getDepositSlotWithDim(dim);
    }

    public DepositSlot getLeaderDepositNumberX(int x){
        return playerBoard.getLeaderDepositNumberX(x);
    }

    public DepositSlot getDepositSlotByID(int id){
        return playerBoard.getDepositSlotByID(id);
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public ArrayList<LeaderCard> getHand() {
        return hand;
    }

    public void setHand(ArrayList<LeaderCard> hand) {
        this.hand = hand;
    }

    public boolean isLeadersHaveBeenDiscarded() {
        return leadersHaveBeenDiscarded;
    }

    public void setLeadersHaveBeenDiscarded(boolean leadersHaveBeenDiscarded) {
        this.leadersHaveBeenDiscarded = leadersHaveBeenDiscarded;
    }

    public int getSelectedResources() {
        return selectedResources;
    }

    public void incrementSelectedResources() {
        this.selectedResources += 1;

        if(orderID == 3 && selectedResources != 2)
            return;

        setReady(true);
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    //------------------------------------------------------------------------------------------------------------------


    //JAVA--------------------------------------------------------------------------------------------------------------
    @Override
    public String toString() {
        return "Player{" +
                "nickname='" + nickname + '\n' +
                // ", hand=" + hand + '\n' +
                ", playerBoard=" + playerBoard + '\n' +
                ", orderID=" + orderID +
                '}';
    }
    //------------------------------------------------------------------------------------------------------------------


    @Override
    public void addView(ObserverModel view) {
        observers.add(view);
    }



    /*
                                ⢀⡴⠑⡄⠀⠀⠀⠀⠀⠀⠀⣀⣀⣤⣤⣤⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
                                ⠸⡇⠀⠿⡀⠀⠀⠀⣀⡴⢿⣿⣿⣿⣿⣿⣿⣿⣷⣦⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀
                                ⠀⠀⠀⠀⠑⢄⣠⠾⠁⣀⣄⡈⠙⣿⣿⣿⣿⣿⣿⣿⣿⣆⠀⠀⠀⠀⠀⠀⠀⠀
                                ⠀⠀⠀⠀⢀⡀⠁⠀⠀⠈⠙⠛⠂⠈⣿⣿⣿⣿⣿⠿⡿⢿⣆⠀⠀⠀⠀⠀⠀⠀
                                ⠀⠀⠀⢀⡾⣁⣀⠀⠴⠂⠙⣗⡀⠀⢻⣿⣿⠭⢤⣴⣦⣤⣹⠀⠀⠀⢀⢴⣶⣆
                                ⠀⠀⢀⣾⣿⣿⣿⣷⣮⣽⣾⣿⣥⣴⣿⣿⡿⢂⠔⢚⡿⢿⣿⣦⣴⣾⠁⠸⣼⡿
                                ⠀⢀⡞⠁⠙⠻⠿⠟⠉⠀⠛⢹⣿⣿⣿⣿⣿⣌⢤⣼⣿⣾⣿⡟⠉⠀⠀⠀⠀⠀
                                ⠀⣾⣷⣶⠇⠀⠀⣤⣄⣀⡀⠈⠻⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡇⠀⠀⠀⠀⠀⠀
                                ⠀⠉⠈⠉⠀⠀⢦⡈⢻⣿⣿⣿⣶⣶⣶⣶⣤⣽⡹⣿⣿⣿⣿⡇⠀⠀⠀⠀⠀⠀
                                ⠀⠀⠀⠀⠀⠀⠀⠉⠲⣽⡻⢿⣿⣿⣿⣿⣿⣿⣷⣜⣿⣿⣿⡇⠀⠀⠀⠀⠀⠀
                                ⠀⠀⠀⠀⠀⠀⠀⠀⢸⣿⣿⣷⣶⣮⣭⣽⣿⣿⣿⣿⣿⣿⣿⠀⠀⠀⠀⠀⠀⠀
                                ⠀⠀⠀⠀⠀⠀⣀⣀⣈⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠇⠀⠀⠀⠀⠀⠀⠀
                                ⠀⠀⠀⠀⠀⠀⢿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠃⠀⠀⠀⠀⠀⠀⠀⠀
                                ⠀⠀⠀⠀⠀⠀⠀⠹⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡿⠟⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀
                                ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠛⠻⠿⠿⠿⠿⠛⠉
     */
}
