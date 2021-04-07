package Model.Player;

import Model.Cards.DevelopmentCard;
import Model.Exceptions.DepositSlotMaxDimExceeded;
import Model.Exceptions.NotEnoughResources;
import Model.Player.Production.ProductionSlot;
import Model.Resources.ResourceContainer;
import Model.Resources.ResourceType;

import java.util.ArrayList;
import java.util.HashSet;

public class Player {
    private String nickname;
    private PlayerBoard playerBoard;

    public Player(String nickname) {
        this.nickname = nickname;
        this.playerBoard = new PlayerBoard(3,3);
    }


    public boolean activateProduction(ArrayList<ProductionSlot> selectedProductionCard){
        return playerBoard.getProductionSite().activateProduction(selectedProductionCard);
    }

    public boolean canProduce(ArrayList<ResourceContainer> selectedResources) throws NotEnoughResources, DepositSlotMaxDimExceeded {
        return playerBoard.canProduce(selectedResources);
    }

    public boolean produce(){
       return playerBoard.produce();
    }

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
