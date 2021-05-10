package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.player.PlayerBoard;
import it.polimi.ingsw.model.resources.ResourceContainer;
import it.polimi.ingsw.view.cli.Color;

import java.util.ArrayList;
import java.util.Objects;

abstract class Card {

    private int id;
    private int victoryPoints;
    private Status status;
    private ArrayList<Requirement> requirements;
    private ArrayList<ResourceContainer> price;

    public Card(int vPoints, Status status, ArrayList<Requirement> req, ArrayList<ResourceContainer> price){
        this.victoryPoints = vPoints;
        this.status = status;
        this.requirements = new ArrayList<>(req);
        this.price = price;
    }

    public Card(int vPoints, Status status, ArrayList<ResourceContainer> price){
        this.victoryPoints = vPoints;
        this.status = status;
        this.requirements = new ArrayList<>();
        this.price = price;
    }

    public Card(int vPoints, ArrayList<Requirement> req, Status status){
        this.victoryPoints = vPoints;
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

    public Card(int victoryPoints, Status status, int id) {
        this.victoryPoints = victoryPoints;
        this.status = status;
        this.id = id;
        this.requirements = new ArrayList<>();
        this.price = new ArrayList<>();
    }


    //CARD MANAGEMENT---------------------------------------------------------------------------------------------------
    /**
     * changes the current card's status
     * @param status is the status that i want to set
     * @return true if he changed it
     */
    public abstract boolean changeStatus(Status status);

    /**
     * Adds a requirement to the list
     * @return true if the add finishes without problems
     */
    public boolean addRequirement(Requirement requirement) throws NullPointerException, IllegalArgumentException {
        return requirement != null && this.requirements.add(requirement);
    }

    /**
     * Adds a ResourceContainer to the price List
     * @param priceContainer is the added price
     * @return true if the insert ends without exception
     */
    public boolean addPrice(ResourceContainer priceContainer) throws NullPointerException, IllegalArgumentException{
        return priceContainer != null && this.price.add(priceContainer);
    }
    //------------------------------------------------------------------------------------------------------------------




    //GETTER AND SETTER-------------------------------------------------------------------------------------------------
    /**
     * Checks if the current users has some active discount and return the card's discounted price
     * @param playerBoard is the current's player's playerBoard
     * @return the discounted price for the current player
     */
    public ArrayList<ResourceContainer> getDiscountedPrice(PlayerBoard playerBoard){
        ArrayList<ResourceContainer> discountedPrice = new ArrayList<>();

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

    public int getId() { return id; }
    //------------------------------------------------------------------------------------------------------------------


    //JAVA--------------------------------------------------------------------------------------------------------------
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("(ID: ").append(id).append(") \n");
        stringBuilder.append("VictoryPoints: ").append(victoryPoints);

        if(status != null)
            stringBuilder.append(", Status: ").append(status);
        if(requirements != null){
            stringBuilder.append(Color.ANSI_CYAN.escape()).append("\nRequirements: ").append(Color.ANSI_RESET.escape());
            for (Requirement req: requirements) {
                stringBuilder.append(req);
            }
        }
        if(price != null)
            stringBuilder.append("Price:\n").append(price).append(".");

        return stringBuilder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Card)) return false;
        Card card = (Card) o;
        return  victoryPoints == card.victoryPoints &&
                status == card.status &&
                Objects.equals(requirements, card.requirements) &&
                Objects.equals(price, card.price);
    }
    //------------------------------------------------------------------------------------------------------------------


}
