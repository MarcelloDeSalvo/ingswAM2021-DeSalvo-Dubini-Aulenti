package Model.Cards;

import Model.Player.PlayerBoard;
import Model.Resources.ResourceType;

public class ConversionAbility implements Ability {
    private ResourceType output;

    public ConversionAbility(ResourceType output) {
        this.output = output;
    }

    @Override
    public boolean useAbility(PlayerBoard playerBoard) {
        return false;
    }
}
