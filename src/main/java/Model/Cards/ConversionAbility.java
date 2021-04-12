package Model.Cards;

import Model.Player.PlayerBoard;
import Model.Resources.ResourceContainer;
import Model.Resources.ResourceType;

public class ConversionAbility implements Ability {
    private final ResourceType output;

    public ConversionAbility(ResourceType output) {
        this.output = output;
    }

    /**
     * Adds an available conversion to the current player
     * @param playerBoard is the current Player's playerBoard
     */
    @Override
    public boolean useAbility(PlayerBoard playerBoard) {
        playerBoard.getConversionSite().addConversion(new ResourceContainer(output, 1));
        return true;
    }

    @Override
    public String toString() {
        return "ConversionAbility{" +
                "output=" + output +
                '}';
    }
}
