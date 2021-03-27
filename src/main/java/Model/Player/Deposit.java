package Model.Player;

import java.util.ArrayList;

public class Deposit {
    private ArrayList<DepositSlot> storage;
    /**
     *
     */
    private int defaultDepositNumber;
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

    //public Boolean addDepositSlot();
    //public Boolean removeDepositSlot();
    //public Boolean manageDeposit();

}
