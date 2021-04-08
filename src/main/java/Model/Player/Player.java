package Model.Player;

import Model.Cards.DevelopmentCard;
import Model.Exceptions.DepositSlotMaxDimExceeded;
import Model.Exceptions.NotEnoughResources;
import Model.Player.Production.ProductionSlot;
import Model.Resources.ResourceContainer;

import java.util.ArrayList;

public class Player {
    private String nickname;
    private PlayerBoard playerBoard;

    public Player(String nickname) {
        this.nickname = nickname;
        this.playerBoard = new PlayerBoard(3,3);
    }

    /**
     * Puts all the resources needed to activate the selected production cards and all the resources produced by those cards into a buffer
     * @param selectedProductionCard is the list of the cards selected by the user
     */
    public boolean activateProduction(ArrayList<ProductionSlot> selectedProductionCard){
        return playerBoard.getProductionSite().activateProduction(selectedProductionCard);
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
     * Called after canProduce()
     * Execute the production
     * @return
     */
    public boolean produce(){
       return playerBoard.produce();
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

    //getter and setter
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

}
