package Model.Player.Production;

import Model.Cards.Colour;
import Model.Resources.ResourceContainer;
import Model.Resources.ResourceType;

import java.util.ArrayList;
import java.util.HashMap;

public class ProductionSite {
    private ArrayList<ProductionSlot> productionSlots;
    private HashMap<ResourceType, ResourceContainer> bufferInputMap;
    private HashMap<ResourceType, ResourceContainer> bufferOutputMap;
    int defaultNum;

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

    public boolean hasEnoughDevelopmentCardsWith(int numberRequired, int level, Colour colour){
        int cardNumber = 0;
        for(ProductionSlot ps : productionSlots){
            cardNumber += ps.countCardsWith(level, colour);
        }

        return cardNumber >= numberRequired;
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
}


