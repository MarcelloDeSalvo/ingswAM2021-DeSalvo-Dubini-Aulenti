package it.polimi.ingsw.model.player.deposit;

import it.polimi.ingsw.model.exceptions.DepositSlotMaxDimExceeded;
import it.polimi.ingsw.model.exceptions.DifferentResourceType;
import it.polimi.ingsw.model.exceptions.NotEnoughResources;
import it.polimi.ingsw.model.exceptions.ResourceTypeAlreadyStored;
import it.polimi.ingsw.model.resources.ResourceContainer;
import it.polimi.ingsw.model.resources.ResourceType;
import it.polimi.ingsw.observers.gameListeners.DepositListener;
import it.polimi.ingsw.observers.gameListeners.DepositSubject;
import it.polimi.ingsw.view.VirtualView;
import it.polimi.ingsw.view.cli.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;


public class Deposit implements DepositSubject {

    /**
     * It's the list of the user's deposits
     */
    private final ArrayList<DepositSlot> depositList;

    /**
     * Player's DefaultDeposit number which is 3 by default.
     */
    private int defaultDepositNumber;

    /**
     * It is used to instantiate the DefaultDepositSlots with an increasing max-quantity
     */
    private int pyramidMaxCells;

    private DepositListener depositListener;

    public Deposit(int pyramidHeight, boolean test) {
        this.depositList = new ArrayList<>();
        this.defaultDepositNumber = pyramidHeight;
        this.pyramidMaxCells = 1;
        HashSet<ResourceType> notAvailableResourceType = new HashSet<>();
        VirtualView virtualView = new VirtualView();
        for(int i=0; i<pyramidHeight; i++){
            DefaultDeposit defaultDeposit = new DefaultDeposit(pyramidMaxCells, notAvailableResourceType, i+1);
            depositList.add(defaultDeposit);
            pyramidMaxCells++;
        }
        addListeners(virtualView);
    }

    public Deposit(int pyramidHeight) {
        this.depositList = new ArrayList<>();
        this.defaultDepositNumber = pyramidHeight;
        this.pyramidMaxCells = 1;
        HashSet<ResourceType> notAvailableResourceType = new HashSet<>();

        for(int i=0; i<pyramidHeight; i++){
            DefaultDeposit defaultDeposit = new DefaultDeposit(pyramidMaxCells, notAvailableResourceType, i+1);
            depositList.add(defaultDeposit);
            pyramidMaxCells++;
        }
    }


    //DEPOSIT MANAGEMENT------------------------------------------------------------------------------------------------
    /**
     * Integrates switchToDeposit() and transferToDeposit() for an automated selection between the two methods
     * @param selected is the one selected by the user
     * @param selectedQty is the resource quantity that the user wants to move
     * @param destination is the deposit where the user wants the resources to be placed
     * @return true if transfer or switch deposit ends without exceptions
     * @throws DepositSlotMaxDimExceeded if in the destination deposit there's not enough space to insert the transferred resources
     * @throws DifferentResourceType if the selected slot has some ResourceType restrictions
     * @throws NotEnoughResources if the user wants to move a quantity that's greater than the selected deposit's max dimension
     * @throws ResourceTypeAlreadyStored if another default deposit is already storing the same resource type
     */
    public boolean moveTo(DepositSlot selected, int selectedQty, DepositSlot destination) throws DepositSlotMaxDimExceeded, DifferentResourceType, NotEnoughResources, ResourceTypeAlreadyStored{

        if(!selected.isTheSameType(destination))
            if(selected.getResourceQty()==selectedQty)
                if (canSwitchDeposit(selected, destination))
                    return switchToDeposit(selected, destination);

        if(selected.canTransferTo(destination, selectedQty))
            return selected.transferTo(destination,selectedQty);

        return false;
    }

    /**
     * Checks if the controller can transfer some number of resources from one deposit(selected) to another(destination)
     * @param selected is the one selected by the user
     * @param selectedQta is the resource quantity that the user wants to move
     * @param destination is the deposit where the user wants the resources to be placed
     * @return true if the user chose a legit quantity from the selected deposit that can fits in the target deposit
     * @throws DepositSlotMaxDimExceeded if in the destination deposit there's not enough space to insert the transferred resources
     * @throws DifferentResourceType if the selected slot has some ResourceType restrictions
     * @throws NotEnoughResources if the user wants to move a quantity that's greater than the selected deposit's max dimension
     */
    public boolean canTransferDeposit(DepositSlot selected, int selectedQta, DepositSlot destination) throws DepositSlotMaxDimExceeded, DifferentResourceType, NotEnoughResources, ResourceTypeAlreadyStored{
        return selected.canTransferTo(destination, selectedQta);
    }

    /**
     * Transfer a selected number of resources from one deposit to another (destination)
     * @param selected is the one selected by the user
     * @param selectedQta is the resource quantity that the user wants to move
     * @param destination is the deposit where the user wants the resources to be placed
     * @return true
     */
    public boolean transferToDeposit(DepositSlot selected, int selectedQta, DepositSlot destination){
        selected.transferTo(destination, selectedQta);
        return true;

    }

    /**
     * Checks if the controller can call switchDeposit() in order to switch some number of resources of different kinds from one deposit(selected) to another(destination)
     * @param selected is the one selected by the user
     * @param destination is the deposit that will switch resources with the selected one
     * @return true if the Deposit's type can switch his resources with another generic deposit
     * @throws DepositSlotMaxDimExceeded if in the destination deposit there's not enough space to insert the transferred resources
     * @throws ResourceTypeAlreadyStored if another default deposit is already storing the same resource type
     */
    public boolean canSwitchDeposit(DepositSlot selected, DepositSlot destination) throws DepositSlotMaxDimExceeded, ResourceTypeAlreadyStored {
        return selected.canSwitchWith(destination) && destination.canSwitchWith(selected);
    }

    /**
     * Switch resources from one deposit to another (destination)
     * @param selected is the one selected by the user
     * @param destination is the deposit that will switch resources with the selected one
     * @return true
     */
    public boolean switchToDeposit(DepositSlot selected, DepositSlot destination){
        selected.switchTo(destination);
        return true;
    }

    /**
     * Returns the number of all the resources present in the deposit
     */
    public int totalQuantityOfResourcesInDeposit(){
        int c=0;
        for (DepositSlot slot:depositList) {
            c=c+slot.getResourceQty();
        }
        return c;
    }
    //------------------------------------------------------------------------------------------------------------------


    //BUFFERS MANAGEMENT------------------------------------------------------------------------------------------------
    /**
     * Called when a transaction (buy or produce) can be completed without problems
     * Ends a transaction
     */
    public boolean removeAllBuffers(){
        for (DepositSlot ds: depositList) {
            ds.removeTheBuffer();
        }

        return true;
    }

    /**
     * Clears all the buffers
     */
    public boolean clearBuffer(){
        for (DepositSlot ds: depositList) {
            ds.clearCurrentBuffer();
        }

        return true;
    }
    //------------------------------------------------------------------------------------------------------------------


    //OTHER METHODS-----------------------------------------------------------------------------------------------------
    /**
     * Returns the quantity of a chosen resourceType present in the deposit
     * @return num
     */
    public int checkDeposit (ResourceType requested){
        int num = 0;
        Iterator<DepositSlot> iter = depositList.iterator();
        DepositSlot current;
        while(iter.hasNext()){
            current = iter.next();
            if(current.getDepositResourceType() == requested)
                num = num + current.getResourceQty();
        }
        return num;
    }

    /**
     * Adds a LeaderDeposit to the depositList
     * @param lds LeaderDeposit to add
     * @return false if there is an argument exception (NoSuchElementException)
     */
    public boolean addDepositSlot(LeaderDeposit lds) {
        if (lds != null && depositList.add(lds)){
            lds.addListeners(depositListener);
            depositListener.notifyNewDepositSlot(lds.getMaxDim(), lds.getDepositResourceType(), "");
            return true;
        }

        return false;
    }

    /**
     * warning: this feature should not be used according to the rules of the base game
     * removes a DepositSlot from the depositList
     * @param depositSlot DepositSlot to remove
     * @return false if there is an argument exception (NoSuchElementException)
     */
    public boolean removeDepositSlot(DepositSlot depositSlot){
        return depositSlot != null && depositList.remove(depositSlot);
    }

    public boolean mustDiscardResource(ResourceContainer marketOutCont){
        boolean mustDiscard = true;

        for (DepositSlot depositSlot: depositList) {
            if (depositSlot.simpleCanAddToDepositSlot(marketOutCont))
                mustDiscard=false;
        }

        return mustDiscard;
    }
    //------------------------------------------------------------------------------------------------------------------


    //GETTER AND SETTER-------------------------------------------------------------------------------------------------
    /**
     * get method to retrieve the DefaultDeposit's slots indexes
     * @param i is the index related to the default slot with max_dim = i
     * @return the desired index of the default slot or the first slot with max_dim = 1 if it receives '0' as input
     */
    public DepositSlot getDefaultSlot_WithDim(int i){
        if (i>0)
            return (this.getDepositList().get(i-1));
        else
            return (this.getDepositList().get(0));
    }

    public ArrayList<DepositSlot> getDepositList() {
        return depositList;
    }

    public int getDefaultDepositNumber() {
        return defaultDepositNumber;
    }

    public void setDefaultDepositNumber(int defaultDepositNumber) {
        this.defaultDepositNumber = defaultDepositNumber;
    }

    public int getPyramidMaxCells() {
        return pyramidMaxCells;
    }
    //------------------------------------------------------------------------------------------------------------------


    //TO STRING---------------------------------------------------------------------------------------------------------
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        int i = 1;
        for (DepositSlot depositSlot : depositList) {
            stringBuilder.append("----------------------------------\n")
            .append(Color.ANSI_CYAN.escape()).append("ID - ").append(i).append(Color.ANSI_RESET.escape()).append("\n").append(depositSlot.toString());
            i++;
        }

        return Color.ANSI_BLUE.escape() + "DEPOSIT: " + Color.ANSI_RESET.escape() +
                "\n" +
                stringBuilder.append("----------------------------------\n").toString();
    }
    //------------------------------------------------------------------------------------------------------------------


    @Override
    public void addListeners(DepositListener depositListener) {
        this.depositListener = depositListener;
        for (DepositSlot depositSlot: depositList) {
            depositSlot.addListeners(depositListener);
        }
    }
}
