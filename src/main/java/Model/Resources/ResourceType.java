package Model.Resources;

import Model.ObserverFaithPath;

import java.util.ArrayList;

public enum ResourceType{
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


    ResourceType(boolean canAddToVault, boolean canAddToDeposit, boolean canAddToFaithPath){
        this.canAddToVault = canAddToVault;
        this.canAddToDeposit = canAddToDeposit;
        this.canAddToFaithPath = canAddToFaithPath;
    }


    //RESOURCE PERMISSIONS----------------------------------------------------------------------------------------------
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
}



