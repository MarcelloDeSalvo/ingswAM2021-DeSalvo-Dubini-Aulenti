package Model.Player;

import Model.Resources.Resource;
import Model.Resources.ResourceContainer;
import Model.Resources.ResourceType;

public abstract class DepositSlot {
    private int maxDim;
    private ResourceContainer storageArea;
    private ResourceType DepositResourceType;

    public DepositSlot(int maxDim, ResourceType depositResourceType) {
        this.maxDim = maxDim;
        DepositResourceType = depositResourceType;
    }
}
