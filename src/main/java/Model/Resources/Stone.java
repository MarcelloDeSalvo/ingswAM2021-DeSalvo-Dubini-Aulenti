package Model.Resources;

import Model.Player.DepositSlot;

public class Stone extends Resource{


    public Stone() {
        super (ResourceType.STONE);
    }

    @Override
    public boolean addToDeposit (DepositSlot depositslot) {
        if (depositslot.addToDepositSlot(new ResourceContainer(this.getResourceType(), 1)))
            return true;
        else
            return false;
    }


}