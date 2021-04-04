package Model.Cards;

import Model.Player.PlayerBoard;
import Model.Resources.ResourceContainer;
import Model.Resources.ResourceType;

public class ConversionAbility implements Ability {
    private ResourceType output;

    public ConversionAbility(ResourceType output) {
        this.output = output;
    }

    /**
     * Adds an available conversion to the current player
     * @param playerBoard is the current Player's playerBoard
     * @return
     */
    @Override
    public boolean useAbility(PlayerBoard playerBoard) {
        playerBoard.getConvertionSite().addConversion(new ResourceContainer(output, 1));
        return true;
    }
}
