package it.polimi.ingsw.observers.gameListeners;

import it.polimi.ingsw.model.resources.ResourceContainer;

public interface VaultListener {

    /**
     * Updates the listener (View) that a resourceContainer is added or removed
     * @param added true if the container was added, false if it was removed
     */
    void notifyVaultChanges(ResourceContainer container, boolean added);
}
