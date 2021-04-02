package Model.Cards;

import Model.Player.PlayerBoard;
import Model.Resources.ResourceContainer;
import Model.Resources.ResourceType;

public class DiscountAbility implements Ability{
    private ResourceType resourceType;
    private int discount;

    public DiscountAbility(ResourceType resourceType, int discount) {
        this.resourceType = resourceType;
        this.discount = discount;
    }

    /**
     * creates a new Discount to put in "DiscountSite" section using "addDiscount"
     * @param playerBoard
     * @return true
     */
    @Override
    public boolean useAbility(PlayerBoard playerBoard) {
        playerBoard.getDiscountSite().addDiscount(new ResourceContainer(resourceType, discount));
        return true;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public void setResourceType(ResourceType resourceType) {
        this.resourceType = resourceType;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }
}
