package it.polimi.ingsw.liteModel;

import it.polimi.ingsw.model.Util;
import it.polimi.ingsw.model.resources.ResourceContainer;
import it.polimi.ingsw.model.resources.ResourceType;
import it.polimi.ingsw.view.cli.Color;

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
     * The method removes a specific quantity of a specific resource type from the HashMap.
     * @param inputContainer is a single ResourceContainer
     */
    public void removeFromVault(ResourceContainer inputContainer) {
        vaultMap.get(inputContainer.getResourceType()).addQty(-inputContainer.getQty());
    }

    /**
     * If a container for a specific ResourceType already exists, the method simply adds the quantity to it <br>
     * otherwise it creates the relative ResourceType key element in the HashMap
     */
    public void addToVault(ResourceContainer container) {
        if(Util.isPresent(container.getResourceType(), vaultMap))
            vaultMap.get(container.getResourceType()).addQty(container.getQty());
        else
            vaultMap.put(container.getResourceType(), container);
    }

    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Color.ANSI_BLUE.escape()).append("VAULT: \n\n").append(Color.ANSI_RESET.escape());

        if(vaultMap.isEmpty())
            stringBuilder.append(Color.ANSI_RED.escape()).append("EMPTY!").append(Color.ANSI_RESET.escape());
        else{
            stringBuilder.append("--------------------\n");
            for (ResourceType key:vaultMap.keySet()) {
                if(vaultMap.get(key).getQty()!=0) {
                    stringBuilder.append(key.toString()).append(": ");
                    stringBuilder.append(vaultMap.get(key).getQty()).append("\n");
                    stringBuilder.append("--------------------\n");
                }
            }
        }
        return stringBuilder.toString();
    }
}
