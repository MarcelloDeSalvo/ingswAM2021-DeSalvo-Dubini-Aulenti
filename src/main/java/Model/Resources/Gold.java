package Model.Resources;

import Model.Player.DepositSlot;

public class Gold extends Resource{
   // ResourceType resourceType; -> Lo eredita da Resource

    public Gold() {
        super (ResourceType.GOLD);
    }

    @Override
    public boolean addToDeposit (DepositSlot depositslot) {
        if (depositslot.addToDepositSlot(new ResourceContainer(this.getResourceType(), 1))) //Ho aggiunto la get
            return true;
        else
            return false;
    }

}

