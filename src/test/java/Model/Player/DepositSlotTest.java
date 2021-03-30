package Model.Player;

import Model.Resources.ResourceContainer;
import Model.Resources.ResourceType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DepositSlotTest {

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


    /*@Test
    void setDepositResourceType() {
        LeaderDepositSlot slot = new LeaderDepositSlot(ResourceType.MINION, 2);

        assertThrows( , () -> slot.setDepositResourceType(ResourceType.STONE));
    }*/
}