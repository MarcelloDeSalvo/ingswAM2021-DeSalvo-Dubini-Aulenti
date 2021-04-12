package Model.Player.Production;

import Model.Cards.ResourceRequirement;
import Model.Exceptions.NotEnoughResources;
import Model.Player.Deposit.DepositSlot;
import Model.Player.Player;
import Model.Player.PlayerBoard;
import Model.Resources.ResourceContainer;
import Model.Resources.ResourceType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class BaseProductionTest {

    @Test
    void fillQuestionMarkInput_Output_Def_Numbers() {
        BaseProduction baseProduction = new BaseProduction();

        for (int i = 0; i<baseProduction.getQMI(); i++) {
            assertAll(()->baseProduction.fillQuestionMarkInput(ResourceType.STONE));
        }

        for (int i = 0; i<baseProduction.getQMO(); i++){
            assertAll(()->baseProduction.fillQuestionMarkInput(ResourceType.GOLD));
        }

        assertEquals(baseProduction.getProductionInput().get(0).getResourceType(), ResourceType.STONE);
    }

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


    //production simulation
    Player pippo = new Player("Pippo");
    PlayerBoard playerBoard = pippo.getPlayerBoard();

    @Test
    void fillQuestionMarkInput_Output_Pippo() {
        ProductionSlot baseProduction = pippo.getProductionSlotByID(0);

        for (int i = 0; i<baseProduction.getQMI(); i++) {
            assertAll(()->baseProduction.fillQuestionMarkInput(ResourceType.STONE));
        }

        for (int i = 0; i<baseProduction.getQMO(); i++){
            assertAll(()->baseProduction.fillQuestionMarkOutput(ResourceType.GOLD));
        }

        assertEquals(baseProduction.getProductionInput().get(0).getResourceType(), ResourceType.STONE);
        assertEquals(baseProduction.getProductionInput().get(1).getResourceType(), ResourceType.STONE);
        assertEquals(baseProduction.getProductionOutput().get(0).getResourceType(), ResourceType.GOLD);
    }

    @Test
    void insert_3_stones_inside_deposit(){
        playerBoard.addResourceToDefaultDepositWithDim(3,new ResourceContainer(ResourceType.STONE, 3));
        assertEquals(playerBoard.getDepositSlotWithDim(3).getDepositResourceType(),ResourceType.STONE);
        assertEquals(playerBoard.getDepositSlotWithDim(3).getResourceQty(),3);
    }

    @Test
    void base_production_simulation_canProduce_true(){
        insert_3_stones_inside_deposit();

        //he starts selecting the cards or the base production
        ArrayList<ProductionSlot> selectedByTheUser = new ArrayList<>();
        //he selects the base production
        assertAll(()->selectedByTheUser.add(pippo.getProductionSlotByID(0)));

        //at first he needs to fill all the undefined input/output
        fillQuestionMarkInput_Output_Pippo(); //2 stones -> 1 gold

        //then the controller checks if he has enough total resources in order to produce those cards
        assertTrue(()->playerBoard.fillProductionBuffers(selectedByTheUser)); //fills the buffers inside production site
        assertTrue(playerBoard.hasEnoughResourcesForProduction());//compares the buffers with all the player's resources

        //he starts selecting the resources from his vault/deposit
        ArrayList<ResourceContainer> selectedResourcesForProduction = new ArrayList<>();
        //user> 2 Stones from deposit 3
        ResourceContainer selectedResources = new ResourceContainer(ResourceType.STONE, 2); //he selects the ones with 3 stone
        assertAll(()->playerBoard.getDepositSlotWithDim(3).canRemoveFromDepositSlot(selectedResources));
        selectedResourcesForProduction.add(selectedResources);
        //user> confirm

        //the controller checks if the selected resources matches the inputs needed to produce
        assertAll(()->pippo.canProduce(selectedResourcesForProduction));

        //if it is all ok the controller removes the selected resources from the previously selected vault/deposits and adds the production outputs to the vault
        assertAll(()->pippo.produce());

        assertEquals(playerBoard.getDepositSlotWithDim(3).getResourceQty(), 1); // 3 stones - 1 stone
        assertEquals(playerBoard.getVault().getResourceQuantity(ResourceType.GOLD),1);

    }

    @Test
    void base_production_simulation_canProduce_false(){
        //insert_3_stones_inside_deposit(); -> No resources for Pippo

        //he starts selecting the cards or the base production
        ArrayList<ProductionSlot> selectedByTheUser = new ArrayList<>();
        //he selects the base production
        assertAll(()->selectedByTheUser.add(pippo.getProductionSlotByID(0)));

        //at first he needs to fill all the undefined input/output
        fillQuestionMarkInput_Output_Pippo(); //2 stones -> 1 gold

        //then the controller checks if he has enough total resources in order to produce those cards
        assertTrue(()->playerBoard.fillProductionBuffers(selectedByTheUser)); //fills the buffers inside production site
        assertFalse(playerBoard.hasEnoughResourcesForProduction());//compares the buffers with all the player's resources

        playerBoard.clearAllBuffers();

    }

    @Test
    void base_production_simulation_wrong_selection(){
        //This time Pippo has the enough total resources but he selects only one stone (which is insufficient)

        insert_3_stones_inside_deposit();

        //he starts selecting the cards or the base production
        ArrayList<ProductionSlot> selectedByTheUser = new ArrayList<>();
        //he selects the base production
        assertAll(()->selectedByTheUser.add(pippo.getProductionSlotByID(0)));

        //at first he needs to fill all the undefined input/output
        fillQuestionMarkInput_Output_Pippo(); //2 stones -> 1 gold

        //then the controller checks if he has enough total resources in order to produce those cards
        assertTrue(()->playerBoard.fillProductionBuffers(selectedByTheUser)); //fills the buffers inside production site
        assertTrue(playerBoard.hasEnoughResourcesForProduction());//compares the buffers with all the player's resources

        //he starts selecting the resources from his vault/deposit
        ArrayList<ResourceContainer> selectedResourcesForProduction = new ArrayList<>();
        //user> 1 Stones from deposit 3
        ResourceContainer selectedResources = new ResourceContainer(ResourceType.STONE, 1); //he selects the ones with 3 stone
        assertAll(()->playerBoard.getDepositSlotWithDim(3).canRemoveFromDepositSlot(selectedResources));
        selectedResourcesForProduction.add(selectedResources);
        //user> confirm

        //He can't produce because he needed 2 stone and not 1
        assertThrows(NotEnoughResources.class,()->pippo.canProduce(selectedResourcesForProduction));

        playerBoard.clearAllBuffers();

    }
}