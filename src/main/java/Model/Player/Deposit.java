package Model.Player;

import Model.Exceptions.DepositSlotMaxDimExceeded;
import Model.Exceptions.DifferentResourceType;
import Model.Exceptions.NotEnoughResources;


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
    public Boolean addDepositSlot(LeaderDepositSlot lds) {
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
    public Boolean removeDepositSlot(DepositSlot depositSlot){
        if(depositSlot != null && depositList.remove(depositSlot)){
            return true;
        } else{
            return false;
        }
    }


    /**
     * Switch a selected number of resources from one deposit to another (target)
     * @param selected is the one selected by the user
     * @param selectedQta is the resource quantity that the user wants to move
     * @param target is the deposit where the user wants the resources to be placed
     * @return true
     */
    public Boolean switchDeposit(DepositSlot selected, int selectedQta, DepositSlot target){
        target.getDepositContainer().addQty(selectedQta);
        target.setDepositResourceType(selected.getDepositResourceType());
        selected.getDepositContainer().addQty(-selectedQta);
        return true;
    }


    /**
     * Chacks if the controller can call switchDeposit() in order to switch some number of resources from one deposit(selected) to another(target)
     * @param selected is the one selected by the user
     * @param selectedQta is the resource quantity that the user wants to move
     * @param target is the deposit where the user wants the resources to be placed
     * @return true if the user chose a legit quantity from the selected deposit that can fits in the target deposit
     * @throws DepositSlotMaxDimExceeded
     * @throws DifferentResourceType
     * @throws NotEnoughResources
     */
    public Boolean canSwitchDeposit(DepositSlot selected, int selectedQta, DepositSlot target) throws DepositSlotMaxDimExceeded, DifferentResourceType, NotEnoughResources {
        if(selected.getDepositContainer().getQty()<selectedQta) {
            throw new NotEnoughResources("Not enough resources");
        }

        if(!target.isEmpty()){
            if ((target.hasSameTypeAs(selected))){
                if( selectedQta + target.getResourceQty()<= target.getMaxDim() ) {
                    return true;
                }else {
                    throw new DepositSlotMaxDimExceeded("Maximum dimension exceeded");
                }
            }else{
                throw new DifferentResourceType("Not the same type");
            }
        }else{
            if( selectedQta <= target.getMaxDim() )
                return true;
            else
                throw new DepositSlotMaxDimExceeded("Maximum dimension exceeded");

        }
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
