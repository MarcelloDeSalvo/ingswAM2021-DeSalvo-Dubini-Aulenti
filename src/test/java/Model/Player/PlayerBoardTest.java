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
    void canProduce() {
        Player p = new Player("Nick");
        ArrayList<ResourceContainer> productionInput = new ArrayList<>();
        productionInput.add(new ResourceContainer(ResourceType.GOLD, 1));
        productionInput.add(new ResourceContainer(ResourceType.STONE, 1));

        ArrayList<ResourceContainer> productionOutput = new ArrayList<>();
        productionOutput.add(new ResourceContainer(ResourceType.MINION, 2));

        p.getPlayerBoard().getVault().addToVault(new ResourceContainer(ResourceType.GOLD, 2));
        p.getPlayerBoard().getVault().addToVault(new ResourceContainer(ResourceType.STONE, 1));

        DevelopmentCard dc = new DevelopmentCard(8, 1, Colour.BLUE, productionInput, productionOutput );
        assertTrue(p.insertBoughtCardOn(1,dc));
        ArrayList<ProductionSlot> selectedSlot = new ArrayList<>();
        selectedSlot.add(p.getProductionSlotByID(1));

        //User's turn
        //He activates the production and selects the only card available (dc)
        //He selects the input needed to produce
        p.activateProduction(selectedSlot);

        ArrayList<ResourceContainer> selectedInput = new ArrayList<>();
        assertAll(()->p.getPlayerBoard().getVault().canRemoveFromVault(new ResourceContainer(ResourceType.GOLD, 1)));
        selectedInput.add(new ResourceContainer(ResourceType.GOLD, 1));

        assertAll(()->p.getPlayerBoard().getVault().canRemoveFromVault(new ResourceContainer(ResourceType.STONE, 1)));
        selectedInput.add(new ResourceContainer(ResourceType.STONE, 1));

        assertAll(()->p.getPlayerBoard().canProduce(selectedInput));
        assertTrue(p.getPlayerBoard().produce());

        assertEquals(p.getPlayerBoard().getProductionSite().getBufferOutputMap().get(ResourceType.MINION).getQty(),2);
        assertEquals(p.getPlayerBoard().getVault().getVaultMap().get(ResourceType.GOLD).getQty(),1);
        assertEquals(p.getPlayerBoard().getVault().getVaultMap().get(ResourceType.STONE).getQty(),0);
        assertEquals(p.getPlayerBoard().getVault().getVaultMap().get(ResourceType.MINION).getQty(),2);
        p.getPlayerBoard().getVault().clearBuffer();

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