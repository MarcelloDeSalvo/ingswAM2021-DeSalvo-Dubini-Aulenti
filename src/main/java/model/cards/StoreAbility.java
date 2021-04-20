package model.cards;

import model.player.deposit.LeaderDeposit;
import model.player.PlayerBoard_AbilityAccess;
import model.resources.ResourceType;

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
        return "StoreAbility{" +
                "resourceType=" + resourceType +
                ", maxDim=" + maxDim +
                '}';
    }
    //------------------------------------------------------------------------------------------------------------------

}
