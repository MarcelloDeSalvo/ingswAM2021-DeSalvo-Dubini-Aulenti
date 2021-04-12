package Model.Player.Deposit;

import Model.Exceptions.DepositSlotMaxDimExceeded;
import Model.Exceptions.DifferentResourceType;
import Model.Exceptions.NotEnoughResources;
import Model.Exceptions.ResourceTypeAlreadyStored;
import Model.Resources.ResourceType;


import java.util.ArrayList;
import java.util.Iterator;


public class Deposit {

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

    public Deposit(int pyramidHeight) {
        this.depositList = new ArrayList<>();
        this.defaultDepositNumber = pyramidHeight;
        this.pyramidMaxCells = 1;

        for(int i=0; i<pyramidHeight; i++){
            depositList.add(new DefaultDeposit(pyramidMaxCells));
            pyramidMaxCells++;
        }
    }

    /**
     * adds a LeaderDeposit to the depositList
     * @param lds LeaderDeposit to add
     * @return false if there is an argument exception (NoSuchElementException)
     */
    public boolean addDepositSlot(LeaderDeposit lds) {
        return lds != null && depositList.add(lds);
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

    /**
     * Check deposit returns the quantity of a chosen resourceType present in the deposit
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

    //getter and setter
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
}
