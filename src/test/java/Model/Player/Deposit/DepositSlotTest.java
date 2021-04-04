package Model.Player.Deposit;

import Model.Exceptions.DepositSlotMaxDimExceeded;
import Model.Player.Deposit.DefaultDepositSlot;
import Model.Player.Deposit.Deposit;
import Model.Player.Deposit.LeaderDepositSlot;
import Model.Resources.ResourceContainer;
import Model.Resources.ResourceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DepositSlotTest {

    @BeforeEach
    void clearStaticSet(){
        DefaultDepositSlot clear = new DefaultDepositSlot(1);
        clear.clearSet();
    }

    @Test
    void canAdd() {
        DefaultDepositSlot slot = new DefaultDepositSlot(3);

        assertTrue(slot.canAdd(2));
        assertTrue(slot.canAdd(3));
        assertFalse(slot.canAdd(4));
    }

    @Test
    void canAdd2() {
        DefaultDepositSlot slot = new DefaultDepositSlot(3);
        ResourceContainer container = new ResourceContainer(ResourceType.SHIELD, 1);

        slot.addToDepositSlot(container);

        assertTrue(slot.canAdd(1));
        assertTrue(slot.canAdd(2));
        assertFalse(slot.canAdd(4));
    }

    @Test
    void canAddLeader() {
        LeaderDepositSlot slot = new LeaderDepositSlot(ResourceType.MINION, 2);

        assertTrue(slot.canAdd(1));
        assertTrue(slot.canAdd(2));
        assertFalse(slot.canAdd(4));
    }

    @Test
    void canAddLeader2() {
        LeaderDepositSlot slot = new LeaderDepositSlot(ResourceType.MINION, 2);
        ResourceContainer container = new ResourceContainer(ResourceType.MINION, 1);

        slot.addToDepositSlot(container);

        assertTrue(slot.canAdd(1));
        assertFalse(slot.canAdd(2));
    }

    @Test
    void canRemove() {
        DefaultDepositSlot slot = new DefaultDepositSlot(2);

        assertFalse(slot.canRemove(1));
        assertFalse(slot.canRemove(3));
    }

    @Test
    void canRemove2() {
        DefaultDepositSlot slot = new DefaultDepositSlot(2);
        ResourceContainer container = new ResourceContainer(ResourceType.MINION, 2);

        slot.addToDepositSlot(container);

        assertTrue(slot.canRemove(1));
        assertTrue(slot.canRemove(2));
        assertFalse(slot.canRemove(3));
    }

    @Test
    void canRemoveLeader() {
        LeaderDepositSlot slot = new LeaderDepositSlot(ResourceType.MINION, 2);

        assertFalse(slot.canRemove(1));
        assertFalse(slot.canRemove(3));
    }

    @Test
    void canRemoveLeader2() {
        LeaderDepositSlot slot = new LeaderDepositSlot(ResourceType.MINION, 2);
        ResourceContainer container = new ResourceContainer(ResourceType.MINION, 2);

        slot.addToDepositSlot(container);

        assertTrue(slot.canRemove(1));
        assertTrue(slot.canRemove(2));
        assertFalse(slot.canRemove(3));
    }

    @Test
    void isNullAndEmpty() {
        DefaultDepositSlot slot = new DefaultDepositSlot(2);

        assertTrue(slot.isNull());

        slot.addToDepositSlot(new ResourceContainer(ResourceType.GOLD, 3));

        assertFalse((slot.isNull()));
        assertFalse(slot.isEmpty());

        slot.removeFromDepositSlot(new ResourceContainer(ResourceType.GOLD, 3));

        assertFalse((slot.isNull()));
        assertTrue(slot.isEmpty());
    }

    @Test
    void isNullAndEmptyLeader() {
        LeaderDepositSlot slot = new LeaderDepositSlot(ResourceType.MINION, 2);

        assertFalse(slot.isNull());

        slot.addToDepositSlot(new ResourceContainer(ResourceType.MINION, 1));

        assertFalse((slot.isEmpty()));
    }

    @Test
    void isEmpty() {
        DefaultDepositSlot slot = new DefaultDepositSlot(2);

        assertTrue(slot.isEmpty());

        slot.addToDepositSlot(new ResourceContainer(ResourceType.GOLD, 2));

        assertFalse((slot.isEmpty()));

        slot.removeFromDepositSlot(new ResourceContainer(ResourceType.GOLD, 2));

        assertTrue(slot.isEmpty());

    }

    @Test
    void isEmptyLeader() {
        LeaderDepositSlot slot = new LeaderDepositSlot(ResourceType.MINION, 2);

        assertTrue(slot.isEmpty());

        slot.addToDepositSlot(new ResourceContainer(ResourceType.MINION, 1));

        assertFalse((slot.isEmpty()));

        slot.removeFromDepositSlot(new ResourceContainer(ResourceType.MINION, 1));

        assertTrue(slot.isEmpty());
    }

    @Test
    void canSwitchWithTest1() {
        Deposit deposit = new Deposit(5);
        deposit.getDefaultSlot_WithDim(2).addToDepositSlot(new ResourceContainer(ResourceType.GOLD,2));
        deposit.getDefaultSlot_WithDim(4).addToDepositSlot(new ResourceContainer(ResourceType.MINION, 2));
        assertEquals(2,deposit.getDefaultSlot_WithDim(2).getResourceQty());
        assertEquals(2,deposit.getDefaultSlot_WithDim(4).getResourceQty());
        assertAll(()->deposit.canSwitchDeposit(deposit.getDefaultSlot_WithDim(2), deposit.getDefaultSlot_WithDim(4)));
        deposit.getDefaultSlot_WithDim(4).addToDepositSlot(new ResourceContainer(ResourceType.MINION, 2));
        assertThrows(DepositSlotMaxDimExceeded.class, ()->deposit.canSwitchDeposit(deposit.getDefaultSlot_WithDim(2),deposit.getDefaultSlot_WithDim(4) ));
        deposit.getDefaultSlot_WithDim(4).removeFromDepositSlot(new ResourceContainer(ResourceType.MINION, 2));
        deposit.switchToDeposit(deposit.getDefaultSlot_WithDim(2), deposit.getDefaultSlot_WithDim(4) );
        assertEquals(2,deposit.getDefaultSlot_WithDim(2).getResourceQty());
        assertEquals(2,deposit.getDefaultSlot_WithDim(4).getResourceQty());
        assertEquals(ResourceType.GOLD,deposit.getDefaultSlot_WithDim(4).getDepositResourceType());
    }

    @Test
    void canSwitchWithTest2(){
        Deposit deposit = new Deposit(5);
        deposit.getDefaultSlot_WithDim(2).addToDepositSlot(new ResourceContainer(ResourceType.GOLD,2));
        deposit.getDefaultSlot_WithDim(4).addToDepositSlot(new ResourceContainer(ResourceType.MINION, 2));

    }



    /*@Test
    void setDepositResourceType() {
        LeaderDepositSlot slot = new LeaderDepositSlot(ResourceType.MINION, 2);

        assertThrows( , () -> slot.setDepositResourceType(ResourceType.STONE));
    }*/
}