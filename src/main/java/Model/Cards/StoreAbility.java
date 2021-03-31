package Model.Cards;

import Model.Player.Deposit.LeaderDepositSlot;
import Model.Player.PlayerBoard;
import Model.Resources.ResourceType;

public class StoreAbility implements Ability {
    private ResourceType resourceType;
    int maxDim;

    public StoreAbility(ResourceType resourceType, int maxDim) {
        this.resourceType = resourceType;
        this.maxDim = maxDim;
    }

    /**
     * create a new LeaderDepositSlot in the "Deposit" section
     * @param playerBoard
     * @return true
     */
    @Override
    public boolean useAbility(PlayerBoard playerBoard) {
        playerBoard.getDeposit().addDepositSlot(new LeaderDepositSlot(resourceType, maxDim));
        return true;
    }
}
