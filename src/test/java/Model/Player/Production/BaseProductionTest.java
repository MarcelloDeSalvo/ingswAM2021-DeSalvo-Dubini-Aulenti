package Model.Player.Production;

import Model.Resources.ResourceType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BaseProductionTest {

    @Test
    void fillQuestionMarkInput() {
        BaseProduction baseProduction = new BaseProduction();

        baseProduction.fillQuestionMarkInput(ResourceType.STONE);

        assertEquals(baseProduction.getProductionInput().get(0).getResourceType(), ResourceType.STONE);
    }

    @Test
    void fillQuestionMarkInput2() {
        BaseProduction baseProduction = new BaseProduction(3, 0);

        baseProduction.fillQuestionMarkInput(ResourceType.STONE);
        baseProduction.fillQuestionMarkInput(ResourceType.SHIELD);
        baseProduction.fillQuestionMarkInput(ResourceType.MINION);

        assertEquals(baseProduction.getProductionInput().get(0).getResourceType(), ResourceType.STONE);
        assertEquals(baseProduction.getProductionInput().get(1).getResourceType(), ResourceType.SHIELD);
        assertEquals(baseProduction.getProductionInput().get(2).getResourceType(), ResourceType.MINION);
    }

    @Test
    void fillQuestionMarkOutput() {
        BaseProduction baseProduction = new BaseProduction();

        baseProduction.fillQuestionMarkOutput(ResourceType.GOLD);

        assertEquals(baseProduction.getProductionOutput().get(0).getResourceType(), ResourceType.GOLD);
    }

    @Test
    void fillQuestionMarkOutput2() {
        BaseProduction baseProduction = new BaseProduction(0, 3);

        baseProduction.fillQuestionMarkOutput(ResourceType.GOLD);
        baseProduction.fillQuestionMarkOutput(ResourceType.SHIELD);
        baseProduction.fillQuestionMarkOutput(ResourceType.STONE);

        assertEquals(baseProduction.getProductionOutput().get(0).getResourceType(), ResourceType.GOLD);
        assertEquals(baseProduction.getProductionOutput().get(1).getResourceType(), ResourceType.SHIELD);
        assertEquals(baseProduction.getProductionOutput().get(2).getResourceType(), ResourceType.STONE);
    }


    @Test
    void clearCurrentBuffer() {
        BaseProduction baseProduction = new BaseProduction();

        baseProduction.fillQuestionMarkInput(ResourceType.GOLD);
        baseProduction.fillQuestionMarkOutput(ResourceType.STONE);

        assertEquals(baseProduction.getProductionInput().get(0).getResourceType(), ResourceType.GOLD);
        assertEquals(baseProduction.getProductionOutput().get(0).getResourceType(), ResourceType.STONE);

        baseProduction.clearCurrentBuffer();

        assertEquals(baseProduction.getProductionInput().size(), 0);
        assertEquals(baseProduction.getProductionOutput().size(), 0);
    }

    @Test
    void base_production_simulation(){

    }
}