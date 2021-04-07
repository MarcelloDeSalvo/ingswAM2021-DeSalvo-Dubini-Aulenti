package Model.Player;

import Model.Cards.Colour;
import Model.Cards.DevelopmentCard;
import Model.Player.Deposit.DefaultDepositSlot;
import Model.Player.Production.ProductionSlot;
import Model.Resources.ResourceContainer;
import Model.Resources.ResourceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PlayerBoardTest {

    @BeforeEach
    void clearStaticSet(){
        DefaultDepositSlot clear = new DefaultDepositSlot(1);
        clear.clearSet();
    }

    @Test
    void checkResources() {
    }

    @Test
    void ProductionSimulation() {
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

        p.activateProduction(selectedSlot);
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
        assertTrue(p.getPlayerBoard().produce());

        //checks if the production finished correctly
        assertEquals(p.getPlayerBoard().getProductionSite().getBufferOutputResourceQty(ResourceType.MINION),2);
        assertEquals(p.getPlayerBoard().getVault().getResourceQuantity(ResourceType.GOLD),1);
        assertEquals(p.getPlayerBoard().getVault().getResourceQuantity(ResourceType.STONE),0);
        assertEquals(p.getPlayerBoard().getVault().getResourceQuantity(ResourceType.MINION),2);

        //clears the transaction buffers for the next turn
        p.getPlayerBoard().getVault().clearBuffer();

    }

    @Test
    void canProduce(){

    }

    @Test
    void produce() {
    }

    @Test
    void canBuy() {
    }

    @Test
    void arraylistToMap() {
    }

    @Test
    void buy() {
    }

    @Test
    void insertBoughtCard() {
    }
}