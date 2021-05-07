package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.player.deposit.LeaderDeposit;
import it.polimi.ingsw.model.player.PlayerBoard_AbilityAccess;
import it.polimi.ingsw.model.resources.ResourceType;

public class StoreAbility implements Ability {
    private final ResourceType resourceType;
    int maxDim;

    public StoreAbility(ResourceType resourceType, int maxDim) {
        this.resourceType = resourceType;
        this.maxDim = maxDim;
    }

    /**
     * create a new LeaderDeposit in the "Deposit" section
     */
    @Override
    public boolean useAbility(PlayerBoard_AbilityAccess playerBoard) {
        playerBoard.getDeposit().addDepositSlot(new LeaderDeposit(resourceType, maxDim));
        return true;
    }

    //JAVA--------------------------------------------------------------------------------------------------------------
    @Override
    public String toString() {
        return "\nStoreAbility: " +
                "ResourceType = " + resourceType +
                ", MaxDim = " + maxDim;
    }
    //------------------------------------------------------------------------------------------------------------------

}
