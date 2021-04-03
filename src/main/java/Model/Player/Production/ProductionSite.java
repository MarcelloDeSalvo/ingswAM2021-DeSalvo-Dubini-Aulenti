package Model.Player.Production;

import Model.Cards.Colour;
import Model.Player.PlayerBoard;
import Model.Player.Vault;
import Model.Resources.ResourceContainer;
import Model.Resources.ResourceType;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class ProductionSite {
    private ArrayList<ProductionSlot> productionSlots;
    private HashMap<ResourceType, ResourceContainer> bufferInputMap;
    private HashMap<ResourceType, ResourceContainer> bufferOutputMap;

    int defaultNum;

    /**
     * Builds a new Production site with a maximum number of DevelopmentCards defaultnum
     * @param defaultNum
     */
    public ProductionSite(int defaultNum) {
        this.defaultNum = defaultNum;
        this.productionSlots = new ArrayList<ProductionSlot>();

        productionSlots.add(new BaseProductionSlot());

        for(int i=0; i<defaultNum; i++){
            productionSlots.add(new DevelopmentCardProduction());
        }

        this.bufferInputMap = new HashMap<ResourceType, ResourceContainer>();
        this.bufferOutputMap = new HashMap<ResourceType, ResourceContainer>();
    }

    //Per aggiungere LeaderCardProduction useremo una funz addProductionSlot
    /**
     * checks if a specific ResourceType is present in the HashMap
     * @param type is the key that will be used to check in the HashMap
     * @return true if present, false otherwise
     */
    private boolean isInputPresent(ResourceType type){
        return bufferInputMap.containsKey(type);
    }
    /**
     * checks if a specific ResourceType is present in the HashMap
     * @param type is the key that will be used to check in the HashMap
     * @return true if present, false otherwise
     */
    private boolean isOutputPresent(ResourceType type){
        return bufferOutputMap.containsKey(type);
    }

    public boolean hasEnoughDevelopmentCardsWith(int numberRequired, int level, Colour colour){
        int cardNumber = 0;
        for(ProductionSlot ps : productionSlots){
            cardNumber += ps.countCardsWith(level, colour);
        }

        return cardNumber >= numberRequired;
    }

    public boolean addProductionSlot(ProductionSlot productionSlot){
        if(productionSlot != null && productionSlots.add(productionSlot))
            return true;
        return false;
    }

    public boolean addToInputMap (ArrayList<ResourceContainer> tempProductionInput){
        Iterator<ResourceContainer> iterator= tempProductionInput.iterator();
        ResourceContainer current;
        while(iterator.hasNext()){
            current=iterator.next();
            if(isInputPresent(current.getResourceType())){
                bufferInputMap.get(current.getResourceType()).addQty(current.getQty());
            }
            else
                bufferInputMap.put(current.getResourceType(),new ResourceContainer(current.getResourceType(),current.getQty()));
        }
        return true;
    }

    public boolean addToOutputMap (ArrayList<ResourceContainer> tempProductionOutput){
        Iterator<ResourceContainer> iterator= tempProductionOutput.iterator();
        ResourceContainer current;
        while(iterator.hasNext()){
            current=iterator.next();
            if(isOutputPresent(current.getResourceType())){
                bufferOutputMap.get(current.getResourceType()).addQty(current.getQty());
            }
            else
                bufferOutputMap.put(current.getResourceType(),new ResourceContainer(current.getResourceType(),current.getQty()));
        }
        return true;
    }

    public  boolean hasEnoughInputResources(HashMap<ResourceType,ResourceContainer> bufferInputMap, PlayerBoard playerBoard){
        for (ResourceType key : bufferInputMap.keySet())
        {
            if(playerBoard.checkResources(key)<bufferInputMap.get(key).getQty())
                return false;
        }
        return true;
    }

    public boolean clearBuffers(){
        bufferInputMap.clear();
        bufferOutputMap.clear();
        return true;
    }

    public boolean produce(HashMap<ResourceType,ResourceContainer> bufferOutputMap, Vault vault){
        for (ResourceType key : bufferInputMap.keySet()){
            if(!vault.addToVault(bufferOutputMap.get(key)))
                return false;
        }
        return true;
    }





    //getter and setter
    public ArrayList<ProductionSlot> getProductionSlots() {
        return productionSlots;
    }

    public void setProductionSlots(ArrayList<ProductionSlot> productionSlots) {
        this.productionSlots = productionSlots;
    }

    public HashMap<ResourceType, ResourceContainer> getBufferInputMap() {
        return bufferInputMap;
    }

    public void setBufferInputMap(HashMap<ResourceType, ResourceContainer> bufferInputMap) {
        this.bufferInputMap = bufferInputMap;
    }

    public HashMap<ResourceType, ResourceContainer> getBufferOutputMap() {
        return bufferOutputMap;
    }

    public void setBufferOutputMap(HashMap<ResourceType, ResourceContainer> bufferOutputMap) {
        this.bufferOutputMap = bufferOutputMap;
    }



}


