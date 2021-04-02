package Model.Cards;

import Model.Player.PlayerBoard;
import Model.Resources.ResourceContainer;

public class ResourceRequirement implements Requirement{
    private ResourceContainer resourceContainer;

    public ResourceRequirement(ResourceContainer resourceContainer) {
        this.resourceContainer = resourceContainer;
    }

    /**
     * Checks if the player has enough resources to satisfy the requirement
     * @param playerBoard
     * @return true if he has enough resources, false if he doesn't
     */
    @Override
    public boolean checkRequirements(PlayerBoard playerBoard) {
        int amount = playerBoard.checkResources(resourceContainer.getResourceType());

        return amount >= resourceContainer.getQty();
    }
}
