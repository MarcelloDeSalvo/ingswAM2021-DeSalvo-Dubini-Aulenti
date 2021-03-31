package Model.Player.Deposit;

import Model.Exceptions.DepositSlotMaxDimExceeded;
import Model.Exceptions.DifferentResourceType;
import Model.Exceptions.NotEnoughResources;
import Model.Exceptions.ResourceTypeAlreadyStored;


import java.util.ArrayList;


public class Deposit {

    /**
     * It's the list of the user's deposits
     */
    private ArrayList<DepositSlot> depositList;

    /**
     * Player's deposit number.
     * By default it's 3
     */
    private int defaultDepositNumber;

    /**
     * It is used to instantiate the DefaultDepositSlots with an increasing max-quantity
     */
    private int pyramidMaxCells;

    public Deposit(int num) {
        this.depositList = new ArrayList<DepositSlot>();
        this.defaultDepositNumber = num;
        this.pyramidMaxCells = 1;

        for(int i=0; i<num; i++){
            depositList.add(new DefaultDepositSlot(pyramidMaxCells));
            pyramidMaxCells++;
        }
    }

    /**
     * adds a LeaderDepositSlot to the depositList
     * @param lds
     * @return false if there is an argument exception (NoSuchElementException)
     */
    public boolean addDepositSlot(LeaderDepositSlot lds) {
        if(lds != null && depositList.add(lds)){
            return true;
        }else {
            return false;
        }

    }

    /**
     * removes a DepostiSlot from the depositList
     * @param depositSlot
     * @return false if there is an argument exception (NoSuchElementException)
     */
    public boolean removeDepositSlot(DepositSlot depositSlot){
        if(depositSlot != null && depositList.remove(depositSlot)){
            return true;
        } else{
            return false;
        }
    }


    /**
     * Chacks if the controller can transfer some number of resources from one deposit(selected) to another(destination)
     * @param selected is the one selected by the user
     * @param selectedQta is the resource quantity that the user wants to move
     * @param destination is the deposit where the user wants the resources to be placed
     * @return true if the user chose a legit quantity from the selected deposit that can fits in the target deposit
     * @throws DepositSlotMaxDimExceeded
     * @throws DifferentResourceType
     * @throws NotEnoughResources
     */
    public boolean canTransferDeposit(DepositSlot selected, int selectedQta, DepositSlot destination) throws DepositSlotMaxDimExceeded, DifferentResourceType, NotEnoughResources, ResourceTypeAlreadyStored{
        if (selected.canTransferTo(destination, selectedQta))
            return true;
        return false;
    }

    /**
     * Chacks if the controller can call switchDeposit() in order to switch some number of resources of different kinds from one deposit(selected) to another(destination)
     * @param selected is the one selected by the user
     * @param destination is the deposit that will switch resources with the selected one
     * @return true if the Deposit's type can switch his resources with another generic deposit
     * @throws DepositSlotMaxDimExceeded
     * @throws DifferentResourceType
     * @throws NotEnoughResources
     */
    public boolean canSwitchDeposit(DepositSlot selected, DepositSlot destination) throws DepositSlotMaxDimExceeded, DifferentResourceType, NotEnoughResources, ResourceTypeAlreadyStored {
        if (selected.canSwitchWith(destination) && destination.canSwitchWith(selected))
            return true;
        return false;
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
     * Transfer a selected number of resources from one deposit to another (destination)
     * @param selected is the one selected by the user
     * @param selectedQta is the resource quantity that the user wants to move
     * @param destination is the deposit where the user wants the resources to be placed
     * @return true
     */
    public boolean switchToDeposit(DepositSlot selected, int selectedQta, DepositSlot destination){
        int initialDestinationQuantity =  destination.getResourceQty();

        selected.transferTo(destination, selectedQta);
        destination.transferTo(selected, initialDestinationQuantity);
        return true;

    }


    //getter and setter
    /**
     * get method to retrieve the DefaultDeposit's slots indexes
     * @param i is the index related to the default slot with max_dim = i
     * @return the desired index of the default slots.
     * @return the first slot with max_dim = 1 if it receives '0' as input
     */
    public DepositSlot getDefaultSlot_WithDim(int i){
        if (i>0){
            return (this.getDepositList().get(i-1));
        }
        else return (this.getDepositList().get(0));
    }

    public ArrayList<DepositSlot> getDepositList() {
        return depositList;
    }

    public void setDepositList(ArrayList<DepositSlot> depositList) {
        this.depositList = depositList;
    }

    public int getDefaultDepositNumber() {
        return defaultDepositNumber;
    }

    public void setDefaultDepositNumber(int defaultDepositNumber) {
        this.defaultDepositNumber = defaultDepositNumber;
    }

    public int getPiramidMaxCells() {
        return pyramidMaxCells;
    }

    public void setPiramidMaxCells(int piramidMaxCells) {
        this.pyramidMaxCells = piramidMaxCells;
    }
}
