package Model.Player;

import Model.Exceptions.DepositSlotMaxDimExceeded;
import Model.Exceptions.NotEnoughResources;
import Model.Player.Deposit.Deposit;
import Model.Player.Deposit.DepositSlot;
import Model.Player.Production.ProductionSite;
import Model.Resources.ResourceContainer;
import Model.Resources.ResourceType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.stream.Collectors;

public class PlayerBoard {

    private final Deposit deposit;
    private final Vault vault;
    private final ProductionSite productionSite;
    private final ConversionSite conversionSite;
    private final DiscountSite discountSite;

    public PlayerBoard(int PyramidNum, int ProdSlotNum) {
        this.deposit = new Deposit(PyramidNum);
        this.vault = new Vault();
        this.productionSite = new ProductionSite(ProdSlotNum);
        this.conversionSite = new ConversionSite();
        this.discountSite = new DiscountSite();
    }

    /**
     * Returns the  current quantity of the requested ResourceType ( sum of deposit and vault)
     * @return
     */
    public int checkResources(ResourceType requested){
        return(deposit.checkDeposit(requested) + vault.getResourceQuantity(requested));
    }

    public boolean canProduce(ArrayList<ResourceContainer> selectedResources) throws NotEnoughResources, DepositSlotMaxDimExceeded {
        return productionSite.canProduce(selectedResources);
    }

    public boolean produce(){
        return productionSite.produce(vault);
    }

    public boolean canBuy(ArrayList<ResourceContainer> selectedResources) {
        HashMap<ResourceType, ResourceContainer> bufferMap = new HashMap<ResourceType, ResourceContainer>();
        HashMap<ResourceType, ResourceContainer> selectedResourcesMap = new HashMap<ResourceType, ResourceContainer>();

        for (DepositSlot ds: deposit.getDepositList()) {
            ResourceType key = ds.getDepositResourceType();
            if(!bufferMap.containsKey(key))
                bufferMap.put(key, new ResourceContainer(key, ds.getBufferContainer().getQty()));
            else
                bufferMap.get(key).addQty(ds.getBufferContainer().getQty());
        }

        for (ResourceContainer rc: vault.getBufferArr()) {
            ResourceType key = rc.getResourceType();
            if(!bufferMap.containsKey(key))
                bufferMap.put(key, new ResourceContainer(key, rc.getQty()));
            else
                bufferMap.get(key).addQty(rc.getQty());
        }

        selectedResourcesMap = arrayliistToMap(selectedResources);

        return selectedResourcesMap.equals(bufferMap);
    }

    /*public HashMap<ResourceType, ResourceContainer> convertListAfterJava8(ArrayList<ResourceContainer> list) {
        HashMap<ResourceType, ResourceContainer>  map = list.stream()
                .collect(Collectors.toMap(ResourceContainer::getResourceType, type -> type));
        return map;
    }*/

    public HashMap<ResourceType, ResourceContainer> arrayliistToMap (ArrayList<ResourceContainer> tempProductionInput){
        HashMap<ResourceType, ResourceContainer> map = new HashMap<ResourceType, ResourceContainer>();
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
        return map;
    }

    /**
     * checks if a specific ResourceType is present in the HashMap
     * @param type is the key that will be used to check in the HashMap
     * @return true if present, false otherwise
     */
    private boolean isPresent(ResourceType type, HashMap<ResourceType, ResourceContainer> map){
        return map.containsKey(type);
    }

    public Deposit getDeposit() {
        return deposit;
    }

    public Vault getVault() {
        return vault;
    }

    public ProductionSite getProductionSite() {
        return productionSite;
    }

    public ConversionSite getConvertionSite() {
        return conversionSite;
    }

    public DiscountSite getDiscountSite() {
        return discountSite;
    }
}


