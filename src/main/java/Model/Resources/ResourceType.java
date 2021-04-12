package Model.Resources;

import Model.Player.Deposit.DepositSlot;
import Model.Player.Vault;

public enum ResourceType {
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
    private final boolean canAddToFaithPath ;

    ResourceType(boolean canAddToVault, boolean canAddToDeposit, boolean canAddToFaithPath){
        this.canAddToVault = canAddToVault;
        this.canAddToDeposit = canAddToDeposit;
        this.canAddToFaithPath = canAddToFaithPath;
    }


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
     * Increments the current player position
     * @param container is the input container
     * @return true if the resource has the permission
     */
    public boolean addToFaithPath (ResourceContainer container){
        return container.getResourceType().canAddToFaithPath();
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
}



