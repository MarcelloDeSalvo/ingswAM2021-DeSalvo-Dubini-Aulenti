package Model.Cards;

import Model.Player.PlayerBoard;
import Model.Resources.ResourceContainer;

import java.util.ArrayList;

abstract class Card {
    private int victoryPoints;
    private Status status;
    private ArrayList<Requirement> requirements;
    private ArrayList<ResourceContainer> price;

    public Card(int vpoints, Status status, ArrayList<Requirement> req, ArrayList<ResourceContainer> price){
        this.victoryPoints = vpoints;
        this.status = status;
        this.requirements = new ArrayList<>(req);
        this.price = price;
    }

    public Card(int vpoints, Status status, ArrayList<ResourceContainer> price){
        this.victoryPoints = vpoints;
        this.status = status;
        this.requirements = new ArrayList<>();
        this.price = price;
    }

    public Card(int vpoints, ArrayList<Requirement> req, Status status){
        this.victoryPoints = vpoints;
        this.status = status;
        this.requirements = new ArrayList<>(req);
        this.price = new ArrayList<>();
    }

    public Card(int victoryPoints, Status status) {
        this.victoryPoints = victoryPoints;
        this.status = status;
        this.requirements = new ArrayList<>();
        this.price = new ArrayList<>();
    }

    /**
     * Adds a requirement to the list
     * @param requirement
     * @return
     */
    public boolean addRequirement(Requirement requirement) {
        if(requirement!= null && this.requirements.add(requirement))
            return true;
        return false;
    }

    /**
     * Adds a ResourceContainer to the price List
     * @param priceContainer is the added price
     * @return true if the insert ends without exception
     */
    public boolean addPrice(ResourceContainer priceContainer){
        if(priceContainer!= null && this.price.add(priceContainer))
            return true;
        return false;
    }

    /**
     * Checks if the current users has some active discount and return the card's discounted price
     * @param playerBoard
     * @return
     */
    public ArrayList<ResourceContainer> getDiscountedPrice(PlayerBoard playerBoard){
        ArrayList<ResourceContainer> discountedPrice = new ArrayList<ResourceContainer>();

        for (ResourceContainer rs: price) {
            int discountAmount = playerBoard.getDiscountSite().getDiscount(rs.getResourceType());
            int discountPrice;

            if(discountAmount > rs.getQty())
                discountPrice = 0;
            else
                discountPrice = rs.getQty() - discountAmount;

            discountedPrice.add(new ResourceContainer(rs.getResourceType(), discountPrice));
        }

        return discountedPrice;
    }


    /**
     * changes the current card's status
     * @param status
     * @return
     */
    public abstract boolean changeStatus(Status status);


    public int getVictoryPoints() {
        return victoryPoints;
    }

    public void setVictoryPoints(int victoryPoints) { this.victoryPoints = victoryPoints; }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public ArrayList<Requirement> getRequirements() {
        return requirements;
    }

    public void setRequirements(ArrayList<Requirement> requirements) { this.requirements = requirements; }


    public ArrayList<ResourceContainer> getPrice() {
        return price;
    }

    public void setPrice(ArrayList<ResourceContainer> price) {
        this.price = price;
    }
}
