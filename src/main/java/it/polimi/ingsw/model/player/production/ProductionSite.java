package it.polimi.ingsw.model.player.production;

import it.polimi.ingsw.model.FaithPath;
import it.polimi.ingsw.model.Util;
import it.polimi.ingsw.model.cards.Colour;
import it.polimi.ingsw.model.exceptions.DepositSlotMaxDimExceeded;
import it.polimi.ingsw.model.exceptions.NotEnoughResources;
import it.polimi.ingsw.model.resources.ResourceContainer;
import it.polimi.ingsw.model.resources.ResourceType;
import it.polimi.ingsw.model.player.PlayerBoard;
import it.polimi.ingsw.model.player.Vault;

import java.util.ArrayList;
import java.util.HashMap;

public class ProductionSite {
    private final ArrayList<ProductionSlot> productionSlots;
    private final HashMap<ResourceType, ResourceContainer> bufferSelectedResources;
    private final HashMap<ResourceType, ResourceContainer> bufferInputMap;
    private final HashMap<ResourceType, ResourceContainer> bufferOutputMap;

    private FaithPath faithPath;

    int defaultNum;

    /**
     * Main Constructor
     * Builds a new Production site with a maximum number of DevelopmentCards defaultNum
     * @param defaultNum is the default number of slots (3 by the standard rules)
     */
    public ProductionSite(int defaultNum, FaithPath faithPath) {
        this.defaultNum = defaultNum;
        this.faithPath = faithPath;
        this.productionSlots = new ArrayList<>();

        productionSlots.add(new BaseProduction());

        for(int i=0; i<defaultNum; i++){
            productionSlots.add(new DevelopmentCardProduction());
        }

        this.bufferInputMap = new HashMap<>();
        this.bufferOutputMap = new HashMap<>();
        this.bufferSelectedResources = new HashMap<>();
    }

    /**
     * Used for testing
     */
    public ProductionSite(int defaultNum) {
        this.defaultNum = defaultNum;
        this.faithPath = faithPath;
        this.productionSlots = new ArrayList<>();

        productionSlots.add(new BaseProduction());

        for(int i=0; i<defaultNum; i++){
            productionSlots.add(new DevelopmentCardProduction());
        }

        this.bufferInputMap = new HashMap<>();
        this.bufferOutputMap = new HashMap<>();
        this.bufferSelectedResources = new HashMap<>();
    }




    //PRODUCTION PIPELINE-----------------------------------------------------------------------------------------------

    /*  User selects the production slots (Base/DevelopmentCard/LeaderCard)
         L He fills all the question marks with the desired resourceType
             L fillProductionBuffers()
                 L hasEnoughInputResources():  checks his total resources
                      L User selects the resources from his vault or his deposits
                            L canProduce():         validates the selection
                                  L produce()
                                       L  clearBuffers()
    */

    /**
     * Inserts the production inputs and outputs of the selected slots into the relative buffer maps
     * @param productionSlots are the selected slots
     * @return true if the add() finish without errors
     */
    public boolean fillProductionBuffers(ArrayList<ProductionSlot> productionSlots){
        for (ProductionSlot ps: productionSlots) {
            if(!Util.arraylistToMap(ps.getProductionInput(),bufferInputMap))
                return false;

            if(!Util.arraylistToMap(ps.getProductionOutput(),bufferOutputMap))
                return false;
        }
        return true;
    }

    /**
     * Inserts the production inputs and outputs of the selected slot into the relative buffer maps <br>
     * Called if the controller checks one slot at a time after the user fills all the question marks
     * @param ps is the selected slot
     * @return true if the add() finish without errors
     */
    public boolean fillProductionBuffers(ProductionSlot ps){
            if(!Util.arraylistToMap(ps.getProductionInput(),bufferInputMap))
                return false;

        return Util.arraylistToMap(ps.getProductionOutput(), bufferOutputMap);
    }


    /**
     * Checks if the user has enough resources altogether before the user starts to selected them (vault + deposit) in order to activate the production
     * @return true if he has enough total resources
     */
    public  boolean hasEnoughInputResources(PlayerBoard playerBoard){
        if (bufferInputMap.size()==0)
            return false;

        for (ResourceType key : bufferInputMap.keySet())
        {
            if( playerBoard.checkResources(key) < bufferInputMap.get(key).getQty() )
                return false;
        }
        return true;
    }

    /**
     * Checks if the user can produce by checking if the selected resources match the inputs of the activated production cards
     * @param selectedResources are the resources selected by the user
     * @return true if the selected resources are equals to the input resources needed to produce
     * @throws NotEnoughResources if there are missing resources
     * @throws DepositSlotMaxDimExceeded if the user selected too many resources
     */
    public boolean canProduce(ArrayList<ResourceContainer> selectedResources) throws NotEnoughResources, DepositSlotMaxDimExceeded{
        Util.arraylistToMap(selectedResources, bufferSelectedResources);
        for (ResourceType key: bufferInputMap.keySet()) {

            if(!bufferSelectedResources.containsKey(key))
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

            bufferOutputMap.get(key).addToVault(vault);
            bufferOutputMap.get(key).addToFaithPath(faithPath);

        }
        return true;
    }


    /**
     * Clears the current buffers
     */
    public boolean clearBuffers(){
        bufferInputMap.clear();
        bufferOutputMap.clear();
        return true;
    }
    //END OF THE PIPELINE ----------------------------------------------------------------------------------------------


    //OTHER METHODS-----------------------------------------------------------------------------------------------------
    /**
     * Counts the active Development Cards with specific attributes
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
     * Adds a production slot to the list
     * @param productionSlot is the input production slot
     * @return true if it can be added
     */
    public boolean addProductionSlot(ProductionSlot productionSlot){
        return productionSlot != null && productionSlots.add(productionSlot);
    }
    //------------------------------------------------------------------------------------------------------------------


    //GETTER AND SETTER-------------------------------------------------------------------------------------------------
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


    public int getVictoryPoints(){
        int tot = 0;
        for (ProductionSlot p: productionSlots) {
            tot+=p.getVictoryPoints();
        }
        return tot;
    }


    public HashMap<ResourceType, ResourceContainer> getBufferInputMap() {
        return bufferInputMap;
    }

    public HashMap<ResourceType, ResourceContainer> getBufferOutputMap() {
        return bufferOutputMap;
    }

    public HashMap<ResourceType, ResourceContainer> getBufferSelectedResources() {
        return bufferSelectedResources;
    }

    public int getDefaultNum() {
        return defaultNum;
    }

    public void setDefaultNum(int defaultNum) {
        this.defaultNum = defaultNum;
    }

    //------------------------------------------------------------------------------------------------------------------

}


