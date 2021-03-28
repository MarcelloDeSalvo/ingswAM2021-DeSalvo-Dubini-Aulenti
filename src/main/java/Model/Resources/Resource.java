package Model.Resources;

import Model.FaithPath;
import Model.Player.DepositSlot;


abstract class Resource {

    private ResourceType resourceType;

    public Resource(ResourceType resourceType) {
        this.resourceType = resourceType;
    }

    /**
     * This method adds a single resource to a Deposit Slot. Both this method and addToFaithPath return false by default
     * since they will be later overridden.
     * @param depositslot
     */
    public boolean addToDeposit (DepositSlot depositslot){
        return false;
    }

    /**
     * This method moves the current player's position on the faithpath by one
     * @param faithPath
     */
    public boolean addToFaithPath (FaithPath faithPath){
        return false;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public void setResourceType(ResourceType resourceType) {
        this.resourceType = resourceType;
    }
}
