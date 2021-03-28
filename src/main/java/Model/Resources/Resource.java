package Model.Resources;

import Model.FaithPath;
import Model.Player.Deposit;
import Model.Player.DepositSlot;


import java.io.IOException;

abstract class Resource {

    private ResourceType resourceType;

    /**
     * This method adds a single resource to a Deposit Slot
     * @param depositslot
     * @return
     */
    public boolean addToDeposit (DepositSlot depositslot) {
            if (depositslot.addToDepositSlot(new ResourceContainer(this.resourceType, 1)))
                return true;
            else
                return false;
    }



    public boolean addToFaithPath (FaithPath faithPath){
        return true;
    }




}
