package Model.Player;

import Model.Cards.DevelopmentCard;
import Model.Cards.LeaderCard;
import Model.Cards.Status;
import Model.Exceptions.DepositSlotMaxDimExceeded;
import Model.Exceptions.NotEnoughResources;
import Model.Player.Production.ProductionSlot;
import Model.Resources.ResourceContainer;

import java.util.ArrayList;

public class Player {
    private String nickname;
    private ArrayList<LeaderCard> hand;
    private PlayerBoard playerBoard;
    private int orderID;

    public Player(String nickname) {
        this.nickname = nickname;
        this.playerBoard = new PlayerBoard(3,3);
        hand = new ArrayList<>();
    }

    public Player(String nickname,int orderID) {
        this.nickname = nickname;
        this.playerBoard = new PlayerBoard(3,3);
        this.orderID = orderID;
        hand = new ArrayList<>();
    }

    public Player(String nickname, int pyramidHeight, int prodSlotNum) {
        this.nickname = nickname;
        this.playerBoard = new PlayerBoard(pyramidHeight,prodSlotNum);
        hand = new ArrayList<>();
    }

    public Player(String nickname, int pyramidHeight, int prodSlotNum, int orderID) {
        this.nickname = nickname;
        this.playerBoard = new PlayerBoard(pyramidHeight,prodSlotNum);
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
     * @param i is the index that starts from 0 (and must be less than hand.size())
     * @return true if it can be removed
     * @throws NullPointerException
     * @throws IndexOutOfBoundsException
     */
    public boolean discardFromHand(int i) throws NullPointerException, IndexOutOfBoundsException{
        if (i >= 0 && i<hand.size() && hand.contains(hand.get(i))){
            return hand.remove(hand.get(i));
        }

        return false;
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

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public ArrayList<LeaderCard> getHand() {
        return hand;
    }

    public void setHand(ArrayList<LeaderCard> hand) {
        this.hand = hand;
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
}
