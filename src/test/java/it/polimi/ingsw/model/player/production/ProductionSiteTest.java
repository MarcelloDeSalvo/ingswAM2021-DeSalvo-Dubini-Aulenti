package it.polimi.ingsw.model.player.production;

import it.polimi.ingsw.model.Util;
import it.polimi.ingsw.model.cards.Colour;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.exceptions.NotEnoughResources;
import it.polimi.ingsw.model.player.PlayerBoard;
import it.polimi.ingsw.model.resources.ResourceContainer;
import it.polimi.ingsw.model.resources.ResourceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ProductionSiteTest {

    Player player = new Player("Goku");
    PlayerBoard playerBoard = player.getPlayerBoard();

    @BeforeEach
    void setUp(){

        //FaithPoints Production to be tested

        //SLOT 1
        assertTrue(playerBoard.insertBoughtCard(playerBoard.getProductionSlotByID(1), Util.getCardWithVpColour(2, Colour.YELLOW))); //   1Sh -> 1G          Level 1 Active -> cannot be used for production
        assertTrue(playerBoard.insertBoughtCard(playerBoard.getProductionSlotByID(1), Util.getCardWithVpColour(6, Colour.YELLOW))); //   1Sh + 1 St -> 3G   Level 2 OnTop

        //SLOT 2
        assertTrue(playerBoard.insertBoughtCard(playerBoard.getProductionSlotByID(2), Util.getCardWithVpColour(2, Colour.BLUE)));  //    1M -> 1S           Level 1 OnTop

        //SLOT 3
        assertTrue(playerBoard.insertBoughtCard(playerBoard.getProductionSlotByID(3), Util.getCardWithVpColour(2, Colour.GREEN))); //    1St -> 1M          Level 1 OnTop

    }


    @Test
    void insert_all_res_needed(){
        ResourceContainer containerM = new ResourceContainer(ResourceType.MINION,1);
        ResourceContainer containerSh = new ResourceContainer(ResourceType.SHIELD,2);
        ResourceContainer containerSt = new ResourceContainer(ResourceType.STONE,3);

        assertTrue(playerBoard.getDeposit().getDefaultSlot_WithDim(1).addToDepositSlot(containerM));
        assertTrue(playerBoard.getDeposit().getDefaultSlot_WithDim(2).addToDepositSlot(containerSh));
        assertTrue(playerBoard.getDeposit().getDefaultSlot_WithDim(3).addToDepositSlot(containerSt));

        ArrayList<ResourceContainer> selectedRes = new ArrayList<>();
        selectedRes.add(containerM);
        selectedRes.add(containerSh);
        selectedRes.add(containerSt);

        assertTrue(playerBoard.hasEnoughResources(selectedRes));
    }


    @Test
    void insert_not_all_res_needed(){
        ResourceContainer containerM = new ResourceContainer(ResourceType.MINION,1);
        ResourceContainer containerSh = new ResourceContainer(ResourceType.SHIELD,2);


        assertTrue(playerBoard.getDeposit().getDefaultSlot_WithDim(1).addToDepositSlot(containerM));
        assertTrue(playerBoard.getDeposit().getDefaultSlot_WithDim(2).addToDepositSlot(containerSh));


        ArrayList<ResourceContainer> selectedRes = new ArrayList<>();
        selectedRes.add(containerM);
        selectedRes.add(containerSh);

        assertTrue(playerBoard.hasEnoughResources(selectedRes));
    }

    @Test
    void fillProductionBuffers() {

        insert_all_res_needed();

        ArrayList<ProductionSlot> selectedProductionCards = new ArrayList<>();
        selectedProductionCards.add(playerBoard.getProductionSlotByID(1));

        assertTrue(playerBoard.fillProductionBuffers(selectedProductionCards));

        assertTrue(playerBoard.hasEnoughResourcesForProduction());

        //it must be equal to the level 2 card on Top, not to the one on bottom
        assertEquals(Util.arraylistToMap(Util.getCardWithVpColour(6, Colour.YELLOW).getInput()), playerBoard.getProductionSite().getBufferInputMap());
        assertEquals(Util.arraylistToMap(Util.getCardWithVpColour(6, Colour.YELLOW).getOutput()), playerBoard.getProductionSite().getBufferOutputMap());
    }

    @Test
    void hasEnoughDevelopmentCardsWith() {
        assertTrue(playerBoard.getProductionSite().hasEnoughDevelopmentCardsWith(2,0,Colour.YELLOW));
        assertTrue(playerBoard.getProductionSite().hasEnoughDevelopmentCardsWith(1,2,Colour.YELLOW));
        assertTrue(playerBoard.getProductionSite().hasEnoughDevelopmentCardsWith(1,1,Colour.YELLOW));

        assertTrue(playerBoard.getProductionSite().hasEnoughDevelopmentCardsWith(1,1,Colour.GREEN));
        assertTrue(playerBoard.getProductionSite().hasEnoughDevelopmentCardsWith(1,1,Colour.BLUE));
    }


    @Test
    void hasEnoughInputResources() {
        insert_all_res_needed();

        ArrayList<ProductionSlot> selectedProductionCards = new ArrayList<>();
        selectedProductionCards.add(playerBoard.getProductionSlotByID(1));

        assertTrue(playerBoard.fillProductionBuffers(selectedProductionCards));
        assertTrue(playerBoard.hasEnoughResourcesForProduction());
    }

    @Test
    void hasNotEnoughInputResources() {
        insert_not_all_res_needed();

        ArrayList<ProductionSlot> selectedProductionCards = new ArrayList<>();
        selectedProductionCards.add(playerBoard.getProductionSlotByID(3));//    1St -> 1M          Level 1 OnTop

        assertTrue(playerBoard.fillProductionBuffers(selectedProductionCards));
        assertFalse(playerBoard.hasEnoughResourcesForProduction());
    }

    @Test
    void clearBuffers() {
        fillProductionBuffers();
        assertTrue(playerBoard.getProductionSite().clearBuffers());

        assertTrue(playerBoard.getProductionSite().getBufferInputMap().isEmpty());
        assertTrue(playerBoard.getProductionSite().getBufferOutputMap().isEmpty());
    }

    @Test
    void canProduce_true_1() {
        insert_all_res_needed();

        ArrayList<ProductionSlot> selectedProductionCards = new ArrayList<>();
        selectedProductionCards.add(playerBoard.getProductionSlotByID(3));//    1St -> 1M          Level 1 OnTop
        assertTrue(playerBoard.fillProductionBuffers(selectedProductionCards));

        assertTrue(playerBoard.hasEnoughResourcesForProduction());

        ArrayList<ResourceContainer> selectedByTheUser = new ArrayList<>();
        selectedByTheUser.add(new ResourceContainer(ResourceType.STONE,1));


        assertAll(()->playerBoard.canProduce(selectedByTheUser));
        //produce()
    }

    @Test
    void canProduce_true_2() {
        insert_all_res_needed();

        ArrayList<ProductionSlot> selectedProductionCards = new ArrayList<>();
        selectedProductionCards.add(playerBoard.getProductionSlotByID(3));//    1St -> 1M          Level 1 OnTop
        selectedProductionCards.add(playerBoard.getProductionSlotByID(2));//    1M -> 1S           Level 1 OnTop
        assertTrue(playerBoard.fillProductionBuffers(selectedProductionCards));

        assertTrue(playerBoard.hasEnoughResourcesForProduction());

        ArrayList<ResourceContainer> selectedByTheUser = new ArrayList<>();
        selectedByTheUser.add(new ResourceContainer(ResourceType.STONE,1));
        selectedByTheUser.add(new ResourceContainer(ResourceType.MINION,1));


        assertAll(()->playerBoard.canProduce(selectedByTheUser));
        //produce()

    }

    @Test
    void canProduce_false_1() {
        insert_all_res_needed();

        ArrayList<ProductionSlot> selectedProductionCards = new ArrayList<>();
        selectedProductionCards.add(playerBoard.getProductionSlotByID(3));//    1St -> 1M          Level 1 OnTop
        assertTrue(playerBoard.fillProductionBuffers(selectedProductionCards));

        assertTrue(playerBoard.hasEnoughResourcesForProduction());

        ArrayList<ResourceContainer> selectedByTheUser = new ArrayList<>();
        selectedByTheUser.add(new ResourceContainer(ResourceType.MINION,1));


        assertThrows(NotEnoughResources.class, ()->playerBoard.canProduce(selectedByTheUser));
        //he can't
        assertTrue(playerBoard.getProductionSite().clearBuffers());

    }

    @Test
    void canProduce_false_2() {
        insert_all_res_needed();

        ArrayList<ProductionSlot> selectedProductionCards = new ArrayList<>();
        selectedProductionCards.add(playerBoard.getProductionSlotByID(3));//    1St -> 1M          Level 1 OnTop
        selectedProductionCards.add(playerBoard.getProductionSlotByID(2));//    1M -> 1S           Level 1 OnTop

        assertTrue(playerBoard.fillProductionBuffers(selectedProductionCards));
        assertTrue(playerBoard.hasEnoughResourcesForProduction());

        ArrayList<ResourceContainer> selectedByTheUser = new ArrayList<>();
        selectedByTheUser.add(new ResourceContainer(ResourceType.MINION,1));


        assertThrows(NotEnoughResources.class, ()->playerBoard.canProduce(selectedByTheUser));
        //he can't
        assertTrue(playerBoard.getProductionSite().clearBuffers());

    }

    @Test
    void produce_1() {
        canProduce_true_1();
        assertTrue(playerBoard.produce());
        assertEquals(playerBoard.getVault().getResourceQuantity(ResourceType.MINION),1);

        assertTrue(playerBoard.getProductionSite().clearBuffers());

    }

    @Test
    void produce_2() {
        canProduce_true_2();
        assertTrue(playerBoard.produce());

        assertEquals(playerBoard.getVault().getResourceQuantity(ResourceType.MINION),1);
        assertEquals(playerBoard.getVault().getResourceQuantity(ResourceType.STONE),1);

        assertTrue(playerBoard.getProductionSite().clearBuffers());

    }


}