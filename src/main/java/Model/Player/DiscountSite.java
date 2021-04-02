package Model.Player;

import Model.Resources.ResourceContainer;
import Model.Resources.ResourceType;

import java.util.HashMap;

public class DiscountSite {
    private HashMap<ResourceType, Integer> discountMap;

    public DiscountSite() {
        this.discountMap = new HashMap<ResourceType, Integer>();
    }

    public int getDiscount(ResourceType resourceType) {
        if(!isPresent(resourceType))
            return 0;
        else
            return discountMap.get(resourceType);
    }

    private boolean isPresent(ResourceType type){
        return discountMap.containsKey(type);
    }

    public boolean addDiscount(ResourceContainer resourceContainer) {
        ResourceType currType = resourceContainer.getResourceType();

        if(isPresent(currType))
            discountMap.replace(currType, discountMap.get(currType) + resourceContainer.getQty());
        else
            discountMap.put(currType, resourceContainer.getQty());

        return true;
    }



}
