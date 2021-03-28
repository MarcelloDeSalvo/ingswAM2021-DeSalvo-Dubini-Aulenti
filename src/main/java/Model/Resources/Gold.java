package Model.Resources;

import Model.Player.DepositSlot;

public class Gold {
    ResourceType resourceType;

    public boolean addToDeposit (DepositSlot depositslot) {
        if (depositslot.addToDepositSlot(new ResourceContainer(this.resourceType, 1)))
            return true;
        else
            return false;
    }


    public Gold(ResourceType resourceType) {
        this.resourceType = ResourceType.GOLD;
    }
}

