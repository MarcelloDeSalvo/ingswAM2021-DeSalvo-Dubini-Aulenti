package Model.Player.Production;

import Model.Resources.ResourceContainer;
import Model.Resources.ResourceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class LeaderCardProductionTest {
    ArrayList<ResourceContainer> input;
    ArrayList<ResourceContainer> output;

    @BeforeEach
    void setUp() {
        input = new ArrayList<ResourceContainer>();
        output = new ArrayList<ResourceContainer>();
        input.add(new ResourceContainer(ResourceType.GOLD, 1));
        output.add(new ResourceContainer(ResourceType.FAITHPOINT, 1));
    }

    @Test
    void fillQuestionMarkInput() {
        LeaderCardProduction leaderCardProduction = new LeaderCardProduction(input, output, 1, 0);

        leaderCardProduction.fillQuestionMarkInput(ResourceType.MINION);

        assertEquals(leaderCardProduction.getInputBuffer().get(0).getResourceType(), ResourceType.GOLD);
        assertEquals(leaderCardProduction.getInputBuffer().get(1).getResourceType(), ResourceType.MINION);

    }

    @Test
    void fillQuestionMarkInput2() {
        LeaderCardProduction leaderCardProduction = new LeaderCardProduction(input, output, 3, 0);

        leaderCardProduction.fillQuestionMarkInput(ResourceType.MINION);
        leaderCardProduction.fillQuestionMarkInput(ResourceType.GOLD);
        leaderCardProduction.fillQuestionMarkInput(ResourceType.STONE);

        assertEquals(leaderCardProduction.getInputBuffer().get(0).getResourceType(), ResourceType.GOLD);
        assertEquals(leaderCardProduction.getInputBuffer().get(1).getResourceType(), ResourceType.MINION);
        assertEquals(leaderCardProduction.getInputBuffer().get(2).getResourceType(), ResourceType.GOLD);
        assertEquals(leaderCardProduction.getInputBuffer().get(3).getResourceType(), ResourceType.STONE);

    }

    @Test
    void fillQuestionMarkOutput() {
        LeaderCardProduction leaderCardProduction = new LeaderCardProduction(input, output, 0, 1);

        leaderCardProduction.fillQuestionMarkOutput(ResourceType.SHIELD);

        assertEquals(leaderCardProduction.getOutputBuffer().get(0).getResourceType(), ResourceType.FAITHPOINT);
        assertEquals(leaderCardProduction.getOutputBuffer().get(1).getResourceType(), ResourceType.SHIELD);
    }

    @Test
    void fillQuestionMarkOutput2() {
        LeaderCardProduction leaderCardProduction = new LeaderCardProduction(input, output, 0, 3);

        leaderCardProduction.fillQuestionMarkOutput(ResourceType.GOLD);
        leaderCardProduction.fillQuestionMarkOutput(ResourceType.STONE);
        leaderCardProduction.fillQuestionMarkOutput(ResourceType.SHIELD);

        assertEquals(leaderCardProduction.getOutputBuffer().get(0).getResourceType(), ResourceType.FAITHPOINT);
        assertEquals(leaderCardProduction.getOutputBuffer().get(1).getResourceType(), ResourceType.GOLD);
        assertEquals(leaderCardProduction.getOutputBuffer().get(2).getResourceType(), ResourceType.STONE);
        assertEquals(leaderCardProduction.getOutputBuffer().get(3).getResourceType(), ResourceType.SHIELD);
    }

    @Test
    void clearCurrentBuffer() {
        LeaderCardProduction leaderCardProduction = new LeaderCardProduction(input, output, 2, 0);

        leaderCardProduction.fillQuestionMarkInput(ResourceType.MINION);
        leaderCardProduction.fillQuestionMarkInput(ResourceType.GOLD);

        assertEquals(leaderCardProduction.getInputBuffer().get(0).getResourceType(), ResourceType.GOLD);
        assertEquals(leaderCardProduction.getInputBuffer().get(1).getResourceType(), ResourceType.MINION);
        assertEquals(leaderCardProduction.getInputBuffer().get(2).getResourceType(), ResourceType.GOLD);

        assertTrue(leaderCardProduction.clearCurrentBuffer());

        assertEquals(leaderCardProduction.getInputBuffer(), input);

    }

    @Test
    void clearCurrentBuffer2() {
        LeaderCardProduction leaderCardProduction = new LeaderCardProduction(input, output, 0, 2);

        leaderCardProduction.fillQuestionMarkOutput(ResourceType.STONE);
        leaderCardProduction.fillQuestionMarkOutput(ResourceType.SHIELD);

        assertEquals(leaderCardProduction.getOutputBuffer().get(0).getResourceType(), ResourceType.FAITHPOINT);
        assertEquals(leaderCardProduction.getOutputBuffer().get(1).getResourceType(), ResourceType.STONE);
        assertEquals(leaderCardProduction.getOutputBuffer().get(2).getResourceType(), ResourceType.SHIELD);

        assertTrue(leaderCardProduction.clearCurrentBuffer());

        assertEquals(leaderCardProduction.getOutputBuffer(), output);

    }

    @Test
    void clearCurrentBuffer3() {
        LeaderCardProduction leaderCardProduction = new LeaderCardProduction(input, output, 1, 1);

        leaderCardProduction.fillQuestionMarkInput(ResourceType.STONE);
        leaderCardProduction.fillQuestionMarkOutput(ResourceType.SHIELD);

        assertEquals(leaderCardProduction.getInputBuffer().get(0).getResourceType(), ResourceType.GOLD);
        assertEquals(leaderCardProduction.getInputBuffer().get(1).getResourceType(), ResourceType.STONE);

        assertEquals(leaderCardProduction.getOutputBuffer().get(0).getResourceType(), ResourceType.FAITHPOINT);
        assertEquals(leaderCardProduction.getOutputBuffer().get(1).getResourceType(), ResourceType.SHIELD);

        assertTrue(leaderCardProduction.clearCurrentBuffer());

        assertEquals(leaderCardProduction.getInputBuffer(), input);
        assertEquals(leaderCardProduction.getOutputBuffer(), output);

    }

    @Test
    void hasQuestionMarks() {
        LeaderCardProduction leaderCardProduction = new LeaderCardProduction(input, output, 0, 0);
        assertFalse(leaderCardProduction.hasQuestionMarks());

        LeaderCardProduction leaderCardProduction2 = new LeaderCardProduction(input, output, 2, 0);
        assertTrue(leaderCardProduction2.hasQuestionMarks());

        LeaderCardProduction leaderCardProduction3 = new LeaderCardProduction(input, output, 0, 3);
        assertTrue(leaderCardProduction3.hasQuestionMarks());
    }
}