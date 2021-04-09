package Model.Resources;
import Model.Player.Deposit.DefaultDepositSlot;
import Model.Player.Deposit.DepositSlot;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GoldTest {
    @Test
    void testAdd1(){
        DepositSlot tempDeposit= new DefaultDepositSlot(2);
        Gold tempGold=new Gold();
        tempGold.addToDeposit(tempDeposit);
        assertTrue(tempDeposit.getDepositResourceType()==ResourceType.GOLD);
        assertTrue(tempDeposit.getDepositContainer().getQty()==1);
    }
    @Test
    void testMultipleAdd(){
        DepositSlot tempDeposit= new DefaultDepositSlot(7);
        Gold tempGold=new Gold();
        for (int i = 0; i <5; i++) {
            tempGold.addToDeposit(tempDeposit);
        }
        assertTrue(tempDeposit.getDepositResourceType()==ResourceType.GOLD);
        assertTrue(tempDeposit.getDepositContainer().getQty()==5);

    }

}