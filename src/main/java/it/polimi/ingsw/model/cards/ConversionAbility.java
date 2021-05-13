package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.player.PlayerBoard_AbilityAccess;
import it.polimi.ingsw.model.resources.ResourceContainer;
import it.polimi.ingsw.model.resources.ResourceType;

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
        return "\n - Conversion Ability: " +
                "Output = " + output;
    }
    //------------------------------------------------------------------------------------------------------------------

}
