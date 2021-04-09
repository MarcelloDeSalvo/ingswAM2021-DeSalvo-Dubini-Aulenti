package Model.Resources;


import Model.FaithPath;
import Model.Player.Deposit.DepositSlot;
import Model.Player.Player;
import Model.Player.Vault;

public enum ResourceType {
    GOLD(1), MINION(1), STONE(1), SHIELD(1), FAITHPOINT(2), BLANK(0);

    private final int permission;
    ResourceType(int permission){
        this.permission = permission;
    }


    public boolean addToVault(ResourceContainer container, Vault vault){

        if(container.getResourceType().getPermission() != 1 )
            return false;

        if (vault.addToVault(container))
            return true;
        else
            return false;
    }

    public boolean addToDeposit (ResourceContainer container, DepositSlot depositslot) {
        if(container.getResourceType().getPermission() != 1 )
            return false;

        if (depositslot.addToDepositSlot(new ResourceContainer(container.getResourceType(), 1)))
            return true;
        else
            return false;
    }

    public boolean addToFaithPath (ResourceContainer container){
        if(container.getResourceType().getPermission() != 2 )
            return false;

        return true;
    }

    public int getPermission() {
        return permission;
    }
}



