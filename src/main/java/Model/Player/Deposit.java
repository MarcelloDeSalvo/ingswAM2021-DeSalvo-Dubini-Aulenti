package Model.Player;

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
    private int piramidMaxCells;

    public Deposit(int num) {
        this.storage = new ArrayList<DepositSlot>();
        this.defaultDepositNumber = num;
        this.piramidMaxCells = num;

        for(int i=0; i<num; i++){
            storage.add(new DefaultDepositSlot(piramidMaxCells,null));
            piramidMaxCells--;
        }
    }

    /**
     * adds a LeaderDepositSlot to the storage
     * @param lds
     * @return false if there is an argument exception (NoSuchElementException)
     */
    public Boolean addDepositSlot(LeaderDepositSlot lds){
        try{
            storage.add(lds);
        }catch (NoSuchElementException e){
            System.out.println("The objects does not exist");
            return false;
        }
        return true;

        /*altro modo
        if(storage.add(lds)){
            return true;
        }else{
            return false;
        }
         */
    }

    /**
     * removes a DepostiSlot from the storage
     * @param depositSlot
     * @return false if there is an argument exception (NoSuchElementException)
     */
    public Boolean removeDepositSlot(DepositSlot depositSlot) {
        try {
            storage.remove(depositSlot);

        } catch (NoSuchElementException e) {
            System.out.println("The objects does not exist");
            return false;
        }
        return true;
    }


    //public Boolean manageDeposit();


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
}
