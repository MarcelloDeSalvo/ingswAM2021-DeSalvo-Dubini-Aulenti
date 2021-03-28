package Model.Player;

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
     * for each element of in inputArr it calls the method that adds a single ResourceContainer
     * to the HashMap
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
        if(isPresent(container.getResourceType())){
            vaultMap.get(container.getResourceType()).addQta(container.getQta());
        }
        else
            vaultMap.put(container.getResourceType(), container);

        return true;
    }

    /**
     * The method removes a series of different Resources from the HashMap
     * @param inputArr is an ArrayList of ResourceContainer
     * @return true if it successfully removes the resources, false otherwise
     */
    public boolean removeFromVault(ArrayList<ResourceContainer> inputArr){
        if(checkVault(inputArr))
        {
            Iterator<ResourceContainer> iter = inputArr.iterator();

            while(iter.hasNext()){
                if(!removeFromVault(iter.next()))
                    return false;
            }
            return true;
        }
        else
            return false;
    }

    /**
     * The method removes a specific quantity of a specific resource type from the HashMap
     * @param inputcontainer is a single ResourceContainer
     * @return true if it successfully removes the resources, false otherwise
     */
    public boolean removeFromVault(ResourceContainer inputcontainer){
        ResourceType currType = inputcontainer.getResourceType();

        if(isPresent(currType)){
            if(vaultMap.get(currType).canRemove(inputcontainer)){
                vaultMap.get(currType).addQta(-inputcontainer.getQta());
                return true;
            }
            else
                return false;
        }
        else
            return false;
    }

    /**
     * checks if a specific ResourceType is present in the HashMap
     * @param type is the key that will be used to check in the HashMap
     * @return true if present, false otherwise
     */
    public boolean isPresent(ResourceType type){
        return vaultMap.containsKey(type);
    }

    /**
     * checks if a series of elements inside of inputArr is present in the HashMap
     * @param inputArr is an array of ResourceContainer that contains a list of elements that
     * @return true if the elements in inputArr are present in the HashMap, false otherwise
     */
    public boolean checkVault(ArrayList<ResourceContainer> inputArr){
        Iterator<ResourceContainer> iter = inputArr.iterator();

        while(iter.hasNext()){
            if(!vaultMap.get(iter.next().getResourceType()).canRemove(iter.next()))
                return false;
        }
        return true;
    }
}
