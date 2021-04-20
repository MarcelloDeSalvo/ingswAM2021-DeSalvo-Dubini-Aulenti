package model.cards;

import model.player.PlayerBoard_AbilityAccess;
import model.resources.ResourceContainer;
import model.resources.ResourceType;

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
    public boolean useAbility(PlayerBoard_AbilityAccess playerBoard) {
        playerBoard.getConversionSite().addConversion(new ResourceContainer(output, 1));
        return true;
    }


    //JAVA--------------------------------------------------------------------------------------------------------------
    @Override
    public String toString() {
        return "ConversionAbility{" +
                "output=" + output +
                '}';
    }
    //------------------------------------------------------------------------------------------------------------------

}
