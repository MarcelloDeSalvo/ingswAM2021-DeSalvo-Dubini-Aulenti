package model.cards;

import model.player.PlayerBoard_AbilityAccess;
import model.resources.ResourceContainer;
import model.resources.ResourceType;

public class DiscountAbility implements Ability{
    private final ResourceType resourceType;
    private final int discount;

    public DiscountAbility(ResourceType resourceType, int discount) {
        this.resourceType = resourceType;
        this.discount = discount;
    }


    /**
     * creates a new Discount to put in "DiscountSite" section using "addDiscount"
     * @param playerBoard is the current player's playerBoard
     * @return true
     */
    @Override
    public boolean useAbility(PlayerBoard_AbilityAccess playerBoard) {
        playerBoard.getDiscountSite().addDiscount(new ResourceContainer(resourceType, discount));
        return true;
    }

    //GETTER AND SETTER-------------------------------------------------------------------------------------------------
    public ResourceType getResourceType() {
        return resourceType;
    }

    public int getDiscount() {
        return discount;
    }
    //------------------------------------------------------------------------------------------------------------------


    //JAVA--------------------------------------------------------------------------------------------------------------
    @Override
    public String toString() {
        return "DiscountAbility{" +
                "resourceType=" + resourceType +
                ", discount=" + discount +
                '}';
    }
    //------------------------------------------------------------------------------------------------------------------

}
