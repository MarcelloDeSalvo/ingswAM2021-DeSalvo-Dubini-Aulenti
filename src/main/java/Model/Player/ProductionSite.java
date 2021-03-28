package Model.Player;

import Model.Resources.ResourceContainer;
import Model.Resources.ResourceType;

import java.util.ArrayList;
import java.util.HashMap;

public class ProductionSite {
    private ArrayList<ProductionSlot> productionSlots;
    private HashMap<ResourceType, ResourceContainer> bufferInputMap;
    private HashMap<ResourceType, ResourceContainer> bufferOutputMap;

    public ProductionSite(int DefaultNum) {
        this.productionSlots = new ArrayList<ProductionSlot>();
        productionSlots.add(new BaseProductionSlot());
        for(int i=0; i<DefaultNum; i++){
            productionSlots.add(new DevelopmentCardProduction());
        }

        this.bufferInputMap= new HashMap<ResourceType, ResourceContainer>();
        this.bufferOutputMap= new HashMap<ResourceType, ResourceContainer>();
    }

    //Per aggiungere LeaderCardProduction useremo una funz addProductionSlot

}


