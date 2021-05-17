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
import it.polimi.ingsw.model.resources.ResourceType;
import it.polimi.ingsw.observers.ObservableModel;
import it.polimi.ingsw.observers.ObserverModel;
import it.polimi.ingsw.view.VirtualView;

import java.util.ArrayList;
import java.util.HashMap;

public class Player implements ObservableModel {

    private ObserverModel view;

    private final String nickname;
    private final ArrayList<LeaderCard> hand;
    private final PlayerBoard playerBoard;
    private int orderID;
    private PlayerStatus playerStatus = PlayerStatus.IDLE;

    //set-up phase flags
    private boolean leadersHaveBeenDiscarded = false;
    private int selectedResources = 0;
    private boolean ready = false;

    public Player(String nickname) {
        this.nickname = nickname;
        this.playerBoard = new PlayerBoard(3,3);
        hand = new ArrayList<>();
        view = new VirtualView();
    }

    public Player(String nickname, int orderID, FaithPath faithPath) {
        this.nickname = nickname;
        this.playerBoard = new PlayerBoard(3,3, faithPath);
        this.orderID = orderID;
        hand = new ArrayList<>();
    }

    public Player(String nickname, int pyramidHeight, int prodSlotNum) {
        this.nickname = nickname;
        this.playerBoard = new PlayerBoard(pyramidHeight,prodSlotNum);
        hand = new ArrayList<>();

    }

    public Player(String nickname, int pyramidHeight, int prodSlotNum, int orderID, FaithPath faithPath) {
        this.nickname = nickname;
        this.playerBoard = new PlayerBoard(pyramidHeight, prodSlotNum, faithPath);
        this.orderID = orderID;
        hand = new ArrayList<>();

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

                view.notifyLeaderDiscarded(id, nickname);

                if(hand.size() == 2)
                    setLeadersHaveBeenDiscarded(true);

                return true;
            }
        }

        return false;
    }


    public ArrayList<Integer> leaderListToInt () {
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
            view.notifyLeaderActivated(leaderCard.getId(), nickname);
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
        return playerBoard.canBuy(developmentCard.getDiscountedPrice(playerBoard));
    }

    /**
     * Called when canBuy() returns true <br>
     * Subtracts all buffers from all the deposits and the vault in order to buy one development card
     * @return true if the subtraction is successful
     */
    public boolean buy(){
        view.notifyBoughtCard(nickname);
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

    /**
     * Clears all the buffers when the selected resources are wrong and we have to cancel the payment
     */
    public boolean emptyBuffers(){
        return playerBoard.emptyBuffers();
    }
    //------------------------------------------------------------------------------------------------------------------

    //CONVERT METHODS---(Calls the methods of the lowest levels)------------------------------------------------------------
    /**
     * canConvert checks if and how many conversions are available
     * @return INACTIVE if the player has no active conversion leader <br>
     *         AUTOMATIC if he has only one active conversion leader <br>
     *         CHOICE REQUIRED if he has two conversion leader
     */
    public ConversionMode canConvert() {
        return playerBoard.getConversionSite().canConvert();
    }

    /**
     * Method called for conversion when there's only one a single conversion available, thus no choice by the user is needed.
     * @return The converted input array
     */
    public boolean convert(ArrayList<ResourceContainer> marketOutput) {
        return playerBoard.getConversionSite().convert(marketOutput);
    }


    //------------------------------------------------------------------------------------------------------------------

    //PRODUCTION PIPELINE---(Calls the methods of the lowest levels)----------------------------------------------------
    /**
     * Puts all the resources needed to activate the selected production cards and all the resources produced by those cards into a buffer
     * @param selectedProductionCards is the list of the cards selected by the user
     */
    public boolean fillProductionBuffers(ArrayList<ProductionSlot> selectedProductionCards){
        return playerBoard.fillProductionBuffers(selectedProductionCards);
    }

    /**
     * Checks if the user has enough resources altogether before the user starts to selected them (vault + deposit)
     *  in order to activate the production
     * @return true if he has enough total resources
     */
    public  boolean hasEnoughResourcesForProduction(){
        return playerBoard.hasEnoughResourcesForProduction();
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
     * Checks if the resources selected before and now present in the buffers are equal to the ones needed to produce
     * @return true if he can produce the previously selected cards
     * @throws NotEnoughResources if the user has not enough resources
     * @throws DepositSlotMaxDimExceeded if the user selected too many resources
     */
    public boolean canProduce() throws NotEnoughResources, DepositSlotMaxDimExceeded {
        return playerBoard.canProduce();
    }

    /**
     * Called after canProduce() <br>
     * Execute the production
     */
    public boolean produce(){
       return playerBoard.produce();
    }
    //------------------------------------------------------------------------------------------------------------------


    //CHECKS ------(Calls the methods of the lowest levels)-------------------------------------------------------------
    /**
     * Uses the current quantity of the single requested ResourceContainer (sum of deposit and vault)
     * @return true if the user has enough resources
     */
    public boolean hasEnoughResources(ResourceContainer requested){
        return playerBoard.hasEnoughResources(requested);
    }

    /**
     * Returns the  current quantity of the requested ArrayList of resources (sum of deposit and vault)
     * @return true if the user has enough resources
     */
    public boolean hasEnoughResources (ArrayList<ResourceContainer> requested) {
        return playerBoard.hasEnoughResources(requested);
    }

    /**
     * Returns the  current quantity of the requested Map<ResourceType, ResourceContainer> (sum of deposit and vault)
     * @return true if the user has enough resources
     */
    public boolean hasEnoughResources(HashMap<ResourceType, ResourceContainer> requested){
        return playerBoard.hasEnoughResources(requested);
    }

    /**
     * Checks if a specific element inside of inputArr is present in the HashMap and if they can be subtracted <br>
     * If everything goes right the element is added bufferArr
     * @return true if the ResourceType of inputContainer is present as a Key and if the quantity can be removed
     * @throws NotEnoughResources if it doesn't contain a specific ResourceType OR if it doesn't contain enough resources
     * of that specific ResourceType
     */
    public boolean canRemoveFromVault(ResourceContainer inputContainer) throws NotEnoughResources {
        return playerBoard.getVault().canRemoveFromVault(inputContainer);
    }

    /**
     * Checks if a series of elements inside of inputArr is present in the HashMap and if they can be subtracted <br>
     * if everything goes right bufferMap is put "=" to inputArr
     * @param inputArr is an array of ResourceContainer. It contains a list of elements <br>
     * (they must be different from each other) and we need to check if they are in the Vault
     * @return true if the elements in inputArr are present in the HashMap
     * @throws NotEnoughResources if it doesn't contain a specific ResourceType OR if it doesn't contain enough resources
     * of that specific ResourceType
     */
    public boolean canRemoveFromVault(ArrayList<ResourceContainer> inputArr) throws NotEnoughResources {
        return playerBoard.getVault().canRemoveFromVault(inputArr);
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

    public ProductionSlot getProductionSlotByID(int n) throws IndexOutOfBoundsException {
        return playerBoard.getProductionSlotByID(n);
    }

    public String getNickname() {
        return nickname;
    }

    public PlayerBoard getPlayerBoard() {
        return playerBoard;
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

    public DepositSlot getDepositSlotByID(int id) throws IndexOutOfBoundsException{
        return playerBoard.getDepositSlotByID(id);
    }

    public ArrayList<LeaderCard> getHand() {
        return hand;
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

    public PlayerStatus getPlayerStatus() {
        return playerStatus;
    }

    public void setPlayerStatus(PlayerStatus playerStatus) {
        this.playerStatus = playerStatus;
    }
    //------------------------------------------------------------------------------------------------------------------

    //OBSERVABLE MODEL--------------------------------------------------------------------------------------------------
    @Override
    public void addView(ObserverModel view) {
        this.view = view;
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
