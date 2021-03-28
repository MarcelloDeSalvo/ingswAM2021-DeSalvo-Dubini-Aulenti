package Model.Player;

import Model.Exceptions.DepositSlotMaxDimExceeded;
import Model.Resources.ResourceContainer;
import Model.Resources.ResourceType;

import java.util.ArrayList;
import java.util.NoSuchElementException;

public class Deposit {
    private ArrayList<DepositSlot> storage;
    private ResourceContainer switchBuffer;

    /**
     * Player's deposit number.
     * By default it's 3
     */
    private int defaultDepositNumber;

    /**
     * It is used to instantiate the DefaultDepositSlots with a piramid quantity
     */
    private int piramidMaxCells;

    public Deposit(int num) {
        this.storage = new ArrayList<DepositSlot>();
        this.defaultDepositNumber = num;
        this.piramidMaxCells = num;
        this.switchBuffer = new ResourceContainer(null, 0);

        for(int i=0; i<num; i++){
            storage.add(new DefaultDepositSlot(piramidMaxCells,null));
            piramidMaxCells--;
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


    public Boolean switchDeposit(DepositSlot target){
        target.addToDepositSlot(switchBuffer);
        return true;
    }

    public Boolean addToBuffer(DepositSlot selectedDeposit, int  selectedQta) throws DepositSlotMaxDimExceeded{
        ResourceType selectedResourceType = selectedDeposit.getDepositResourceType();

        if( selectedQta < selectedDeposit.getStorageArea().getQta() ) {

            if (switchBuffer.getResourceType().equals(null)) {
                switchBuffer.setResourceType(selectedResourceType);
                return true;
            } else if (switchBuffer.getResourceType().equals(selectedResourceType)) {
                switchBuffer.addQta(selectedQta);
                return true;
            } else {
                switchBuffer.setResourceType(selectedDeposit.getDepositResourceType());
                switchBuffer.setQta(selectedQta);
                return true;
            }
        }else {
            throw new DepositSlotMaxDimExceeded("Select the right quantity");
        }

    }

    public Boolean removeFromBuffer(ResourceContainer selctedContainer) throws NullPointerException{
        switchBuffer.setResourceType(null);
        switchBuffer.setQta(0);
        return true;
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
        return piramidMaxCells;
    }

    public void setPiramidMaxCells(int piramidMaxCells) {
        this.piramidMaxCells = piramidMaxCells;
    }

    public ResourceContainer getSwitchBuffer() {
        return switchBuffer;
    }

    public void setSwitchBuffer(ResourceContainer switchBuffer) {
        this.switchBuffer = switchBuffer;
    }
}
