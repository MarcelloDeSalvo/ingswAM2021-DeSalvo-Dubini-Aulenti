package it.polimi.ingsw.liteModel;

import it.polimi.ingsw.model.Util;
import it.polimi.ingsw.model.resources.ResourceContainer;
import it.polimi.ingsw.model.resources.ResourceType;

import java.util.HashMap;

public class LiteVault {
    /**
     * vaultMap is an HashMap used to store ResourceContainers (Resources and their quantity) using ResourceType as key
     */
    private final HashMap<ResourceType, ResourceContainer> vaultMap;

    public LiteVault() {
        vaultMap = new HashMap<>();
    }

    /**
     * The method removes a specific quantity of a specific resource type from the HashMap <br>
     * this method needs to be called after canRemoveFrom Vault
     * @param inputContainer is a single ResourceContainer
     * @return true
     */
    public boolean removeFromVault(ResourceContainer inputContainer) {
        vaultMap.get(inputContainer.getResourceType()).addQty(-inputContainer.getQty());
        return true;
    }

    /**
     * If a container for a specific ResourceType already exists, the method simply adds the quantity to it <br>
     * otherwise it creates the relative ResourceType key element in the HashMap
     * @return true
     */
    public boolean addToVault(ResourceContainer container) {
        if(Util.isPresent(container.getResourceType(), vaultMap))
            vaultMap.get(container.getResourceType()).addQty(container.getQty());
        else
            vaultMap.put(container.getResourceType(), container);

        return true;
    }
}
