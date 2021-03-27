package Model.Player;

import Model.Resources.ResourceContainer;
import Model.Resources.ResourceType;

import java.io.IOException;

public class DefaultDepositSlot extends DepositSlot {

    public DefaultDepositSlot(int maxDim, ResourceType depositResourceType) {
        super(maxDim,  depositResourceType);
    }

    @Override
    public Boolean addToDepositSlot(ResourceContainer inputContainer) throws IOException {
        return null;
    }

    @Override
    public Boolean removeFromDepositSlot(ResourceContainer inputContainer) throws IOException {
        return null;
    }
}
