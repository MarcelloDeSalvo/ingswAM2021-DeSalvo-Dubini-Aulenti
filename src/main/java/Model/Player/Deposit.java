package Model.Player;

import Model.Exceptions.DepositSlotMaxDimExceeded;
import Model.Exceptions.DifferentResourceType;
import Model.Exceptions.NotEnoughResources;
import Model.Resources.ResourceContainer;
import Model.Resources.ResourceType;

import java.util.ArrayList;
import java.util.NoSuchElementException;

public class Deposit {
    private ArrayList<DepositSlot> storage;

    /**
     * Player's deposit number.
     * By default it's 3
     */
    private int defaultDepositNumber;

    /**
     * It is used to instantiate the DefaultDepositSlots with a piramid quantity
     */
    private int pyramidMaxCells;

    public Deposit(int num) {
        this.storage = new ArrayList<DepositSlot>();
        this.defaultDepositNumber = num;
        this.pyramidMaxCells = 1;

        for(int i=0; i<num; i++){
            storage.add(new DefaultDepositSlot(pyramidMaxCells));
            pyramidMaxCells++;
        }
    }

    /**
     * adds a LeaderDepositSlot to the storage
     * @param lds
     * @return false if there is an argument exception (NoSuchElementException)
     * @throws NullPointerException if the input object does not exist
     */
    public Boolean addDepositSlot(LeaderDepositSlot lds) throws NullPointerException{
        if(storage.add(lds)){
            return true;
        }else{
            throw new NullPointerException("The object does not exist");
        }

    }

    /**
     * removes a DepostiSlot from the storage
     * @param depositSlot
     * @return false if there is an argument exception (NoSuchElementException)
     * @throws NullPointerException if the input object does not exist
     */
    public Boolean removeDepositSlot(DepositSlot depositSlot) throws NullPointerException{
        if(storage.remove(depositSlot)){
            return true;
        } else {
            throw new NullPointerException("The object does not exist");
        }

    }


    public Boolean switchDeposit(DepositSlot selected, int selectedQta, DepositSlot target){
        target.getStorageArea().addQta(selectedQta);
        target.setDepositResourceType(selected.getDepositResourceType());
        selected.getStorageArea().addQta(-selectedQta);
        return true;
    }

    public Boolean canSwitchDeposit(DepositSlot selected, int selectedQta, DepositSlot target) throws DepositSlotMaxDimExceeded, DifferentResourceType, NotEnoughResources {
        if(selected.getStorageArea().getQta()<selectedQta) {
            throw new NotEnoughResources("Not enough resources");
        }else if(!target.isNullAndEmpty()){
            if ((target.getDepositResourceType().equals(selected.getDepositResourceType()))){
                if( selectedQta + target.getStorageArea().getQta() <= target.getMaxDim() ) {
                    return true;
                }else {
                    throw new DepositSlotMaxDimExceeded("Maximum dimension exceeded");
                }
            }else{
                throw new DifferentResourceType("Not the same type");
            }
        }else{
            if( selectedQta <= target.getMaxDim() ) {
                return true;
            }else {
                throw new DepositSlotMaxDimExceeded("Maximum dimension exceeded");
            }
        }
    }


    //getter and setter
    public ArrayList<DepositSlot> getStorage() {
        return storage;
    }

    public void setStorage(ArrayList<DepositSlot> storage) {
        this.storage = storage;
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
