package Model.Player;

import Model.Resources.ResourceContainer;
import Model.Resources.ResourceType;

import java.util.HashMap;

public class DiscountSite {
    private final HashMap<ResourceType, Integer> discountMap;

    public DiscountSite() {
        this.discountMap = new HashMap<>();
    }

    /**
     * This method is used to know the discount relating to a specific ResourceType
     * @param resourceType is the ResourceType that i'm interested to know the discount of
     * @return 0 if the element is absent, the correct value otherwise
     */
    public int getDiscount(ResourceType resourceType) {
        if(!isPresent(resourceType))
            return 0;
        else
            return discountMap.get(resourceType);
    }

    private boolean isPresent(ResourceType type){
        return discountMap.containsKey(type);
    }

    /**
     * If a ResourceType is present the method simply adds the qty of the HashMap to the qty of ResourceContainer
     * otherwise, when a ResourceType is absent, the method put a new element in the HashMap
     * @param resourceContainer is the element to add in the HashMap
     * @return true
     */
    public boolean addDiscount(ResourceContainer resourceContainer) {
        ResourceType currType = resourceContainer.getResourceType();

        if(isPresent(currType))
            discountMap.replace(currType, discountMap.get(currType) + resourceContainer.getQty());
        else
            discountMap.put(currType, resourceContainer.getQty());

        return true;
    }

    public HashMap<ResourceType, Integer> getDiscountMap() {
        return discountMap;
    }
}
