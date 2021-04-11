package Model.Cards;

import Model.Player.PlayerBoard;
import Model.Resources.ResourceContainer;

import java.util.ArrayList;

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

    @Override
    public String toString() {
        return "ResourceRequirement{" +
                "resourceContainer=" + resourceContainer +
                '}';
    }

    @Override
    public Colour getColour() {
        return null;
    }

    @Override
    public int getLevel() {
        return 0;
    }

    @Override
    public int getDevelopmentCardNumber() {
        return 0;
    }

    @Override
    public ResourceContainer getResourceRequirement() {
        return resourceContainer;
    }
}
