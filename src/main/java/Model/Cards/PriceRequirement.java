package Model.Cards;

import Model.Player.PlayerBoard;
import Model.Resources.ResourceContainer;

public class PriceRequirement implements Requirement{
    private ResourceContainer price;

    public PriceRequirement(ResourceContainer price) {
        this.price = price;
    }

    @Override
    public boolean checkRequirements(PlayerBoard playerBoard) {
        return false;
    }
}
