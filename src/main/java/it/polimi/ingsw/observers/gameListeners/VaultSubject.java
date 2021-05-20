package it.polimi.ingsw.observers.gameListeners;

import it.polimi.ingsw.model.resources.ResourceContainer;

public interface VaultSubject {
    void addListeners(VaultListener faithPathListener);
}
