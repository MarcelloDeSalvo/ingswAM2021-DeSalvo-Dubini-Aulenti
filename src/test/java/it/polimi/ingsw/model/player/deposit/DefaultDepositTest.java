package it.polimi.ingsw.model.player.deposit;

import it.polimi.ingsw.model.exceptions.DepositSlotMaxDimExceeded;
import it.polimi.ingsw.model.exceptions.DifferentResourceType;
import it.polimi.ingsw.model.exceptions.NotEnoughResources;
import it.polimi.ingsw.model.resources.ResourceContainer;
import it.polimi.ingsw.model.resources.ResourceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DefaultDepositTest {

    @BeforeEach
    void clearStaticSet(){
        DefaultDeposit clear = new DefaultDeposit(1);
        clear.clearSet();
    }

    @Test
    void canAddToDepositSlot() {
        DefaultDeposit slot = new DefaultDeposit(3);
        ResourceContainer container = new ResourceContainer(ResourceType.STONE, 1);

        assertAll(() -> slot.canAddToDepositSlot(container));
    }

    @Test
    void canAddToDepositSlot2() {
        DefaultDeposit slot = new DefaultDeposit(3);
        ResourceContainer container = new ResourceContainer(ResourceType.MINION, 1);
        ResourceContainer container2 = new ResourceContainer(ResourceType.MINION, 2);

        assertAll(() -> slot.canAddToDepositSlot(container));
        assertAll(() -> slot.canAddToDepositSlot(container2));
    }

    @Test
    void canAddToDepositSlot3() {
        DefaultDeposit slot = new DefaultDeposit(3);
        ResourceContainer container = new ResourceContainer(ResourceType.STONE, 1);
        ResourceContainer container2 = new ResourceContainer(ResourceType.GOLD, 1);
        slot.addToDepositSlot(container);

        assertThrows(DifferentResourceType.class, () -> slot.canAddToDepositSlot(container2));
    }

    @Test
    void canAddToDepositSlot4() {
        DefaultDeposit slot = new DefaultDeposit(3);
        ResourceContainer container = new ResourceContainer(ResourceType.SHIELD, 2);
        ResourceContainer container2 = new ResourceContainer(ResourceType.SHIELD, 2);
        slot.addToDepositSlot(container);

        assertThrows(DepositSlotMaxDimExceeded.class, () -> slot.canAddToDepositSlot(container2));
    }

    @Test
    void canAddToDepositSlot5() {
        DefaultDeposit slot = new DefaultDeposit(2);
        ResourceContainer container = new ResourceContainer(ResourceType.SHIELD, 5);
        slot.addToDepositSlot(container);

        assertThrows(DepositSlotMaxDimExceeded.class, () -> slot.canAddToDepositSlot(container));
    }


    @Test
    void canRemoveFromDepositSlot() {
        DefaultDeposit slot = new DefaultDeposit(3);
        ResourceContainer container = new ResourceContainer(ResourceType.SHIELD, 2);
        ResourceContainer container2 = new ResourceContainer(ResourceType.SHIELD, 2);

        assertTrue(slot.addToDepositSlot(container));

        assertAll(() -> slot.canRemoveFromDepositSlot(container2));
    }

    @Test
    void canRemoveFromDepositSlot2() {
        DefaultDeposit slot = new DefaultDeposit(3);
        ResourceContainer container = new ResourceContainer(ResourceType.SHIELD, 2);

        assertThrows(NotEnoughResources.class, () -> slot.canRemoveFromDepositSlot(container));
    }

    @Test
    void canRemoveFromDepositSlot3() {
        DefaultDeposit slot = new DefaultDeposit(3);
        ResourceContainer container = new ResourceContainer(ResourceType.SHIELD, 2);
        ResourceContainer container2 = new ResourceContainer(ResourceType.GOLD, 1);

        assertTrue(slot.addToDepositSlot(container));

        assertThrows(DifferentResourceType.class, () -> slot.canRemoveFromDepositSlot(container2));
    }

    @Test
    void canRemoveFromDepositSlot4() {
        DefaultDeposit slot = new DefaultDeposit(3);
        ResourceContainer container = new ResourceContainer(ResourceType.GOLD, 2);
        ResourceContainer container2 = new ResourceContainer(ResourceType.GOLD, 3);

        assertTrue(slot.addToDepositSlot(container));

        assertThrows(NotEnoughResources.class, () -> slot.canRemoveFromDepositSlot(container2));
    }


    @Test
    void addToDepositSlot() {
        DefaultDeposit slot = new DefaultDeposit(2);
        ResourceContainer container = new ResourceContainer(ResourceType.GOLD, 1);

        assertTrue(slot.addToDepositSlot(container));
        assertEquals(container, slot.getDepositContainer());

        assertTrue(slot.addToDepositSlot(container));
        assertEquals(slot.getDepositContainer(), new ResourceContainer(ResourceType.GOLD, 2));

    }

    @Test
    void removeFromDepositSlot() {
        DefaultDeposit slot = new DefaultDeposit(3);
        ResourceContainer addContainer = new ResourceContainer(ResourceType.MINION, 3);
        ResourceContainer removeContainer = new ResourceContainer(ResourceType.MINION, 2);

        assertTrue(slot.addToDepositSlot(addContainer));
        assertTrue(slot.removeFromDepositSlot(removeContainer));

        assertEquals(slot.getDepositContainer(), new ResourceContainer(ResourceType.MINION, 1));
    }

}