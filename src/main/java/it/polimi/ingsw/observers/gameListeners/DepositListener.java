package it.polimi.ingsw.observers.gameListeners;

import it.polimi.ingsw.model.resources.ResourceContainer;
import it.polimi.ingsw.model.resources.ResourceType;

public interface DepositListener {
    void notifyNewDepositSlot(int maxDim, ResourceType resourceType, String senderNick);
    void notifyDepositChanges(int id, ResourceContainer resourceContainer, boolean added, String senderNick);
}
