package Model.Resources;

import Model.Player.DepositSlot;

public class Minion extends Resource{

    public Minion() {
        super (ResourceType.MINION);
    }

    @Override
    public boolean addToDeposit (DepositSlot depositslot) {
        if (depositslot.addToDepositSlot(new ResourceContainer(this.getResourceType(), 1)))
            return true;
        else
            return false;
    }

}