package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.Util;
import it.polimi.ingsw.model.exceptions.NotEnoughResources;
import it.polimi.ingsw.model.resources.ResourceContainer;
import it.polimi.ingsw.model.resources.ResourceType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Vault {

    /**
     * vaultMap is an HashMap used to store ResourceContainers (Resources and their quantity) using ResourceType as key
     */
    private final HashMap<ResourceType, ResourceContainer> vaultMap;
    private HashMap<ResourceType, ResourceContainer> bufferMap;

    public Vault() {
        vaultMap = new HashMap<>();
        bufferMap = new HashMap<>();
    }


    //VAULT MANAGEMENT--------------------------------------------------------------------------------------------------
    /**
     * For each element of inputArr it calls the method that adds a single ResourceContainer to the HashMap
     * @param inputArr is an ArrayList of ResourceContainer
     * @return true
     */
    public boolean addToVault(ArrayList<ResourceContainer> inputArr) {

        for (ResourceContainer resourceContainer : inputArr) addToVault(resourceContainer);

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

    /**
     * For each element of bufferMap it calls the method that remove a single ResourceContainer to the HashMap <br>
     * this method needs to be called after canRemoveFrom Vault
     * @return true
     */
    public boolean removeFromVault(){
        if(bufferMap == null)
            return false;
        for (ResourceType key : bufferMap.keySet()) removeFromVault(bufferMap.get(key));
        return true;
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
     * Checks if a series of elements inside of inputArr is present in the HashMap and if they can be subtracted <br>
     * if everything goes right bufferMap is put "=" to inputArr
     * @param inputArr is an array of ResourceContainer. It contains a list of elements <br>
     * (they must be different from each other) and we need to check if they are in the Vault
     * @return true if the elements in inputArr are present in the HashMap
     * @throws NotEnoughResources if it doesn't contain a specific ResourceType OR if it doesn't contain enough resources
     * of that specific ResourceType
     */
    public boolean canRemoveFromVault(ArrayList<ResourceContainer> inputArr) throws NotEnoughResources {
        Iterator<ResourceContainer> iter = inputArr.iterator();
        ResourceContainer current ;

        while(iter.hasNext()){
            current = iter.next();
            if(!Util.isPresent(current.getResourceType(),vaultMap))
                throw new NotEnoughResources("There are currently 0 " + current.getResourceType() + " in the Vault!");
            else if(!vaultMap.get(current.getResourceType()).hasEnough(current, bufferMap.get(current.getResourceType())))
                throw new NotEnoughResources("Not enough resources");
        }

        bufferMap = Util.arraylistToMap(inputArr);
        return true;
    }

    /**
     * Checks if a specific element inside of inputArr is present in the HashMap and if they can be subtracted <br>
     * If everything goes right the element is added bufferArr
     * @return true if the ResourceType of inputContainer is present as a Key and if the quantity can be removed
     * @throws NotEnoughResources if it doesn't contain a specific ResourceType OR if it doesn't contain enough resources
     * of that specific ResourceType
     */
    public boolean canRemoveFromVault(ResourceContainer inputContainer) throws NotEnoughResources {

        if(!Util.isPresent(inputContainer.getResourceType(),vaultMap))
            throw new NotEnoughResources("There are currently 0 " + inputContainer.getResourceType() + " in the Vault!");
        else if(!vaultMap.get(inputContainer.getResourceType()).hasEnough(inputContainer, bufferMap.get(inputContainer.getResourceType())))
            throw new NotEnoughResources("Not enough resources");


        if(Util.isPresent(inputContainer.getResourceType(), bufferMap))
            bufferMap.get(inputContainer.getResourceType()).addQty(inputContainer.getQty());
        else
            bufferMap.put(inputContainer.getResourceType(), inputContainer);

        return true;
    }

    /**
     * Clears the buffer containing elements that needs to be removed
     * @return true
     */
    public boolean clearBuffer() {
        bufferMap.clear();
        return true;
    }

    /**
     * Returns the total amount of resources present in the vault
     */
    public int totalQuantityOfResourcesInVault(){
        int c = 0;
        for (Map.Entry<ResourceType, ResourceContainer> entry:vaultMap.entrySet()) {
            c = c + entry.getValue().getQty();

        }
        return c;
    }
    //------------------------------------------------------------------------------------------------------------------



    //GETTER AND SETTER-------------------------------------------------------------------------------------------------
    public HashMap<ResourceType, ResourceContainer> getVaultMap() {
        return vaultMap;
    }

    public int getResourceQuantity(ResourceType type) {
        if(Util.isPresent(type,vaultMap))
            return vaultMap.get(type).getQty();
        else
            return 0;
    }

    public HashMap<ResourceType, ResourceContainer> getBufferMap() {
        return bufferMap;
    }
    //------------------------------------------------------------------------------------------------------------------


}
