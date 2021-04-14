package Model.Resources;

import Model.ObservableFaithPath;
import Model.ObserverFaithPath;
import Model.Player.Deposit.DepositSlot;
import Model.Player.Vault;

import java.util.ArrayList;

public class ResourceContainer implements ObservableFaithPath {

    private ResourceType resourceType;

    /**
     * Quantity of resources inside the container
     */
    private int qty;

    static ArrayList<ObserverFaithPath> observers = new ArrayList<>();

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
    public boolean addToFaithPath (){
        if(this.getResourceType().canAddToFaithPath()) {
            notifyFaithPath(this.getQty());
            return true;
        }
        else
            return false;
    }

    //------------------------------------------------------------------------------------------------------------------



    //OBSERVER METHODS--------------------------------------------------------------------------------------------------
    @Override
    public void addObserver(ObserverFaithPath observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(ObserverFaithPath observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyFaithPath(int faithPoints) {
        for (ObserverFaithPath observer : observers) {
            observer.update(faithPoints);
        }
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

    public ArrayList<ObserverFaithPath> getObservers() {
        return observers;
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
        return "ResourceContainer{" +
                "resourceType=" + resourceType +
                ", qty=" + qty +
                '}';
    }
    //------------------------------------------------------------------------------------------------------------------

}
