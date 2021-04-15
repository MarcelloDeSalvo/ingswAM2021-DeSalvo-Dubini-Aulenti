package Model.Resources;

import Model.FaithPath;
import Model.Game;
import Model.Parser.FaithPathSetUpParser;
import Model.Player.Deposit.Deposit;
import Model.Player.Vault;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class ResourceTypeTest {

    @Test
    void goldToDepositTest(){
        ResourceContainer aCoin= new ResourceContainer(ResourceType.GOLD,1);
        ResourceContainer aFP= new ResourceContainer(ResourceType.FAITHPOINT,1);

        assertTrue(aCoin.getResourceType().canAddToDeposit());
        assertTrue(aCoin.getResourceType().canAddToVault());
        assertFalse(aCoin.getResourceType().canAddToFaithPath());

        Deposit deposit = new Deposit(2);
        Vault vault = new Vault();

        assertTrue(deposit.getDefaultSlot_WithDim(1).addToDepositSlot(aCoin));
        assertTrue(vault.addToVault(aCoin));
        assertFalse(aFP.addToDeposit(deposit.getDefaultSlot_WithDim(2)));
        assertFalse(aFP.addToVault(vault));
    }


    @Test
    void observerTest() throws FileNotFoundException {
        FaithPath faithPath = FaithPathSetUpParser.deserializeFaithPathSetUp();

        ResourceContainer faithPoint = new ResourceContainer(ResourceType.FAITHPOINT, 1);
        ResourceContainer gold = new ResourceContainer(ResourceType.GOLD, 1);

        faithPoint.addObserver(faithPath);

        assertTrue(faithPoint.addToFaithPath());
        assertTrue(faithPoint.addToFaithPath());

        assertFalse(gold.addToFaithPath());

        assertEquals(faithPath.getPositions().get(0), 2);
    }
}