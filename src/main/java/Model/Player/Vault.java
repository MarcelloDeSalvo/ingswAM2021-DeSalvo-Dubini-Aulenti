package Model.Player;

import Model.Exceptions.NotEnoughResources;
import Model.Resources.*;
import Model.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Vault {
    /**
     * vaultMap is an HashMap used to store ResourceContainers (Resources and their quantity) using ResourceType as key
     */
    private final HashMap<ResourceType, ResourceContainer> vaultMap;
    private ArrayList<ResourceContainer> bufferList;

    public Vault() {
        vaultMap = new HashMap<>();
        bufferList = new ArrayList<>();
    }


    /**
     * for each element of inputArr it calls the method that adds a single ResourceContainer to the HashMap
     * @param inputArr is an ArrayList of ResourceContainer
     * @return true
     */
    public boolean addToVault(ArrayList<ResourceContainer> inputArr) {

        for (ResourceContainer resourceContainer : inputArr) addToVault(resourceContainer);

        return true;
    }

    /**
     * if a container for a specific ResourceType already exists, the method simply adds the quantity to it
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
     * for each element of bufferArr it calls the method that remove a single ResourceContainer to the HashMap
     * this method needs to be called after canRemoveFrom Vault
     * @return true
     */
    public boolean removeFromVault(){
        if(bufferList == null)
            return false;
        for (ResourceContainer resourceContainer : bufferList) removeFromVault(resourceContainer);
        return true;
    }

    /**
     * The method removes a specific quantity of a specific resource type from the HashMap
     * this method needs to be called after canRemoveFrom Vault
     * @param inputContainer is a single ResourceContainer
     * @return true
     */
    public boolean removeFromVault(ResourceContainer inputContainer) {
        vaultMap.get(inputContainer.getResourceType()).addQty(-inputContainer.getQty());
        return true;
    }

    /**
     * checks if a series of elements inside of inputArr is present in the HashMap and if they can be subtracted
     * if everything goes right bufferArr is put "=" to inputArr
     * @param inputArr is an array of ResourceContainer. It contains a list of elements
     * (they must be different from each other) and we need to check if they are in the Vault
     * @return true if the elements in inputArr are present in the HashMap
     * @throws NotEnoughResources if it doesn't contain a specific ResourceType OR if it doesn't contain enough resources of that specific ResourceType
     */
    public boolean canRemoveFromVault(ArrayList<ResourceContainer> inputArr) throws NotEnoughResources {
        Iterator<ResourceContainer> iter = inputArr.iterator();
        ResourceContainer current ;

        while(iter.hasNext()){
            current = iter.next();
            if(!Util.isPresent(current.getResourceType(),vaultMap))
                throw new NotEnoughResources("There are currently 0 " + current.getResourceType() + " in the Vault!");
            else if(!vaultMap.get(current.getResourceType()).hasEnough(current))
                throw new NotEnoughResources("Not enough resources");
        }

        bufferList = inputArr;
        return true;
    }

    /**
     * checks if a specific element inside of inputArr is present in the HashMap and if they can be subtracted
     * If everything goes right the element is added bufferArr
     * @return true if the ResourceType of inputContainer is present as a Key and if the quantity can be removed
     * @throws NotEnoughResources if it doesn't contain a specific ResourceType OR if it doesn't contain enough resources of that specific ResourceType
     */
    public boolean canRemoveFromVault(ResourceContainer inputContainer) throws NotEnoughResources {

        if(!Util.isPresent(inputContainer.getResourceType(),vaultMap))
            throw new NotEnoughResources("There are currently 0 " + inputContainer.getResourceType() + " in the Vault!");
        else if(!vaultMap.get(inputContainer.getResourceType()).hasEnough(inputContainer))
            throw new NotEnoughResources("Not enough resources");

        bufferList.add(inputContainer);
        return true;
    }

    /**
     * clears the buffer containing elements that needs to be removed
     * @return true
     */
    public boolean clearBuffer() {
        bufferList.clear();
        return true;
    }


    //getter and setter
    public HashMap<ResourceType, ResourceContainer> getVaultMap() {
        return vaultMap;
    }

    public int getResourceQuantity(ResourceType type) {
        if(Util.isPresent(type,vaultMap))
            return vaultMap.get(type).getQty();
        else
            return 0;
    }

    public ArrayList<ResourceContainer> getBufferList() {
        return bufferList;
    }

}
