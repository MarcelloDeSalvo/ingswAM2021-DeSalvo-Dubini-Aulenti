package Model.Player.Production;

import Model.Cards.Colour;
import Model.Exceptions.DepositSlotMaxDimExceeded;
import Model.Exceptions.NotEnoughResources;
import Model.Player.PlayerBoard;
import Model.Player.Vault;
import Model.Resources.ResourceContainer;
import Model.Resources.ResourceType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class ProductionSite {
    private ArrayList<ProductionSlot> productionSlots;
    private HashMap<ResourceType, ResourceContainer> bufferSelectedResources;
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
        this.bufferSelectedResources = new HashMap<ResourceType, ResourceContainer>();
    }

    public boolean activateProduction(ArrayList<ProductionSlot> productionSlots){
        for (ProductionSlot ps: productionSlots) {
            if(!addToMap(ps.getProductionInput(),bufferInputMap))
                return false;

            if(!addToMap(ps.getProductionOutput(),bufferOutputMap))
                return false;
        }
        return true;
    }


    /**
     * checks if a specific ResourceType is present in the HashMap
     * @param type is the key that will be used to check in the HashMap
     * @return true if present, false otherwise
     */
    private boolean isPresent(ResourceType type, HashMap<ResourceType, ResourceContainer> map){
        return map.containsKey(type);
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

    public boolean addToMap (ArrayList<ResourceContainer> tempProductionInput, HashMap<ResourceType, ResourceContainer> map){
        Iterator<ResourceContainer> iterator= tempProductionInput.iterator();
        ResourceContainer current;
        while(iterator.hasNext()){
            current=iterator.next();
            if(isPresent(current.getResourceType(), map)){
                map.get(current.getResourceType()).addQty(current.getQty());
            }
            else
                map.put(current.getResourceType(),new ResourceContainer(current.getResourceType(),current.getQty()));
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

    public boolean canProduce(ArrayList<ResourceContainer> selectedResources) throws NotEnoughResources, DepositSlotMaxDimExceeded{
        addToMap(selectedResources, bufferSelectedResources);
        for (ResourceType key: bufferSelectedResources.keySet()) {

            if(!bufferInputMap.containsKey(key))
                throw new NotEnoughResources ("You miss one ResourceType");
            if(bufferInputMap.get(key).getQty() > bufferSelectedResources.get(key).getQty())
                throw new NotEnoughResources ("You need more resources");
            if(bufferInputMap.get(key).getQty() < bufferSelectedResources.get(key).getQty())
                throw new DepositSlotMaxDimExceeded("You selected too many resources");
        }

        return true;
    }


    public boolean produce(Vault vault){
        for (ResourceType key : bufferOutputMap.keySet()){
            if(!vault.addToVault(bufferOutputMap.get(key)))
                return false;
        }
        return true;
    }


    //getter and setter
    public int getBufferInputResourceQty(ResourceType resourceType){
       if (bufferInputMap.containsKey(resourceType))
           return bufferInputMap.get(resourceType).getQty();
       return 0;
    }
    public int getBufferOutputResourceQty(ResourceType resourceType){
        if (bufferOutputMap.containsKey(resourceType))
            return bufferOutputMap.get(resourceType).getQty();
        return 0;
    }

    public ProductionSlot getProductionSlotByID(int id){
        return productionSlots.get(id);
    }

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

    public HashMap<ResourceType, ResourceContainer> getBufferSelectedResources() {
        return bufferSelectedResources;
    }

    public void setBufferSelectedResources(HashMap<ResourceType, ResourceContainer> bufferSelectedResources) {
        this.bufferSelectedResources = bufferSelectedResources;
    }

    public int getDefaultNum() {
        return defaultNum;
    }

    public void setDefaultNum(int defaultNum) {
        this.defaultNum = defaultNum;
    }
}


