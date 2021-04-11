package Model.Player.Production;

import Model.Cards.Colour;
import Model.Exceptions.DepositSlotMaxDimExceeded;
import Model.Exceptions.NotEnoughResources;
import Model.Player.PlayerBoard;
import Model.Player.Vault;
import Model.Resources.ResourceContainer;
import Model.Resources.ResourceType;
import Model.Util;

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
     * @param defaultNum is the default number of slots (3 by the standard rules)
     */
    public ProductionSite(int defaultNum) {
        this.defaultNum = defaultNum;
        this.productionSlots = new ArrayList<>();

        productionSlots.add(new BaseProductionSlot());

        for(int i=0; i<defaultNum; i++){
            productionSlots.add(new DevelopmentCardProduction());
        }

        this.bufferInputMap = new HashMap<>();
        this.bufferOutputMap = new HashMap<>();
        this.bufferSelectedResources = new HashMap<>();
    }

    /**
     * inserts the production inputs and outputs of the selected cards into the relative buffer maps
     * @param productionSlots are the selected slot
     * @return true if the add() finish without errors
     */
    public boolean activateProduction(ArrayList<ProductionSlot> productionSlots){
        for (ProductionSlot ps: productionSlots) {
            if(!Util.arraylistToMap(ps.getProductionInput(),bufferInputMap))
                return false;

            if(!Util.arraylistToMap(ps.getProductionOutput(),bufferOutputMap))
                return false;
        }
        return true;
    }


    /**
     * counts the active Development Cards with specific attributes
     * @param numberRequired is the total amount of cards that must have a specific level and colour
     * @param level is the level required
     * @param colour is the color required
     * @return true if the card's count is greater or equal compared to the numberRequired
     */
    public boolean hasEnoughDevelopmentCardsWith(int numberRequired, int level, Colour colour){
        int cardNumber = 0;
        for(ProductionSlot ps : productionSlots){
            cardNumber += ps.countCardsWith(level, colour);
        }

        return cardNumber >= numberRequired;
    }

    /**
     * adds a production slot to the list
     * @param productionSlot is the input production slot
     * @return true if it can be added
     */
    public boolean addProductionSlot(ProductionSlot productionSlot){
        return productionSlot != null && productionSlots.add(productionSlot);
    }


    /**
     * checks if the user has enough input resources in order to activate the production
     * @param bufferInputMap
     * @param playerBoard
     * @return
     */
    public  boolean hasEnoughInputResources(HashMap<ResourceType,ResourceContainer> bufferInputMap, PlayerBoard playerBoard){
        for (ResourceType key : bufferInputMap.keySet())
        {
            if(playerBoard.checkResources(key)<bufferInputMap.get(key).getQty())
                return false;
        }
        return true;
    }

    /**
     * clears the current buffers
     * @return
     */
    public boolean clearBuffers(){
        bufferInputMap.clear();
        bufferOutputMap.clear();
        return true;
    }

    /**
     * checks if the user can produce by checking if the selected resources match the inputs of the activated production cards
     * @param selectedResources are the resources selected by the user
     * @return true if the selected resources are equals to the input resources needed to produce
     * @throws NotEnoughResources if there are missing resources
     * @throws DepositSlotMaxDimExceeded if the user selected too many resources
     */
    public boolean canProduce(ArrayList<ResourceContainer> selectedResources) throws NotEnoughResources, DepositSlotMaxDimExceeded{
        Util.arraylistToMap(selectedResources, bufferSelectedResources);
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


    /**
     * Sends the production output to the current player's vault
     * @param vault is the current player's vault
     * @return true if it adds the resources
     */
    public boolean produce(Vault vault){
        for (ResourceType key : bufferOutputMap.keySet()){


            key.addToVault(bufferOutputMap.get(key), vault);
            key.addToFaithPath(bufferOutputMap.get(key));
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

    public ProductionSlot getProductionSlotByID(int id) throws IndexOutOfBoundsException{
        if(id>=0 && id<productionSlots.size())
            return productionSlots.get(id);
        return null;
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


