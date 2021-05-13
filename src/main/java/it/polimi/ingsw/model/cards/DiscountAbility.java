package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.player.PlayerBoard_AbilityAccess;
import it.polimi.ingsw.model.resources.ResourceContainer;
import it.polimi.ingsw.model.resources.ResourceType;

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
        return "\n - Discount Ability: " +
                "ResourceType = " + resourceType +
                ", Discount = " + discount;
    }
    //------------------------------------------------------------------------------------------------------------------

}
