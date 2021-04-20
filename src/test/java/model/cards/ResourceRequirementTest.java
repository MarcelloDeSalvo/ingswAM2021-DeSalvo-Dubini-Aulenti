package model.cards;

import model.player.PlayerBoard;
import model.resources.ResourceContainer;
import model.resources.ResourceType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResourceRequirementTest {

    PlayerBoard playerBoard = new PlayerBoard(3, 0);

    @Test
    void checkRequirements() {
        ResourceRequirement resourceRequirement = new ResourceRequirement(new ResourceContainer(ResourceType.GOLD, 2));

        assertFalse(resourceRequirement.checkRequirements(playerBoard));
    }

    @Test
    void checkRequirements2() {
        ResourceRequirement resourceRequirement = new ResourceRequirement(new ResourceContainer(ResourceType.GOLD, 2));
        ResourceContainer container = new ResourceContainer(ResourceType.GOLD, 2);

        playerBoard.getVault().addToVault(container);

        assertTrue(resourceRequirement.checkRequirements(playerBoard));
    }

    @Test
    void checkRequirements3() {
        ResourceRequirement resourceRequirement = new ResourceRequirement(new ResourceContainer(ResourceType.STONE, 3));
        ResourceContainer container = new ResourceContainer(ResourceType.STONE, 1);
        ResourceContainer container2 = new ResourceContainer(ResourceType.STONE, 2);

        playerBoard.getVault().addToVault(container);
        playerBoard.getDeposit().getDefaultSlot_WithDim(3).addToDepositSlot(container2);

        assertTrue(resourceRequirement.checkRequirements(playerBoard));
    }

    @Test
    void checkRequirements4() {
        ResourceRequirement resourceRequirement = new ResourceRequirement(new ResourceContainer(ResourceType.MINION, 3));
        ResourceContainer container = new ResourceContainer(ResourceType.SHIELD, 2);
        ResourceContainer container2 = new ResourceContainer(ResourceType.MINION, 3);

        playerBoard.getVault().addToVault(container);
        playerBoard.getDeposit().getDefaultSlot_WithDim(3).addToDepositSlot(container2);

        assertTrue(resourceRequirement.checkRequirements(playerBoard));
    }

    @Test
    void checkRequirements5() {
        ResourceRequirement resourceRequirement = new ResourceRequirement(new ResourceContainer(ResourceType.SHIELD, 3));
        ResourceContainer container = new ResourceContainer(ResourceType.SHIELD, 1);

        playerBoard.getVault().addToVault(container);
        playerBoard.getDeposit().getDefaultSlot_WithDim(1).addToDepositSlot(container);

        assertFalse(resourceRequirement.checkRequirements(playerBoard));

        playerBoard.getVault().addToVault(container);

        assertTrue(resourceRequirement.checkRequirements(playerBoard));
    }

}