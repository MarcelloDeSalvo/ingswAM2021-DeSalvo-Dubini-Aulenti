package Model.Player;

import Model.Resources.ResourceContainer;
import Model.Resources.ResourceType;

public class DefaultDepositSlot extends DepositSlot {
    public DefaultDepositSlot(int maxDim, ResourceType depositResourceType) {
        super(maxDim,  depositResourceType);
    }
}
