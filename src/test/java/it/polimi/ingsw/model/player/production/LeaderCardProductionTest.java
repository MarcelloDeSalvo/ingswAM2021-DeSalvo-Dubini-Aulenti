package it.polimi.ingsw.model.player.production;

import it.polimi.ingsw.model.FaithPath;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerBoard;
import it.polimi.ingsw.model.resources.ResourceContainer;
import it.polimi.ingsw.model.resources.ResourceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class LeaderCardProductionTest {
    ArrayList<ResourceContainer> input;
    ArrayList<ResourceContainer> output;

    @BeforeEach
    void setUp() {
        input = new ArrayList<>();
        output = new ArrayList<>();
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

    @Test
    void LeaderProduction() throws FileNotFoundException {
        Game game = new Game("Ben Dover");

        Player player = game.getPlayer(0);
        PlayerBoard playerBoard = player.getPlayerBoard();

        //new leaderCardProduction gets added to ProductionSite
        LeaderCardProduction leaderCardProduction = new LeaderCardProduction(input, output, 1, 5);
        leaderCardProduction.fillQuestionMarkInput(ResourceType.MINION);
        assertTrue(leaderCardProduction.fillQuestionMarkOutput(ResourceType.STONE));
        assertTrue(leaderCardProduction.fillQuestionMarkOutput(ResourceType.STONE));
        assertTrue(leaderCardProduction.fillQuestionMarkOutput(ResourceType.SHIELD));
        assertTrue(leaderCardProduction.fillQuestionMarkOutput(ResourceType.SHIELD));
        assertTrue(leaderCardProduction.fillQuestionMarkOutput(ResourceType.GOLD));

        assertTrue(playerBoard.getProductionSite().addProductionSlot(leaderCardProduction));

        //adds a few elements to Deposit
        assertTrue(playerBoard.getDeposit().getDefaultSlot_WithDim(1).addToDepositSlot(new ResourceContainer(ResourceType.MINION,1)));
        assertTrue(playerBoard.getDeposit().getDefaultSlot_WithDim(2).addToDepositSlot(new ResourceContainer(ResourceType.SHIELD,2)));
        assertTrue(playerBoard.getDeposit().getDefaultSlot_WithDim(3).addToDepositSlot(new ResourceContainer(ResourceType.GOLD,2)));

        //this fills the buffers
        ArrayList<ProductionSlot> selectedSlots = new ArrayList<>();
        selectedSlots.add(leaderCardProduction);
        assertTrue(playerBoard.fillProductionBuffers(selectedSlots));

        //controls if in vault/deposit there are enough resources for production
        assertTrue(playerBoard.hasEnoughResourcesForProduction());

        //ArrayList that i receive as a "payment" for production
        ArrayList<ResourceContainer> selectedRes = new ArrayList<>();
        selectedRes.add(new ResourceContainer(ResourceType.MINION, 1));
        selectedRes.add(new ResourceContainer(ResourceType.GOLD, 1));

        //checks if selectedRes is enough to "pay" for the production
        assertAll(() -> playerBoard.canProduce(selectedRes));

        //clear all buffers from Vault and Deposit then adds the production outputs in Vault
        assertTrue(playerBoard.produce());

        //checks if the elements are actually produced and stored in Vault
        assertEquals(playerBoard.getVault().getResourceQuantity(ResourceType.STONE), 2);
        assertEquals(playerBoard.getVault().getResourceQuantity(ResourceType.SHIELD), 2);
        assertEquals(playerBoard.getVault().getResourceQuantity(ResourceType.GOLD), 1);
    }
}