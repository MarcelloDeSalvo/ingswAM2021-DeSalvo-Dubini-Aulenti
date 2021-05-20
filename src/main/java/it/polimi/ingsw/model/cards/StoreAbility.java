package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.player.deposit.LeaderDeposit;
import it.polimi.ingsw.model.player.PlayerBoard_AbilityAccess;
import it.polimi.ingsw.model.resources.ResourceType;

public class StoreAbility implements Ability {
    private final ResourceType resourceType;
    private final int maxDim;

    public StoreAbility(ResourceType resourceType, int maxDim) {
        this.resourceType = resourceType;
        this.maxDim = maxDim;
    }

    /**
     * create a new LeaderDeposit in the "Deposit" section
     */
    @Override
    public boolean useAbility(PlayerBoard_AbilityAccess playerBoard) {
        int id = playerBoard.getDeposit().getDepositList().size()+1;
        System.out.println("store ability: " + id);
        playerBoard.getDeposit().addDepositSlot(new LeaderDeposit(resourceType, maxDim, id));
        return true;
    }

    //JAVA--------------------------------------------------------------------------------------------------------------
    @Override
    public String toString() {
        return "\n - Store Ability: " +
                "ResourceType = " + resourceType +
                ", MaxDim = " + maxDim;
    }
    //------------------------------------------------------------------------------------------------------------------

}
