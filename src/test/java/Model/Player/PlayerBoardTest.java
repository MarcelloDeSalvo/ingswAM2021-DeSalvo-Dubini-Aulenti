package Model.Player;

import Model.Player.Deposit.DefaultDeposit;
import Model.Resources.ResourceContainer;
import Model.Resources.ResourceType;
import Model.Cards.Colour;
import Model.Cards.DevelopmentCard;
import Model.Player.Production.ProductionSlot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;


import static org.junit.jupiter.api.Assertions.*;

class PlayerBoardTest {

    @BeforeEach
    void clearStaticSet(){
        DefaultDeposit clear = new DefaultDeposit(1);
        clear.clearSet();
    }

    @Test
    void hashEnoughResourcesArrayList() {
        Player player = new Player("Presidente? PRESIDENTE?");

        player.getPlayerBoard().getDeposit().getDefaultSlot_WithDim(2).addToDepositSlot(new ResourceContainer(ResourceType.SHIELD, 2));
        player.getPlayerBoard().getDeposit().getDefaultSlot_WithDim(1).addToDepositSlot(new ResourceContainer(ResourceType.MINION, 1));
        player.getPlayerBoard().getVault().addToVault(new ResourceContainer(ResourceType.GOLD, 3));
        player.getPlayerBoard().getVault().addToVault(new ResourceContainer(ResourceType.SHIELD, 3));
        player.getPlayerBoard().getVault().addToVault(new ResourceContainer(ResourceType.STONE, 5));

        ArrayList<ResourceContainer> requested = new ArrayList<>();

        requested.add(new ResourceContainer(ResourceType.SHIELD, 4));
        requested.add(new ResourceContainer(ResourceType.MINION, 1));
        requested.add(new ResourceContainer(ResourceType.GOLD, 3));
        requested.add(new ResourceContainer(ResourceType.STONE, 2));
        requested.add(new ResourceContainer(ResourceType.STONE, 3));



        assertTrue(player.getPlayerBoard().hasEnoughResources(requested));

        requested.add(new ResourceContainer(ResourceType.MINION, 1));

        assertFalse(player.getPlayerBoard().hasEnoughResources(requested));
    }

    @Test
    void hashEnoughResourcesHashMap() {
        Player player = new Player("Antonio Zequila");

        player.getPlayerBoard().getDeposit().getDefaultSlot_WithDim(2).addToDepositSlot(new ResourceContainer(ResourceType.SHIELD, 2));
        player.getPlayerBoard().getDeposit().getDefaultSlot_WithDim(1).addToDepositSlot(new ResourceContainer(ResourceType.MINION, 1));
        player.getPlayerBoard().getVault().addToVault(new ResourceContainer(ResourceType.GOLD, 3));
        player.getPlayerBoard().getVault().addToVault(new ResourceContainer(ResourceType.SHIELD, 3));
        player.getPlayerBoard().getVault().addToVault(new ResourceContainer(ResourceType.STONE, 5));

        HashMap<ResourceType, ResourceContainer> requested = new HashMap<>();

        requested.put(ResourceType.SHIELD, new ResourceContainer(ResourceType.SHIELD, 5));
        requested.put(ResourceType.MINION, new ResourceContainer(ResourceType.MINION, 1));
        requested.put(ResourceType.GOLD, new ResourceContainer(ResourceType.GOLD, 2));
        requested.put(ResourceType.STONE, new ResourceContainer(ResourceType.STONE, 5));



        assertTrue(player.getPlayerBoard().hasEnoughResources(requested));

        requested.replace(ResourceType.SHIELD, new ResourceContainer(ResourceType.SHIELD, 6));

        assertFalse(player.getPlayerBoard().hasEnoughResources(requested));
    }

    @Test
    void checkResources() {
        Player player = new Player("Luca Giurato");

        player.getPlayerBoard().getDeposit().getDefaultSlot_WithDim(2).addToDepositSlot(new ResourceContainer(ResourceType.SHIELD, 2));
        player.getPlayerBoard().getDeposit().getDefaultSlot_WithDim(1).addToDepositSlot(new ResourceContainer(ResourceType.MINION, 1));
        player.getPlayerBoard().getVault().addToVault(new ResourceContainer(ResourceType.GOLD, 3));
        player.getPlayerBoard().getVault().addToVault(new ResourceContainer(ResourceType.SHIELD, 3));

        assertEquals(player.getPlayerBoard().checkResources(ResourceType.SHIELD), 5);
        assertEquals(player.getPlayerBoard().checkResources(ResourceType.MINION), 1);
        assertEquals(player.getPlayerBoard().checkResources(ResourceType.GOLD), 3);
        assertEquals(player.getPlayerBoard().checkResources(ResourceType.STONE), 0);
    }

    @Test
    void productionSimulation() {
        Player p = new Player("Nick");

        //Development card creation
        ArrayList<ResourceContainer> productionInput = new ArrayList<>();
        productionInput.add(new ResourceContainer(ResourceType.GOLD, 1));
        productionInput.add(new ResourceContainer(ResourceType.STONE, 1));

        ArrayList<ResourceContainer> productionOutput = new ArrayList<>();
        productionOutput.add(new ResourceContainer(ResourceType.MINION, 2));
        DevelopmentCard dc = new DevelopmentCard(8, 1, Colour.BLUE, productionInput, productionOutput );

        //Add development card to Nick's board
        assertTrue(p.insertBoughtCardOn(1,dc));

        //Add resources to Nick's vault
        p.getPlayerBoard().getVault().addToVault(new ResourceContainer(ResourceType.GOLD, 2));
        p.getPlayerBoard().getVault().addToVault(new ResourceContainer(ResourceType.STONE, 1));

        //Nick's decides to produce. He selects the previous dev. card
        ArrayList<ProductionSlot> selectedSlot = new ArrayList<>();
        selectedSlot.add(p.getProductionSlotByID(1));

        p.fillProductionBuffers(selectedSlot);
        assertTrue(p.getPlayerBoard().hasEnoughResources(p.getPlayerBoard().getProductionSite().getBufferInputMap()));

        //Nick's selects all the resources he needs to validate the production
        ArrayList<ResourceContainer> selectedInput = new ArrayList<>();

        assertAll(()->p.getPlayerBoard().getVault().canRemoveFromVault(new ResourceContainer(ResourceType.GOLD, 1)));
        selectedInput.add(new ResourceContainer(ResourceType.GOLD, 1));

        assertAll(()->p.getPlayerBoard().getVault().canRemoveFromVault(new ResourceContainer(ResourceType.STONE, 1)));
        selectedInput.add(new ResourceContainer(ResourceType.STONE, 1));

        //Controller checks if the selected resources are legit
        assertAll(()->p.canProduce(selectedInput));
        //Controller executes the production
        assertEquals(p.getPlayerBoard().getProductionSite().getBufferOutputResourceQty(ResourceType.MINION),2);
        assertTrue(p.getPlayerBoard().produce());

        //checks if the production finished correctly
        assertEquals(p.getPlayerBoard().getVault().getResourceQuantity(ResourceType.GOLD),1);
        assertEquals(p.getPlayerBoard().getVault().getResourceQuantity(ResourceType.STONE),0);
        assertEquals(p.getPlayerBoard().getVault().getResourceQuantity(ResourceType.MINION),2);

        //clears the transaction buffers for the next turn
        p.getPlayerBoard().getVault().clearBuffer();
    }

    @Test
    void productionSimulation2() {


    }


    //Tests canBuy and buy methods in the same Test
    @Test
    void buySimulation() {
        Player player = new Player("Paolo Brosio");

        player.getPlayerBoard().getDeposit().getDefaultSlot_WithDim(1).addToDepositSlot(new ResourceContainer(ResourceType.SHIELD, 1));
        player.getPlayerBoard().getDeposit().getDefaultSlot_WithDim(3).addToDepositSlot(new ResourceContainer(ResourceType.STONE, 2));

        player.getPlayerBoard().getVault().addToVault(new ResourceContainer(ResourceType.GOLD, 3));
        player.getPlayerBoard().getVault().addToVault(new ResourceContainer(ResourceType.STONE, 1));
        player.getPlayerBoard().getVault().addToVault(new ResourceContainer(ResourceType.SHIELD, 2));

        assertAll(() -> player.getPlayerBoard().getDeposit().getDefaultSlot_WithDim(1).canRemoveFromDepositSlot(new ResourceContainer(ResourceType.SHIELD, 1)));
        assertAll(() -> player.getPlayerBoard().getDeposit().getDefaultSlot_WithDim(3).canRemoveFromDepositSlot(new ResourceContainer(ResourceType.STONE, 1)));
        assertAll(() -> player.getPlayerBoard().getVault().canRemoveFromVault(new ResourceContainer(ResourceType.GOLD, 2)));
        assertAll(() -> player.getPlayerBoard().getVault().canRemoveFromVault(new ResourceContainer(ResourceType.SHIELD, 2)));

        ArrayList<ResourceContainer> inputList = new ArrayList<>();
        inputList.add(new ResourceContainer(ResourceType.GOLD, 2));
        inputList.add(new ResourceContainer(ResourceType.SHIELD, 2));
        inputList.add(new ResourceContainer(ResourceType.STONE, 1));
        inputList.add(new ResourceContainer(ResourceType.SHIELD, 1));

        assertTrue(player.getPlayerBoard().canBuy(inputList));

        inputList.add(new ResourceContainer(ResourceType.GOLD, 1));

        assertFalse(player.getPlayerBoard().canBuy(inputList));


        //buy method test
        assertTrue(player.getPlayerBoard().buy());
    }



    @Test
    void insertBoughtCard() {
    }
}