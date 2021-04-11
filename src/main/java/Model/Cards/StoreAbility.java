package Model.Cards;

import Model.Player.Deposit.LeaderDepositSlot;
import Model.Player.PlayerBoard;
import Model.Resources.ResourceType;

public class StoreAbility implements Ability {
    private final ResourceType resourceType;
    int maxDim;

    public StoreAbility(ResourceType resourceType, int maxDim) {
        this.resourceType = resourceType;
        this.maxDim = maxDim;
    }

    /**
     * create a new LeaderDepositSlot in the "Deposit" section
     */
    @Override
    public boolean useAbility(PlayerBoard playerBoard) {
        playerBoard.getDeposit().addDepositSlot(new LeaderDepositSlot(resourceType, maxDim));
        return true;
    }

    @Override
    public String toString() {
        return "StoreAbility{" +
                "resourceType=" + resourceType +
                ", maxDim=" + maxDim +
                '}';
    }
}
