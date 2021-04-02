package Model.Cards;

import Model.Player.PlayerBoard;
import Model.Resources.ResourceContainer;

public class PriceRequirement implements Requirement{
    private ResourceContainer price;

    public PriceRequirement(ResourceContainer price) {
        this.price = price;
    }

    /**
     * Check if the player has some active discount for one specific ResourceType
     * @param playerBoard is the current player's PlayerBoard
     * @return the discounted price or the original price
     */
    @Override
    public boolean checkRequirements(PlayerBoard playerBoard) {
        int discountQty = playerBoard.getDiscountSite().getDiscount(this.price.getResourceType());
        int discountedPrice = this.price.getQty() - discountQty;
        return true;
    }


    //getter and setter
    public ResourceContainer getPrice() {
        return price;
    }

    public void setPrice(ResourceContainer price) {
        this.price = price;
    }
}
