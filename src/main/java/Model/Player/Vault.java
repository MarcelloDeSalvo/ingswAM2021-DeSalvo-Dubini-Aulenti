package Model.Player;

import Model.Exceptions.NotEnoughResources;
import Model.Resources.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Vault {
    /**
     * vaultMap is an HashMap used to store ResourceContainers (Resources and their quantity) using ResourceType as key
     */
    private HashMap<ResourceType, ResourceContainer> vaultMap;

    public Vault() {
        vaultMap = new HashMap<ResourceType, ResourceContainer>();
    }

    /**
     * checks if a specific ResourceType is present in the HashMap
     * @param type is the key that will be used to check in the HashMap
     * @return true if present, false otherwise
     */
    private boolean isPresent(ResourceType type){
        return vaultMap.containsKey(type);
    }

    /**
     * for each element of inputArr it calls the method that adds a single ResourceContainer to the HashMap
     * @param inputArr is an ArrayList of ResourceContainer
     * @return true
     */
    public boolean addToVault(ArrayList<ResourceContainer> inputArr) {
        Iterator<ResourceContainer> iter = inputArr.iterator();

        while(iter.hasNext())
            addToVault(iter.next());

        return true;
    }

    /**
     * if a container for a specific ResourceType already exists, the method simply adds the quantity to it
     * otherwise it creates the relative ResourceType key element in the HashMap
     * @param container
     * @return true
     */
    public boolean addToVault(ResourceContainer container) {
        if(isPresent(container.getResourceType()))
            vaultMap.get(container.getResourceType()).addQta(container.getQta());
        else
            vaultMap.put(container.getResourceType(), container);

        return true;
    }

    /**
     * for each element of inputArr it calls the method that remove a single ResourceContainer to the HashMap
     * this method needs to be called after canRemoveFrom Vault
     * @param inputArr is an ArrayList of ResourceContainer
     * @return true
     */
    public boolean removeFromVault(ArrayList<ResourceContainer> inputArr){
        Iterator<ResourceContainer> iter = inputArr.iterator();

        while(iter.hasNext())
            removeFromVault(iter.next());

        return true;
    }

    /**
     * The method removes a specific quantity of a specific resource type from the HashMap
     * this method needs to be called after canRemoveFrom Vault
     * @param inputcontainer is a single ResourceContainer
     * @return true
     */
    public boolean removeFromVault(ResourceContainer inputcontainer){
        vaultMap.get(inputcontainer.getResourceType()).addQta(-inputcontainer.getQta());
        return true;
    }

    /**
     * checks if a series of elements inside of inputArr is present in the HashMap
     * @param inputArr is an array of ResourceContainer. It contains a list of elements
     * (they must be different from each other) and we need to check if they are in the Vault
     * @return true if the elements in inputArr are present in the HashMap
     * @throws NotEnoughResources if it doesn't contain a specific ResourceType OR if it doesn't contain enough resources of that specific ResourceType
     */
    public boolean canRemoveFromVault(ArrayList<ResourceContainer> inputArr) throws NotEnoughResources {
        Iterator<ResourceContainer> iter = inputArr.iterator();

        while(iter.hasNext()){
            if(!isPresent(iter.next().getResourceType()))
                throw new NotEnoughResources("There are currently 0 " + iter.next().getResourceType() + " in the Vault!");
            else if(!vaultMap.get(iter.next().getResourceType()).hasEnough(iter.next()))
                throw new NotEnoughResources("Not enough resources");
        }

        return true;
    }

    public HashMap<ResourceType, ResourceContainer> getVaultMap() {
        return vaultMap;
    }

    public ResourceContainer getResourceQuantity(ResourceType type) throws NotEnoughResources{
        if(isPresent(type))
            return vaultMap.get(type);
        else
            throw new NotEnoughResources("There are currently 0 " + type + " in the Vault!");
    }
}
