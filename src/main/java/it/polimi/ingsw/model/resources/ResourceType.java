package it.polimi.ingsw.model.resources;

import it.polimi.ingsw.view.cli.Color;

public enum ResourceType{
    GOLD(true,true,false, Color.ANSI_YELLOW),
    MINION(true,true,false, Color.ANSI_PURPLE),
    STONE(true,true,false, Color.ANSI_GRAY),
    SHIELD(true,true,false, Color.ANSI_BLUE),
    FAITH(false,false,true, Color.ANSI_RED),
    BLANK(false,false,false, Color.ANSI_WHITE);

    /**
     * Resources' permissions
     */
    private final boolean canAddToVault;
    private final boolean canAddToDeposit;
    private final boolean canAddToFaithPath;

    private final Color color;


    ResourceType(boolean canAddToVault, boolean canAddToDeposit, boolean canAddToFaithPath, Color color){
        this.canAddToVault = canAddToVault;
        this.canAddToDeposit = canAddToDeposit;
        this.canAddToFaithPath = canAddToFaithPath;
        this.color = color;
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


    @Override
    public String toString() {
        return this.color.escape() + super.toString() + Color.ANSI_RESET.escape();
    }


}



