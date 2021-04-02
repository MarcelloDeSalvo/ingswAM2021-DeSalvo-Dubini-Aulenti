package Model.Cards;

import Model.Player.PlayerBoard;
import Model.Resources.ResourceContainer;

public class PriceRequirement implements Requirement{
    private ResourceContainer priceContainer;
    private int discountedPrice;

    public PriceRequirement(ResourceContainer price) {
        this.priceContainer = price;
        this.discountedPrice = price.getQty();
    }

    /**
     * Check if the player has some active discount for one specific ResourceType
     * @param playerBoard is the current player's PlayerBoard
     * @return the discounted price or the original price
     */
    @Override
    public boolean checkRequirements(PlayerBoard playerBoard) {
        int discountQty = playerBoard.getDiscountSite().getDiscount(this.priceContainer.getResourceType());
        this.discountedPrice = this.priceContainer.getQty() - discountQty;
        return true;
    }


    //getter and setter

    public ResourceContainer getPriceContainer() {
        return priceContainer;
    }

    public void setPriceContainer(ResourceContainer priceContainer) {
        this.priceContainer = priceContainer;
    }

    public int getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(int discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

}
