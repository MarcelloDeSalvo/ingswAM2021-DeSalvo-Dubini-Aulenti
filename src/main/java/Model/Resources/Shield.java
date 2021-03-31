package Model.Resources;

import Model.Player.Deposit.DepositSlot;

public class Shield extends Resource{


    public Shield() {
        super (ResourceType.SHIELD);
    }
    @Override
    public boolean addToDeposit (DepositSlot depositslot) {
        if (depositslot.addToDepositSlot(new ResourceContainer(this.getResourceType(), 1)))
            return true;
        else
            return false;
    }


}