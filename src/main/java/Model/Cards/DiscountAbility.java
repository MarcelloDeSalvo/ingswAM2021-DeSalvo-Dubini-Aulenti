package Model.Cards;

import Model.Player.PlayerBoard;
import Model.Resources.ResourceType;

public class DiscountAbility implements Ability{
    private ResourceType resourceType;
    private int discount;

    public DiscountAbility(ResourceType resourceType, int discount) {
        this.resourceType = resourceType;
        this.discount = discount;
    }

    @Override
    public boolean useAbility(PlayerBoard playerBoard) {
        return false;
    }
}
