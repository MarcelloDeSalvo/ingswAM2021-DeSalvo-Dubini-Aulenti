package Model.Cards;

import Model.Player.PlayerBoard;
import Model.Resources.ResourceContainer;

public class ResourceRequirement implements Requirement{
    private ResourceContainer resourceContainer;

    public ResourceRequirement(ResourceContainer resourceContainer) {
        this.resourceContainer = resourceContainer;
    }

    @Override
    public boolean checkRequirements(PlayerBoard playerBoard) {
        return false;
    }
}
