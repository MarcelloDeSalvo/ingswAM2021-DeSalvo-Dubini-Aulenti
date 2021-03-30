package Model.Player;

import Model.Exceptions.DepositSlotMaxDimExceeded;
import Model.Exceptions.DifferentResourceType;
import Model.Exceptions.NotEnoughResources;
import Model.Player.Deposit.DefaultDepositSlot;
import Model.Resources.ResourceContainer;
import Model.Resources.ResourceType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DefaultDepositSlotTest {

    @Test
    void canAddToDepositSlot() {
        DefaultDepositSlot slot = new DefaultDepositSlot(3);
        ResourceContainer container = new ResourceContainer(ResourceType.STONE, 1);

        assertAll(() -> slot.canAddToDepositSlot(container));
    }

    @Test
    void canAddToDepositSlot2() {
        DefaultDepositSlot slot = new DefaultDepositSlot(3);
        ResourceContainer container = new ResourceContainer(ResourceType.MINION, 1);
        ResourceContainer container2 = new ResourceContainer(ResourceType.MINION, 2);

        assertAll(() -> slot.canAddToDepositSlot(container));
        assertAll(() -> slot.canAddToDepositSlot(container2));
    }

    @Test
    void canAddToDepositSlot3() {
        DefaultDepositSlot slot = new DefaultDepositSlot(3);
        ResourceContainer container = new ResourceContainer(ResourceType.STONE, 1);
        ResourceContainer container2 = new ResourceContainer(ResourceType.GOLD, 1);
        slot.addToDepositSlot(container);

        assertThrows(DifferentResourceType.class, () -> slot.canAddToDepositSlot(container2));
    }

    @Test
    void canAddToDepositSlot4() {
        DefaultDepositSlot slot = new DefaultDepositSlot(3);
        ResourceContainer container = new ResourceContainer(ResourceType.SHIELD, 2);
        ResourceContainer container2 = new ResourceContainer(ResourceType.SHIELD, 2);
        slot.addToDepositSlot(container);

        assertThrows(DepositSlotMaxDimExceeded.class, () -> slot.canAddToDepositSlot(container2));
    }

    @Test
    void canAddToDepositSlot5() {
        DefaultDepositSlot slot = new DefaultDepositSlot(2);
        ResourceContainer container = new ResourceContainer(ResourceType.SHIELD, 5);
        slot.addToDepositSlot(container);

        assertThrows(DepositSlotMaxDimExceeded.class, () -> slot.canAddToDepositSlot(container));
    }


    @Test
    void canRemoveFromDepositSlot() {
        DefaultDepositSlot slot = new DefaultDepositSlot(3);
        ResourceContainer container = new ResourceContainer(ResourceType.SHIELD, 2);
        ResourceContainer container2 = new ResourceContainer(ResourceType.SHIELD, 2);

        assertTrue(slot.addToDepositSlot(container));

        assertAll(() -> slot.canRemoveFromDepositSlot(container2));
    }

    @Test
    void canRemoveFromDepositSlot2() {
        DefaultDepositSlot slot = new DefaultDepositSlot(3);
        ResourceContainer container = new ResourceContainer(ResourceType.SHIELD, 2);

        assertThrows(NotEnoughResources.class, () -> slot.canRemoveFromDepositSlot(container));
    }

    @Test
    void canRemoveFromDepositSlot3() {
        DefaultDepositSlot slot = new DefaultDepositSlot(3);
        ResourceContainer container = new ResourceContainer(ResourceType.SHIELD, 2);
        ResourceContainer container2 = new ResourceContainer(ResourceType.GOLD, 1);

        assertTrue(slot.addToDepositSlot(container));

        assertThrows(DifferentResourceType.class, () -> slot.canRemoveFromDepositSlot(container2));
    }

    @Test
    void canRemoveFromDepositSlot4() {
        DefaultDepositSlot slot = new DefaultDepositSlot(3);
        ResourceContainer container = new ResourceContainer(ResourceType.GOLD, 2);
        ResourceContainer container2 = new ResourceContainer(ResourceType.GOLD, 3);

        assertTrue(slot.addToDepositSlot(container));

        assertThrows(NotEnoughResources.class, () -> slot.canRemoveFromDepositSlot(container2));
    }


    @Test
    void addToDepositSlot() {
        DefaultDepositSlot slot = new DefaultDepositSlot(2);
        ResourceContainer container = new ResourceContainer(ResourceType.GOLD, 1);

        assertTrue(slot.addToDepositSlot(container));
        assertEquals(container, slot.getDepositContainer());

        assertTrue(slot.addToDepositSlot(container));
        assertEquals(slot.getDepositContainer(), new ResourceContainer(ResourceType.GOLD, 2));

    }

    @Test
    void removeFromDepositSlot() {
        DefaultDepositSlot slot = new DefaultDepositSlot(3);
        ResourceContainer addContainer = new ResourceContainer(ResourceType.MINION, 3);
        ResourceContainer removeContainer = new ResourceContainer(ResourceType.MINION, 2);

        assertTrue(slot.addToDepositSlot(addContainer));
        assertTrue(slot.removeFromDepositSlot(removeContainer));

        assertEquals(slot.getDepositContainer(), new ResourceContainer(ResourceType.MINION, 1));
    }
}