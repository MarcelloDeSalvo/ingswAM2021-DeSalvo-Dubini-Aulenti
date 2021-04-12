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
import Model.Util;

import java.util.ArrayList;
import java.util.HashMap;

public class PlayerBoard {

    private final Deposit deposit;
    private final Vault vault;
    private final ProductionSite productionSite;
    private final ConversionSite conversionSite;
    private final DiscountSite discountSite;

    public PlayerBoard(int pyramidNum, int prodSlotNum) {
        this.deposit = new Deposit(pyramidNum);
        this.vault = new Vault();
        this.productionSite = new ProductionSite(prodSlotNum);
        this.conversionSite = new ConversionSite();
        this.discountSite = new DiscountSite();
    }


    /**
     * Returns the  current quantity of the requested ArrayList of resources (sum of deposit and vault)
     * @return true if the user has enough resources
     */
    public boolean hasEnoughResources(ArrayList<ResourceContainer> requested){
        HashMap<ResourceType, ResourceContainer> resourceMap = Util.arraylistToMap(requested);

        return hasEnoughResources(resourceMap);
    }

    /**
     * Returns the  current quantity of the requested Map<ResourceType, ResourceContainer> (sum of deposit and vault)
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
     * checks if the user has enough resources altogether before the user starts to selected them (vault + deposit)
     * in order to activate the production
     * @return true if he has enough total resources
     */
    public  boolean hasEnoughResourcesForProduction(){
        return productionSite.hasEnoughInputResources(this);
    }


    /**
     * Returns the  current quantity of the requested ResourceType
     * @return the sum of the ResourceType's quantity inside the vault and all the deposits
     */
    public int checkResources(ResourceType requested){
        return(deposit.checkDeposit(requested) + vault.getResourceQuantity(requested));
    }


    /**
     * Puts all the resources needed to activate the selected production cards and all the resources
     * produced by those cards into a buffer
     * @param selectedProductionCard is the list of the cards selected by the user
     */
    public boolean activateProduction(ArrayList<ProductionSlot> selectedProductionCard){
        return productionSite.activateProduction(selectedProductionCard);
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

        cardPriceMap = Util.arraylistToMap(cardPrice);

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
     * Inserts the just bought card into the selected production slot
     * @param productionSlot is selected by the user
     * @param boughtCard is the card bought by the user
     * @return true if the insert is successful
     */
    public boolean insertBoughtCard(ProductionSlot productionSlot, DevelopmentCard boughtCard){
        int index;
        if(productionSlot != null && productionSite.getProductionSlots().contains(productionSlot)) {
            index = productionSite.getProductionSlots().indexOf(productionSlot);
            return productionSite.getProductionSlots().get(index).insertOnTop(boughtCard);
        }
        return false;
    }


    public ProductionSlot getProductionSlotByID(int n) throws IndexOutOfBoundsException{
        return productionSite.getProductionSlotByID(n);

    }

    public DepositSlot getDepositSlotByID(int n){
        if (n>=0 && n< deposit.getDepositList().size())
            return deposit.getDepositList().get(n);
        return null;
    }

    /**
     *Adds a resourceContainer to the defaultDeposit with dimension dim
     */
    public boolean addResourceToDefaultDepositWithDim(ResourceContainer myContainer,Deposit deposit, int dim){
       return( deposit.getDefaultSlot_WithDim(dim).addToDepositSlot(myContainer));

    }

    /**
     *Adds a resourceContainer to the depositLeader number x. We take the depositLeaderSpace from the arrayList of all depositSlots and skip past the non-leader ones.
     */
    public boolean addResourceToLeaderDepositNumberX(ResourceContainer myContainer,Deposit deposit, int x){
        if(x>0)
            return(deposit.getDepositList().get(deposit.getDefaultDepositNumber()+x).addToDepositSlot(myContainer));
        return false;
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

    public ConversionSite getConversionSite() {
        return conversionSite;
    }

    public DiscountSite getDiscountSite() {
        return discountSite;
    }
}


