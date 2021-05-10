package it.polimi.ingsw.model.player.deposit;

import it.polimi.ingsw.model.exceptions.DepositSlotMaxDimExceeded;
import it.polimi.ingsw.model.resources.ResourceContainer;
import it.polimi.ingsw.model.resources.ResourceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class DepositSlotTest {

    private HashSet<ResourceType> notAvailableResourceType = new HashSet<>();

    @BeforeEach
    void clearStaticSet(){
        DefaultDeposit clear = new DefaultDeposit(1,notAvailableResourceType);
        clear.clearSet();
    }

    @Test
    void canAdd() {
        DefaultDeposit slot = new DefaultDeposit(3,notAvailableResourceType);

        assertTrue(slot.canAdd(2));
        assertTrue(slot.canAdd(3));
        assertFalse(slot.canAdd(4));
    }

    @Test
    void canAdd2() {
        DefaultDeposit slot = new DefaultDeposit(3,notAvailableResourceType);
        ResourceContainer container = new ResourceContainer(ResourceType.SHIELD, 1);

        slot.addToDepositSlot(container);

        assertTrue(slot.canAdd(1));
        assertTrue(slot.canAdd(2));
        assertFalse(slot.canAdd(4));
    }

    @Test
    void canAddLeader() {
        LeaderDeposit slot = new LeaderDeposit(ResourceType.MINION, 2);

        assertTrue(slot.canAdd(1));
        assertTrue(slot.canAdd(2));
        assertFalse(slot.canAdd(4));
    }

    @Test
    void canAddLeader2() {
        LeaderDeposit slot = new LeaderDeposit(ResourceType.MINION, 2);
        ResourceContainer container = new ResourceContainer(ResourceType.MINION, 1);

        slot.addToDepositSlot(container);

        assertTrue(slot.canAdd(1));
        assertFalse(slot.canAdd(2));
    }

    @Test
    void canRemove() {
        DefaultDeposit slot = new DefaultDeposit(2,notAvailableResourceType);

        assertFalse(slot.canRemove(1));
        assertFalse(slot.canRemove(3));
    }

    @Test
    void canRemove2() {
        DefaultDeposit slot = new DefaultDeposit(2,notAvailableResourceType);
        ResourceContainer container = new ResourceContainer(ResourceType.MINION, 2);

        slot.addToDepositSlot(container);

        assertTrue(slot.canRemove(1));
        assertTrue(slot.canRemove(2));
        assertFalse(slot.canRemove(3));
    }

    @Test
    void canRemoveLeader() {
        LeaderDeposit slot = new LeaderDeposit(ResourceType.MINION, 2);

        assertFalse(slot.canRemove(1));
        assertFalse(slot.canRemove(3));
    }

    @Test
    void canRemoveLeader2() {
        LeaderDeposit slot = new LeaderDeposit(ResourceType.MINION, 2);
        ResourceContainer container = new ResourceContainer(ResourceType.MINION, 2);

        slot.addToDepositSlot(container);

        assertTrue(slot.canRemove(1));
        assertTrue(slot.canRemove(2));
        assertFalse(slot.canRemove(3));
    }

    @Test
    void isNullAndEmpty() {
        DefaultDeposit slot = new DefaultDeposit(2,notAvailableResourceType);

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
        LeaderDeposit slot = new LeaderDeposit(ResourceType.MINION, 2);

        assertFalse(slot.isNull());

        slot.addToDepositSlot(new ResourceContainer(ResourceType.MINION, 1));

        assertFalse((slot.isEmpty()));
    }

    @Test
    void isEmpty() {
        DefaultDeposit slot = new DefaultDeposit(2,notAvailableResourceType);

        assertTrue(slot.isEmpty());

        slot.addToDepositSlot(new ResourceContainer(ResourceType.GOLD, 2));

        assertFalse((slot.isEmpty()));

        slot.removeFromDepositSlot(new ResourceContainer(ResourceType.GOLD, 2));

        assertTrue(slot.isEmpty());

    }

    @Test
    void isEmptyLeader() {
        LeaderDeposit slot = new LeaderDeposit(ResourceType.MINION, 2);

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

}