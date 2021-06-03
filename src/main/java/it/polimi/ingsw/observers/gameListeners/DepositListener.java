package it.polimi.ingsw.observers.gameListeners;

import it.polimi.ingsw.model.resources.ResourceContainer;
import it.polimi.ingsw.model.resources.ResourceType;

public interface DepositListener {
    /**
     * Notifies that a new deposit slot has been added
     * @param maxDim maximum dimension of the deposit
     * @param resourceType ResourceType stored in the deposit
     */
    void notifyNewDepositSlot(int maxDim, ResourceType resourceType, String senderNick);

    /**
     * Notifies that something changed in a specific deposit slot
     * @param id deposit slot id
     * @param resourceContainer is the container containing info about the changes
     * @param added true if added, false if subtracted
     */
    void notifyDepositChanges(int id, ResourceContainer resourceContainer, boolean added, String senderNick);
}
