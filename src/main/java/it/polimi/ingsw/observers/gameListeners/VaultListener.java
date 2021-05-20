package it.polimi.ingsw.observers.gameListeners;

import it.polimi.ingsw.model.resources.ResourceContainer;

public interface VaultListener {

    void notifyVaultAdd(ResourceContainer added);
    void notifyVaultRemove(ResourceContainer removed);
}
