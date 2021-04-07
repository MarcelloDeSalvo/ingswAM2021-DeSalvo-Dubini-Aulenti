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

import java.sql.Struct;
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
     * Returns the  current quantity of the requested ArrayList ( sum of deposit and vault)
     * @return true if the user has enough resources
     */
    public boolean hasEnoughResources(ArrayList<ResourceContainer> requested){
        HashMap<ResourceType, ResourceContainer> resourceMap = arraylistToMap(requested);
        for (ResourceType key : resourceMap.keySet()) {
            if( resourceMap.get(key).getQty() > checkResources(key) )
                return false;
        }

        return true;
    }

    /**
     * Returns the  current quantity of the requested Map<ResourceType, ResourceContainer>( sum of deposit and vault)
     * @return true if the user has enough resources
     */
    public boolean hasEnoughResources(HashMap<ResourceType, ResourceContainer> requested){

        for (ResourceType key : requested.keySet()) {
            if( requested.get(key).getQty() > checkResources(key) )
                return false;
        }

        return true;
    }

    /**
     * Returns the  current quantity of the requested ResourceType
     * @return the sum of the ResourceType's quantity inside the vault and all the deposits
     */
    public int checkResources(ResourceType requested){
        return(deposit.checkDeposit(requested) + vault.getResourceQuantity(requested));
    }

    /**
     * Tells the controller if the user has selected the right quantity of resources in order to produce the activated production cards
     * @param selectedResources contains all the selected resources by the user (i.e. 3 Stones from Vault, 1 Gold from Deposit 1)
     * @return true if the selected resources are equals to the input resources needed to produce
     * @throws NotEnoughResources if there are missing resources
     * @throws DepositSlotMaxDimExceeded if the user selected too many resources
     */
    public boolean canProduce(ArrayList<ResourceContainer> selectedResources) throws NotEnoughResources, DepositSlotMaxDimExceeded {
        return productionSite.canProduce(selectedResources);
    }

    /**
     * Execute the production of the selected cards
     * Adds to the current player's vault the output resources
     * @return true if the production is completed without problems
     */
    public boolean produce(){
        return deposit.removeAllBuffers() && vault.removeFromVault() && productionSite.produce(vault);
    }

    /**
     * Tells the controller if the user has selected the right quantity of resources in order to buy one development card
     * @param cardPrice contains the card's price (it's a list of resourceContainer)
     * @return true if the selected resources are equals to the card's price
     */
    public boolean canBuy(ArrayList<ResourceContainer> cardPrice) {
        HashMap<ResourceType, ResourceContainer> bufferMap = new HashMap<>();
        HashMap<ResourceType, ResourceContainer> cardPriceMap;

        addDepositBuffer(bufferMap);
        addVaultBuffer(bufferMap);

        cardPriceMap = arraylistToMap(cardPrice);

        return cardPriceMap.equals(bufferMap);
    }

    /**
     * Called when canBuy() returns true
     * subtracts all buffers from all the deposits and the vault in order to buy one development card
     * @return true if the subtraction is successful
     */
    public boolean buy(){
        return deposit.removeAllBuffers() && vault.removeFromVault();
    }

    /**
     * The method adds the buffers from each DepositSlot in Deposit
     * if the element is already present in the HashMap it simply adds the qty, otherwise it creates a new element in the HashMap
     * @param bufferMap
     */
    private void addDepositBuffer (HashMap<ResourceType, ResourceContainer> bufferMap) {
        for (DepositSlot ds: deposit.getDepositList()) {
            ResourceType key = ds.getDepositResourceType();
            if(key != null) {
                if(!bufferMap.containsKey(key))
                    bufferMap.put(key, new ResourceContainer(key, ds.getBufferContainer().getQty()));
                else
                    bufferMap.get(key).addQty(ds.getBufferContainer().getQty());
            }

        }
    }

    /**
     * The method adds the buffers from Vault
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

    /**
     * Converts a list to a Map
     * @param tempProductionInput
     * @return true if the conversion ends successfully
     */
    public HashMap<ResourceType, ResourceContainer> arraylistToMap (ArrayList<ResourceContainer> tempProductionInput){
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

    /*/**
     * Converts a list to a map
     * @param list

    public Map<ResourceType, ResourceContainer> arraylistToMap (ArrayList<ResourceContainer> list) {
        Map<ResourceType, ResourceContainer> map = list.stream()
                .collect(Collectors.toMap(ResourceContainer::getResourceType, resourceContainer -> resourceContainer));
        return map;
    }*/

    /**
     * Checks if a specific ResourceType is present in the HashMap
     * @param type is the key that will be used to check in the HashMap
     * @return true if present, false otherwise
     */
    private boolean isPresent(ResourceType type, HashMap<ResourceType, ResourceContainer> map){
        return map.containsKey(type);
    }


    /**
     * Inserts the just bought card into the selected production slot
     * @param productionSlot is selected by the user
     * @param boughtCard is the card bought by the user
     * @return true if the isnert is successful
     */
    public boolean insertBoughtCard(ProductionSlot productionSlot, DevelopmentCard boughtCard){
        int index =0;
        if(productionSlot != null && productionSite.getProductionSlots().contains(productionSlot)) {
            index = productionSite.getProductionSlots().indexOf(productionSlot);
            return productionSite.getProductionSlots().get(index).insertOnTop(boughtCard);
        }
        return false;
    }

    public ProductionSlot getProductionSlotByID(int n){
        return productionSite.getProductionSlotByID(n);
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


