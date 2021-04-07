package Model.Player;

import Model.Cards.DevelopmentCard;
import Model.Exceptions.DepositSlotMaxDimExceeded;
import Model.Exceptions.NotEnoughResources;
import Model.Player.Deposit.Deposit;
import Model.Player.Deposit.DepositSlot;
import Model.Player.Production.ProductionSite;
import Model.Player.Production.ProductionSlot;
import Model.Resources.ResourceContainer;
import Model.Resources.ResourceType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
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
        HashMap<ResourceType, ResourceContainer> bufferMap = new HashMap<>();
        Map<ResourceType, ResourceContainer> selectedResourcesMap = new HashMap<>();

        addDepositBuffer(bufferMap);
        addVaultBuffer(bufferMap);

        arraylistToMap(selectedResources);

        return selectedResourcesMap.equals(bufferMap);
    }

    /**
     * the method adds the buffers from each DepositSlot in Deposit
     * if the element is already present in the HashMap it simply adds the qty, otherwise it creates a new element in the HashMap
     * @param bufferMap
     */
    private void addDepositBuffer (HashMap<ResourceType, ResourceContainer> bufferMap) {
        for (DepositSlot ds: deposit.getDepositList()) {
            ResourceType key = ds.getDepositResourceType();
            if(!bufferMap.containsKey(key))
                bufferMap.put(key, new ResourceContainer(key, ds.getBufferContainer().getQty()));
            else
                bufferMap.get(key).addQty(ds.getBufferContainer().getQty());
        }
    }

    /**
     * the method adds the buffers from Vault
     * if the element is already present in the HashMap it simply adds the qty, otherwise it creates a new element in the HashMap
     * @param bufferMap
     */
    private void addVaultBuffer (HashMap<ResourceType, ResourceContainer> bufferMap) {
        for (ResourceContainer rc: vault.getBufferList()) {
            ResourceType key = rc.getResourceType();
            if(!bufferMap.containsKey(key))
                bufferMap.put(key, new ResourceContainer(key, rc.getQty()));
            else
                bufferMap.get(key).addQty(rc.getQty());
        }
    }

    public void arraylistToMap (ArrayList<ResourceContainer> list) {
        Map<ResourceType, ResourceContainer> map = list.stream()
                .collect(Collectors.toMap(ResourceContainer::getResourceType, resourceContainer -> resourceContainer));
    }

    public boolean buy(){
        return deposit.removeAllBuffers() && vault.removeFromVault();
    }

    public boolean insertBoughtCard(ProductionSlot productionSlot, DevelopmentCard boughtCard){
        int index =0;
        if(productionSlot != null && productionSite.getProductionSlots().contains(productionSlot)) {
            index = productionSite.getProductionSlots().indexOf(productionSlot);
            return productionSite.getProductionSlots().get(index).insertOnTop(boughtCard);
        }
        return false;
    }



    /*public HashMap<ResourceType, ResourceContainer> arraylistToMap (ArrayList<ResourceContainer> tempProductionInput){
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
    }*/

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


