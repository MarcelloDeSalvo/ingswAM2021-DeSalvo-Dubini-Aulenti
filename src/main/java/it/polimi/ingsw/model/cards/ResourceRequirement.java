package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.player.PlayerBoard;
import it.polimi.ingsw.model.resources.ResourceContainer;


public class ResourceRequirement implements Requirement{
    private final ResourceContainer resourceContainer;

    public ResourceRequirement(ResourceContainer resourceContainer) {
        this.resourceContainer = resourceContainer;
    }


    /**
     * Checks if the player has enough resources to satisfy the requirement
     * @return true if he has enough resources, false if he doesn't
     */
    @Override
    public boolean checkRequirements(PlayerBoard playerBoard) {
        int amount = playerBoard.checkResources(resourceContainer.getResourceType());

        return amount >= resourceContainer.getQty();
    }


    //GETTER AND SETTER-------------------------------------------------------------------------------------------------
    @Override
    public Colour getColour() {
        return null;
    }

    @Override
    public int getLevel() {
        return 0;
    }

    @Override
    public ResourceContainer getResourceRequirement() {
        return resourceContainer;
    }
    //------------------------------------------------------------------------------------------------------------------


    //JAVA--------------------------------------------------------------------------------------------------------------
    @Override
    public String toString() {
        return "\n - Resource Requirement: " +
                resourceContainer.getQty() + " " + resourceContainer.getResourceType();
    }
    //------------------------------------------------------------------------------------------------------------------

}
