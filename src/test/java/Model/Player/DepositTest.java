package Model.Player;

import Model.Exceptions.DepositSlotMaxDimExceeded;
import Model.Exceptions.NotEnoughResources;
import Model.Player.Deposit.Deposit;
import Model.Player.Deposit.LeaderDepositSlot;
import Model.Resources.ResourceContainer;
import Model.Resources.ResourceType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DepositTest {

    @Test
    void pyramid3() {
        Deposit deposit = new Deposit(3);

        assertEquals(deposit.getDepositList().get(0).getMaxDim(), 1);
        assertEquals(deposit.getDepositList().get(1).getMaxDim(), 2);
        assertEquals(deposit.getDepositList().get(2).getMaxDim(), 3);
        assertEquals(deposit.getDepositList().size(), 3);
    }

    @Test
    void pyramid5() {
        Deposit deposit = new Deposit(5);

        assertEquals(deposit.getDepositList().get(0).getMaxDim(), 1);
        assertEquals(deposit.getDepositList().get(1).getMaxDim(), 2);
        assertEquals(deposit.getDepositList().get(2).getMaxDim(), 3);
        assertEquals(deposit.getDepositList().get(3).getMaxDim(), 4);
        assertEquals(deposit.getDepositList().get(4).getMaxDim(), 5);
        assertEquals(deposit.getDepositList().size(), 5);
    }


    @Test
    void addDepositSlot() {
        Deposit deposit = new Deposit(3);

        LeaderDepositSlot lds = new LeaderDepositSlot(ResourceType.STONE,2);
        assertTrue(deposit.addDepositSlot(lds));
        assertTrue(deposit.getDepositList().contains(lds));
    }

    @Test
    void removeDepositSlot() {
    }

    @Test
    void canSwitchDeposit_1() {
        Deposit deposit = new Deposit(3);
        LeaderDepositSlot lds = new LeaderDepositSlot(ResourceType.GOLD,2);

        lds.addToDepositSlot(new ResourceContainer(ResourceType.GOLD,2));
        assertTrue(deposit.getDepositList().get(0).isEmpty());
        assertThrows(DepositSlotMaxDimExceeded.class, ()-> deposit.canSwitchDeposit(lds,2, deposit.getDepositList().get(0)));
        assertAll( ()-> deposit.canSwitchDeposit(lds,2, deposit.getDepositList().get(1)));
    }

    @Test
    void canSwitchDeposit_2() {
        Deposit deposit = new Deposit(3);
        LeaderDepositSlot lds = new LeaderDepositSlot(ResourceType.GOLD,2);
        lds.addToDepositSlot(new ResourceContainer(ResourceType.GOLD,2));

        assertThrows(NotEnoughResources.class, ()-> deposit.canSwitchDeposit(lds,3, deposit.getDepositList().get(0)));
    }


    @Test
    void switchDeposit_3() {
        Deposit deposit = new Deposit(3);
        LeaderDepositSlot lds = new LeaderDepositSlot(ResourceType.GOLD,2);
        deposit.addDepositSlot(lds);
        deposit.getDefaultSlot_WithDim(2).addToDepositSlot(new ResourceContainer(ResourceType.GOLD, 2));
        assertTrue(deposit.switchDeposit(deposit.getDefaultSlot_WithDim(2),2, deposit.getDepositList().get(deposit.getDepositList().indexOf(lds))));


    }
}