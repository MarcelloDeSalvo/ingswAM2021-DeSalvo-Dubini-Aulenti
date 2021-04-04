package Model.Player.Production;

import Model.Resources.ResourceContainer;
import Model.Resources.ResourceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class BaseProductionSlotTest {

    @Test
    void fillQuestionMarkInput() {
        BaseProductionSlot baseProductionSlot = new BaseProductionSlot();

        baseProductionSlot.fillQuestionMarkInput(ResourceType.STONE);

        assertEquals(baseProductionSlot.getProductionInput().get(0).getResourceType(), ResourceType.STONE);
    }

    @Test
    void fillQuestionMarkInput2() {
        BaseProductionSlot baseProductionSlot = new BaseProductionSlot(3, 0);

        baseProductionSlot.fillQuestionMarkInput(ResourceType.STONE);
        baseProductionSlot.fillQuestionMarkInput(ResourceType.SHIELD);
        baseProductionSlot.fillQuestionMarkInput(ResourceType.MINION);

        assertEquals(baseProductionSlot.getProductionInput().get(0).getResourceType(), ResourceType.STONE);
        assertEquals(baseProductionSlot.getProductionInput().get(1).getResourceType(), ResourceType.SHIELD);
        assertEquals(baseProductionSlot.getProductionInput().get(2).getResourceType(), ResourceType.MINION);
    }

    @Test
    void fillQuestionMarkOutput() {
        BaseProductionSlot baseProductionSlot = new BaseProductionSlot();

        baseProductionSlot.fillQuestionMarkOutput(ResourceType.GOLD);

        assertEquals(baseProductionSlot.getProductionOutput().get(0).getResourceType(), ResourceType.GOLD);
    }

    @Test
    void fillQuestionMarkOutput2() {
        BaseProductionSlot baseProductionSlot = new BaseProductionSlot(0, 3);

        baseProductionSlot.fillQuestionMarkOutput(ResourceType.GOLD);
        baseProductionSlot.fillQuestionMarkOutput(ResourceType.SHIELD);
        baseProductionSlot.fillQuestionMarkOutput(ResourceType.STONE);

        assertEquals(baseProductionSlot.getProductionOutput().get(0).getResourceType(), ResourceType.GOLD);
        assertEquals(baseProductionSlot.getProductionOutput().get(1).getResourceType(), ResourceType.SHIELD);
        assertEquals(baseProductionSlot.getProductionOutput().get(2).getResourceType(), ResourceType.STONE);
    }


    @Test
    void clearCurrentBuffer() {
        BaseProductionSlot baseProductionSlot = new BaseProductionSlot();

        baseProductionSlot.fillQuestionMarkInput(ResourceType.GOLD);
        baseProductionSlot.fillQuestionMarkOutput(ResourceType.STONE);

        assertEquals(baseProductionSlot.getProductionInput().get(0).getResourceType(), ResourceType.GOLD);
        assertEquals(baseProductionSlot.getProductionOutput().get(0).getResourceType(), ResourceType.STONE);

        baseProductionSlot.clearCurrentBuffer();

        assertEquals(baseProductionSlot.getProductionInput().size(), 0);
        assertEquals(baseProductionSlot.getProductionOutput().size(), 0);
    }
}