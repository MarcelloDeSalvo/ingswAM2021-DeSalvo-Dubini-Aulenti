package Model.Resources;

import Model.ObservableFaithPath;
import Model.ObserverFaithPath;
import Model.Player.Deposit.DepositSlot;
import Model.Player.Vault;

import java.util.ArrayList;

public enum ResourceType implements ObservableFaithPath {
    GOLD(true,true,false),
    MINION(true,true,false),
    STONE(true,true,false),
    SHIELD(true,true,false),
    FAITHPOINT(false,false,true),
    BLANK(false,false,false);

    /**
     * Resources' permissions
     */
    private final boolean canAddToVault;
    private final boolean canAddToDeposit;
    private final boolean canAddToFaithPath;

    static ArrayList<ObserverFaithPath> observers = new ArrayList<>();

    ResourceType(boolean canAddToVault, boolean canAddToDeposit, boolean canAddToFaithPath){
        this.canAddToVault = canAddToVault;
        this.canAddToDeposit = canAddToDeposit;
        this.canAddToFaithPath = canAddToFaithPath;
    }


    //RESOURCE PERMISSIONS----------------------------------------------------------------------------------------------
    /**
     * Adds a container to a Vault
     * @param container is the input container
     * @param vault is the destination
     * @return true if the resource has the permission
     */
    public boolean addToVault(ResourceContainer container, Vault vault){

        if(!container.getResourceType().canAddToVault())
            return false;

        return vault.addToVault(container);
    }

    /**
     * Adds a container to a Deposit
     * @param container is the input container
     * @param depositSlot is the destination
     * @return true if the resource has the permission
     */
    public boolean addToDeposit (ResourceContainer container, DepositSlot depositSlot) {
        if(!container.getResourceType().canAddToDeposit())
            return false;

        return depositSlot.addToDepositSlot(new ResourceContainer(container.getResourceType(), 1));
    }

    /**
     * Increments the current player position by notifying FaithPath with an Observer
     * @param container is the input container
     * @return true if the resource has the permission
     */
    public boolean addToFaithPath (ResourceContainer container){
        if(container.getResourceType().canAddToFaithPath()) {
            notifyFaithPath(container.getQty());
            return true;
        }
        else
            return false;
    }

    public boolean canAddToVault() {
        return canAddToVault;
    }

    public boolean canAddToDeposit() {
        return canAddToDeposit;
    }

    public boolean canAddToFaithPath() {
        return canAddToFaithPath;
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


    public ArrayList<ObserverFaithPath> getObservers() {
        return observers;
    }
}



