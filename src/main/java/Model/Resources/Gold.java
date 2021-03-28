package Model.Resources;

import Model.Player.DepositSlot;

public class Gold extends Resource{
    ResourceType resourceType;

    public Gold() {
        super (ResourceType.GOLD);
    }
    @Override
    public boolean addToDeposit (DepositSlot depositslot) {
        if (depositslot.addToDepositSlot(new ResourceContainer(this.resourceType, 1)))
            return true;
        else
            return false;
    }

}

