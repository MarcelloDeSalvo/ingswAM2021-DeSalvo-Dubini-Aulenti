package it.polimi.ingsw.model.resources;

import it.polimi.ingsw.model.FaithPath;
import it.polimi.ingsw.model.player.deposit.DepositSlot;
import it.polimi.ingsw.model.player.Vault;

public class ResourceContainer {

    private ResourceType resourceType;

    /**
     * Quantity of resources inside the container
     */
    private int qty;

    public ResourceContainer(ResourceType resourceType, int qty) throws ArithmeticException {

        if(qty < 0)
            throw new ArithmeticException("ResourceContainer can't have a negative qty!");
        else {
            this.resourceType = resourceType;
            this.qty = qty;
        }
    }


    //CONTAINER MANAGEMENT----------------------------------------------------------------------------------------------
    public void addQty(int n) {
        setQty(this.qty + n);
    }


    public boolean canRemove(ResourceContainer container) {
        return this.isTheSameType(container) && this.hasEnough(container);
    }

    public boolean isTheSameType(ResourceContainer container) {
        return this.resourceType == container.getResourceType();
    }

    public boolean hasEnough(ResourceContainer container) {
        return this.getQty() >= container.getQty();
    }

    /**
     * Sums the qty of two containers and check if it has enough <br>
     * Used mainly when there are buffers involved <br>
     * Requires two containers of the same type
     * @return true if this container can subtract the sum of two containers
     */
    public boolean hasEnough(ResourceContainer container1, ResourceContainer bufferContainer) {
        if(bufferContainer == null)
            return this.getQty() >= container1.getQty();
        else
            return this.getQty() >= container1.getQty() + bufferContainer.getQty();
    }

    public boolean isEmpty(){
        return this.qty == 0;
    }

    public boolean isEditable(){ return this.resourceType == null; }
    //------------------------------------------------------------------------------------------------------------------



    //MOVING RESOURCES AROUND-------------------------------------------------------------------------------------------
    /**
     * Adds the container to a Vault
     * @param vault is the destination
     * @return true if the resource has the permission
     */
    public boolean addToVault(Vault vault){
        if(!this.getResourceType().canAddToVault())
            return false;

        return vault.addToVault(this);
    }

    /**
     * Adds the container to a Deposit
     * @param depositSlot is the destination
     * @return true if the resource has the permission
     */
    public boolean addToDeposit (DepositSlot depositSlot) {
        if(!this.getResourceType().canAddToDeposit())
            return false;

        return depositSlot.addToDepositSlot(this);
    }

    /**
     * Increments the current player position by notifying FaithPath with an Observer
     * @return true if the resource has the permission
     */
    public boolean addToFaithPath (FaithPath faithPath){
        if(this.getResourceType().canAddToFaithPath()) {
            faithPath.incrementPosition(this.getQty());
            return true;
        }
        else
            return false;
    }
    //------------------------------------------------------------------------------------------------------------------



    //GETTER AND SETTER-------------------------------------------------------------------------------------------------
    public ResourceType getResourceType() {
        return resourceType;
    }

    public void setResourceType(ResourceType resourceType) {
        this.resourceType = resourceType;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) throws ArithmeticException {
        if(qty < 0)
            throw new ArithmeticException("ResourceContainer can't have a negative qty");
        else
            this.qty = qty;
    }
    //------------------------------------------------------------------------------------------------------------------



    //JAVA-------------------------------------------------------------------------------------------------------------
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResourceContainer)) return false;
        ResourceContainer container = (ResourceContainer) o;
        return qty == container.qty && resourceType == container.resourceType;
        
    }

    @Override
    public String toString() {
        return "ResourceType = " + resourceType +
                ", Qty = " + qty ;
    }
    //------------------------------------------------------------------------------------------------------------------

}
